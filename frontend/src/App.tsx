import { useEffect } from 'react';
import { BrowserRouter } from 'react-router-dom';
import { useThemeStore } from '@/store/useThemeStore';
import { useAuthStore } from '@/store/useAuthStore';
import { useBookStore } from '@/store/useBookStore';
import Navbar from '@/components/Navbar';
import Footer from '@/components/Footer';
import ToastNotifications from '@/components/ToastNotifications';
import AppRoutes from '@/routes';
import HeatmapTracker from '@/components/HeatmapTracker';
import { PixelService } from '@/services/PixelService';

export default function App() {
  const { theme } = useThemeStore();
  const isDarkMode = theme === 'dark';
  const loadSession = useAuthStore((s) => s.loadSession);
  const initialize = useBookStore((s) => s.initialize);

  useEffect(() => {
    const root = window.document.documentElement;
    if (isDarkMode) {
      root.classList.add('dark');
    } else {
      root.classList.remove('dark');
    }
  }, [isDarkMode]);

  // Pixel and A/B Tracking Initialization
  useEffect(() => {
    const variant = localStorage.getItem('ab_test_theme_variant');
    if (variant) {
      PixelService.track('ab_test_view', { variant });
    }
    
    // Simplistic page_view track on load
    PixelService.track('page_view', { initial: true });
  }, []);

  useEffect(() => {
    loadSession();
    initialize();
  }, [loadSession, initialize]);

  return (
    <BrowserRouter>
      <div 
        className="min-h-screen flex flex-col transition-colors duration-200 bg-zinc-50 dark:bg-zinc-950 text-zinc-910 dark:text-zinc-100 selection:bg-blue-600 selection:text-white"
        id="bookverse-root-node"
      >
        <Navbar />

        <main className="flex-grow max-w-7xl w-full mx-auto px-4 sm:px-6 lg:px-8 py-6 mb-12">
          <AppRoutes />
        </main>

        <Footer />
        <ToastNotifications />
        <HeatmapTracker />
      </div>
    </BrowserRouter>
  );
}
