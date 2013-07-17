package com.drawstuff.drawer.bitmap.search;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class BitmapPixelGrabber {

	private final Bitmap bitmap;
	private Paint paint;

	public BitmapPixelGrabber(Bitmap bitmap) {
		this.bitmap = bitmap;
		this.paint = new Paint();
		paint.setColor(Color.BLACK);
	}

	public boolean isBlack(int x, int y) {
		if (x >= bitmap.getWidth())
			x = bitmap.getWidth() - 1;
		if (y >= bitmap.getHeight())
			y = bitmap.getHeight() - 1;

		if (x <= 0)
			x = 1;
		if (y <= 0)
			y = 1;
		try {
			int c = bitmap.getPixel(x, y);
			return c == Color.BLACK && Color.alpha(c) == Color.alpha(Color.BLACK);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public int getMaxDimension() {
		return bitmap.getWidth() > bitmap.getHeight() ? bitmap.getWidth() : bitmap.getHeight();
	}

	public int width() {
		return bitmap.getWidth();
	}

	public int height() {
		return bitmap.getHeight();
	}

	public Bitmap subSet(Rect rect) {
		return Bitmap.createBitmap(bitmap, rect.left, rect.top, rect.width(), rect.height());
	}

	public int left() {
		return 0;
	}

	public int top() {
		return 0;
	}

	public int right() {
		return width();
	}

	public int bottom() {
		return height();
	}

}
