import { create } from 'zustand';
import { User, UserType } from '../types';
import { authService } from '../services/authService';

interface AuthState {
  user: User | null;
  isAuthenticated: boolean;
  isLoading: boolean;
  login: (email: string, password: string) => Promise<void>;
  register: (
    email: string,
    password: string,
    name: string,
    userType?: UserType,
    extra?: Partial<User>
  ) => Promise<void>;
  logout: () => void;
  loadSession: () => Promise<void>;
  updateProfile: (updated: Partial<User>) => Promise<void>;
}

export const useAuthStore = create<AuthState>((set, get) => ({
  user: null,
  isAuthenticated: false,
  isLoading: false,

  login: async (email, password) => {
    set({ isLoading: true });
    try {
      const { user } = await authService.login(email, password);
      set({ user, isAuthenticated: true, isLoading: false });
    } catch {
      set({ isLoading: false });
      throw new Error('Credenciais inválidas');
    }
  },

  register: async (email, password, name, userType = 'reader', extra = {}) => {
    set({ isLoading: true });
    try {
      const { user } = await authService.register({
        name,
        email,
        password,
        userType,
        companyName: extra.companyName,
        city: extra.city,
        state: extra.state,
        whatsapp: extra.whatsapp,
        phone: extra.phone,
      });
      set({ user, isAuthenticated: true, isLoading: false });
    } catch {
      set({ isLoading: false });
      throw new Error('Erro ao registar');
    }
  },

  logout: () => {
    authService.logout();
    import('./useBookStore').then(m => m.useBookStore.getState().clearUserData());
    set({ user: null, isAuthenticated: false });
  },

  loadSession: async () => {
    const token = authService.getToken();
    if (!token) {
      set({ user: null, isAuthenticated: false });
      return;
    }
    try {
      const user = await authService.getProfile();
      set({ user, isAuthenticated: true });
    } catch {
      authService.logout();
      set({ user: null, isAuthenticated: false });
    }
  },

  updateProfile: async (updated) => {
    const user = await authService.updateProfile(updated);
    set({ user });
  },
}));
