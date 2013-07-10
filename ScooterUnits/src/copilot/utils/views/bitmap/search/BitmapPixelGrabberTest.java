package copilot.utils.views.bitmap.search;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;

import com.drawshtuff.test.runner.CoPilotTestRunner;

import copilot.utils.views.bitmap.search.BitmapSearcher.IBitmapSearcherListener;

@RunWith(CoPilotTestRunner.class)
public class BitmapPixelGrabberTest {

	private BitmapPixelGrabber bitmapPixelGrabber;
	private int width = 20;
	private int height = 20;
	@Mock
	private Bitmap bitmap;

	@Before
	public void setUp() {
		when(bitmap.getWidth()).thenReturn(width);
		when(bitmap.getHeight()).thenReturn(height);
		bitmapPixelGrabber = new BitmapPixelGrabber(bitmap);
	}

	@Test
	public void itRecognizesBlack() {
		when(bitmap.getPixel(10, 10)).thenReturn(Color.BLACK);
		assertThat(bitmapPixelGrabber.isBlack(10, 10), is(true));
	}

	@Test
	public void itDistinguishesAlpha() {
		int color = Color.argb(100, 0, 0, 0);
		when(bitmap.getPixel(10, 10)).thenReturn(color);
		assertThat(bitmapPixelGrabber.isBlack(10, 10), is(false));
	}

	@Test
	public void itReturnsFalseIfTestingOutOfBounds() {
		when(bitmap.getPixel(100, 10)).thenReturn(Color.BLACK);
		assertThat(bitmapPixelGrabber.isBlack(100, 10), is(false));
	}
}
