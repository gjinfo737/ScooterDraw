package copilot.utils.views.bitmap.search;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

public class BitmapPixelGrabber {

	private static final int ALPHA = 50;
	private final Bitmap bitmap;
	private Canvas canvas;
	private Paint paint;

	public BitmapPixelGrabber(Bitmap bitmap) {
		this.bitmap = bitmap;
		this.canvas = new Canvas(bitmap);
		this.paint = new Paint();
		paint.setColor(Color.BLACK);
	}

	public boolean testAndDrawColor(int x, int y, int color, float radius) {
		if (isBlack(x, y)) {
			return true;
		}
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
		return false;
	}

	public boolean testLine(Point startPoint, Point endPoint, float searchDensity, int color) {
		int numberOfXSteps = (int) Math.abs((endPoint.x - startPoint.x) / searchDensity);
		int numberOfYSteps = (int) Math.abs((endPoint.y - startPoint.y) / searchDensity);
		for (int i = 0; i < numberOfXSteps; i++) {
			if (testAndDrawColor((int) (startPoint.x + (i * searchDensity)), startPoint.y, color, 1))
				return true;
		}
		for (int i = 0; i < numberOfYSteps; i++) {
			if (testAndDrawColor(startPoint.x, (int) (startPoint.y + (i * searchDensity)), color, 1))
				return true;
		}
		return false;
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

	public void drawBox(Rect bounds, int color, int alpha) {

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
}
