import { create } from 'zustand';
import { User, UserType } from '../types';

interface AuthState {
  user: User | null;
  isAuthenticated: boolean;
  login: (email: string, name?: string, userType?: UserType, extra?: Partial<User>) => void;
  logout: () => void;
  toggleRole: () => void;
  updateProfile: (updated: Partial<User>) => void;
}

const DEFAULT_USER: User = {
  id: 'usr-412',
  name: 'Alexandre Da Costa',
  email: 'alexandredacosta595@gmail.com',
  role: 'user',
  userType: 'reader',
  avatar: 'https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=150&auto=format&fit=crop&q=80',
  memberSince: '2025-06-03',
  bio: 'Leitor ávido e entusiasta de livros físicos e digitais. Buscando e compartilhando novas leituras na Livraria Mulemba!',
};

export const useAuthStore = create<AuthState>((set) => ({
  user: DEFAULT_USER,
  isAuthenticated: true,
  login: (email, name = 'Alexandre Da Costa', userType = 'reader', extra = {}) => {
    set({
      user: {
        id: 'usr-custom',
        name,
        email,
        role: email.startsWith('admin') ? 'admin' : 'user',
        userType,
        avatar: `https://api.dicebear.com/7.x/adventurer/svg?seed=${encodeURIComponent(name)}`,
        memberSince: new Date().toISOString().split('T')[0],
        ...extra,
      },
      isAuthenticated: true,
    });
  },
  logout: () => {
    set({ user: null, isAuthenticated: false });
  },
  toggleRole: () => {
    set((state) => {
      if (!state.user) return {};
      const newRole = state.user.role === 'admin' ? 'user' : 'admin';
      return {
        user: { ...state.user, role: newRole },
      };
    });
  },
  updateProfile: (updated) => {
    set((state) => {
      if (!state.user) return {};
      return {
        user: { ...state.user, ...updated },
      };
    });
  },
}));
