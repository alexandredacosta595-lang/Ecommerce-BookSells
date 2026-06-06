import { create } from 'zustand';
import { Book, Category, Author, Review, Order, User } from '../types';
import { mockBooks, mockCategories, mockAuthors, mockReviews } from '../utils/mockData';

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
  wishlist: string[]; // bookIds
  library: LibraryItem[];
  orders: Order[];

  // Filter States
  searchQuery: string;
  selectedCategoryId: string;
  selectedAuthorId: string;
  selectedType: 'all' | 'physical' | 'digital';
  priceRange: [number, number];
  minRating: number;
  sortBy: 'featured' | 'price-low' | 'price-high' | 'rating' | 'newest';
  currentPage: number;
  itemsPerPage: number;

  // Set Filters
  setSearchQuery: (query: string) => void;
  setSelectedCategoryId: (id: string) => void;
  setSelectedAuthorId: (id: string) => void;
  setSelectedType: (type: 'all' | 'physical' | 'digital') => void;
  setPriceRange: (range: [number, number]) => void;
  setMinRating: (rating: number) => void;
  setSortBy: (sort: 'featured' | 'price-low' | 'price-high' | 'rating' | 'newest') => void;
  setCurrentPage: (page: number) => void;
  resetFilters: () => void;

  // Wishlist Actions
  toggleWishlist: (bookId: string) => void;
  isInWishlist: (bookId: string) => boolean;

  // Review Actions
  addReview: (review: Omit<Review, 'id' | 'date'>) => void;
  getBookReviews: (bookId: string) => Review[];

  // Library Actions
  updateReadingProgress: (bookId: string, progress: number) => void;
  downloadFormat: (bookId: string) => void;
  addBooksToLibraryOnPurchase: (purchaseItems: { bookId: string; format: 'physical' | 'pdf' | 'epub' }[]) => void;

  // Order Actions
  placeOrder: (order: Order) => void;
  updateOrderStatus: (orderId: string, status: Order['status']) => void;

  // Admin Actions
  addBook: (book: Omit<Book, 'id'>) => void;
  updateBook: (id: string, updated: Partial<Book>) => void;
  deleteBook: (id: string) => void;
}

const INITIAL_LIBRARY: LibraryItem[] = [
  { bookId: 'book-2', progress: 45, format: 'pdf', lastRead: '2026-06-02', downloaded: true },
  { bookId: 'book-8', progress: 12, format: 'epub', lastRead: '2026-05-18', downloaded: false },
  { bookId: 'book-13', progress: 85, format: 'epub', lastRead: '2026-06-03', downloaded: true },
];

const INITIAL_ORDERS: Order[] = [
  {
    id: 'ord-1001',
    userId: 'usr-412',
    date: '2026-05-15',
    status: 'delivered',
    subtotal: 37.95,
    discount: 5.00,
    shippingCharge: 4.99,
    tax: 2.63,
    total: 40.57,
    paymentMethod: 'card',
    shippingAddress: {
      fullName: 'Alexandre Da Costa',
      street: '124 Science Way, Apt 3B',
      city: 'Boston',
      state: 'MA',
      zipCode: '02111',
      country: 'USA',
      phone: '+1 617-555-0199',
    },
    items: [
      { bookId: 'book-4', title: 'Habits of Mind: Structural Routine', price: 19.95, quantity: 1, selectedFormat: 'physical' },
      { bookId: 'book-13', title: 'Mindfulness & Neural Re-wiring', price: 18.00, quantity: 1, selectedFormat: 'epub' }
    ],
    trackingNumber: 'TRK-89210291-US',
  },
  {
    id: 'ord-1002',
    userId: 'usr-412',
    date: '2026-06-02',
    status: 'processing',
    subtotal: 49.99,
    discount: 0.00,
    shippingCharge: 0.00,
    tax: 4.00,
    total: 53.99,
    paymentMethod: 'card',
    shippingAddress: {
      fullName: 'Alexandre Da Costa',
      street: '124 Science Way, Apt 3B',
      city: 'Boston',
      state: 'MA',
      zipCode: '02111',
      country: 'USA',
      phone: '+1 617-555-0199',
    },
    items: [
      { bookId: 'book-2', title: 'Cognitive Synapse: Algorithmic Logic', price: 49.99, quantity: 1, selectedFormat: 'pdf' }
    ],
  }
];

