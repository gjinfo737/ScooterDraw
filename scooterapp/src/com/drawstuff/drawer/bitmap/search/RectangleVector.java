package com.drawstuff.drawer.bitmap.search;

import com.drawstuff.drawer.bitmap.search.BitmapSearcher.Edge;

import android.graphics.Point;
import android.graphics.Rect;

public class RectangleVector {

	private int direction = 1;
	private int startEdge = 0;
	private int numberOfSteps = 0;
	private int searchDensity;
	private final Edge edge;

	public RectangleVector(final int searchDensity, final Rect rect, final Edge edge) {
		this.searchDensity = searchDensity;
		this.edge = edge;
		this.direction = determineDirection(edge);
		this.startEdge = determineStartEdge(rect, edge);
		if (edge == Edge.TOP || edge == Edge.BOTTOM)
			this.numberOfSteps = determineNumberOfSteps(rect.top, rect.bottom);
		else
			this.numberOfSteps = determineNumberOfSteps(rect.left, rect.right);
	}

	public int determineNumberOfSteps(final int from, final int to) {
		return Math.abs((to - from) / searchDensity);
	}

	public int getEdgeOffset(int i) {
		int offset = (int) (i * searchDensity);
		return startEdge + (offset * direction);
	}

	public Point determineEndPoint(Rect rect, Edge edge, int edgeOffset) {
		Point endPoint;
		if (edge == Edge.TOP || edge == Edge.BOTTOM) {
			endPoint = new Point(rect.right, edgeOffset);
		} else {
			endPoint = new Point(edgeOffset, rect.bottom);
		}
		return endPoint;
	}

	public Point determineStartPoint(Rect rect, Edge edge, int edgeOffset) {
		Point startPoint;
		if (edge == Edge.TOP || edge == Edge.BOTTOM) {
			startPoint = new Point(rect.left, edgeOffset);
		} else {
			startPoint = new Point(edgeOffset, rect.top);
		}
		return startPoint;
	}

	private int determineDirection(Edge edge) {
		int direction = 1;
		if (edge == Edge.RIGHT || edge == Edge.BOTTOM) {
			direction = -1;
		}
		return direction;
	}

	private int determineStartEdge(Rect rect, Edge edge) {
		if (edge == Edge.TOP) {
			return rect.top;
		} else if (edge == Edge.RIGHT) {
			return rect.right;
		} else if (edge == Edge.BOTTOM) {
			return rect.bottom;
		} else {
			return rect.left;
		}
	}

	public int getDirection() {
		return direction;
	}

	public int getStartEdge() {
		return startEdge;
	}

	public int getNumberOfSteps() {
		return numberOfSteps;
	}

	public int getSearchDensity() {
		return searchDensity;
	}

	public Edge getEdge() {
		return edge;
	}

}
