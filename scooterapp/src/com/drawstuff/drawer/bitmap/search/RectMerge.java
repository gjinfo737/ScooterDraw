package com.drawstuff.drawer.bitmap.search;

import java.util.List;

import android.graphics.Rect;

public class RectMerge {
	private int rectIndex1;
	private int rectIndex2;
	private Rect merged;

	public void mergeOverlappingRects(List<Rect> rects) {
		RectMerge rectMerge = findOverlapping(rects);
		if (rectMerge.getMerged() != null) {
			if (rectMerge.getRectIndex1() < rectMerge.getRectIndex2()) {
				rects.remove(rectMerge.getRectIndex2());
				rects.remove(rectMerge.getRectIndex1());
			} else {
				rects.remove(rectMerge.getRectIndex1());
				rects.remove(rectMerge.getRectIndex2());
			}
			rects.add(rectMerge.getMerged());
			mergeOverlappingRects(rects);
		}
	}

	private RectMerge findOverlapping(List<Rect> rects) {
		RectMerge rectMerge = new RectMerge();

		rectMerge.setRectIndex1(-1);
		rectMerge.setRectIndex2(-1);
		for (int i = 0; i < rects.size(); i++) {
			for (int j = 0; j < rects.size(); j++) {
				if (i != j) {
					if (overLaps(rects.get(i), rects.get(j))) {
						rectMerge.setMerged(BitmapSearcher.getSuperlativeBounds(rects.get(i), rects.get(j)));
						rectMerge.setRectIndex1(i);
						rectMerge.setRectIndex2(j);
						break;
					}
				}
			}
			if (merged != null)
				break;
		}

		return rectMerge;
	}

	private boolean overLaps(Rect rect, Rect otherRect) {
		Rect r1 = cloneRect(rect);
		Rect r2 = cloneRect(otherRect);
		return r1.intersect(r2);
	}

	private Rect cloneRect(Rect rect) {
		return new Rect(rect.left, rect.top, rect.right, rect.bottom);
	}

	private int getRectIndex1() {
		return rectIndex1;
	}

	private void setRectIndex1(int rectIndex1) {
		this.rectIndex1 = rectIndex1;
	}

	private int getRectIndex2() {
		return rectIndex2;
	}

	private void setRectIndex2(int rectIndex2) {
		this.rectIndex2 = rectIndex2;
	}

	private Rect getMerged() {
		return merged;
	}

	private void setMerged(Rect merged) {
		this.merged = merged;
	}
}
