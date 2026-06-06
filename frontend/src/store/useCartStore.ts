import { create } from 'zustand';
import { CartItem, Book } from '../types';
import { mockPromoCodes } from '../utils/mockData';

interface CartState {
  items: CartItem[];
  couponCode: string;
  discount: number;
  addItem: (book: Book, format: 'physical' | 'pdf' | 'epub') => void;
  removeItem: (bookId: string, format: 'physical' | 'pdf' | 'epub') => void;
  updateQuantity: (bookId: string, format: 'physical' | 'pdf' | 'epub', quantity: number) => void;
  applyCoupon: (code: string) => boolean;
  removeCoupon: () => void;
  clearCart: () => void;
  getCartSummary: () => {
    subtotal: number;
    discount: number;
    shipping: number;
    tax: number;
    total: number;
    hasPhysical: boolean;
  };
}

export const useCartStore = create<CartState>((set, get) => ({
  items: [],
  couponCode: '',
  discount: 0,

  addItem: (book, format) => {
    set((state) => {
      const existingIndex = state.items.findIndex(
        (item) => item.bookId === book.id && item.selectedFormat === format
      );

      if (existingIndex > -1) {
        const updatedItems = [...state.items];
        updatedItems[existingIndex].quantity += 1;
        return { items: updatedItems };
      }

      return {
        items: [...state.items, { bookId: book.id, book, quantity: 1, selectedFormat: format }],
      };
    });
  },

  removeItem: (bookId, format) => {
    set((state) => ({
      items: state.items.filter((item) => !(item.bookId === bookId && item.selectedFormat === format)),
    }));
  },

  updateQuantity: (bookId, format, quantity) => {
    if (quantity <= 0) {
      get().removeItem(bookId, format);
      return;
    }
    set((state) => ({
      items: state.items.map((item) =>
        item.bookId === bookId && item.selectedFormat === format ? { ...item, quantity } : item
      ),
    }));
  },

  applyCoupon: (code) => {
    const formattedCode = code.trim().toUpperCase();
    if (mockPromoCodes[formattedCode] !== undefined) {
      set({ couponCode: formattedCode });
      return true;
    }
    return false;
  },

  removeCoupon: () => {
    set({ couponCode: '', discount: 0 });
  },

  clearCart: () => {
    set({ items: [], couponCode: '', discount: 0 });
  },

  getCartSummary: () => {
    const { items, couponCode } = get();
    const subtotal = items.reduce((sum, item) => sum + item.book.price * item.quantity, 0);

    let discount = 0;
    if (couponCode) {
      const discountValue = mockPromoCodes[couponCode];
      if (discountValue < 1) {
        // Percentage discount
        discount = subtotal * discountValue;
      } else {
        // Flat discount
        discount = Math.min(subtotal, discountValue);
      }
    }

    const hasPhysical = items.some((item) => item.selectedFormat === 'physical');
    const shipping = hasPhysical ? (subtotal - discount >= 50 ? 0 : 4.99) : 0;
    const taxableAmount = Math.max(0, subtotal - discount);
    const tax = taxableAmount * 0.08; // 8% local tax
    const total = taxableAmount + shipping + tax;

    return {
      subtotal,
      discount,
      shipping,
      tax,
      total,
      hasPhysical,
    };
  },
}));
