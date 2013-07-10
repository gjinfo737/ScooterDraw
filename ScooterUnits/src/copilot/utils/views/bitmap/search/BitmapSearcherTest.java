package copilot.utils.views.bitmap.search;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.graphics.Point;
import android.graphics.Rect;

import com.drawshtuff.test.runner.CoPilotTestRunner;

@RunWith(CoPilotTestRunner.class)
public class BitmapSearcherTest {

	private BitmapSearcher bitmapSearcher;
	private Rect rect1;
	private Rect rect2;

	@Before
	public void setUp() {
		bitmapSearcher = new BitmapSearcher();

	}

	@Test
	public void itGetsSuperlativeBoundsForIdenticalRectangles() {
		rect1 = new Rect(0, 0, 10, 10);
		rect2 = new Rect(0, 0, 10, 10);
		Rect superlativeRect = BitmapSearcher.getSuperlativeBounds(rect1, rect2);
		assertThat(superlativeRect, is(new Rect(0, 0, 10, 10)));
	}

	@Test
	public void itGetsSuperlativeBoundsForIsolatedRectangles() {
		rect1 = new Rect(0, 0, 10, 10);
		rect2 = new Rect(20, 20, 100, 100);
		Rect superlativeRect = BitmapSearcher.getSuperlativeBounds(rect1, rect2);
		assertThat(superlativeRect, is(new Rect(0, 0, 100, 100)));
	}

	@Test
	public void itGetsSuperlativeBoundsForRectanglesSharingASide() {
		rect1 = new Rect(0, 0, 10, 10);
		rect2 = new Rect(0, 20, 10, 100);
		Rect superlativeRect = BitmapSearcher.getSuperlativeBounds(rect1, rect2);
		assertThat(superlativeRect, is(new Rect(0, 0, 10, 100)));
	}

	@Test
	public void itGetsSuperlativeBoundsForOverlappingRectangles() {
		rect1 = new Rect(0, 0, 100, 100);
		rect2 = new Rect(0, 0, 10, 10);
		Rect superlativeRect = BitmapSearcher.getSuperlativeBounds(rect1, rect2);
		assertThat(superlativeRect, is(new Rect(0, 0, 100, 100)));
	}

}
