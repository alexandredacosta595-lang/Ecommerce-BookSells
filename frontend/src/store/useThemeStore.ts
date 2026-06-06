import { create } from 'zustand';

interface ThemeState {
  theme: 'light' | 'dark';
  toggleTheme: () => void;
  initializeTheme: () => void;
}

export const useThemeStore = create<ThemeState>((set, get) => ({
  theme: 'light',
  toggleTheme: () => {
    const nextTheme = get().theme === 'light' ? 'dark' : 'light';
    set({ theme: nextTheme });
    if (nextTheme === 'dark') {
      document.documentElement.classList.add('dark');
      localStorage.setItem('bookverse-theme', 'dark');
    } else {
      document.documentElement.classList.remove('dark');
      localStorage.setItem('bookverse-theme', 'light');
    }
  },
  initializeTheme: () => {
    const saved = localStorage.getItem('bookverse-theme') as 'light' | 'dark' | null;
    const systemPrefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches;
    const initial = saved || (systemPrefersDark ? 'dark' : 'light');
    
    set({ theme: initial });
    if (initial === 'dark') {
      document.documentElement.classList.add('dark');
    } else {
      document.documentElement.classList.remove('dark');
    }
  },
}));
