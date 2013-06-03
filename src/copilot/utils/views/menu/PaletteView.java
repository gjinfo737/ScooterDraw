package copilot.utils.views.menu;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.drawshtuff.drawer.R.drawable;

public class PaletteView extends Spinner {
	private boolean open;

	public PaletteView(Context context) {
		super(context);
		initialize();
	}

	public PaletteView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize();
	}

	public PaletteView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initialize();
	}

	private void initialize() {
		setAdapter(new Spapter());
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return super.onTouchEvent(event);
		// if (!open) {
		// open = true;
		// return super.onTouchEvent(event);
		// }
		// open = !open;
		// return false;
	}

	public class Spapter extends BaseAdapter {
		private List<String> list = new ArrayList<String>() {
			{
				add("one");
			}
		};

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView imageView = new ImageView(getContext());
			imageView.setImageResource(drawable.actionbar_sync_checked);
			return imageView;
		}

		@Override
		public View getDropDownView(int position, View convertView, ViewGroup parent) {

			PaletteTool item = new PaletteTool(getContext(), drawable.forms_pen_weight_icon, "weight");

			return item;
		}
	}

	public class PaletteTool extends ImageView implements OnClickListener {

		private String id;

		public PaletteTool(Context context, int icon, String id) {
			super(context);
			setOnClickListener(this);
			setImageResource(icon);
			this.id = id;
		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			performClick();
			return true;
		}

		@Override
		public void onClick(View v) {
			Log.e("", "**" + id + "**");
		}

	}
}
