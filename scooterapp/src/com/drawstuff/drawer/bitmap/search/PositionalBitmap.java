package com.drawstuff.drawer.bitmap.search;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;

public class PositionalBitmap {

	private final Point position;
	private final Bitmap bitmap;
	private float scale;

	public PositionalBitmap(Bitmap bitmap, Rect rect) {
		this(bitmap, new Point(rect.left, rect.top));
	}

	public PositionalBitmap(Bitmap bitmap, Point position) {
		this.bitmap = bitmap;
		this.position = position;
		this.scale = 1;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public Point getPositin() {
		return position;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public int getX() {
		return (int) (position.x * scale);
	}

	public int getY() {
		return (int) (position.y * scale);
	}

	public int getWidth() {
		return (int) (bitmap.getWidth() * scale);
	}

	public int getHeight() {
		return (int) (bitmap.getHeight() * scale);
	}
}
