import { Routes, Route, Navigate } from 'react-router-dom';
import { useAuthStore } from '@/store/useAuthStore';

// Pages
import Home from '@/pages/Home';
import Catalog from '@/pages/Catalog';
import BookDetails from '@/pages/BookDetails';
import CartPage from '@/pages/CartPage';
import CheckoutPage from '@/pages/CheckoutPage';
import LoginPage from '@/pages/LoginPage';
import RegisterPage from '@/pages/RegisterPage';
import Dashboard from '@/pages/Dashboard';
import DigitalLibrary from '@/pages/DigitalLibrary';
import AdminDashboard from '@/pages/AdminDashboard';

export default function AppRoutes() {
  const { isAuthenticated, user } = useAuthStore();

  return (
    <Routes>
      <Route path="/" element={<Home />} />
      <Route path="/catalog" element={<Catalog />} />
      <Route path="/book/:id" element={<BookDetails />} />
      <Route path="/cart" element={<CartPage />} />
      <Route path="/checkout" element={<CheckoutPage />} />
      <Route path="/login" element={<LoginPage />} />
      <Route path="/register" element={<RegisterPage />} />
      
      {/* Protected User Routes */}
      <Route 
        path="/dashboard" 
        element={isAuthenticated ? <Dashboard /> : <Navigate to="/login" replace />} 
      />
      <Route 
        path="/digital-library" 
        element={isAuthenticated ? <DigitalLibrary /> : <Navigate to="/login" replace />} 
      />
      
      {/* Protected Admin/Seller Routes */}
      <Route 
        path="/admin" 
        element={isAuthenticated && (user?.role === 'admin' || user?.role === 'seller') ? <AdminDashboard /> : <Navigate to="/login" replace />} 
      />

      {/* Fallback 404 handler */}
      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  );
}
