package com.drawshtuff.drawer.copilot;

import java.util.ArrayList;

import android.graphics.Path;
import android.graphics.RectF;

public class SerializablePath extends Path {

	private ArrayList<float[]> m_points;

	public ArrayList<float[]> getPoints() {
		return m_points;
	}

	public SerializablePath() {
		super();
		m_points = new ArrayList<float[]>();
	}

	@Override
	public void moveTo(float x, float y) {
		m_points.add(new float[] { x, y });

		super.moveTo(x, y);
	}

	@Override
	public void quadTo(float x1, float y1, float x2, float y2) {
		m_points.add(new float[] { x1, y1, x2, y2 });

		super.quadTo(x1, y1, x2, y2);
	}

	@Override
	public void lineTo(float x, float y) {
		m_points.add(new float[] { x, y });

		super.lineTo(x, y);
	}

	public void adjustForOffsets(int offsetX, int offsetY) {
		ArrayList<float[]> tempPoints = new ArrayList<float[]>();

		for (int i = 0; i < m_points.size(); i++) {
			if (m_points.get(i).length == 2) {
				tempPoints.add(new float[] { m_points.get(i)[0] + offsetX, m_points.get(i)[1] += offsetY });
			} else if (m_points.get(i).length == 4) {
				tempPoints.add(new float[] { m_points.get(i)[0] + offsetX, m_points.get(i)[1] += offsetY, m_points.get(i)[2] + offsetX, m_points.get(i)[3] + offsetY });
			}
		}

		m_points.clear();

		replayPoints(tempPoints, this);
	}

	public boolean contains(int x, int y) {
		return getBoundingRectangle(m_points).contains(x, y);
	}

	public RectF getBoundingRectangle(ArrayList<float[]> points) {
		RectF oRet = new RectF();
		this.computeBounds(oRet, true);
		return oRet;
	}

	public static SerializablePath createInstance(ArrayList<float[]> points) {
		SerializablePath oRet = new SerializablePath();

		replayPoints(points, oRet);

		return oRet;
	}

	public static void replayPoints(ArrayList<float[]> points, Path path) {
		path.reset();
		if (points.size() > 2) {

			for (int i = 0; i < points.size(); i++) {
				if (i == 0) { // first item is our moveTo()
					path.moveTo(points.get(i)[0], points.get(i)[1]);
				} else if (i == (points.size() - 1)) { // last item is a lineTo
					path.lineTo(points.get(i)[0], points.get(i)[1]);
				} else { // everything else is a quadTo()
					path.quadTo(points.get(i)[0], points.get(i)[1], points.get(i)[2], points.get(i)[3]);
				}
			}

		}
	}

}
