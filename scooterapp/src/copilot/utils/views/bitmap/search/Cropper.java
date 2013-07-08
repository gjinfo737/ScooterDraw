package copilot.utils.views.bitmap.search;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.util.Log;

public class Cropper {

	private final BitmapPixelGrabber bitmapPixelGrabber;
	private final Bitmap bitmap;

	public Cropper(BitmapPixelGrabber bitmapPixelGrabber, Bitmap bitmap) {
		this.bitmapPixelGrabber = bitmapPixelGrabber;
		this.bitmap = bitmap;
	}

	public void cropRegion(Point hitPoint, Bitmap bitmap) {
		bitmapPixelGrabber.drawColor(hitPoint.x, hitPoint.y, Color.MAGENTA, 50);
		Log.e("", "crop: " + hitPoint.x + " " + hitPoint.y);
	}
}
