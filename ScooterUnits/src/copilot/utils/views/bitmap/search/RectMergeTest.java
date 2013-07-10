package copilot.utils.views.bitmap.search;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.graphics.Rect;

import com.drawshtuff.test.runner.CoPilotTestRunner;

@RunWith(CoPilotTestRunner.class)
public class RectMergeTest {

	private RectMerge rectMerge;
	private List<Rect> rects;

	@Before
	public void setUp() {
		rectMerge = new RectMerge();
		rects = new ArrayList<Rect>();
	}

	@Test
	public void itMergesTwoOverlappingRects() {
		Rect merged = new Rect(100, 100, 110, 110);
		setUpTheRects().withTwoOverlappingRects();
		rectMerge.mergeOverlappingRects(rects);
		assertThat(rects.size(), is(1));
		assertThat(rects.get(0), is(merged));
	}

	@Test
	public void itMergesTwoIsolatedRects() {
		setUpTheRects().withTwoIsolatedRects();
		rectMerge.mergeOverlappingRects(rects);
		assertThat(rects.size(), is(2));
	}

	@Test
	public void itMergesOnlyOverlappingRects() {
		Rect merged = new Rect(100, 100, 110, 110);
		setUpTheRects().withTwoIsolatedRects().and().withTwoOverlappingRects();
		rectMerge.mergeOverlappingRects(rects);
		assertThat(rects.size(), is(3));
		assertThat(rects.get(2), is(merged));
	}

	private Builder setUpTheRects() {
		return new Builder();
	}

	private class Builder {

		public Builder and() {
			return this;
		}

		public Builder withTwoIsolatedRects() {
			rects.add(new Rect(0, 0, 5, 5));
			rects.add(new Rect(10, 10, 20, 20));
			return this;
		}

		public Builder withTwoOverlappingRects() {
			rects.add(new Rect(100, 100, 105, 110));
			rects.add(new Rect(104, 100, 110, 105));
			return this;
		}

	}

}
