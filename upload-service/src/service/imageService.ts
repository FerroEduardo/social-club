import dotenv from 'dotenv';
import sharp from 'sharp';
import StorageFactory from '../storage/storageFactory';

dotenv.config();

const availableFormats = (process.env.SHARP_ALLOWED_EXTENSIONS ?? '').split(',');

const storage = StorageFactory.create();

export async function processImage(rawImage: Buffer): Promise<Buffer> {
  const imageSharp = sharp(rawImage);
  const metadata = await imageSharp.metadata();

  if (availableFormats.includes(metadata.format?.toUpperCase() ?? '')) {
    return imageSharp
      .webp({
        lossless: false,
        quality: 100,
        alphaQuality: 100,
        preset: 'default',
        force: true,
      })
      .toBuffer();
  }
  throw new Error(`Incompatible image format, send any of these: ${availableFormats.toString()}`);
}

export async function save(rawImage: Buffer): Promise<number> {
  try {
    const processedImageBuffer = await processImage(rawImage);
    const imageId = await storage.save(processedImageBuffer);

    return imageId;
  } catch (error) {
    console.error('Failed to save image', error);

    throw new Error('Failed to save image');
  }
}

export default {
  save,
};
