import Storage from './storage';
import ImageData from '../interface/imageData';

export default class DatabaseStorage extends Storage {
  async save(imageData: ImageData): Promise<number> {
    const image = await this.prisma.image.create(
      {
        data: {
          blob: await imageData.buffer,
          extension: imageData.format.id,
        },
        select: {
          id: true,
        },
      },
    );

    return image.id;
  }
}
