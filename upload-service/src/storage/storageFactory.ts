import fs from 'fs';
import { prisma } from '../database';

import Storage from './storage';
import LocalStorage from './localStorage';
import S3Storage from './s3Storage';
import DatabaseStorage from './databaseStorage';

function validateStoragePath(storagePath: string) {
  if (!fs.lstatSync(storagePath).isDirectory()) {
    throw new Error('UPLOAD_STORAGE_PATH is not a directory');
  }
}

export function create(): Storage {
  const uploadStorageType = process.env.UPLOAD_STORAGE_TYPE;
  const uploadStoragePath = process.env.UPLOAD_STORAGE_PATH!;

  let currentStorage: Storage;
  if (uploadStorageType === 'LOCAL') {
    validateStoragePath(uploadStoragePath);
    currentStorage = new LocalStorage(prisma, uploadStoragePath);
  } else if (uploadStorageType === 'DATABASE') {
    currentStorage = new DatabaseStorage(prisma);
  } else if (uploadStorageType === 'S3') {
    currentStorage = new S3Storage(prisma);
  } else {
    throw new Error(`Unknown storage type: ${uploadStorageType}`);
  }

  return currentStorage;
}

export default {
  create,
};
