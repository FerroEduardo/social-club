import fs from 'fs';
import crypto from 'crypto';
import { PrismaClient } from '@prisma/client';
import Storage from './storage';

export default class LocalStorage extends Storage {
  readonly uploadStoragePath: string;

  constructor(prisma: PrismaClient, uploadStoragePath: string) {
    super(prisma);
    this.uploadStoragePath = uploadStoragePath;
  }

  async save(imageData: Buffer) {
    const filePath = `${this.uploadStoragePath}/${this.generateFilename()}`;

    fs.writeFileSync(filePath, imageData);

    const image = await this.prisma.image.create(
      {
        data: {
          local: filePath,
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
