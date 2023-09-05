/* eslint-disable class-methods-use-this */
import { PrismaClient } from '@prisma/client';
import ImageData from '../interface/imageData';

export default abstract class Storage {
  protected prisma: PrismaClient;

  constructor(prisma: PrismaClient) {
    this.prisma = prisma;
  }

  abstract save(imageData: ImageData): number | Promise<number>;
}
