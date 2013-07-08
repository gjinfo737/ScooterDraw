package copilot.utils.views.bitmap.search;

import android.os.Handler;

public class SquareSplittingSearcherSpawner {

	public void spawnAndSearch(BitmapSearcher bitmapSearcher, BitmapPixelGrabber bitmapPixelGrabber, int centerX, int centerY, int width, int height, float searchDensity,
			Handler handler, Runnable runnable) {
		SquareSplittingSearcher squareSplittingSearcher = new SquareSplittingSearcher(bitmapSearcher, bitmapPixelGrabber, this, centerX, centerY, width, height, searchDensity);
		squareSplittingSearcher.search(handler, runnable);
	}
}
