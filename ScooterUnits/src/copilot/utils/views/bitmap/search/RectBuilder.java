package copilot.utils.views.bitmap.search;

import java.util.List;

import android.graphics.Rect;

public class RectBuilder {

	private final List<Rect> rects;

	public RectBuilder(List<Rect> rects) {
		this.rects = rects;
	}

	public RectBuilder and() {
		return this;
	}

	public RectBuilder withTwoIsolatedRects() {
		rects.add(new Rect(0, 0, 5, 5));
		rects.add(new Rect(10, 10, 20, 20));
		return this;
	}

	public RectBuilder withTwoOverlappingRects() {
		rects.add(new Rect(100, 100, 105, 110));
		rects.add(new Rect(104, 100, 110, 105));
		return this;
	}
}
