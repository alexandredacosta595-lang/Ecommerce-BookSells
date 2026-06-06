import { create } from 'zustand';

export interface ToastMessage {
  id: string;
  type: 'success' | 'info' | 'error' | 'warning';
  message: string;
  duration?: number;
}

interface NotificationState {
  toasts: ToastMessage[];
  showToast: (message: string, type?: ToastMessage['type'], duration?: number) => void;
  removeToast: (id: string) => void;
}

export const useNotificationStore = create<NotificationState>((set) => ({
  toasts: [],
  showToast: (message, type = 'success', duration = 3000) => {
    const id = `toast-${Date.now()}-${Math.random().toString(36).substr(2, 9)}`;
    const newToast: ToastMessage = { id, type, message, duration };
    set((state) => ({ toasts: [...state.toasts, newToast] }));

    setTimeout(() => {
      set((state) => ({
        toasts: state.toasts.filter((t) => t.id !== id),
      }));
    }, duration);
  },
  removeToast: (id) => {
    set((state) => ({
      toasts: state.toasts.filter((t) => t.id !== id),
    }));
  },
}));
