import express, {
  Express, Request, Response, RequestHandler,
} from 'express';
import dotenv from 'dotenv';
import fs from 'fs';

import ErrorCode from './enum/errorCode';
import ImageService from './service/imageService';
import { upload, isMemoryStorage } from './config/multerConfig';

dotenv.config();

const app: Express = express();
const port: string = process.env.SERVER_PORT ?? '8000';

app.get('/', (req: Request, res: Response) => {
  res.send('Express + TypeScript Server');
});

app.post('/', upload.single('image'), (async (req: Request, res: Response) => {
  try {
    const { file } = req;

    if (!file) {
      res
        .status(400)
        .send({
          message: 'Missing image',
          code: ErrorCode.MissingImage,
        });
      return;
    }

    let rawImageBuffer;
    if (isMemoryStorage) {
      rawImageBuffer = file.buffer;
    } else {
      rawImageBuffer = fs.readFileSync(file.path);
    }
    const processedImageBuffer = await ImageService.processImage(rawImageBuffer);
    const imageId = await ImageService.saveImage(processedImageBuffer);

    res
      .status(201)
      .send({
        id: imageId,
      });
  } catch (error) {
    console.error({ error });

    if (error instanceof Error) {
      const msg = String(error);

      if (msg === 'Error: Input buffer contains unsupported image format') {
        res
          .status(400)
          .send({
            message: 'Invalid filetype',
            code: ErrorCode.InvalidFileType,
          });
        return;
      }
      if (msg.startsWith('Incompatible image format')) {
        res
          .status(400)
          .send({
            message: msg,
            code: ErrorCode.IncompatibleImageFormat,
          });
        return;
      }
    }

    res.sendStatus(500);
  }
}) as RequestHandler);

app.listen(port, () => {
  console.log(`Server is running at http://localhost:${port}`);
});
