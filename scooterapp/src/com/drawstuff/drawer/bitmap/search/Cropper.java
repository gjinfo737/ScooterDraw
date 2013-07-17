package com.drawstuff.drawer.bitmap.search;

import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;

public class Cropper {

	private static final int MAX_WHITE_COUNT = 6;
	private static final float ANGLE_DENSITY = (float) (Math.PI / 100f);
	private static final int ARC_RADIUS_DENSITY = 5;
	private final BitmapPixelGrabber bitmapPixelGrabber;

	public enum Quadrant {
		FIRST, SECOND, THIRD, FOURTH;
	}

	public Cropper(BitmapPixelGrabber bitmapPixelGrabber) {
		this.bitmapPixelGrabber = bitmapPixelGrabber;
	}

	public Rect cropRegion(Point hitPoint) {
		Rect bounds1 = searchArcsInQudrant(hitPoint, Quadrant.FIRST);
		Rect bounds2 = searchArcsInQudrant(hitPoint, Quadrant.SECOND);
		Rect bounds3 = searchArcsInQudrant(hitPoint, Quadrant.THIRD);
		Rect bounds4 = searchArcsInQudrant(hitPoint, Quadrant.FOURTH);
		Rect superlativeBounds = BitmapSearcher.getSuperlativeBounds(bounds1, bounds2, bounds3, bounds4);
		return superlativeBounds;
	}

	private Rect searchArcsInQudrant(Point hitPoint, Quadrant quadrant) {
		Rect bounds1 = searchArc(hitPoint, 0f, (float) (Math.PI / 6f), quadrant);
		Rect bounds2 = searchArc(hitPoint, (float) (Math.PI / 6f), (float) (Math.PI / 3f), quadrant);
		Rect bounds3 = searchArc(hitPoint, (float) (Math.PI / 3f), (float) (Math.PI / 2f), quadrant);

		return BitmapSearcher.getSuperlativeBounds(bounds1, bounds2, bounds3);
	}

	private Rect searchArc(Point hitPoint, float angleMin, float angleMax, Quadrant quadrant) {
		Point greatest = new Point(0, 0);
		Point least = new Point(10000000, 10000000);
		int allWhiteCount = 0;

		int minRadius = 1;
		int width = bitmapPixelGrabber.width();
		int height = bitmapPixelGrabber.height();
		int maxRadius = bitmapPixelGrabber.getMaxDimension();
		int radius = minRadius;
		int numberOfSteps = (maxRadius - minRadius) / ARC_RADIUS_DENSITY;
		boolean allWhite = false;
		for (int i = 0; i < numberOfSteps + 100; i++) {
			PointF[] sectPoints = getArcPoints(hitPoint, radius, ANGLE_DENSITY, angleMin, angleMax, quadrant);
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

				greatest = bound(greatest, new Point(0, 0), new Point(width, height));
				least = bound(least, new Point(0, 0), new Point(width, height));

				if (bitmapPixelGrabber.isBlack((int) pointF.x, (int) pointF.y)) {
					allWhiteCount = 0;
					allWhite = false;
					break;
				}
			}
			if (allWhite) {
				allWhiteCount++;
				if (allWhiteCount >= MAX_WHITE_COUNT) {
					break;
				}
			}
			radius += ARC_RADIUS_DENSITY;
		}

		return new Rect(least.x, least.y, greatest.x, greatest.y);
	}

	public Point bound(Point original, Point min, Point max) {
		Point boundedPoint = new Point(original.x, original.y);
		if (boundedPoint.x < min.x)
			boundedPoint.x = min.x;
		if (boundedPoint.x > max.x)
			boundedPoint.x = max.x;
		if (boundedPoint.y < min.y)
			boundedPoint.y = min.y;
		if (boundedPoint.y > max.y)
			boundedPoint.y = max.y;
		return boundedPoint;
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
