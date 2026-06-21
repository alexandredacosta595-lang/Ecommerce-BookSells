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
    let initial: 'light' | 'dark';
    
    if (saved) {
      initial = saved;
    } else {
      // A/B Test: Randomly assign 50% users to Dark Mode and 50% to Light Mode
      initial = Math.random() > 0.5 ? 'dark' : 'light';
      localStorage.setItem('bookverse-theme', initial);
      localStorage.setItem('ab_test_theme_variant', initial);
      
      // We will track this assignment in App.tsx using PixelService
    }
    
    set({ theme: initial });
    if (initial === 'dark') {
      document.documentElement.classList.add('dark');
    } else {
      document.documentElement.classList.remove('dark');
    }
  },
}));
