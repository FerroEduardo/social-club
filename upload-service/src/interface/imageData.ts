import sharp from 'sharp';

export default interface ImageData {
  format: sharp.AvailableFormatInfo;
  buffer: Promise<Buffer>;
}
