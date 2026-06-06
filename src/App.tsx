import { useEffect } from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { useThemeStore } from './store/useThemeStore';
import { useAuthStore } from './store/useAuthStore';
import Navbar from './components/Navbar';
import Footer from './components/Footer';
import ToastNotifications from './components/ToastNotifications';

// Pages
import Home from './pages/Home';
import Catalog from './pages/Catalog';
import BookDetails from './pages/BookDetails';
import CartPage from './pages/CartPage';
import CheckoutPage from './pages/CheckoutPage';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import Dashboard from './pages/Dashboard';
import DigitalLibrary from './pages/DigitalLibrary';
import AdminDashboard from './pages/AdminDashboard';

export default function App() {
  const { theme } = useThemeStore();
  const { isAuthenticated, user } = useAuthStore();
  const isDarkMode = theme === 'dark';

  // Sync systemic theme variables
  useEffect(() => {
    const root = window.document.documentElement;
    if (isDarkMode) {
      root.classList.add('dark');
    } else {
      root.classList.remove('dark');
    }
  }, [isDarkMode]);

  return (
    <BrowserRouter>
      <div 
        className="min-h-screen flex flex-col transition-colors duration-200 bg-zinc-50 dark:bg-zinc-950 text-zinc-910 dark:text-zinc-100 selection:bg-blue-600 selection:text-white"
        id="bookverse-root-node"
      >
        {/* Animated header navigation bar */}
        <Navbar />

        {/* Dynamic responsive main slot */}
        <main className="flex-grow max-w-7xl w-full mx-auto px-4 sm:px-6 lg:px-8 py-6 mb-12">
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/catalog" element={<Catalog />} />
            <Route path="/book/:id" element={<BookDetails />} />
            <Route path="/cart" element={<CartPage />} />
            <Route path="/checkout" element={<CheckoutPage />} />
            <Route path="/login" element={<LoginPage />} />
            <Route path="/register" element={<RegisterPage />} />
            
            {/* User Profile tab routes */}
            <Route 
              path="/dashboard" 
              element={isAuthenticated ? <Dashboard /> : <Navigate to="/login" replace />} 
            />
            
            {/* Digital bookshelf reader routes */}
            <Route 
              path="/digital-library" 
              element={isAuthenticated ? <DigitalLibrary /> : <Navigate to="/login" replace />} 
            />
            
            {/* Secured administrator panel parameters */}
            <Route 
              path="/admin" 
              element={isAuthenticated && user?.role === 'admin' ? <AdminDashboard /> : <Navigate to="/login" replace />} 
            />

            {/* Spillover 404 handler reroutes */}
            <Route path="*" element={<Navigate to="/" replace />} />
          </Routes>
        </main>

        {/* Informative customized site footer */}
        <Footer />

        {/* Global absolute notifications overlay */}
        <ToastNotifications />
      </div>
    </BrowserRouter>
  );
}
