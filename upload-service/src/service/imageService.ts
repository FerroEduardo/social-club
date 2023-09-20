import dotenv from 'dotenv';
import sharp from 'sharp';
import StorageFactory from '../storage/storageFactory';
import ImageData from '../interface/imageData';

dotenv.config();

const availableFormats = (process.env.SHARP_ALLOWED_EXTENSIONS ?? '').split(',');

const storage = StorageFactory.create();

export async function processImage(rawImage: Buffer, options?: ImageOptions): Promise<ImageData> {
  const imageSharp = sharp(rawImage);
  const metadata = await imageSharp.metadata();

  if (availableFormats.includes(metadata.format?.toUpperCase() ?? '')) {
    let newImageSharp = imageSharp
      .webp({
        lossless: false,
        quality: 80,
        alphaQuality: 100,
        preset: 'default',
        force: true,
      });

    if (options?.resizeOptions) {
      newImageSharp = newImageSharp.resize(options.resizeOptions);
    }

    return {
      format: sharp.format.webp,
      buffer: newImageSharp.toBuffer(),
    };
  }
  throw new Error(`Incompatible image format, send any of these: ${availableFormats.toString()}`);
}

interface ImageOptions {
  resizeOptions?: sharp.ResizeOptions;
}

export async function save(rawImage: Buffer, options?: ImageOptions): Promise<number> {
  try {
    const imageId = await storage.save(await processImage(rawImage, options));

    return imageId;
  } catch (error) {
    console.error('Failed to save image', error);

    throw new Error('Failed to save image');
  }
}

export default {
  save,
};
