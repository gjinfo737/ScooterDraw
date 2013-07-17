package com.drawstuff.drawer.bitmap.search;

import com.drawstuff.drawer.bitmap.search.BitmapSearcher.Edge;

import android.graphics.Point;
import android.graphics.Rect;

public class Trimmer {

	public static final int DEFAULT_PADDING = 3;
	private BitmapPixelGrabber bitmapPixelGrabber;
	private int padding;

	public Trimmer() {
		this.padding = DEFAULT_PADDING;
	}

	public Trimmer(int padding) {
		this.padding = padding;
	}

	public Trimmer(BitmapPixelGrabber bitmapPixelGrabber) {
		this.bitmapPixelGrabber = bitmapPixelGrabber;
	}

	public void trimFromEdge(final int searchDensity, Rect rect, Edge edge) {

		RectangleVector rectangleVector = new RectangleVector(searchDensity, rect, edge);

		for (int i = 0; i < rectangleVector.getNumberOfSteps(); i++) {
			int edgeOffset = rectangleVector.getEdgeOffset(i);
			Point startPoint = rectangleVector.determineStartPoint(rect, edge, edgeOffset);
			Point endPoint = rectangleVector.determineEndPoint(rect, edge, edgeOffset);
			if (lineContainsBlack(startPoint, endPoint, rectangleVector)) {
				if (edge == Edge.LEFT) {
					rect.left = edgeOffset - padding;
					rect.left = rect.left < bitmapPixelGrabber.left() ? bitmapPixelGrabber.left() : rect.left;
				} else if (edge == Edge.TOP) {
					rect.top = edgeOffset - padding;
					rect.top = rect.top < bitmapPixelGrabber.top() ? bitmapPixelGrabber.top() : rect.top;
				} else if (edge == Edge.RIGHT) {
					rect.right = edgeOffset + padding;
					rect.right = rect.right > bitmapPixelGrabber.right() ? bitmapPixelGrabber.right() : rect.right;
				} else if (edge == Edge.BOTTOM) {
					rect.bottom = edgeOffset + padding;
					rect.bottom = rect.bottom > bitmapPixelGrabber.bottom() ? bitmapPixelGrabber.bottom() : rect.bottom;
				}
				break;
			}
		}
	}

	public boolean lineContainsBlack(Point startPoint, Point endPoint, RectangleVector rectangleVector) {
		int numberOfXSteps = rectangleVector.determineNumberOfSteps(startPoint.x, endPoint.x);
		int numberOfYSteps = rectangleVector.determineNumberOfSteps(startPoint.y, endPoint.y);
		for (int i = 0; i < numberOfXSteps; i++) {
			if (bitmapPixelGrabber.isBlack((int) (startPoint.x + (i * rectangleVector.getSearchDensity())), startPoint.y))
				return true;
		}
		for (int i = 0; i < numberOfYSteps; i++) {
			if (bitmapPixelGrabber.isBlack(startPoint.x, (int) (startPoint.y + (i * rectangleVector.getSearchDensity()))))
				return true;
		}
		return false;
	}

	public void setBitmapPixelGrabber(BitmapPixelGrabber bitmapPixelGrabber) {
		this.bitmapPixelGrabber = bitmapPixelGrabber;
	}
}
