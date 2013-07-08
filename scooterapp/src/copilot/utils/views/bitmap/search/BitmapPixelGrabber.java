package copilot.utils.views.bitmap.search;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class BitmapPixelGrabber {

	private static final int ALPHA = 255;
	private final Bitmap bitmap;
	private Canvas canvas;
	private Paint paint;

	public BitmapPixelGrabber(Bitmap bitmap) {
		this.bitmap = bitmap;
		this.canvas = new Canvas(bitmap);
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

	public boolean testAndDrawColor(int x, int y, int color, float radius) {
		boolean found = false;
		if (isBlack(x, y)) {
			found = true;
		}
		drawColor(x, y, color, radius);
		return found;
	}

	public void drawColor(int x, int y, int color, float radius) {
		paint.setColor(color);
		paint.setAlpha(ALPHA);
		if (x >= bitmap.getWidth())
			x = bitmap.getWidth() - 1;
		if (y >= bitmap.getHeight())
			y = bitmap.getHeight() - 1;

		if (x <= 0)
			x = 1;
		if (y <= 0)
			y = 1;
		//
		canvas.drawCircle(x, y, radius, paint);
	}

	public void drawBox(Rect superlativeBounds, int color, int alpha) {
		Paint rectPaint = new Paint();
		rectPaint.setColor(color);
		rectPaint.setAlpha(alpha);

		canvas.drawRect(superlativeBounds, rectPaint);
	}
}
