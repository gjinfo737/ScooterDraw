package com.drawstuff.drawer.bitmap.search;

import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class BitmapPixelGrabber {

	private final Bitmap bitmap;
	private Paint paint;
	private Canvas canvas;

	public BitmapPixelGrabber(Bitmap bitmap) {
		this.bitmap = bitmap;
		this.canvas = new Canvas(this.bitmap);
		this.paint = new Paint();
		paint.setColor(Color.MAGENTA);
	}

	public boolean testAndDraw(int x, int y) {
		boolean isBlack = test(x, y);
		if (isBlack)
			return isBlack;

		drawPoint(x, y);
		return isBlack;
	}

	public void drawPoint(int x, int y) {
		canvas.drawCircle(x, y, 1, paint);
	}

	public boolean test(int x, int y) {

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

	public void nextColor() {
		Random rand = new Random();
		int red = rand.nextInt(256);
		int green = rand.nextInt(256);
		int blue = rand.nextInt(256);
		paint.setColor(Color.rgb(red, green, blue));
	}

}
