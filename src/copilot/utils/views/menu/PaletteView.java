package copilot.utils.views.menu;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.drawshtuff.drawer.R.drawable;
import com.drawshtuff.drawer.R.id;
import com.drawshtuff.drawer.R.layout;

public class PaletteView extends Spinner {
	private boolean open;
	private LayoutInflater layoutInflater;

	public PaletteView(Context context, LayoutInflater layoutInflater) {
		super(context);
		this.layoutInflater = layoutInflater;
		initialize();
	}

	private void initialize() {
		setAdapter(new Spapter());
		setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				performClick();
			}
		});
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
			View view = layoutInflater.inflate(layout.form_pallete, null);
			view.findViewById(id.palette_layout).setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					return true;
				}
			});
			return view;
		}
	}

}
