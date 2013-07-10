package com.drawshtuff.drawer.copilot;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

import com.drawshtuff.drawer.ColorBox;

import copilot.utils.views.bitmap.search.BitmapSearcher;
import copilot.utils.views.bitmap.search.BitmapSearcher.IBitmapSearcherListener;

public class SignatureView extends View {

	public static final float SEARCH_DENSITY = .02f;
	private ArrayList<SerializablePath> m_paths;
	private SerializablePath m_currentPath;
	private Paint m_penPaint;
	private float m_X;
	private float m_Y;
	private Bitmap bitmap;
	private boolean erasing;

	private static final float TOUCH_TOLERANCE = 4;
	private Paint clearPaint;
	Canvas highCanvas;
	Canvas drawCanvas;

	public ArrayList<SerializablePath> getPaths() {
		return m_paths;
	}

	public SignatureView(Context c, Paint penPaint) {
		super(c);
		colorBox = new ColorBox();
		m_paths = new ArrayList<SerializablePath>();
		m_penPaint = penPaint;
		bitmap = Bitmap.createBitmap(800, 600, Bitmap.Config.ARGB_8888);

		clearPaint = new Paint();
		clearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

		drawCanvas = new Canvas(bitmap);
	}

	public void setPenPaint(Paint value) {
		m_penPaint = value;
	}

	@Override
	public void onDraw(Canvas canvas) {
		canvas.drawColor(Color.WHITE);
		for (SerializablePath path : m_paths) {
			drawCanvas.drawPath(path, m_penPaint);
		}

		canvas.drawBitmap(bitmap, 0, 0, null);
	}

	private void touch_start(float x, float y) {
		m_currentPath = new SerializablePath();
		m_currentPath.moveTo(x, y);
		m_paths.add(m_currentPath);

		m_X = x;
		m_Y = y;
	}

	private void touch_move(float x, float y) {
		float dx = Math.abs(x - m_X);
		float dy = Math.abs(y - m_Y);

		if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
			m_currentPath.quadTo(m_X, m_Y, (x + m_X) / 2, (y + m_Y) / 2);
			m_X = x;
			m_Y = y;
		}
	}

	private void touch_up() {
		m_currentPath.lineTo(m_X, m_Y);
	}

	PointF least = new PointF();
	PointF greatest = new PointF();
	private ColorBox colorBox;
	private BitmapSearcher bitmapSearcher;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			touch_start(x, y);
			least = new PointF(x, y);
			greatest = new PointF(x, y);
			invalidate();
			break;

		case MotionEvent.ACTION_MOVE:
			touch_move(x, y);
			if (x < least.x)
				least.x = x;
			if (y < least.y)
				least.y = y;
			if (x > greatest.x)
				greatest.x = x;
			if (y > greatest.y)
				greatest.y = y;
			invalidate();
			break;

		case MotionEvent.ACTION_UP:
			touch_up();
			invalidate();
			clearPaths();

			break;
		}

		return true;
	}

	public boolean isErasing() {
		return erasing;
	}

	public void setErasing(boolean erasing) {
		this.erasing = erasing;
	}

	private void clearPaths() {
		m_paths = new ArrayList<SerializablePath>();
	}

	public void setColor(int r, int g, int b) {
		m_penPaint.setARGB(m_penPaint.getAlpha(), r, g, b);
	}

	public void cropSearch(IBitmapSearcherListener bitmapSearcherListener) {
		final SignatureView view = this;
		final Handler handler = new Handler();
		bitmapSearcher = new BitmapSearcher();
		bitmapSearcher.cropSearchBitmap(bitmap, bitmapSearcherListener, SEARCH_DENSITY, handler, new Runnable() {
			@Override
			public void run() {
				view.invalidate();
			}
		});
	}

	public void clean() {
		Canvas canvas = new Canvas(bitmap);
		canvas.drawColor(Color.WHITE);
		invalidate();
	}

	public void stopSearching() {
		if (bitmapSearcher != null)
			bitmapSearcher.stopSearching();
	}
}
