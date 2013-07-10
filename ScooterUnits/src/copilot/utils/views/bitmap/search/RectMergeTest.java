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

	private RectBuilder setUpTheRects() {
		return new RectBuilder(rects);
	}

}
