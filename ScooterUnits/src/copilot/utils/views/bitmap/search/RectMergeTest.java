package copilot.utils.views.bitmap.search;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
	public void itDoesIt() {
		Rect merged = new Rect(0, 0, 10, 10);
		withTwoOverlappingRects();
		rectMerge.mergeOverlappingRects(rects);
		assertThat(rects.size(), is(1));
		assertThat(rects.get(0), is(merged));
	}

	private void withTwoOverlappingRects() {
		// TODO Auto-generated method stub

	}
}
