package copilot.utils.views.bitmap.search;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import android.graphics.Rect;

import com.drawshtuff.test.runner.CoPilotTestRunner;

@RunWith(CoPilotTestRunner.class)
public class DrawingFinderTest {
	private DrawingFinder drawingFinder;
	private List<Rect> rects;
	private BitmapSearcher bitmapSearcher;

	@Before
	public void setUp() {
		drawingFinder = new DrawingFinder(rects, bitmapSearcher);
		rects = new ArrayList<Rect>();
	}

	@Test
	public void itMergesTwoOverlappingRects() {
		setUpTheRects().withTwoOverlappingRects();
	}

	private RectBuilder setUpTheRects() {
		return new RectBuilder(rects);
	}
}
