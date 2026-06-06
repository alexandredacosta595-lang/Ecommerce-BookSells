import { LucideIcon, HelpCircle } from 'lucide-react';

interface EmptyProps {
  title: string;
  description: string;
  icon?: LucideIcon;
  actionLabel?: string;
  onAction?: () => void;
}

export default function EmptyState({
  title,
  description,
  icon: Icon = HelpCircle,
  actionLabel,
  onAction,
}: EmptyProps) {
  return (
    <div className="flex flex-col items-center justify-center text-center p-8 md:p-12 rounded-2xl border border-dashed border-zinc-200 dark:border-zinc-800 bg-zinc-50/50 dark:bg-zinc-900/40">
      <div className="rounded-2xl bg-zinc-100 dark:bg-zinc-800 p-4 mb-4 text-zinc-500 dark:text-zinc-400">
        <Icon className="h-8 w-8" />
      </div>
      <h3 className="text-lg font-semibold text-zinc-900 dark:text-zinc-50 mb-1">
        {title}
      </h3>
      <p className="text-sm text-zinc-500 dark:text-zinc-400 max-w-sm mb-6">
        {description}
      </p>
      {actionLabel && onAction && (
        <button
          onClick={onAction}
          className="px-6 py-2.5 bg-blue-600 hover:bg-blue-700 text-white font-medium text-sm rounded-xl shadow-sm transition-all hover:shadow-md hover:scale-102 active:scale-98"
        >
          {actionLabel}
        </button>
      )}
    </div>
  );
}
