import React, { useEffect, useState } from 'react';
import { PixelService } from '../services/PixelService';

export default function HeatmapTracker() {
  const [isVisible, setIsVisible] = useState(false);
  const [clicks, setClicks] = useState<{ x: number; y: number }[]>([]);

  useEffect(() => {
    // Load previously recorded clicks for the current path
    const rawData = localStorage.getItem(`heatmap_${window.location.pathname}`);
    if (rawData) {
      setClicks(JSON.parse(rawData));
    }

    const handleClick = (e: MouseEvent) => {
      const newClick = { x: e.pageX, y: e.pageY };
      
      setClicks((prev) => {
        const updated = [...prev, newClick];
        localStorage.setItem(`heatmap_${window.location.pathname}`, JSON.stringify(updated));
        return updated;
      });

      // Also send it to our global tracking pixel
      PixelService.track('heatmap_click', { x: e.pageX, y: e.pageY });
    };

    const handleKeyDown = (e: KeyboardEvent) => {
      // Secret combo: Ctrl + H to toggle Heatmap Visualization
      if (e.ctrlKey && e.key.toLowerCase() === 'h') {
        e.preventDefault();
        setIsVisible((prev) => !prev);
      }
    };

    document.addEventListener('click', handleClick);
    document.addEventListener('keydown', handleKeyDown);

    return () => {
      document.removeEventListener('click', handleClick);
      document.removeEventListener('keydown', handleKeyDown);
    };
  }, []);

  if (!isVisible) return null;

  return (
    <div className="pointer-events-none absolute inset-0 z-[9999] overflow-hidden">
      {clicks.map((c, i) => (
        <div
          key={i}
          className="absolute rounded-full bg-rose-500/50 blur-[2px]"
          style={{
            left: c.x - 10,
            top: c.y - 10,
            width: 20,
            height: 20,
          }}
        />
      ))}
      <div className="fixed bottom-4 right-4 bg-black/80 text-white text-xs px-3 py-1.5 rounded-lg shadow-lg border border-white/10 font-mono flex items-center gap-2">
        <span className="h-2 w-2 rounded-full bg-rose-500 animate-pulse"></span>
        Heatmap View Active ({clicks.length} clicks)
      </div>
    </div>
  );
}
