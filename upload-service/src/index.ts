import express, { Express, Request, Response } from 'express';
import dotenv from 'dotenv';
import sharp from 'sharp';
import multer from 'multer';
import fs from 'fs';

dotenv.config();

const isMemoryStorage = process.env.UPLOAD_STORAGE_TYPE === 'MEMORY'
let multerStorage: multer.StorageEngine;
if (isMemoryStorage) {
  multerStorage = multer.memoryStorage()
} else {
  multerStorage = multer.diskStorage({
    destination: process.env.UPLOAD_FOLDER ?? '/tmp',
  })
}

const upload = multer({
  storage: multerStorage,
  limits: {
    fileSize: 5000000, // bytes
    files: 1
  }
});

const availableFormats = (process.env.ALLOWED_EXTENSIONS ?? '').split(',');

const app: Express = express();
const port: String = process.env.PORT ?? '8000';

app.get('/', (req: Request, res: Response) => {
  res.send('Express + TypeScript Server');
});

app.post('/', upload.single('image'), async (req: Request, res: Response) => {
  try {

      if (!req.file) {
        return res.send({
          message: 'Missing image'
        }).status(400);
      }

      const { file } = req;

      let buffer;
      if (isMemoryStorage) {
        buffer = file.buffer
      } else {
        buffer = fs.readFileSync(file.path);
      }
      let imageSharp = sharp(buffer);
      const metadata = await imageSharp.metadata();

      if (availableFormats.includes(metadata.format?.toUpperCase() ?? '')) {
        imageSharp = imageSharp.webp({
          lossless: false,
          quality: 80,
          alphaQuality: 100,
          preset: 'default',
          force: true
        })
        res.set('Content-Type', 'image/webp');
        return res.send(await imageSharp.toBuffer()).status(200);
      } else {
        return res.send({
          message: 'Incompatible image format, send any of these: ' + availableFormats
        }).status(400);
      }
  } catch (error) {
    console.error({error});
    return res.sendStatus(500);
  }
});

app.listen(port, () => {
  console.log(`Server is running at http://localhost:${port}`);
});
