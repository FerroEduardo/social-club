import dotenv from 'dotenv';
import sharp from 'sharp';
import StorageFactory from '../storage/storageFactory';
import ImageData from '../interface/imageData';

dotenv.config();

const availableFormats = (process.env.SHARP_ALLOWED_EXTENSIONS ?? '').split(',');

const storage = StorageFactory.create();

export async function processImage(rawImage: Buffer): Promise<ImageData> {
  const imageSharp = sharp(rawImage);
  const metadata = await imageSharp.metadata();

  if (availableFormats.includes(metadata.format?.toUpperCase() ?? '')) {
    const newImageSharp = imageSharp
      .webp({
        lossless: false,
        quality: 100,
        alphaQuality: 100,
        preset: 'default',
        force: true,
      });

    return {
      format: sharp.format.webp,
      buffer: newImageSharp.toBuffer(),
    };
  }
  throw new Error(`Incompatible image format, send any of these: ${availableFormats.toString()}`);
}

export async function save(rawImage: Buffer): Promise<number> {
  try {
    const imageId = await storage.save(await processImage(rawImage));

    return imageId;
  } catch (error) {
    console.error('Failed to save image', error);

    throw new Error('Failed to save image');
  }
}

export default {
  save,
};
