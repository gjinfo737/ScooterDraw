package com.drawshtuff.drawer.view;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Pather {
	private ArrayList<SerializablePath> paths;
	private SerializablePath currentPath;
	private float X;
	private float Y;

	public Pather() {
		this.paths = new ArrayList<SerializablePath>();
	}

	public void drawPaths(Canvas canvas, Paint paint) {
		for (SerializablePath path : paths) {
			canvas.drawPath(path, paint);
		}
	}

	public void onTouchStart(float x, float y) {
		this.currentPath = new SerializablePath();
		this.currentPath.moveTo(x, y);
		this.paths.add(currentPath);
		this.X = x;
		this.Y = y;
	}

	public void onTouchMove(float x, float y, float tolerance) {
		if (this.currentPath == null)
			onTouchStart(x, y);

		float dx = Math.abs(x - X);
		float dy = Math.abs(y - Y);

		if (dx >= tolerance || dy >= tolerance) {
			currentPath.quadTo(X, Y, (x + X) / 2, (y + Y) / 2);
			X = x;
			Y = y;
		}
	}

	public void onTouchUp() {
		currentPath.lineTo(X, Y);
	}

	public ArrayList<SerializablePath> getPaths() {
		return paths;
	}

	public void clearPaths() {
		this.paths.clear();
	}
}
