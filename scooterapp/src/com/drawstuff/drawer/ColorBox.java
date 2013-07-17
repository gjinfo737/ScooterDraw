package com.drawstuff.drawer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class ColorBox {

	private Bitmap bitmap;

	private Paint paint;

	public ColorBox() {
		paint = new Paint();
		bitmap = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		int count = 0;
		for (int x = 0; x < 200; x++) {
			for (int y = 0; y < 200; y++) {
				int r = 0;
				int g = 0;
				int b = 0;
				if (count < 255) {
					r = count;
					g = 255 - count;

				} else if (count >= 255 && count < (2 * 255)) {
					g = 255;
				} else if (count >= (2 * 255) && count < (3 * 255)) {
					b = 255;
				}
				paint.setColor(Color.rgb(r, g, b));
				canvas.drawPoint(x, y, paint);
				count++;
			}
		}
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
}
