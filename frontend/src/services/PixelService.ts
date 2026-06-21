export type PixelEvent = 'page_view' | 'add_to_cart' | 'checkout_init' | 'purchase' | 'heatmap_click' | 'ab_test_view';

interface TrackingData {
  event: PixelEvent;
  timestamp: string;
  path: string;
  metadata?: any;
}

class PixelTracker {
  private storageKey = 'bookverse_pixel_data';

  track(event: PixelEvent, metadata?: any) {
    try {
      const data: TrackingData = {
        event,
        timestamp: new Date().toISOString(),
        path: window.location.pathname,
        metadata,
      };

      const existingDataRaw = localStorage.getItem(this.storageKey);
      const existingData: TrackingData[] = existingDataRaw ? JSON.parse(existingDataRaw) : [];
      
      existingData.push(data);
      localStorage.setItem(this.storageKey, JSON.stringify(existingData));
      
      // Simulate network request
      console.log(`[PIXEL_TRACKING] Event Logged: ${event}`, data);
    } catch (e) {
      console.error('Failed to track pixel event', e);
    }
  }

  getEvents(): TrackingData[] {
    const data = localStorage.getItem(this.storageKey);
    return data ? JSON.parse(data) : [];
  }
  
  clearEvents() {
    localStorage.removeItem(this.storageKey);
  }
}

export const PixelService = new PixelTracker();
