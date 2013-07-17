package com.drawshtuff.drawer.view;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

public class PaintPalette {

	public static final float ERASER_GIRTH = 75f;
	public static final PorterDuffXfermode CLEAR_XFER_MODE = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
	public static final int DEFAULT_PEN_COLOR = Color.BLACK;
	public static final int DEFAULT_PEN_GIRTH = 15;
	private Paint paint;

	public PaintPalette() {
		initializePaint(DEFAULT_PEN_COLOR, DEFAULT_PEN_GIRTH);
	}

	public void setColor(int color) {
		paint.setColor(color);
	}

	public void setToDrawing() {
		paint.setXfermode(null);
	}

	public void setToErasing() {
		paint.setXfermode(CLEAR_XFER_MODE);
		paint.setStrokeWidth(ERASER_GIRTH);
	}

	public void setPaint(Paint paint) {
		this.paint = paint;
	}

	public void setGirth(int value) {
		paint.setStrokeJoin(Paint.Join.ROUND);
		paint.setStrokeCap(Paint.Cap.ROUND);
		paint.setStrokeWidth(value);
	}

	public Paint getPaint() {
		return paint;
	}

	private void initializePaint(int penColor, int strokeWidth) {
		paint = new Paint();
		paint.setColor(penColor);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeJoin(Paint.Join.ROUND);
		paint.setStrokeCap(Paint.Cap.ROUND);
		paint.setStrokeWidth(strokeWidth);
	}

}
