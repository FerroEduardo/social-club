import Storage from './storage';

export default class S3Storage extends Storage {
  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  save(imageData: Buffer): Promise<number> {
    throw new Error('');
  }
}
