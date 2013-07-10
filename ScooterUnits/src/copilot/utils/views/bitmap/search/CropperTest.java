package copilot.utils.views.bitmap.search;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import android.graphics.Point;
import android.graphics.Rect;

import com.drawshtuff.test.runner.CoPilotTestRunner;

@RunWith(CoPilotTestRunner.class)
public class CropperTest {
	private int width = 20;
	private int height = 20;
	private Cropper cropper;
	@Mock
	private BitmapPixelGrabber bitmapPixelGrabber;

	@Before
	public void setUp() {
		cropper = new Cropper(bitmapPixelGrabber);
		when(bitmapPixelGrabber.getMaxDimension()).thenReturn(20);
		when(bitmapPixelGrabber.width()).thenReturn(width);
		when(bitmapPixelGrabber.height()).thenReturn(height);

	}

	@Test
	public void itCropsTheWholeImage() {
		when(bitmapPixelGrabber.isBlack(anyInt(), anyInt())).thenReturn(true);
		Rect cropRegion = cropper.cropRegion(centerPoint());
		assertThat(cropRegion, is(new Rect(0, 0, width, height)));
	}

	@Test
	public void itBoundsNegativePoints() {
		Point bounded;
		bounded = cropper.bound(new Point(-10, 1000), new Point(0, 0), new Point(10, 20));
		assertThat(bounded, is(new Point(0, 20)));
		bounded = cropper.bound(new Point(1000, -10), new Point(0, 0), new Point(10, 20));
		assertThat(bounded, is(new Point(10, 0)));
		bounded = cropper.bound(new Point(-10, -10), new Point(0, 0), new Point(10, 20));
		assertThat(bounded, is(new Point(0, 0)));
	}

	@Test
	public void itBoundsLargePoints() {
		Point bounded = cropper.bound(new Point(5000, 1000), new Point(0, 0), new Point(10, 20));
		assertThat(bounded, is(new Point(10, 20)));
	}

	@Test
	public void itBoundsInclusively() {
		Point bounded;
		bounded = cropper.bound(new Point(0, 0), new Point(0, 0), new Point(10, 20));
		assertThat(bounded, is(new Point(0, 0)));
		bounded = cropper.bound(new Point(10, 20), new Point(0, 0), new Point(10, 20));
		assertThat(bounded, is(new Point(10, 20)));
	}

	@Test
	public void itDoesNotAffectInboundPoints() {
		Point bounded = cropper.bound(new Point(2, 4), new Point(0, 0), new Point(10, 20));
		assertThat(bounded, is(new Point(2, 4)));
	}

	private Point centerPoint() {
		return new Point(width / 2, height / 2);
	}
}
