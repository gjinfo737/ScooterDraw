package copilot.utils.views.inflators;

import java.util.Map;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.drawshtuff.drawer.R.layout;

import copilot.app.data.RefForm.SyncState;

public class SyncStateItemInflater {

	public static final int SYNC_SPINNER_ITEM_LABEL = 99;
	public static final int SYNC_SPINNER_ITEM_ICON = 98;
	public static final int LAYOUT_SYNC_SPINNER_ITEM = layout.syncstate_spinner_item;
	private LayoutInflater layoutInflater;
	private Map<SyncState, EnableDependentLayoutResource> layoutMap;

	public SyncStateItemInflater(LayoutInflater layoutInflater, Map<SyncState, EnableDependentLayoutResource> layoutMap) {
		this.layoutInflater = layoutInflater;
		this.layoutMap = layoutMap;
	}

	public View inflate(SyncState sync, boolean enabled) {
		ViewGroup view = (ViewGroup) layoutInflater.inflate(LAYOUT_SYNC_SPINNER_ITEM, null);
		TextView label = (TextView) view.findViewById(SYNC_SPINNER_ITEM_LABEL);
		IdImageView icon = (IdImageView) view.findViewById(SYNC_SPINNER_ITEM_ICON);

		label.setText(sync.getFriendlyName());
		label.setEnabled(enabled);
		icon.setImageResource(determineIconResource(sync, enabled));

		return view;
	}

	private int determineIconResource(SyncState sync, boolean enabled) {
		return layoutMap.get(sync).getDrawable(enabled);
	}

}
