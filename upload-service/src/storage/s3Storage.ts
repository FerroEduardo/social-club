import Storage from './storage';
import ImageData from '../interface/imageData';

export default class S3Storage extends Storage {
  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  save(imageData: ImageData): Promise<number> {
    throw new Error('');
  }
}