export const useBookStore = create<BookState>((set, get) => ({
  books: mockBooks,
  categories: mockCategories,
  authors: mockAuthors,
  reviews: mockReviews,
  wishlist: ['book-1', 'book-4', 'book-11'],
  library: INITIAL_LIBRARY,
  orders: INITIAL_ORDERS,

  // Filter Initial State
  searchQuery: '',
  selectedCategoryId: '',
  selectedAuthorId: '',
  selectedType: 'all',
  priceRange: [0, 60],
  minRating: 0,
  sortBy: 'featured',
  currentPage: 1,
  itemsPerPage: 12,

  setSearchQuery: (query) => set({ searchQuery: query, currentPage: 1 }),
  setSelectedCategoryId: (id) => set({ selectedCategoryId: id, currentPage: 1 }),
  setSelectedAuthorId: (id) => set({ selectedAuthorId: id, currentPage: 1 }),
  setSelectedType: (type) => set({ selectedType: type, currentPage: 1 }),
  setPriceRange: (range) => set({ priceRange: range, currentPage: 1 }),
  setMinRating: (rating) => set({ minRating: rating, currentPage: 1 }),
  setSortBy: (sort) => set({ sortBy: sort, currentPage: 1 }),
  setCurrentPage: (page) => set({ currentPage: page }),
  resetFilters: () => set({
    searchQuery: '',
    selectedCategoryId: '',
    selectedAuthorId: '',
    selectedType: 'all',
    priceRange: [0, 60],
    minRating: 0,
    sortBy: 'featured',
    currentPage: 1,
  }),

  toggleWishlist: (bookId) => set((state) => {
    const exists = state.wishlist.includes(bookId);
    return {
      wishlist: exists
        ? state.wishlist.filter((id) => id !== bookId)
        : [...state.wishlist, bookId],
    };
  }),

  isInWishlist: (bookId) => {
    return get().wishlist.includes(bookId);
  },

  addReview: (review) => set((state) => {
    const id = `rev-${state.reviews.length + 1}`;
    const date = new Date().toISOString().split('T')[0];
    const newReview = { ...review, id, date } as Review;
    
    // Recalculate book rating
    const updatedBooks = state.books.map((b) => {
      if (b.id === review.bookId) {
        const bookReviews = [...state.reviews.filter((r) => r.bookId === b.id), newReview];
        const newRating = parseFloat((bookReviews.reduce((sum, r) => sum + r.rating, 0) / bookReviews.length).toFixed(1));
        return {
          ...b,
          rating: newRating,
          reviewsCount: bookReviews.length,
        };
      }
      return b;
    });

    return {
      reviews: [...state.reviews, newReview],
      books: updatedBooks,
    };
  }),

  getBookReviews: (bookId) => {
    return get().reviews.filter((r) => r.bookId === bookId);
  },

  updateReadingProgress: (bookId, progress) => set((state) => ({
    library: state.library.map((item) =>
      item.bookId === bookId
        ? { ...item, progress: Math.min(100, Math.max(0, progress)), lastRead: new Date().toISOString().split('T')[0] }
        : item
    ),
  })),

  downloadFormat: (bookId) => set((state) => ({
    library: state.library.map((item) =>
      item.bookId === bookId ? { ...item, downloaded: true } : item
    ),
  })),

  addBooksToLibraryOnPurchase: (purchaseItems) => set((state) => {
    const currentLib = [...state.library];
    
    purchaseItems.forEach((item) => {
      if (item.format === 'physical') return; // physical books not inside digital library
      const exists = currentLib.some((lib) => lib.bookId === item.bookId);
      if (!exists) {
        currentLib.push({
          bookId: item.bookId,
          progress: 0,
          format: item.format,
          lastRead: new Date().toISOString().split('T')[0],
          downloaded: false,
        });
      }
    });

    return { library: currentLib };
  }),

  placeOrder: (order) => set((state) => ({
    orders: [order, ...state.orders],
  })),

  updateOrderStatus: (orderId, status) => set((state) => ({
    orders: state.orders.map((ord) =>
      ord.id === orderId ? { ...ord, status } : ord
    ),
  })),

  // ADMIN ACTIONS
  addBook: (newBookData) => set((state) => {
    const id = `book-${state.books.length + 1}`;
    const newBook: Book = {
      ...newBookData,
      id,
      reviewsCount: 0,
      rating: 5.0,
    };

    // Update categories count
    const updatedCategories = state.categories.map((cat) => {
      if (cat.id === newBook.categoryId) {
        return { ...cat, booksCount: cat.booksCount + 1 };
      }
      return cat;
    });

    return {
      books: [newBook, ...state.books],
      categories: updatedCategories,
    };
  }),

  updateBook: (id, updated) => set((state) => ({
    books: state.books.map((b) => (b.id === id ? { ...b, ...updated } : b)),
  })),

  deleteBook: (id) => set((state) => {
    const bookToDelete = state.books.find((b) => b.id === id);
    if (!bookToDelete) return {};

    // Update categories count
    const updatedCategories = state.categories.map((cat) => {
      if (cat.id === bookToDelete.categoryId) {
        return { ...cat, booksCount: Math.max(0, cat.booksCount - 1) };
      }
      return cat;
    });

    return {
      books: state.books.filter((b) => b.id !== id),
      categories: updatedCategories,
    };
  }),
}));
