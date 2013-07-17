package com.drawstuff.drawer.bitmap.search;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Point;
import android.graphics.Rect;

public class DrawingFinder {
	private List<Rect> drawingRects;
	private BitmapSearcher bitmapSearcher;
	private Point hitPoint;

	public DrawingFinder(BitmapSearcher bitmapSearcher) {
		this.drawingRects = new ArrayList<Rect>();
		this.bitmapSearcher = bitmapSearcher;
	}

	public List<Rect> startFindDrawings(float searchDensity, int width, int height, Cropper cropper) {
		drawingRects = new ArrayList<Rect>();
		return findDrawings(searchDensity, width, height, cropper);
	}

	private List<Rect> findDrawings(float searchDensity, int width, int height, Cropper cropper) {
		if (findADrawingPoint(searchDensity, width, height)) {
			cropRegionFromPoint(cropper);
			findDrawings(searchDensity, height, height, cropper);
		}
		return drawingRects;
	}

	public boolean intersectsExclusions(Point point) {
		for (Rect excRect : drawingRects) {
			if (excRect.contains(point.x, point.y))
				return true;
		}
		return false;
	}

	private void cropRegionFromPoint(Cropper cropper) {
		drawingRects.add(cropper.cropRegion(hitPoint));
	}

	private boolean findADrawingPoint(float searchDensity, int width, int height) {
		hitPoint = bitmapSearcher.searchDiags(searchDensity, width, height, new Point((int) (width / 2f), 0));
		if (hitPoint == null) {
			hitPoint = bitmapSearcher.searchDiags(searchDensity, width, height, new Point(0, (int) (-height / 3f)));
		}
		return hitPoint != null;
	}

}
