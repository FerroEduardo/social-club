import Storage from './storage';

export default class DatabaseStorage extends Storage {
  async save(imageData: Buffer): Promise<number> {
    const image = await this.prisma.image.create(
      {
        data: {
          blob: imageData,
        },
        select: {
          id: true,
        },
      },
    );

    return image.id;
  }
}
