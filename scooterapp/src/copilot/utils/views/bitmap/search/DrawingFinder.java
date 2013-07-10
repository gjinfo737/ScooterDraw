package copilot.utils.views.bitmap.search;

import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;

public class DrawingFinder {
	private List<Rect> drawingRects;
	private BitmapSearcher bitmapSearcher;
	private Point hitPoint;

	public DrawingFinder(List<Rect> drawingRects, BitmapSearcher bitmapSearcher) {
		this.drawingRects = drawingRects;
		this.bitmapSearcher = bitmapSearcher;
	}

	public void findDrawings(float searchDensity, int width, int height, Cropper cropper, Bitmap bitmap) {
		if (findADrawingPoint(searchDensity, width, height)) {
			cropRegionFromPoint(cropper, bitmap);
			findDrawings(searchDensity, height, height, cropper, bitmap);
		} else {
			bitmapSearcher.onSearchComplete();
		}
	}

	private void cropRegionFromPoint(Cropper cropper, Bitmap bitmap) {
		drawingRects.add(cropper.cropRegion(hitPoint, bitmap));
	}

	private boolean findADrawingPoint(float searchDensity, int width, int height) {
		hitPoint = bitmapSearcher.searchDiags(searchDensity, width, height, new Point((int) (width / 2f), 0));
		if (hitPoint == null) {
			hitPoint = bitmapSearcher.searchDiags(searchDensity, width, height, new Point(0, (int) (-height / 3f)));
		}
		return hitPoint != null;
	}

}
