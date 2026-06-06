import { AnimatePresence, motion } from 'motion/react';
import { useNotificationStore } from '../store/useNotificationStore';
import { CheckCircle, AlertCircle, Info, X, ShieldAlert } from 'lucide-react';

export default function ToastNotifications() {
  const { toasts, removeToast } = useNotificationStore();

  const getIcon = (type: string) => {
    switch (type) {
      case 'success':
        return <CheckCircle className="h-5 w-5 text-emerald-500" />;
      case 'error':
        return <ShieldAlert className="h-5 w-5 text-rose-500" />;
      case 'warning':
        return <AlertCircle className="h-5 w-5 text-amber-500" />;
      default:
        return <Info className="h-5 w-5 text-sky-500" />;
    }
  };

  const getColorClass = (type: string) => {
    switch (type) {
      case 'success':
        return 'border-emerald-500/30 bg-white dark:bg-zinc-900 shadow-emerald-500/10';
      case 'error':
        return 'border-rose-500/30 bg-white dark:bg-zinc-900 shadow-rose-500/10';
      case 'warning':
        return 'border-amber-500/30 bg-white dark:bg-zinc-900 shadow-amber-500/10';
      default:
        return 'border-sky-500/30 bg-white dark:bg-zinc-900 shadow-sky-500/10';
    }
  };

  return (
    <div className="fixed top-20 right-4 z-50 flex w-full max-w-sm flex-col gap-2 p-4 pointer-events-none md:right-6">
      <AnimatePresence>
        {toasts.map((toast) => (
          <motion.div
            key={toast.id}
            initial={{ opacity: 0, y: -20, scale: 0.9 }}
            animate={{ opacity: 1, y: 0, scale: 1 }}
            exit={{ opacity: 0, scale: 0.85, transition: { duration: 0.15 } }}
            className={`pointer-events-auto flex items-start gap-3 rounded-xl border p-4 shadow-lg backdrop-blur-md transition-all ${getColorClass(
              toast.type
            )}`}
            style={{ width: '100%' }}
          >
            <div className="flex-shrink-0 mt-0.5">{getIcon(toast.type)}</div>
            <div className="flex-1 text-sm font-medium text-zinc-900 dark:text-zinc-100">
              {toast.message}
            </div>
            <button
              onClick={() => removeToast(toast.id)}
              className="ml-2 flex-shrink-0 rounded-lg p-1 text-zinc-400 hover:bg-zinc-100 hover:text-zinc-600 dark:hover:bg-zinc-800 dark:hover:text-zinc-200 transition-colors"
            >
              <X className="h-4 w-4" />
            </button>
          </motion.div>
        ))}
      </AnimatePresence>
    </div>
  );
}
