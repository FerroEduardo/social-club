import multer from 'multer';
import dotenv from 'dotenv';

dotenv.config();

export const isMemoryStorage = process.env.MULTER_UPLOAD_STORAGE_TYPE === 'MEMORY';
let multerStorage: multer.StorageEngine;
if (isMemoryStorage) {
  multerStorage = multer.memoryStorage();
} else {
  multerStorage = multer.diskStorage({
    filename(req, file, cb) {
      const uniqueSuffix = `${Date.now()}-${Math.round(Math.random() * 1E9)}`;
      cb(null, `multer-${file.fieldname}-${uniqueSuffix}`);
    },
    destination: process.env.MULTER_UPLOAD_FOLDER ?? '/tmp',
  });
}

export const upload = multer({
  storage: multerStorage,
  limits: {
    fileSize: 10_000_000, // bytes
    files: 1,
  },
});

export default {
  upload,
  isMemoryStorage,
};
