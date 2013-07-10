package copilot.utils.views.bitmap.search;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import android.graphics.Bitmap;
import android.graphics.Rect;

import com.drawshtuff.test.runner.CoPilotTestRunner;

import copilot.utils.views.bitmap.search.BitmapSearcher.IBitmapSearcherListener;

@RunWith(CoPilotTestRunner.class)
public class BitmapSearcherTest {

	private BitmapSearcher bitmapSearcher;
	private Rect rect1;
	private Rect rect2;
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	@Mock
	private IBitmapSearcherListener bitmapSearcherListener;
	@Mock
	private Bitmap bitmap;

	@Before
	public void setUp() {
		bitmapSearcher = new BitmapSearcher(bitmapSearcherListener);
	}

	@Test
	public void itDefaultsToNotSearching() {
		assertThat(bitmapSearcher.isSearching(), is(false));
	}

	@Test
	public void itThrowsWhenSearchDensityIsOutOfBounds() throws IllegalArgumentException {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Search density must be >0 and <1.  Was: " + 10);
		bitmapSearcher.cropSearchBitmap(bitmap, 10);
	}

	@Test
	public void itSetsSearchingToTrue() {
		bitmapSearcher.cropSearchBitmap(bitmap, .5f);
		assertThat(bitmapSearcher.isSearching(), is(true));
	}

	@Test
	public void itNotifiesTheBitmapSearchListener() {
		bitmapSearcher.onSearchComplete();
		verify(bitmapSearcherListener).onComplete(true);
		assertThat(bitmapSearcher.isSearching(), is(false));
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
