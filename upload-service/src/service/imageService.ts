import { prisma } from '../database';

export async function saveImage(data: Buffer): Promise<number> {
  try {
    const image = await prisma.image.create(
      {
        data: {
          blob: data,
        },
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
  saveImage,
};
