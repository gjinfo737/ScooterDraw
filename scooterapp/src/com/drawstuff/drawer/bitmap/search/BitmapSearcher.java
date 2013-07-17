package com.drawstuff.drawer.bitmap.search;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;

public class BitmapSearcher {

	public static final int TRIM_DENSITY = 2;
	private static final int COLOR_1 = Color.BLUE;
	private static final int COLOR_2 = Color.CYAN;
	private static final float RADIUS = 1;
	private BitmapPixelGrabber bitmapPixelGrabber;
	private DrawingFinder drawingFinder;
	private RectMerge rectMerge;
	private Trimmer trimmer;

	public enum Edge {
		LEFT, TOP, RIGHT, BOTTOM
	}

	public BitmapSearcher() {
		this.rectMerge = new RectMerge();
		this.drawingFinder = new DrawingFinder(this);
		this.trimmer = new Trimmer();
	}

	public BitmapSearcher(RectMerge rectMerge, DrawingFinder drawingFinder, Trimmer trimmer) {
		this.rectMerge = rectMerge;
		this.drawingFinder = drawingFinder;
		this.trimmer = trimmer;
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

	public List<PositionalBitmap> cropSearchBitmap(final byte[] drawingBmp, final float searchDensity) {
		if (searchDensity <= 0 || searchDensity >= 1)
			throw new IllegalArgumentException("Search density must be >0 and <1.  Was: " + searchDensity);

		Bitmap bitmap = BitmapFactory.decodeByteArray(drawingBmp, 0, drawingBmp.length);

		return cropSearchBitmap(bitmap, searchDensity);
	}

	public List<PositionalBitmap> cropSearchBitmap(final Bitmap bitmap, final float searchDensity) {
		if (searchDensity <= 0 || searchDensity >= 1)
			throw new IllegalArgumentException("Search density must be >0 and <1.  Was: " + searchDensity);

		bitmapPixelGrabber = new BitmapPixelGrabber(bitmap);
		final Cropper cropper = new Cropper(bitmapPixelGrabber);
		List<Rect> subRects = drawingFinder.startFindDrawings(searchDensity, bitmap.getWidth(), bitmap.getHeight(), cropper);
		mergeAndTrim(subRects);
		return getPositionalBitmaps(subRects);
	}

	public List<PositionalBitmap> getPositionalBitmaps(List<Rect> drawingRects) {
		return getSubsetBitmaps(drawingRects);
	}

	private List<PositionalBitmap> getSubsetBitmaps(List<Rect> drawingRects) {
		List<PositionalBitmap> bitmaps = new ArrayList<PositionalBitmap>();
		for (Rect rect : drawingRects) {
			Bitmap subSet = bitmapPixelGrabber.subSet(rect);

			bitmaps.add(new PositionalBitmap(subSet, rect));
		}

		return bitmaps;
	}

	private void mergeAndTrim(List<Rect> drawingRects) {
		rectMerge.mergeOverlappingRects(drawingRects);
		trim(drawingRects, TRIM_DENSITY);
	}

	private void trim(List<Rect> drawingRects, final int searchDensity) {
		trimmer.setBitmapPixelGrabber(bitmapPixelGrabber);
		for (Rect rect : drawingRects) {
			trimmer.trimFromEdge(searchDensity, rect, Edge.LEFT);
			trimmer.trimFromEdge(searchDensity, rect, Edge.TOP);
			trimmer.trimFromEdge(searchDensity, rect, Edge.RIGHT);
			trimmer.trimFromEdge(searchDensity, rect, Edge.BOTTOM);
		}
	}

	public Point searchDiags(float searchDensity, int width, int height, Point govenor) {
		int numberofSteps = (int) (1 / searchDensity);
		int offsetY = (int) (height * searchDensity / 2f);
		for (int offsetStep = 0; offsetStep < numberofSteps; offsetStep++) {
			int offsetX = offset(width, offsetStep, searchDensity);
			for (int step = 0; step < numberofSteps; step++) {
				Point translation = getDiagTranslation(width, height, searchDensity, step, offsetX + govenor.x, govenor.y);
				Point[] quadPoints = new Point[] { getQuadPoint(0, width, height, translation), getQuadPoint(1, width, height, translation),
						getQuadPoint(2, width, height, translation), getQuadPoint(3, width, height, translation) };
				int indexOfHitPoint = testQuadPoints(quadPoints, COLOR_1, RADIUS);
				if (indexOfHitPoint != -1) {
					return quadPoints[indexOfHitPoint];
				}
				//
				translation = getDiagTranslation(-width, height, searchDensity, step, offsetX + govenor.x, offsetY + govenor.y);
				quadPoints = new Point[] { getQuadPoint(0, width, height, translation), getQuadPoint(1, width, height, translation), getQuadPoint(2, width, height, translation),
						getQuadPoint(3, width, height, translation) };
				indexOfHitPoint = testQuadPoints(quadPoints, COLOR_2, RADIUS);
				if (indexOfHitPoint != -1) {
					return quadPoints[indexOfHitPoint];
				}
			}
		}
		return null;
	}

	private int testQuadPoints(Point[] quadPoints, int color, float radius) {
		for (int i = 0; i < quadPoints.length; i++) {
			if (!drawingFinder.intersectsExclusions(quadPoints[i])) {
				if (bitmapPixelGrabber.isBlack(quadPoints[i].x, quadPoints[i].y))
					return i;
			}
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
		double d = Math.random() * size * searchDensity;
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
