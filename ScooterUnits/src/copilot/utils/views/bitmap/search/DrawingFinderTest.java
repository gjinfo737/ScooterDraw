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
import org.mockito.Mock;

import android.graphics.Rect;

import com.drawshtuff.drawer.copilot.SignatureView;
import com.drawshtuff.test.runner.CoPilotTestRunner;

@RunWith(CoPilotTestRunner.class)
public class DrawingFinderTest {
	private DrawingFinder drawingFinder;
	private List<Rect> rects;
	private float searchDensity = SignatureView.SEARCH_DENSITY;
	private int width = 200;
	private int height = 200;
	private Cropper cropper;
	@Mock
	private BitmapPixelGrabber bitmapPixelGrabber;
	@Mock
	private BitmapSearcher bitmapSearcher;

	@Before
	public void setUp() {
		rects = new ArrayList<Rect>();
		cropper = new Cropper(bitmapPixelGrabber);
		drawingFinder = new DrawingFinder(rects, bitmapSearcher);

	}

	@Test
	public void itDoesSomething() {
		for (int i = 10; i < 20; i++) {
			for (int j = 10; j < 20; j++) {
				when(bitmapPixelGrabber.isBlack(i, j)).thenReturn(true);
			}
		}
		drawingFinder.findDrawings(searchDensity, width, height, cropper);
		verify(bitmapSearcher).onSearchComplete();
		assertThat(rects.size(), is(2));
	}
}
