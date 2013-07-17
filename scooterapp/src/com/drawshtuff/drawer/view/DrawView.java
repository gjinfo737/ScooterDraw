package com.drawshtuff.drawer.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

import com.drawstuff.drawer.bitmap.search.BitmapSearcher;

public class DrawView extends View {

	private static final float TOUCH_TOLERANCE = 4;

	private Bitmap savedBitmap;
	private Canvas savedBitmapCanvas;
	private PointF leastPoint;
	private PointF greatestPoint;

	private final Pather pather;

	private boolean clear;
	private float drawingOffsetX;
	private float drawingOffsetY;
	private boolean isPathDrawing;
	private PaintPalette paintPalette;

	private boolean isPenning;

	private boolean isErasing;

	public DrawView(Context c, int scaledWidth, int scaledheight) {
		super(c);
		this.pather = new Pather();
		this.paintPalette = new PaintPalette();
		createCanvas(scaledWidth, scaledheight, Color.WHITE);
		this.leastPoint = new PointF(9999, 9999);
		this.greatestPoint = new PointF(0, 0);
	}

	public void setOffset(float offset_x, float offset_y) {
		this.drawingOffsetX = offset_x;
		this.drawingOffsetY = offset_y;
	}

	@Override
	public void onDraw(Canvas canvas) {
		pather.drawPaths(savedBitmapCanvas, paintPalette.getPaint());
		canvas.drawBitmap(savedBitmap, -drawingOffsetX, -drawingOffsetY, null);
		if (clear) {
			pather.clearPaths();
			clear = false;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (!isPenning && !isErasing)
			return false;
		float x = event.getX() + drawingOffsetX;
		float y = event.getY() + drawingOffsetY;

		int action = event.getAction();
		touch(action, x, y);
		return true;
	}

	public PointF getleastPoint() {
		return leastPoint;
	}

	private void createCanvas(int width, int height, int color) {
		savedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		savedBitmapCanvas = new Canvas(savedBitmap);
		savedBitmapCanvas.drawColor(color);
	}

	private void touch(int action, float x, float y) {
		if (touchIsInBounds(x, y, getBounds())) {
			calculateLeastGreatestPoint(x, y);

			switch (action) {
			case MotionEvent.ACTION_DOWN:
				isPathDrawing = true;
				pather.onTouchStart(x, y);
				invalidate();
				break;

			case MotionEvent.ACTION_MOVE:
				pather.onTouchMove(x, y, TOUCH_TOLERANCE);
				invalidate();
				break;

			case MotionEvent.ACTION_UP:
				onStopPathDrawing(false);
				cropSearch();
				break;
			}
		}
	}

	private void cropSearch() {
		new BitmapSearcher().cropSearchBitmap(savedBitmap, .05f);
	}

	public void onStopPathDrawing(boolean clearNow) {
		if (isPathDrawing) {
			isPathDrawing = false;
			pather.onTouchUp();
			if (clearNow) {
				pather.clearPaths();
			} else {
				clear = true;
				invalidate();
			}
		}
	}

	private Rect getBounds() {
		return new Rect(0, 0, savedBitmap.getWidth(), savedBitmap.getHeight());
	}

	public boolean touchIsInBounds(float x, float y, Rect bounds) {
		return bounds.contains((int) x, (int) y);
	}

	private void calculateLeastGreatestPoint(float x, float y) {
		if (x < leastPoint.x)
			leastPoint.x = x;
		if (y < leastPoint.y)
			leastPoint.y = y;
		if (x > greatestPoint.x)
			greatestPoint.x = x;
		if (y > greatestPoint.y)
			greatestPoint.y = y;
	}

	public Rect getFullyBoundingRectangle() {
		return new Rect((int) leastPoint.x, (int) leastPoint.y, (int) greatestPoint.x, (int) greatestPoint.y);
	}

	public void drawBitmap(byte[] drawing, float scale) throws IllegalArgumentException {
		SignatureBitmapSerializer.drawBitmap(drawing, scale, savedBitmapCanvas);
	}

	public Bitmap getSavedBitmap() {
		return savedBitmap;
	}

	public PointF getOffset() {
		return new PointF(drawingOffsetX, drawingOffsetY);
	}

	public void onPenStart() {
		onEraseEnd();
		this.isPenning = true;
		paintPalette.setToDrawing();
	}

	public void onPenEnd() {
		this.isPenning = false;
	}

	public void onEraseStart() {
		onPenEnd();
		this.isErasing = true;
		paintPalette.setToErasing();
	}

	public void onEraseEnd() {
		this.isErasing = false;
	}

}
