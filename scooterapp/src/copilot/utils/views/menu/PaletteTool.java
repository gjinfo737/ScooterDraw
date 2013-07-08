package copilot.utils.views.menu;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class PaletteTool extends ImageView implements OnClickListener {
	public PaletteTool(Context context) {
		super(context);
		init();
	}

	public PaletteTool(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public PaletteTool(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		setOnClickListener(this);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		performClick();
		return true;
	}

	@Override
	public void onClick(View v) {
		Log.e("", "****");
	}
}
