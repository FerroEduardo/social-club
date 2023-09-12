import { PrismaClient } from '@prisma/client';
import Logger from './config/logger';

export const prisma = new PrismaClient({
  log: [
    {
      emit: 'event',
      level: 'query',
    },
    {
      emit: 'event',
      level: 'error',
    },
    {
      emit: 'event',
      level: 'info',
    },
    {
      emit: 'event',
      level: 'warn',
    },
  ],
});

prisma.$on('query', (e) => {
  Logger.info(`Query: ${e.query} - Params: ${e.params} - Duration: ${e.duration}ms`);
});

prisma.$on('error', (e) => {
  Logger.error(`Message: ${e.message} - Timestamp ${e.timestamp.toUTCString()}`);
});

prisma.$on('info', (e) => {
  Logger.info(`Message: ${e.message} - Timestamp ${e.timestamp.toUTCString()}`);
});

prisma.$on('warn', (e) => {
  Logger.warn(`Message: ${e.message} - Timestamp ${e.timestamp.toUTCString()}`);
});

export default {
  prisma,
};
