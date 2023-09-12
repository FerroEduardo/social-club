import Storage from './storage';
import ImageData from '../interface/imageData';
import Logger from '../config/logger';

export default class DatabaseStorage extends Storage {
  async save(imageData: ImageData): Promise<number> {
    Logger.info('Saving database image');

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
