import { create } from 'zustand';
import { Book, Category, Author, Review, Order } from '../types';
import { bookService, orderService } from '../services/bookService';

interface LibraryItem {
  bookId: string;
  progress: number;
  format: 'pdf' | 'epub';
  lastRead: string;
  downloaded: boolean;
}

interface BookState {
  books: Book[];
  categories: Category[];
  authors: Author[];
  reviews: Review[];
  wishlist: string[];
  library: LibraryItem[];
  orders: Order[];
  isLoading: boolean;
  isInitialized: boolean;

  searchQuery: string;
  selectedCategoryId: string;
  selectedAuthorId: string;
  selectedType: 'all' | 'physical' | 'digital';
  priceRange: [number, number];
  minRating: number;
  sortBy: 'featured' | 'price-low' | 'price-high' | 'rating' | 'newest';
  currentPage: number;
  itemsPerPage: number;
  totalPages: number;

  initialize: () => Promise<void>;
  fetchBooks: () => Promise<void>;
  fetchAllBooks: () => Promise<void>;
  fetchAdminOrders: () => Promise<void>;
  setSearchQuery: (query: string) => void;
  setSelectedCategoryId: (id: string) => void;
  setSelectedAuthorId: (id: string) => void;
  setSelectedType: (type: 'all' | 'physical' | 'digital') => void;
  setPriceRange: (range: [number, number]) => void;
  setMinRating: (rating: number) => void;
  setSortBy: (sort: 'featured' | 'price-low' | 'price-high' | 'rating' | 'newest') => void;
  setCurrentPage: (page: number) => void;
  resetFilters: () => void;

  toggleWishlist: (bookId: string) => Promise<void>;
  isInWishlist: (bookId: string) => boolean;

  addReview: (review: Omit<Review, 'id' | 'date'>) => Promise<void>;
  getBookReviews: (bookId: string) => Review[];
  loadReviews: (bookId: string) => Promise<void>;

  updateReadingProgress: (bookId: string, progress: number) => Promise<void>;
  downloadFormat: (bookId: string) => Promise<void>;

  placeOrder: (order: Order) => void;
  createOrder: (payload: Parameters<typeof orderService.createOrder>[0]) => Promise<Order>;
  updateOrderStatus: (orderId: string, status: Order['status']) => Promise<void>;
  refreshOrders: () => Promise<void>;
  clearUserData: () => void;

  addBook: (book: Omit<Book, 'id'>) => Promise<void>;
  updateBook: (id: string, updated: Partial<Book>) => Promise<void>;
  deleteBook: (id: string) => Promise<void>;
}

