package copilot.utils.views.bitmap.search;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.Log;

public class Cropper {

	private static final int MAX_WHITE_COUNT = 12;
	private static final float ANGLE_DENSITY = (float) (Math.PI / 100f);
	private final BitmapPixelGrabber bitmapPixelGrabber;
	private final Bitmap bitmap;

	public enum Quadrant {
		FIRST, SECOND, THIRD, FOURTH;
	}

	public Cropper(BitmapPixelGrabber bitmapPixelGrabber, Bitmap bitmap) {
		this.bitmapPixelGrabber = bitmapPixelGrabber;
		this.bitmap = bitmap;
	}

	public void cropRegion(Point hitPoint, Bitmap bitmap) {
		bitmapPixelGrabber.drawColor(hitPoint.x, hitPoint.y, Color.GREEN, 7);
		Rect bounds1 = searchArcsInQudrant(hitPoint, Quadrant.FIRST);
		Rect bounds2 = searchArcsInQudrant(hitPoint, Quadrant.SECOND);
		Rect bounds3 = searchArcsInQudrant(hitPoint, Quadrant.THIRD);
		Rect bounds4 = searchArcsInQudrant(hitPoint, Quadrant.FOURTH);
		Rect superlativeBounds = getSuperlativeBounds(bounds1, bounds2, bounds3, bounds4);

		bitmapPixelGrabber.drawBox(superlativeBounds, Color.YELLOW, 100);
	}

	private Rect searchArcsInQudrant(Point hitPoint, Quadrant quadrant) {
		Rect bounds1 = searchArc(hitPoint, 0f, (float) (Math.PI / 6f), quadrant);
		Rect bounds2 = searchArc(hitPoint, (float) (Math.PI / 6f), (float) (Math.PI / 3f), quadrant);
		Rect bounds3 = searchArc(hitPoint, (float) (Math.PI / 3f), (float) (Math.PI / 2f), quadrant);

		return getSuperlativeBounds(bounds1, bounds2, bounds3);
	}

	private Rect getSuperlativeBounds(Rect... bounds) {
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

	private Rect searchArc(Point hitPoint, float angleMin, float angleMax, Quadrant quadrant) {
		Point greatest = new Point(0, 0);
		Point least = new Point(10000000, 10000000);
		int allWhiteCount = 0;

		int minRadius = 1;
		int maxRadius = 1500;
		int radiusDensity = 5;
		int radius = minRadius;
		int numberOfSteps = (maxRadius - minRadius) / radiusDensity;
		boolean allWhite = false;
		for (int i = 0; i < numberOfSteps; i++) {
			PointF[] sectPoints = getArcPoints(hitPoint, radius, ANGLE_DENSITY, angleMin, angleMax, quadrant);
			int color = allWhite ? Color.MAGENTA : Color.RED;
			allWhite = true;
			for (PointF pointF : sectPoints) {
				if (pointF.x > greatest.x)
					greatest.x = (int) pointF.x;
				if (pointF.y > greatest.y)
					greatest.y = (int) pointF.y;
				if (pointF.x < least.x)
					least.x = (int) pointF.x;
				if (pointF.y < least.y)
					least.y = (int) pointF.y;

				if (bitmapPixelGrabber.testAndDrawColor((int) pointF.x, (int) pointF.y, color, 2)) {
					allWhiteCount = 0;
					allWhite = false;
					break;
				}
			}
			if (allWhite) {
				allWhiteCount++;
				if (allWhiteCount >= MAX_WHITE_COUNT) {
					Log.e("allWhite", "stopped arc!!");
					break;
				}
			}
			radius += radiusDensity;
		}

		return new Rect(least.x, least.y, greatest.x, greatest.y);
	}

	private PointF[] getArcPoints(Point hitPoint, int radius, float angleDensity, float angleMin, float angleMax, Quadrant quadrant) {
		double angle = angleMin;
		int numberOfSteps = (int) ((angleMax - angleMin) / angleDensity);
		PointF[] points = new PointF[numberOfSteps];
		float X = 0;
		float Y = 0;
		for (int i = 0; i < points.length; i++) {
			angle += angleDensity;
			if (quadrant == Quadrant.FIRST || quadrant == Quadrant.FOURTH) {
				X = (float) (hitPoint.x + (radius * Math.cos(angle)));
			} else {
				X = (float) (hitPoint.x - (radius * Math.cos(angle)));
			}
			if (quadrant == Quadrant.FIRST || quadrant == Quadrant.SECOND) {
				Y = (float) (hitPoint.y - (radius * Math.sin(angle)));
			} else {
				Y = (float) (hitPoint.y + (radius * Math.sin(angle)));
			}
			points[i] = new PointF(X, Y);
		}

		return points;
	}

}
