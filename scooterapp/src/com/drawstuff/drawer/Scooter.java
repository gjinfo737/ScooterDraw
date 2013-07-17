package com.drawstuff.drawer;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.widget.RelativeLayout;

import com.drawshtuff.drawer.view.DrawView;
import com.drawstuff.drawer.R.id;
import com.drawstuff.drawer.R.layout;

public class Scooter extends Activity {

	private DrawView drawView;
	private DrawMenu drawMenu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(layout.draw);
		drawView = new DrawView(getApplicationContext(), 800, 600);
		RelativeLayout relativeLayout = (RelativeLayout) findViewById(id.main_rel_lay);
		relativeLayout.setBackgroundColor(Color.GRAY);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(800, 600);
		params.addRule(RelativeLayout.CENTER_IN_PARENT);
		relativeLayout.addView(drawView, params);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		drawMenu = new DrawMenu(menu, drawView);
		return super.onCreateOptionsMenu(menu);
	}
}
