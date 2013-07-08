package copilot.utils.views.bitmap.search;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.Log;

public class Cropper {

	private static final float ANGLE_DENSITY = (float) (Math.PI / 100f);
	private final BitmapPixelGrabber bitmapPixelGrabber;
	private final Bitmap bitmap;

	public Cropper(BitmapPixelGrabber bitmapPixelGrabber, Bitmap bitmap) {
		this.bitmapPixelGrabber = bitmapPixelGrabber;
		this.bitmap = bitmap;
	}

	public void cropRegion(Point hitPoint, Bitmap bitmap) {
		bitmapPixelGrabber.drawColor(hitPoint.x, hitPoint.y, Color.GREEN, 7);
		searchArc(hitPoint, 0f, (float) (Math.PI / 6f));
		searchArc(hitPoint, (float) (Math.PI / 6f), (float) (Math.PI / 3f));
		searchArc(hitPoint, (float) (Math.PI / 3f), (float) (Math.PI / 2f));
	}

	private void searchArc(Point hitPoint, float angleMin, float angleMax) {
		int allWhiteCount = 0;
		int neededAllWhiteCount = 20;
		int minRadius = 1;
		int maxRadius = 1500;
		int radiusDensity = 5;
		int radius = minRadius;
		int numberOfSteps = (maxRadius - minRadius) / radiusDensity;
		boolean allWhite = false;
		for (int i = 0; i < numberOfSteps; i++) {
			PointF[] sectPoints = getArcPoints(hitPoint, radius, ANGLE_DENSITY, angleMin, angleMax);
			int color = allWhite ? Color.MAGENTA : Color.RED;
			allWhite = true;
			for (PointF pointF : sectPoints) {
				if (bitmapPixelGrabber.testAndDrawColor((int) pointF.x, (int) pointF.y, color, 2)) {
					allWhiteCount = 0;
					allWhite = false;
					break;
				}
			}
			if (allWhite) {
				allWhiteCount++;
				if (allWhiteCount >= neededAllWhiteCount) {
					Log.e("allWhite", "stopped arc!!");
					break;
				}
			}
			radius += radiusDensity;
		}
	}

	private PointF[] getArcPoints(Point hitPoint, int radius, float angleDensity, float angleMin, float angleMax) {
		double angle = angleMin;
		int numberOfSteps = (int) ((angleMax - angleMin) / angleDensity);
		PointF[] points = new PointF[numberOfSteps];
		for (int i = 0; i < points.length; i++) {
			angle += angleDensity;
			float X = (float) (hitPoint.x + (radius * Math.cos(angle)));
			float Y = (float) (hitPoint.y - (radius * Math.sin(angle)));
			points[i] = new PointF(X, Y);
		}

		return points;
	}
}
