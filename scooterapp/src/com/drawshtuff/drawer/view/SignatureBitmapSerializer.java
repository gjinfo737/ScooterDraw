package com.drawshtuff.drawer.view;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;

public class SignatureBitmapSerializer {
	private static final int COMPRESSION_QUALITY = 90;
	private final float scaleFactor;
	private final Bitmap originalBitmap;

	public SignatureBitmapSerializer(Bitmap originalBitmap, float scaleFactor) {
		this.scaleFactor = scaleFactor;
		this.originalBitmap = originalBitmap;
	}

	public void serializeBitmapAsync() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				serializeBitmap();
			}

		}).start();
	}

	public void serializeBitmap() {
		getSignatureByteArray(originalBitmap, scaleFactor);// FIXME DOES NOTHING
	}

	public static byte[] getSignatureByteArray(Bitmap bm) {
		return getSignatureByteArray(bm, 1);
	}

	public static byte[] getSignatureByteArray(Bitmap bm, float scale) {
		byte[] byteArray = null;
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		if (bm != null) {
			Bitmap scaledCopy = copyBitmapByScale(bm, (1f / scale));
			scaledCopy.compress(Bitmap.CompressFormat.PNG, COMPRESSION_QUALITY, stream);
			byteArray = stream.toByteArray();
		}

		return byteArray;
	}

	public static Bitmap copyBitmapByScale(Bitmap bitmap, float scale) {
		int scaledCopyWidth = (int) (bitmap.getWidth() * scale);
		int scaledCopyHeight = (int) (bitmap.getHeight() * scale);
		Bitmap scaledCopy = Bitmap.createBitmap(scaledCopyWidth, scaledCopyHeight, bitmap.getConfig());
		Canvas canvasOfScaledCopy = new Canvas(scaledCopy);
		canvasOfScaledCopy.scale(scale, scale);
		canvasOfScaledCopy.drawBitmap(bitmap, 0, 0, null);
		return scaledCopy;
	}

	public static void drawBitmap(byte[] drawing, float scale, Canvas canvas) throws IllegalArgumentException {
		if (drawing == null)
			throw new IllegalArgumentException("Cannot decode null byte[]");
		Bitmap mutableBitmap = getMutableBitmap(drawing);
		mutableBitmap = SignatureBitmapSerializer.copyBitmapByScale(mutableBitmap, scale);
		canvas.drawBitmap(mutableBitmap, 0, 0, null);
	}

	private static Bitmap getMutableBitmap(byte[] drawingBmpBytes) throws IllegalArgumentException {
		if (drawingBmpBytes == null)
			throw new IllegalArgumentException("Cannot decode null byte[]");
		Options opts = new Options();
		opts.inMutable = true;
		return BitmapFactory.decodeByteArray(drawingBmpBytes, 0, drawingBmpBytes.length, opts);
	}
}
