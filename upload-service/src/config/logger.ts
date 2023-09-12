import { createLogger, format, transports } from 'winston';
import DailyRotateFile from 'winston-daily-rotate-file';
import dotenv from 'dotenv';

dotenv.config();

const folder = process.env.LOG_FOLDER!;

const logger = createLogger({
  level: process.env.LOG_LEVEL,
  transports: [
    new transports.Console({
      format: format.combine(
        format.timestamp(),
        format.colorize(),
        format.printf(({ timestamp, level, message }) => `${timestamp} ${level}: ${message}`),
      ),
    }),
    new DailyRotateFile({
      dirname: folder,
      filename: 'upload-service-%DATE%.log',
      datePattern: 'YYYY-MM-DD',
      zippedArchive: true,
      maxSize: '10MB',
      maxFiles: '60d',
      utc: true,
      format: format.combine(
        format.timestamp(),
        format.simple(),
        format.printf(({ timestamp, level, message }) => `${timestamp} ${level}: ${message}`),
      ),
    }),
  ],
});

export default logger;
