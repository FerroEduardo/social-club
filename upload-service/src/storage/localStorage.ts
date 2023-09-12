import fs from 'fs';
import crypto from 'crypto';
import { PrismaClient } from '@prisma/client';
import Storage from './storage';
import ImageData from '../interface/imageData';
import Logger from '../config/logger';

export default class LocalStorage extends Storage {
  readonly uploadStoragePath: string;

  constructor(prisma: PrismaClient, uploadStoragePath: string) {
    super(prisma);
    this.uploadStoragePath = uploadStoragePath;
  }

  async save(imageData: ImageData) {
    const filename = this.generateFilename();
    const filePath = `${this.uploadStoragePath}/${filename}`;

    fs.writeFileSync(filePath, await imageData.buffer);

    Logger.info(`Saving local image at ${filePath}`);

    const image = await this.prisma.image.create(
      {
        data: {
          local: filename,
          extension: imageData.format.id,
        },
        select: {
          id: true,
        },
      },
    );

    return image.id;
  }

  private generateFilename(): string {
    return crypto.randomUUID();
  }
}
