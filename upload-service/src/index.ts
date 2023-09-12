import express, {
  Express, Request, Response, RequestHandler,
} from 'express';
import dotenv from 'dotenv';
import fs from 'fs';
import morgan from 'morgan';

import ErrorCode from './enum/errorCode';
import ImageService from './service/imageService';
import { upload, isMemoryStorage } from './config/multerConfig';
import Logger from './config/logger';

dotenv.config();

const app: Express = express();
const port: string = process.env.SERVER_PORT ?? '8000';

const morganMiddleware = morgan(
  ':method :url :status :res[content-length] - :response-time ms',
  {
    stream: {
      write: (message) => Logger.http(message.trim()),
    },
  },
);

app.use(morganMiddleware);

app.get('/', (req: Request, res: Response) => {
  res.send('Express + TypeScript Server');
});

app.post('/', upload.single('image'), (async (req: Request, res: Response) => {
  try {
    const { file } = req;

    if (!file) {
      Logger.debug('Missing image');
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
    const imageId = await ImageService.save(rawImageBuffer);

    res
      .status(201)
      .send({
        id: imageId,
      });
  } catch (error) {
    if (error instanceof Error) {
      const msg = String(error);
      Logger.debug(msg);

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

    Logger.error({ message: 'Unhandled error', error });
    res.sendStatus(500);
  }
}) as RequestHandler);

app.listen(port, () => {
  Logger.info(`Server is running at http://localhost:${port}`);
});
