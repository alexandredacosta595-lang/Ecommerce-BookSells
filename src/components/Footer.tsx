import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import { useNotificationStore } from '../store/useNotificationStore';
import { BookOpen, Send, Github, Twitter, Linkedin, Shield, Heart } from 'lucide-react';

export default function Footer() {
  const [email, setEmail] = useState('');
  const showToast = useNotificationStore((state) => state.showToast);

  const handleSubscribe = (e: React.FormEvent) => {
    e.preventDefault();
    if (!email.trim() || !email.includes('@')) {
      showToast('Please enter a valid email address.', 'warning');
      return;
    }
    showToast(`Welcome! You've successfully subscribed with: ${email}`, 'success');
    setEmail('');
  };

  return (
    <footer className="border-t border-zinc-150 bg-zinc-50 dark:border-zinc-800 dark:bg-zinc-950/60" id="bookverse-main-footer">
      <div className="mx-auto max-w-7xl px-4 py-12 sm:px-6 lg:px-8 lg:py-16">
        <div className="grid grid-cols-1 gap-12 lg:grid-cols-3">
          
          {/* BRAND AND DETAILS */}
          <div className="space-y-4">
            <div className="flex items-center gap-2">
              <div className="flex h-10 w-10 items-center justify-center rounded-xl bg-blue-600 text-white shadow-md shadow-blue-500/10">
                <BookOpen className="h-5 w-5" />
              </div>
              <span className="text-xl font-bold tracking-tight text-zinc-900 dark:text-zinc-50 font-sans">
                Livraria <span className="text-amber-500 font-serif">Mulemba</span>
              </span>
            </div>
            <p className="text-sm text-zinc-500 dark:text-zinc-400 max-w-sm leading-relaxed">
              Curating unparalleled reading journeys across both tactile, smell-of-ink physical bindings and seamless, instant-open EPUB/PDF digital formats. Created with absolute architectural craft.
            </p>
            <div className="flex gap-4.5 pt-2">
              <a href="#" className="text-zinc-400 hover:text-blue-600 transition-colors">
                <Github className="h-5 w-5" />
              </a>
              <a href="#" className="text-zinc-400 hover:text-blue-600 transition-colors">
                <Twitter className="h-5 w-5" />
              </a>
              <a href="#" className="text-zinc-400 hover:text-blue-600 transition-colors">
                <Linkedin className="h-5 w-5" />
              </a>
            </div>
          </div>

          {/* LINK SEGMENTS */}
          <div className="grid grid-cols-2 gap-8 lg:col-span-2 sm:grid-cols-3">
            <div>
              <h5 className="text-xs font-bold uppercase tracking-widest text-zinc-400 dark:text-zinc-500 font-mono">
                Store Catalog
              </h5>
              <ul className="mt-4 space-y-2.5">
                <li>
                  <Link to="/catalog" className="text-sm text-zinc-500 hover:text-zinc-90 w-fit dark:text-zinc-400 dark:hover:text-zinc-100 transition-colors">
                    Physical Bindings
                  </Link>
                </li>
                <li>
                  <Link to="/catalog" className="text-sm text-zinc-500 hover:text-zinc-90 pt-0.5 w-fit dark:text-zinc-400 dark:hover:text-zinc-100 transition-colors">
                    Digital ePUBs
                  </Link>
                </li>
                <li>
                  <Link to="/catalog" className="text-sm text-zinc-500 hover:text-zinc-90 pt-0.5 w-fit dark:text-zinc-400 dark:hover:text-zinc-100 transition-colors">
                    PDF Deliveries
                  </Link>
                </li>
                <li>
                  <span className="text-xs font-mono text-zinc-405 dark:text-zinc-510 bg-zinc-200 dark:bg-zinc-850 px-1.5 py-0.5 rounded-md inline-block mt-1">
                    Spring Boot REST prepared
                  </span>
                </li>
              </ul>
            </div>

            <div>
              <h5 className="text-xs font-bold uppercase tracking-widest text-zinc-400 dark:text-zinc-505 font-mono">
                My Space
              </h5>
              <ul className="mt-4 space-y-2.5">
                <li>
                  <Link to="/digital-library" className="text-sm text-zinc-500 hover:text-zinc-90 dark:text-zinc-400 dark:hover:text-zinc-100 transition-colors">
                    Digital Bookshelves
                  </Link>
                </li>
                <li>
                  <Link to="/dashboard" className="text-sm text-zinc-500 hover:text-zinc-90 dark:text-zinc-400 dark:hover:text-zinc-100 transition-colors">
                    Purchased Orders
                  </Link>
                </li>
                <li>
                  <Link to="/dashboard" className="text-sm text-zinc-500 hover:text-zinc-90 dark:text-zinc-400 dark:hover:text-zinc-100 transition-colors">
                    Saved Wishlist
                  </Link>
                </li>
              </ul>
            </div>

            {/* NEWSLETTER FORM */}
            <div className="col-span-2 sm:col-span-1">
              <h5 className="text-xs font-bold uppercase tracking-widest text-zinc-400 dark:text-zinc-505 font-mono">
                Subscribe Insights
              </h5>
              <p className="mt-2 text-xs text-zinc-500 dark:text-zinc-405 leading-relaxed">
                Receive weekly curated essays, critical breakdowns, and coupon code bundles directly.
              </p>
              <form onSubmit={handleSubscribe} className="mt-4 flex flex-col gap-2">
                <div className="relative flex items-center">
                  <input
                    type="email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    placeholder="Enter email..."
                    className="w-full rounded-xl border border-zinc-250 bg-white px-3.5 py-2 pr-10 text-xs text-zinc-900 shadow-sm outline-none focus:border-blue-650 dark:border-zinc-805 dark:bg-zinc-900 dark:text-zinc-50"
                  />
                  <button
                    type="submit"
                    className="absolute right-1 text-zinc-550 hover:text-blue-600 dark:text-zinc-400 dark:hover:text-blue-400 p-1.5 rounded-lg hover:bg-zinc-50 dark:hover:bg-zinc-800 transition-colors cursor-pointer"
                  >
                    <Send className="h-3.5 w-3.5" />
                  </button>
                </div>
              </form>
            </div>
          </div>
        </div>

        {/* SUB FOOTER DETAILS */}
        <div className="mt-12 border-t border-zinc-150 dark:border-zinc-805 pt-8 flex flex-col sm:flex-row items-center justify-between gap-4">
          <p className="text-xs text-zinc-400">
            &copy; 2026 Livraria Mulemba. Todos os direitos reservados. Rede literária de Angola.
          </p>
          <div className="flex items-center gap-1 text-xs text-zinc-400 font-medium font-mono">
            <Shield className="h-3.5 w-3.5 text-blue-500" />
            <span>Secured Session</span>
            <span className="mx-1">•</span>
            <Heart className="h-3 w-3 text-rose-500 animate-pulse" />
            <span>Modern Stack UI</span>
          </div>
        </div>
      </div>
    </footer>
  );
}
