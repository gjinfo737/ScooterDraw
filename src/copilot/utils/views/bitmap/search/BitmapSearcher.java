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

	private void search(Bitmap bitmap, final IBitmapSearcherListener bitmapSearcherListener, final float searchDensity) {
		bitmapPixelGrabber = new BitmapPixelGrabber(bitmap);
		final int width = bitmap.getWidth();
		final int height = bitmap.getHeight();
		// //////////
		count = 0;
		total = 0;
		new Thread(new Runnable() {
			public void run() {
				total++;
				if (searchDiags(searchDensity, width, height, new Point((int) (width / 2f), 0))) {
					onComplete(true);
				} else {
					onComplete(false);
				}
			}
		}).start();
		new Thread(new Runnable() {
			public void run() {
				total++;
				if (searchDiags(searchDensity, width, height, new Point(0, (int) (-height / 3f)))) {
					onComplete(true);
				} else {
					onComplete(false);
				}
			}
		}).start();

	}

	private boolean searchDiags(float searchDensity, int width, int height, Point govenor) {
		int numberofSteps = (int) (1 / searchDensity);
		int offsetY = (int) (height * searchDensity / 2f);
		for (int offsetStep = 0; offsetStep < numberofSteps; offsetStep++) {
			int offsetX = offset(width, offsetStep, searchDensity);
			for (int step = 0; step < numberofSteps; step++) {
				if (!isSearching)
					return false;
				Point translation1 = getDiagTranslation(width, height, searchDensity, step, offsetX + govenor.x, govenor.y);
				if (drawQuads(new Point[] { getQuadPoint(0, width, height, translation1), getQuadPoint(1, width, height, translation1),
						getQuadPoint(2, width, height, translation1), getQuadPoint(3, width, height, translation1) }, COLOR_1, RADIUS)) {
					return true;
				}
				Point translation2 = getDiagTranslation(-width, height, searchDensity, step, offsetX + govenor.x, offsetY + govenor.y);
				if (drawQuads(new Point[] { getQuadPoint(0, width, height, translation2), getQuadPoint(1, width, height, translation2),
						getQuadPoint(2, width, height, translation2), getQuadPoint(3, width, height, translation2) }, COLOR_2, RADIUS)) {
					return true;
				}
				// /////////////
				handler.post(runnable);
			}
		}
		return false;
	}

	private boolean drawQuads(Point[] quadPoints, int color, float radius) {
		if (bitmapPixelGrabber.drawColor(quadPoints[0].x, quadPoints[0].y, color, radius))
			return true;
		if (bitmapPixelGrabber.drawColor(quadPoints[1].x, quadPoints[1].y, color, radius))
			return true;
		if (bitmapPixelGrabber.drawColor(quadPoints[2].x, quadPoints[2].y, color, radius))
			return true;
		if (bitmapPixelGrabber.drawColor(quadPoints[3].x, quadPoints[3].y, color, radius))
			return true;

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

	public void stopSearching() {
		count = total;
		onComplete(false);
	}
}