export const useBookStore = create<BookState>((set, get) => ({
  books: [],
  categories: [],
  authors: [],
  reviews: [],
  wishlist: [],
  library: [],
  orders: [],
  isLoading: false,
  isInitialized: false,

  searchQuery: '',
  selectedCategoryId: '',
  selectedAuthorId: '',
  selectedType: 'all',
  priceRange: [0, 30000],
  minRating: 0,
  sortBy: 'featured',
  currentPage: 1,
  itemsPerPage: 12,
  totalPages: 1,

  initialize: async () => {
    if (get().isInitialized) return;
    set({ isLoading: true });
    try {
      const [categories, authors] = await Promise.all([
        bookService.getCategories(),
        bookService.getAuthors(),
      ]);
      set({ categories, authors, isInitialized: true });
      await get().fetchBooks();

      const token = localStorage.getItem('token');
      if (token) {
        const [wishlist, library, orders] = await Promise.all([
          bookService.getWishlist().catch(() => []),
          bookService.getLibrary().catch(() => []),
          orderService.getOrders().catch(() => []),
        ]);
        set({ wishlist, library, orders });
      }
    } finally {
      set({ isLoading: false });
    }
  },

  fetchBooks: async () => {
    const state = get();
    set({ isLoading: true });
    try {
      const result = await bookService.getBooks({
        search: state.searchQuery || undefined,
        categoryId: state.selectedCategoryId || undefined,
        authorId: state.selectedAuthorId || undefined,
        type: state.selectedType,
        minPrice: state.priceRange[0],
        maxPrice: state.priceRange[1],
        minRating: state.minRating,
        sortBy: state.sortBy,
        page: state.currentPage,
        size: state.itemsPerPage,
      });
      set({ books: result.content, totalPages: result.totalPages });
    } finally {
      set({ isLoading: false });
    }
  },

  fetchAllBooks: async () => {
    const books = await bookService.getAllBooks();
    set({ books });
  },

  fetchAdminOrders: async () => {
    const orders = await orderService.getAdminOrders();
    set({ orders });
  },

  setSearchQuery: (query) => { set({ searchQuery: query, currentPage: 1 }); get().fetchBooks(); },
  setSelectedCategoryId: (id) => { set({ selectedCategoryId: id, currentPage: 1 }); get().fetchBooks(); },
  setSelectedAuthorId: (id) => { set({ selectedAuthorId: id, currentPage: 1 }); get().fetchBooks(); },
  setSelectedType: (type) => { set({ selectedType: type, currentPage: 1 }); get().fetchBooks(); },
  setPriceRange: (range) => { set({ priceRange: range, currentPage: 1 }); get().fetchBooks(); },
  setMinRating: (rating) => { set({ minRating: rating, currentPage: 1 }); get().fetchBooks(); },
  setSortBy: (sort) => { set({ sortBy: sort, currentPage: 1 }); get().fetchBooks(); },
  setCurrentPage: (page) => { set({ currentPage: page }); get().fetchBooks(); },
  resetFilters: () => {
    set({
      searchQuery: '', selectedCategoryId: '', selectedAuthorId: '',
      selectedType: 'all', priceRange: [0, 30000], minRating: 0,
      sortBy: 'featured', currentPage: 1,
    });
    get().fetchBooks();
  },

  toggleWishlist: async (bookId) => {
    const wishlist = await bookService.toggleWishlist(bookId);
    set({ wishlist });
  },

  isInWishlist: (bookId) => get().wishlist.includes(bookId),

  addReview: async (review) => {
    const created = await bookService.addReview(review.bookId, review.rating, review.comment);
    set((state) => ({
      reviews: [...state.reviews.filter((r) => r.id !== created.id), created],
    }));
    await get().fetchBooks();
  },

  getBookReviews: (bookId) => get().reviews.filter((r) => r.bookId === bookId),

  loadReviews: async (bookId) => {
    const reviews = await bookService.getReviews(bookId);
    set((state) => ({
      reviews: [
        ...state.reviews.filter((r) => r.bookId !== bookId),
        ...reviews,
      ],
    }));
  },

  updateReadingProgress: async (bookId, progress) => {
    const updated = await bookService.updateLibraryProgress(bookId, progress);
    set((state) => ({
      library: state.library.map((item) =>
        item.bookId === bookId ? { ...item, progress: updated.progress, lastRead: updated.lastRead } : item
      ),
    }));
  },

  downloadFormat: async (bookId) => {
    const updated = await bookService.markLibraryDownloaded(bookId);
    set((state) => ({
      library: state.library.map((item) =>
        item.bookId === bookId ? { ...item, downloaded: updated.downloaded } : item
      ),
    }));
  },

  placeOrder: (order) => set((state) => ({ orders: [order, ...state.orders] })),

  createOrder: async (payload) => {
    const order = await orderService.createOrder(payload);
    set((state) => ({ orders: [order, ...state.orders] }));
    const library = await bookService.getLibrary().catch(() => get().library);
    set({ library });
    return order;
  },

  updateOrderStatus: async (orderId, status) => {
    const updated = await orderService.updateOrderStatus(orderId, status);
    set((state) => ({
      orders: state.orders.map((o) => (o.id === orderId ? updated : o)),
    }));
  },

  refreshOrders: async () => {
    const orders = await orderService.getOrders();
    set({ orders });
  },

  clearUserData: () => {
    set({ wishlist: [], library: [], orders: [] });
  },

  addBook: async (newBookData) => {
    const book = await bookService.createBook(newBookData);
    set((state) => ({ books: [book, ...state.books] }));
    const categories = await bookService.getCategories();
    set({ categories });
  },

  updateBook: async (id, updated) => {
    const book = await bookService.updateBook(id, updated);
    set((state) => ({
      books: state.books.map((b) => (b.id === id ? book : b)),
    }));
    const categories = await bookService.getCategories();
    set({ categories });
  },

  deleteBook: async (id) => {
    await bookService.deleteBook(id);
    set((state) => ({
      books: state.books.filter((b) => b.id !== id),
    }));
    const categories = await bookService.getCategories();
    set({ categories });
  },
}));
