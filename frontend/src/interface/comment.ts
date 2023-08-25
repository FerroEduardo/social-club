export default interface Comment {
  id: number;
  value: string;
  createdAt: string;
  authorId: number;
  authorName: string;
  authorImageUrl: string;
}
