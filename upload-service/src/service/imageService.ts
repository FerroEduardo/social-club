import dotenv from 'dotenv';
import crypto from 'crypto';
import fs from 'fs';
import sharp from 'sharp';
import { prisma } from '../database';
import StorageType from '../enum/storageType';

dotenv.config();

const availableFormats = (process.env.SHARP_ALLOWED_EXTENSIONS ?? '').split(',');

interface UploadStorage {
  readonly type: StorageType;
  readonly localStoragePath?: string;
}

function getUploadStorageType(): UploadStorage {
  const uploadStorageType = process.env.UPLOAD_STORAGE_TYPE;
  const uploadStoragePath = process.env.UPLOAD_STORAGE_PATH;

  switch (uploadStorageType) {
    case 'DATABASE':
      return {
        type: StorageType.DATABASE,
      };
    case 'S3':
      throw new Error('Unavailable storage');
      return {
        type: StorageType.S3,
      };
    case 'LOCAL':
      if (!uploadStoragePath) {
        throw new Error('UPLOAD_STORAGE_PATH is required for local storage');
      }
      const storagePath = fs.lstatSync(uploadStoragePath);
      if (!storagePath.isDirectory()) {
        throw new Error('UPLOAD_STORAGE_PATH is not a directory');
      }
      return {
        type: StorageType.LOCAL,
        localStoragePath: uploadStoragePath,
      };
    default:
      throw new Error('Unknown storage type');
  }
}

const uploadStorage = getUploadStorageType();

export async function processImage(imageData: Buffer): Promise<Buffer> {
  const imageSharp = sharp(imageData);
  const metadata = await imageSharp.metadata();

  if (availableFormats.includes(metadata.format?.toUpperCase() ?? '')) {
    return imageSharp
      .webp({
        lossless: false,
        quality: 80,
        alphaQuality: 100,
        preset: 'default',
        force: true,
      })
      .toBuffer();
  }
  throw new Error(`Incompatible image format, send any of these: ${availableFormats.toString()}`);
}

export async function saveImage(imageData: Buffer): Promise<number> {
  try {
    let data;
    if (uploadStorage.type === StorageType.LOCAL) {
      const newFileName = crypto.randomUUID();
      const filePath = `${uploadStorage.localStoragePath!}/${newFileName}`;

      fs.writeFileSync(filePath, imageData);

      data = {
        local: filePath,
      };
    } else if (uploadStorage.type === StorageType.DATABASE) {
      data = {
        blob: imageData,
      };
    }

    const image = await prisma.image.create(
      {
        data,
        select: {
          id: true,
        },
      },
    );

    return image.id;
  } catch (error) {
    console.error('Failed to save image', error);

    throw new Error('Failed to save image');
  }
}

export default {
  processImage,
  saveImage,
};
