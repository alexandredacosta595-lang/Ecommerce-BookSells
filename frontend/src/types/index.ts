export type UserType = 'reader' | 'bookstore' | 'publisher' | 'author';

export interface Book {
  id: string;
  title: string;
  authorId: string;
  categoryId: string;
  description: string;
  price: number;
  rating: number;
  coverImage: string;
  coverColor: string; // Tailwind readable background color representation (e.g. from-blue-500 to-indigo-600)
  ebookFileUrl?: string;
  type: 'physical' | 'digital' | 'both';
  formats: ('physical' | 'pdf' | 'epub')[];
  stock: number;
  pages: number;
  publishedDate: string;
  bestSeller?: boolean;
  newRelease?: boolean;
  reviewsCount: number;
  isbn?: string;
  publisher?: string;
  
  // Connections additions
  sellerId?: string;
  sellerType?: UserType;
  sellerName?: string;
  condition?: 'new' | 'used_like_new' | 'used_very_good' | 'used_good' | 'used_acceptable';
  conditionNotes?: string;
  city?: string;
  state?: string;
  whatsapp?: string;
}

export interface Author {
  id: string;
  name: string;
  bio: string;
  avatar: string;
  booksCount: number;
}

export interface Category {
  id: string;
  name: string;
  slug: string;
  iconName: string; // Lucide icon identifier
  booksCount: number;
}

export interface User {
  id: string;
  name: string;
  email: string;
  role: 'user' | 'admin';
  avatar?: string;
  memberSince: string;
  bio?: string;
  
  // Connections additions
  userType?: UserType;
  companyName?: string; // e.g. "Livraria Cultura", "Editora Rocco"
  city?: string;
  state?: string;
  phone?: string;
  whatsapp?: string;
  website?: string;
}

export interface Review {
  id: string;
  bookId: string;
  userName: string;
  userAvatar?: string;
  rating: number;
  comment: string;
  date: string;
}

export interface CartItem {
  bookId: string;
  book: Book;
  quantity: number;
  selectedFormat: 'physical' | 'pdf' | 'epub';
}

export interface Cart {
  items: CartItem[];
  couponCode?: string;
  discount: number;
  subtotal: number;
  total: number;
}

export interface OrderItem {
  bookId: string;
  title: string;
  price: number;
  quantity: number;
  selectedFormat: 'physical' | 'pdf' | 'epub';
  coverImage?: string;
}

export interface Order {
  id: string;
  userId: string;
  items: OrderItem[];
  total: number;
  subtotal: number;
  tax: number;
  shippingCharge: number;
  discount: number;
  date: string;
  status: 'pending' | 'processing' | 'shipped' | 'delivered' | 'cancelled';
  shippingAddress: {
    fullName: string;
    street: string;
    city: string;
    state: string;
    zipCode: string;
    country: string;
    phone: string;
  };
  billingAddress?: {
    street: string;
    city: string;
    state: string;
    zipCode: string;
    country: string;
  };
  paymentMethod: 'card' | 'paypal' | 'gplay';
  trackingNumber?: string;
}
