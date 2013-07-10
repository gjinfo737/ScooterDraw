package copilot.utils.views.bitmap.search;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.util.Log;

public class BitmapSearcher {

	private static final int COLOR_1 = Color.BLUE;
	private static final int COLOR_2 = Color.CYAN;
	private static final float RADIUS = 1;
	private boolean isSearching;
	private IBitmapSearcherListener bitmapSearcherListener;
	private BitmapPixelGrabber bitmapPixelGrabber;
	private Handler handler;
	private Runnable runnable;
	private int count = 0;
	private int total = 0;
	private List<Rect> drawingRects = new ArrayList<Rect>();

	public enum Edge {
		LEFT, TOP, RIGHT, BOTTOM
	}

	public interface IBitmapSearcherListener {
		public void onComplete(boolean found);
	}

	public BitmapSearcher() {
		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				e.printStackTrace();
			}
		});
	}

	public static Rect getSuperlativeBounds(Rect... bounds) {
		Rect superlativeBounds = bounds[0];
		for (int i = 1; i < bounds.length; i++) {
			if (bounds[i].left < superlativeBounds.left)
				superlativeBounds.left = bounds[i].left;
			if (bounds[i].top < superlativeBounds.top)
				superlativeBounds.top = bounds[i].top;
			if (bounds[i].right > superlativeBounds.right)
				superlativeBounds.right = bounds[i].right;
			if (bounds[i].bottom > superlativeBounds.bottom)
				superlativeBounds.bottom = bounds[i].bottom;
		}
		return superlativeBounds;
	}

	public void cropSearchBitmap(final Bitmap bitmap, final IBitmapSearcherListener bitmapSearcherListener, final float searchDensity, final Handler handler,
			final Runnable runnable) {
		this.handler = handler;
		this.runnable = runnable;
		if (searchDensity <= 0 || searchDensity >= 1)
			throw new IllegalArgumentException("Search density must be >0 and <1.  Was: " + searchDensity);
		this.bitmapSearcherListener = bitmapSearcherListener;
		this.isSearching = true;
		cropSearch(bitmap, bitmapSearcherListener, searchDensity);
	}

	public void onComplete(final boolean found) {
		count++;
		if (count >= total || found) {
			handler.post(new Runnable() {
				@Override
				public void run() {
					isSearching = false;
					bitmapSearcherListener.onComplete(found);
				}
			});
		}
	}

	public boolean isSearching() {
		return isSearching;
	}

	public void stopSearching() {
		count = total;
		onComplete(false);
	}

	private void cropSearch(final Bitmap bitmap, final IBitmapSearcherListener bitmapSearcherListener, final float searchDensity) {
		bitmapPixelGrabber = new BitmapPixelGrabber(bitmap);
		final Cropper cropper = new Cropper(bitmapPixelGrabber);
		drawingRects = new ArrayList<Rect>();

		final int width = bitmap.getWidth();
		final int height = bitmap.getHeight();
		// //////////
		count = 0;
		total = 0;
		new Thread(new Runnable() {
			@Override
			public void run() {
				total++;
				new DrawingFinder(drawingRects, BitmapSearcher.this).findDrawings(searchDensity, width, height, cropper);
			}
		}).start();
	}

	public void onSearchComplete() {
		mergeAndTrim();
		onCompleteCrop();
	}

	private void mergeAndTrim() {
		new RectMerge().mergeOverlappingRects(drawingRects);
		trim(drawingRects, 2);
	}

	private void trim(List<Rect> drawingRects, final int searchDensity) {
		for (Rect rect : drawingRects) {
			trimFromEdge(searchDensity, rect, Edge.LEFT);
			trimFromEdge(searchDensity, rect, Edge.TOP);
			trimFromEdge(searchDensity, rect, Edge.RIGHT);
			trimFromEdge(searchDensity, rect, Edge.BOTTOM);
		}
	}

	private void trimFromEdge(final int searchDensity, Rect rect, Edge edge) {
		int direction = 1;
		int startEdge = rect.left;
		if (edge == Edge.TOP) {
			startEdge = rect.top;
		} else if (edge == Edge.RIGHT) {
			startEdge = rect.right;
			direction = -1;
		} else if (edge == Edge.BOTTOM) {
			startEdge = rect.bottom;
			direction = -1;
		}

		int numberOfSteps = Math.abs((rect.bottom - rect.top) / searchDensity);
		for (int i = 0; i < numberOfSteps; i++) {
			int offset = (int) (i * searchDensity);
			int edgeOffset = startEdge + (offset * direction);
			Point startPoint;
			Point endPoint;
			if (edge == Edge.TOP || edge == Edge.BOTTOM) {
				startPoint = new Point(rect.left, edgeOffset);
				endPoint = new Point(rect.right, edgeOffset);
			} else {
				startPoint = new Point(edgeOffset, rect.top);
				endPoint = new Point(edgeOffset, rect.bottom);
			}
			if (bitmapPixelGrabber.testLine(startPoint, endPoint, searchDensity, Color.BLUE)) {
				if (edge == Edge.LEFT) {
					rect.left = edgeOffset;
				} else if (edge == Edge.TOP) {
					rect.top = edgeOffset;
				} else if (edge == Edge.RIGHT) {
					rect.right = edgeOffset;
				} else if (edge == Edge.BOTTOM) {
					rect.bottom = edgeOffset;
				}
				break;
			}
		}
	}

	private void onCompleteCrop() {

		for (Rect r : drawingRects) {
			Log.e("excludedRects", r.toString());
			bitmapPixelGrabber.drawBox(r, Color.rgb(224, 119, 27), 100);
		}
		handler.post(new Runnable() {
			@Override
			public void run() {
				bitmapSearcherListener.onComplete(true);
			}
		});
	}

	Point searchDiags(float searchDensity, int width, int height, Point govenor) {
		int numberofSteps = (int) (1 / searchDensity);
		int offsetY = (int) (height * searchDensity / 2f);
		for (int offsetStep = 0; offsetStep < numberofSteps; offsetStep++) {
			int offsetX = offset(width, offsetStep, searchDensity);
			for (int step = 0; step < numberofSteps; step++) {
				if (!isSearching)
					return null;
				Point translation = getDiagTranslation(width, height, searchDensity, step, offsetX + govenor.x, govenor.y);
				Point[] quadPoints = new Point[] { getQuadPoint(0, width, height, translation), getQuadPoint(1, width, height, translation),
						getQuadPoint(2, width, height, translation), getQuadPoint(3, width, height, translation) };
				int indexOfHitPoint = testAndDrawQuadPoints(quadPoints, COLOR_1, RADIUS);
				if (indexOfHitPoint != -1) {
					return quadPoints[indexOfHitPoint];
				}
				//
				translation = getDiagTranslation(-width, height, searchDensity, step, offsetX + govenor.x, offsetY + govenor.y);
				quadPoints = new Point[] { getQuadPoint(0, width, height, translation), getQuadPoint(1, width, height, translation), getQuadPoint(2, width, height, translation),
						getQuadPoint(3, width, height, translation) };
				indexOfHitPoint = testAndDrawQuadPoints(quadPoints, COLOR_2, RADIUS);
				if (indexOfHitPoint != -1) {
					return quadPoints[indexOfHitPoint];
				}
				handler.post(runnable);
			}
		}
		return null;
	}

	private int testAndDrawQuadPoints(Point[] quadPoints, int color, float radius) {
		for (int i = 0; i < quadPoints.length; i++) {
			if (!intersectsExclusions(quadPoints[i])) {
				if (bitmapPixelGrabber.testAndDrawColor(quadPoints[i].x, quadPoints[i].y, color, radius))
					return i;
			}
		}
		return -1;
	}

	private boolean intersectsExclusions(Point point) {
		for (Rect excRect : drawingRects) {
			if (excRect.contains(point.x, point.y))
				return true;
		}
		return false;
	}

	private int offset(int size, int offsetStep, float searchDensity) {
		float offsetFactor = searchDensity * offsetStep;
		return (int) (-size * offsetFactor);
	}

	private Point getDiagTranslation(int width, int height, float searchDensity, int step, int offsetX, int offsetY) {
		Point translation = new Point((int) (-width / 4f), (int) (-height / 4f));
		int wp = (int) (step * searchDensity * width / 2f);
		int hp = (int) (step * searchDensity * height / 2f);

		translation.x += wp + offsetX + random(width, searchDensity);
		translation.y += hp + offsetY + random(height, searchDensity);

		return translation;
	}

	private int random(int size, float searchDensity) {
		double random = Math.random();
		double d = random * size * searchDensity;
		if (Math.random() < .5) {
			d = -d;
		}
		return (int) d;
	}

	private Point getQuadPoint(int quadrant, float width, float height, Point translation) {
		int centerX = (int) (width / 2f);
		int centerY = (int) (height / 2f);
		int qx = (int) (width / 4f);
		int qy = (int) (height / 4f);
		int q03cx = centerX + qx + translation.x;
		int q12cx = centerX - qx + translation.x;
		int q01cy = centerY + qy + translation.y;
		int q23cy = centerY - qy + translation.y;

		Point point;
		if (quadrant == 0) {
			point = new Point(q03cx, q01cy);
		} else if (quadrant == 1) {
			point = new Point(q12cx, q01cy);
		} else if (quadrant == 2) {
			point = new Point(q12cx, q23cy);
		} else if (quadrant == 3) {
			point = new Point(q03cx, q23cy);
		} else {
			throw new IllegalArgumentException("Quadrant must be 0, 1, 2, or 3. Was: " + quadrant);
		}
		int DD = 5;
		if (point.x <= DD || point.x >= Math.abs(width) - DD)
			point.x = (int) (Math.random() * Math.abs(width));
		if (point.y <= DD || point.y >= Math.abs(height) - DD)
			point.y = (int) (Math.random() * Math.abs(height));

		return point;
	}

}
