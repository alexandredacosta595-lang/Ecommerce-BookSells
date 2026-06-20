import api from './api';
import { Book, Author, Category, Review, Order } from '../types';

export interface PageResponse<T> {
  content: T[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
}

export interface BookFilters {
  search?: string;
  categoryId?: string;
  authorId?: string;
  type?: 'all' | 'physical' | 'digital';
  minPrice?: number;
  maxPrice?: number;
  minRating?: number;
  sortBy?: string;
  page?: number;
  size?: number;
}

export const bookService = {
  async getBooks(filters: BookFilters = {}): Promise<PageResponse<Book>> {
    const { data } = await api.get<PageResponse<Book>>('/books', { params: filters });
    return data;
  },

  async getAllBooks(): Promise<Book[]> {
    const { data } = await api.get<PageResponse<Book>>('/books', { params: { page: 1, size: 100 } });
    return data.content;
  },

  async getBook(id: string): Promise<Book> {
    const { data } = await api.get<Book>(`/books/${id}`);
    return data;
  },

  async getCategories(): Promise<Category[]> {
    const { data } = await api.get<Category[]>('/categories');
    return data;
  },

  async getAuthors(): Promise<Author[]> {
    const { data } = await api.get<Author[]>('/authors');
    return data;
  },

  async getReviews(bookId: string): Promise<Review[]> {
    const { data } = await api.get<Review[]>(`/reviews/book/${bookId}`);
    return data;
  },

  async addReview(bookId: string, rating: number, comment: string): Promise<Review> {
    const { data } = await api.post<Review>(`/reviews/book/${bookId}`, { rating, comment });
    return data;
  },

  async getWishlist(): Promise<string[]> {
    const { data } = await api.get<string[]>('/wishlist');
    return data;
  },

  async toggleWishlist(bookId: string): Promise<string[]> {
    const { data } = await api.post<string[]>(`/wishlist/${bookId}/toggle`);
    return data;
  },

  async getLibrary() {
    const { data } = await api.get('/library');
    return data;
  },

  async updateLibraryProgress(bookId: string, progress: number) {
    const { data } = await api.put(`/library/${bookId}/progress`, { progress });
    return data;
  },

  async markLibraryDownloaded(bookId: string) {
    const { data } = await api.put(`/library/${bookId}/download`);
    return data;
  },

  async createBook(book: Omit<Book, 'id'>) {
    const { data } = await api.post<Book>('/books', mapBookToRequest(book));
    return data;
  },

  async updateBook(id: string, book: Partial<Book>) {
    const { data } = await api.put<Book>(`/books/${id}`, mapBookToRequest(book as Book));
    return data;
  },

  async deleteBook(id: string) {
    await api.delete(`/books/${id}`);
  },

  async uploadFile(file: File): Promise<string> {
    const formData = new FormData();
    formData.append('file', file);
    const { data } = await api.post<{ url: string }>('/upload', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });
    // The backend returns '/uploads/filename.ext'. We return this to save in the book.
    return data.url;
  },
};

export const orderService = {
  async getOrders(): Promise<Order[]> {
    const { data } = await api.get<Order[]>('/orders');
    return data;
  },

  async createOrder(payload: {
    items: { bookId: string; selectedFormat: string; quantity: number }[];
    couponCode?: string;
    paymentMethod: string;
    shippingAddress: Order['shippingAddress'];
    billingAddress?: Order['billingAddress'];
  }): Promise<Order> {
    const { data } = await api.post<Order>('/orders', payload);
    return data;
  },

  async updateOrderStatus(orderId: string, status: Order['status']): Promise<Order> {
    const { data } = await api.patch<Order>(`/orders/${orderId}/status`, { status });
    return data;
  },

  async getAdminOrders(): Promise<Order[]> {
    const { data } = await api.get<Order[]>('/admin/orders');
    return data;
  },

  async getAdminStats() {
    const { data } = await api.get('/admin/stats');
    return data;
  },
};

export const couponService = {
  async validate(code: string, subtotal: number) {
    const { data } = await api.post('/coupons/validate', { code, subtotal });
    return data;
  },
};

function mapBookToRequest(book: Partial<Book>) {
  return {
    title: book.title,
    authorId: book.authorId,
    categoryId: book.categoryId,
    description: book.description,
    price: book.price,
    coverImage: book.coverImage,
    coverColor: book.coverColor,
    ebookFileUrl: book.ebookFileUrl,
    type: book.type?.toUpperCase(),
    formats: book.formats?.map((f) => f.toUpperCase()),
    stock: book.stock,
    pages: book.pages,
    publishedDate: book.publishedDate,
    bestSeller: book.bestSeller,
    newRelease: book.newRelease,
    isbn: book.isbn,
    publisher: book.publisher,
    sellerType: book.sellerType?.toUpperCase(),
    sellerName: book.sellerName,
    condition: book.condition?.toUpperCase(),
    conditionNotes: book.conditionNotes,
    city: book.city,
    state: book.state,
    whatsapp: book.whatsapp,
  };
}
