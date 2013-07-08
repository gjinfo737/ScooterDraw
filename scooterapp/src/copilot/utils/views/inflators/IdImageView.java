package copilot.utils.views.inflators;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class IdImageView extends ImageView {

	public IdImageView(Context context) {
		super(context);
	}

	public IdImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public IdImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void setImageResource(int resId) {
		setTag(resId);
		super.setImageResource(resId);
	}

	@Override
	public Object getTag() {
		return super.getTag();
	}
}
