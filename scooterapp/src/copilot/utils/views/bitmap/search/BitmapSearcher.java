package copilot.utils.views.bitmap.search;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Handler;

public class BitmapSearcher {

	private static final int COLOR_1 = Color.BLUE;
	private static final int COLOR_2 = Color.CYAN;
	private static final float RADIUS = 1;
	private boolean isSearching;
	private IBitmapSearcherListener bitmapSearcherListener;
	private BitmapPixelGrabber bitmapPixelGrabber;
	private SquareSplittingSearcherSpawner spawner;
	private Handler handler;
	private Runnable runnable;
	private int count = 0;
	private int total = 0;

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

	public void cropSearchBitmap(final Bitmap bitmap, final IBitmapSearcherListener bitmapSearcherListener, final float searchDensity, final Handler handler,
			final Runnable runnable) {
		this.handler = handler;
		this.runnable = runnable;
		if (searchDensity <= 0 || searchDensity >= 1)
			throw new IllegalArgumentException("Search density must be >0 and <1.  Was: " + searchDensity);
		//
		this.bitmapSearcherListener = bitmapSearcherListener;
		this.isSearching = true;
		cropSearch(bitmap, bitmapSearcherListener, searchDensity);
	}

	public void searchBitmap(final Bitmap bitmap, final IBitmapSearcherListener bitmapSearcherListener, final float searchDensity, final Handler handler, final Runnable runnable) {
		this.handler = handler;
		this.runnable = runnable;
		if (searchDensity <= 0 || searchDensity >= 1)
			throw new IllegalArgumentException("Search density must be >0 and <1.  Was: " + searchDensity);
		//
		this.bitmapSearcherListener = bitmapSearcherListener;
		this.isSearching = true;
		search(bitmap, bitmapSearcherListener, searchDensity);
	}

	public boolean isSearching() {
		return isSearching;
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

	private void cropSearch(final Bitmap bitmap, final IBitmapSearcherListener bitmapSearcherListener, final float searchDensity) {
		bitmapPixelGrabber = new BitmapPixelGrabber(bitmap);
		final Cropper cropper = new Cropper(bitmapPixelGrabber, bitmap);
		final int width = bitmap.getWidth();
		final int height = bitmap.getHeight();
		// //////////
		count = 0;
		total = 0;
		new Thread(new Runnable() {
			@Override
			public void run() {
				total++;
				Point hitPoint = searchDiags(searchDensity, width, height, new Point((int) (width / 2f), 0));
				if (hitPoint != null) {
					cropper.cropRegion(hitPoint, bitmap);
					handler.post(runnable);
					onComplete(true);
				} else {
					hitPoint = searchDiags(searchDensity, width, height, new Point(0, (int) (-height / 3f)));
					if (hitPoint != null) {
						cropper.cropRegion(hitPoint, bitmap);
						handler.post(runnable);
						onComplete(true);
					} else {
						onComplete(false);
					}
				}
			}

		}).start();

	}

	private void search(Bitmap bitmap, final IBitmapSearcherListener bitmapSearcherListener, final float searchDensity) {
		bitmapPixelGrabber = new BitmapPixelGrabber(bitmap);
		final int width = bitmap.getWidth();
		final int height = bitmap.getHeight();
		// //////////
		count = 0;
		total = 0;
		new Thread(new Runnable() {
			@Override
			public void run() {
				total++;
				if (searchDiags(searchDensity, width, height, new Point((int) (width / 2f), 0)) != null) {
					onComplete(true);
				} else {
					onComplete(false);
				}
			}
		}).start();
		new Thread(new Runnable() {
			@Override
			public void run() {
				total++;
				if (searchDiags(searchDensity, width, height, new Point(0, (int) (-height / 3f))) != null) {
					onComplete(true);
				} else {
					onComplete(false);
				}
			}
		}).start();

	}

	private Point searchDiags(float searchDensity, int width, int height, Point govenor) {
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
				int indexOfHitPoint = drawQuads(quadPoints, COLOR_1, RADIUS);
				if (indexOfHitPoint != -1) {
					return quadPoints[indexOfHitPoint];
				}
				//
				translation = getDiagTranslation(-width, height, searchDensity, step, offsetX + govenor.x, offsetY + govenor.y);
				quadPoints = new Point[] { getQuadPoint(0, width, height, translation), getQuadPoint(1, width, height, translation), getQuadPoint(2, width, height, translation),
						getQuadPoint(3, width, height, translation) };
				indexOfHitPoint = drawQuads(quadPoints, COLOR_2, RADIUS);
				if (indexOfHitPoint != -1) {
					return quadPoints[indexOfHitPoint];
				}
				// /////////////
				handler.post(runnable);
			}
		}
		return null;
	}

	private int drawQuads(Point[] quadPoints, int color, float radius) {
		for (int i = 0; i < quadPoints.length; i++) {
			if (bitmapPixelGrabber.testAndDrawColor(quadPoints[i].x, quadPoints[i].y, color, radius))
				return i;
		}
		return -1;
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

	public void stopSearching() {
		count = total;
		onComplete(false);
	}
}
