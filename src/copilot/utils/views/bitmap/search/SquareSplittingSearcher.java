package copilot.utils.views.bitmap.search;

import android.graphics.Color;
import android.os.Handler;

public class SquareSplittingSearcher {

	private static final int MINIMUM_DIMMENSION = 2;
	private final BitmapPixelGrabber bitmapPixelGrabber;
	private final BitmapSearcher bitmapSearcher;
	private final SquareSplittingSearcherSpawner spawner;
	private int centerX;
	private int centerY;
	private int width;
	private int height;
	private float searchDensity;// > 0 and < 1

	public SquareSplittingSearcher(BitmapSearcher bitmapSearcher, BitmapPixelGrabber bitmapPixelGrabber, SquareSplittingSearcherSpawner spawner, int centerX, int centerY,
			int width, int height, float searchDensity) {
		this.bitmapSearcher = bitmapSearcher;
		this.bitmapPixelGrabber = bitmapPixelGrabber;
		this.spawner = spawner;
		this.centerX = centerX;
		this.centerY = centerY;
		this.width = width;
		this.height = height;
		this.searchDensity = searchDensity;
	}

	public void search(Handler handler, Runnable runnable) {
		if (bitmapSearcher.isSearching()) {
			if (centerX <= MINIMUM_DIMMENSION || centerY <= MINIMUM_DIMMENSION) {
				bitmapSearcher.onComplete(false);
				return;
			}

			boolean drawBlack = bitmapPixelGrabber.drawColor(centerX, centerY, Color.BLACK, 5);
			handler.post(runnable);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (!drawBlack) {
				int qx = (int) (width / 4f);
				int qy = (int) (height / 4f);
				int newWidth = (int) (width * searchDensity);
				int newHeight = (int) (height * searchDensity);
				spawner.spawnAndSearch(bitmapSearcher, bitmapPixelGrabber, centerX + qx, centerY + qy, newWidth, newHeight, searchDensity, handler, runnable);
				spawner.spawnAndSearch(bitmapSearcher, bitmapPixelGrabber, centerX + -qx, centerY + qy, newWidth, newHeight, searchDensity, handler, runnable);
				spawner.spawnAndSearch(bitmapSearcher, bitmapPixelGrabber, centerX + -qx, centerY + -qy, newWidth, newHeight, searchDensity, handler, runnable);
				spawner.spawnAndSearch(bitmapSearcher, bitmapPixelGrabber, centerX + qx, centerY + -qy, newWidth, newHeight, searchDensity, handler, runnable);
			} else {
				bitmapSearcher.onComplete(true);
			}

		}
	}
}
