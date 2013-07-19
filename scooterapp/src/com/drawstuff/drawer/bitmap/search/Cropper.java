package com.drawstuff.drawer.bitmap.search;

import java.util.Random;

import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;

public class Cropper {

	private static final float ANGLE_DENSITY = (float) (Math.PI / 100f);
	private static final int ARC_RADIUS_DENSITY = 5;
	private static final int DEFAULT_MAX_COUNT = 6;
	private final BitmapPixelGrabber bitmapPixelGrabber;

	public enum Quadrant {
		FIRST, SECOND, THIRD, FOURTH;
	}

	public Cropper(BitmapPixelGrabber bitmapPixelGrabber) {
		this.bitmapPixelGrabber = bitmapPixelGrabber;
	}

	public Rect cropRegion(Point hitPoint) {
		Rect bounds1 = searchArcsInQudrant(hitPoint, Quadrant.FIRST, true, DEFAULT_MAX_COUNT);
		Rect bounds2 = searchArcsInQudrant(hitPoint, Quadrant.SECOND, true, DEFAULT_MAX_COUNT);
		Rect bounds3 = searchArcsInQudrant(hitPoint, Quadrant.THIRD, true, DEFAULT_MAX_COUNT);
		Rect bounds4 = searchArcsInQudrant(hitPoint, Quadrant.FOURTH, true, DEFAULT_MAX_COUNT);
		Rect superlativeBounds = BitmapSearcher.getSuperlativeBounds(bounds1, bounds2, bounds3, bounds4);
		return superlativeBounds;
	}

	private Rect searchArcsInQudrant(Point hitPoint, Quadrant quadrant, boolean test, int maxCount) {
		Rect bounds1 = searchArc(hitPoint, 0f, (float) (Math.PI / 6f), quadrant, test, maxCount);
		Rect bounds2 = searchArc(hitPoint, (float) (Math.PI / 6f), (float) (Math.PI / 3f), quadrant, test, maxCount);
		Rect bounds3 = searchArc(hitPoint, (float) (Math.PI / 3f), (float) (Math.PI / 2f), quadrant, test, maxCount);

		return BitmapSearcher.getSuperlativeBounds(bounds1, bounds2, bounds3);
	}

	private Rect searchArc(Point hitPoint, float angleMin, float angleMax, Quadrant quadrant, boolean test, int maxCount) {
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
				if (test) {
					if (bitmapPixelGrabber.testAndDraw((int) pointF.x, (int) pointF.y)) {
						allWhiteCount = 0;
						allWhite = false;
						break;
					}
				} else {
					bitmapPixelGrabber.drawPoint((int) pointF.x, (int) pointF.y);
				}
			}
			if (allWhite) {
				allWhiteCount++;
				if (allWhiteCount >= maxCount) {
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

	public void radialFrom(Point hitPoint, int minRadius, int maxRadius) {
		Random rand = new Random();
		bitmapPixelGrabber.nextColor();
		if (rand.nextBoolean())
			searchArcsInQudrant(hitPoint, Quadrant.FIRST, true, rand.nextInt(maxRadius - minRadius) + minRadius);
		if (rand.nextBoolean())
			searchArcsInQudrant(hitPoint, Quadrant.SECOND, true, rand.nextInt(maxRadius - minRadius) + minRadius);
		if (rand.nextBoolean())
			searchArcsInQudrant(hitPoint, Quadrant.THIRD, true, rand.nextInt(maxRadius - minRadius) + minRadius);
		if (rand.nextBoolean())
			searchArcsInQudrant(hitPoint, Quadrant.FOURTH, true, rand.nextInt(maxRadius - minRadius) + minRadius);

	}

}
