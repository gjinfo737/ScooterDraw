package com.drawshtuff.drawer;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.drawshtuff.drawer.R.drawable;
import com.drawshtuff.drawer.R.id;
import com.drawshtuff.drawer.R.string;
import com.drawshtuff.drawer.copilot.SignatureView;

import copilot.app.data.RefForm.SyncState;
import copilot.module.forms.menu.FormActivityMenu;
import copilot.module.forms.menu.IFormMenuItemStateProvider;

public class Draw extends Activity implements IFormMenuItemStateProvider {

	private RelativeLayout drawArea;
	private SignatureView signatureView;
	public static final int DEFAULT_PEN_GIRTH = 10;
	public static final int DEFAULT_PEN_COLOR = Color.BLACK;
	private Button btnDraw;
	private Button btnErase;
	private Button btnHigh;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_draw);
		setupBtns();
		drawArea = (RelativeLayout) findViewById(id.draw_area);
		signatureView = new SignatureView(getApplicationContext(), null);
		drawArea.addView(signatureView);

		startDrawing();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		FormActivityMenu formActivityMenu = new FormActivityMenu(getApplicationContext(), getLayoutInflater());
		formActivityMenu.fillMenu(menu, this, SyncState.SYNC);
		formActivityMenu.addNewMenuItem(menu, drawable.sync_sync_color, 100000, string.app_name, true);
		return true;
	}

	private void setupBtns() {
		btnDraw = (Button) findViewById(id.button_draw);
		btnErase = (Button) findViewById(id.button_erase);
		btnHigh = (Button) findViewById(id.button_high);
		btnDraw.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startDrawing();
			}
		});
		btnErase.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startErasing();
			}
		});
		btnHigh.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startHighlighting();

			}
		});
		((Button) findViewById(id.button_clean)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});
		((Button) findViewById(id.button_black)).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				signatureView.setColor(0, 0, 0);
			}
		});
		((Button) findViewById(id.button_red)).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				signatureView.setColor(255, 0, 0);
			}
		});
	}

	private void startDrawing() {

		resetBools();
		setPenAlpha(255);
		toast("Started to draw!");
		btnDraw.setText(">Draw<");
		btnErase.setText("Erase");
		btnHigh.setText("High");
	}

	private void resetBools() {
		signatureView.setErasing(false);
	}

	private void startErasing() {
		resetBools();
		signatureView.setErasing(true);
		setPenAlpha(0);
		toast("Started to erase!!");
		btnDraw.setText("Draw");
		btnErase.setText(">Erase<");
		btnHigh.setText("High");
	}

	private void startHighlighting() {
		resetBools();
		setPenAlpha(15);
		toast("Started to high!!");
		btnDraw.setText("Draw");
		btnErase.setText("Erase");
		btnHigh.setText(">High<");
	}

	private void setPenAlpha(int alpha) {
		signatureView.setPenPaint(getNewPenPaint(DEFAULT_PEN_COLOR, DEFAULT_PEN_GIRTH, alpha));
	}

	private void toast(String msg) {
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
	}

	private Paint getNewPenPaint(int penColor, int strokeWidth, int alpha) {
		Paint paint = new Paint();

		paint.setColor(penColor);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeJoin(Paint.Join.ROUND);
		paint.setStrokeCap(Paint.Cap.ROUND);
		paint.setStrokeWidth(strokeWidth);
		if (alpha == 0)
			paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
		else if (alpha == 15) {
			paint.setColor(Color.YELLOW);
			paint.setAlpha(alpha);
		}
		return paint;
	}

	@Override
	public boolean canSave() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getShowPageMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean shouldSyncStateSpinnerBeEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isReadOnly() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isPKPositive() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canMail() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canPrint() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canDeleteForm() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public SyncState syncState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isDrawing() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isDivertedState() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSavedForm() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onSyncStateChanged() {
		// TODO Auto-generated method stub

	}

}
