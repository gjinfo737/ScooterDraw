package copilot.utils.views.bitmap.search;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;

import com.drawshtuff.test.runner.CoPilotTestRunner;

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

		assertThat(bitmapPixelGrabber.isBlack(10, 10), is(false));
	}

	@Test
	public void itReturnsFalseIfTestingOutOfBounds() {
		when(bitmap.getPixel(100, 10)).thenReturn(Color.BLACK);
		assertThat(bitmapPixelGrabber.isBlack(100, 10), is(false));
	}

	@Test
	public void itRecognizesBlackInALine() {
		when(bitmap.getPixel(10, 10)).thenReturn(Color.BLACK);
		assertThat(bitmapPixelGrabber.testLine(new Point(10, 5), new Point(10, 15), 1), is(true));
	}

	@Test
	public void itRecognizesBlackIsNotInALine() {
		when(bitmap.getPixel(10, 10)).thenReturn(Color.BLACK);
		assertThat(bitmapPixelGrabber.testLine(new Point(11, 5), new Point(11, 15), 1), is(false));
	}

	@Test
	public void itIgnoresTranslucentBlackInALine() {
		int color = Color.argb(100, 0, 0, 0);
		when(bitmap.getPixel(10, 10)).thenReturn(color);
		assertThat(bitmapPixelGrabber.testLine(new Point(10, 5), new Point(10, 15), 1), is(false));
	}
}
