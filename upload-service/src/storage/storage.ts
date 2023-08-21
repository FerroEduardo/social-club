/* eslint-disable class-methods-use-this */
import { PrismaClient } from '@prisma/client';

export default abstract class Storage {
  protected prisma: PrismaClient;

  constructor(prisma: PrismaClient) {
    this.prisma = prisma;
  }

  abstract save(imageData: Buffer): number | Promise<number>;
}
