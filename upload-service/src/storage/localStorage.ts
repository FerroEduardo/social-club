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
    const filename = this.generateFilename();
    const filePath = `${this.uploadStoragePath}/${filename}`;

    fs.writeFileSync(filePath, imageData);

    const image = await this.prisma.image.create(
      {
        data: {
          local: filename,
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
