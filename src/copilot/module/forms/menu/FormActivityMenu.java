package copilot.module.forms.menu;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Spinner;

import com.drawshtuff.drawer.R.drawable;
import com.drawshtuff.drawer.R.string;

import copilot.app.data.RefForm;
import copilot.app.data.RefForm.SyncState;
import copilot.utils.views.inflators.EnableDependentLayoutResource;

public class FormActivityMenu {
	public static final int BUTTON_PAGE_SEEKBAR_DIALOG_ID = -100;
	public static final int BUTTON_SAVE_PAGE_ID = -101;
	public static final int BUTTON_SIGN_PAGE_ID = -102;
	public static final int BUTTON_EMAIL_FORM_ID = -103;
	public static final int BUTTON_PRINT_FORM_ID = -104;
	public static final int BUTTON_PEN_WEIGHT_ID = -105;
	public static final int BUTTON_DELETE_FORM = -106;
	public static final int BUTTON_SYNC_ID = -107;
	private Context context;
	private LayoutInflater layoutInflater;
	public static Map<SyncState, EnableDependentLayoutResource> SYNC_SPINNER_DRAWABLE_MAP = new HashMap<RefForm.SyncState, EnableDependentLayoutResource>() {
		private static final long serialVersionUID = 4920727396231608296L;
		{
			put(SyncState.SYNC, new EnableDependentLayoutResource(drawable.sync_spinner_sync_enabled, drawable.sync_spinner_sync_disabled));
			put(SyncState.DO_NOT_SYNC, new EnableDependentLayoutResource(drawable.sync_spinner_donotsync_enabled, drawable.sync_spinner_donotsync_disabled));
			put(SyncState.FAVORITE, new EnableDependentLayoutResource(drawable.sync_spinner_favorite_enabled, drawable.sync_spinner_favorite_disabled));
		}
	};

	public FormActivityMenu(Context context, LayoutInflater layoutInflater) {
		this.context = context;
		this.layoutInflater = layoutInflater;
	}

	public void fillMenu(Menu menu, IFormMenuItemStateProvider stateProvider, SyncState initialSyncState) {
		addNewMenuItem(menu, -1, BUTTON_PAGE_SEEKBAR_DIALOG_ID, string.eng_0_form_menu_title_seekbar, true);
		addSyncSpinner(addNewMenuItem(menu, drawable.forms_pen_weight_icon, BUTTON_SYNC_ID, string.eng_0_form_menu_title_sync, true), stateProvider, initialSyncState);
		addNewMenuItem(menu, drawable.forms_pen_weight_icon, BUTTON_PEN_WEIGHT_ID, string.eng_0_form_menu_title_pen_weight_icon, true);
		addNewMenuItem(menu, drawable.forms_sign_icon, BUTTON_SIGN_PAGE_ID, string.eng_0_form_menu_title_sign, true);
		addNewMenuItem(menu, drawable.theme_std_menu_email, BUTTON_EMAIL_FORM_ID, string.eng_0_form_menu_title_email, true);
		addNewMenuItem(menu, drawable.theme_std_menu_print, BUTTON_PRINT_FORM_ID, string.eng_0_form_menu_title_print, true);
		addNewMenuItem(menu, drawable.theme_std_menu_delete, BUTTON_DELETE_FORM, string.eng_0_form_menu_title_delete, true);
		addNewMenuItem(menu, drawable.theme_std_menu_save, BUTTON_SAVE_PAGE_ID, string.eng_0_form_menu_title_save, true);
	}

	public MenuItem addNewMenuItem(Menu parent, int icon, int id, int title, boolean alwaysShow) {
		MenuItem oRet = parent.add(Menu.NONE, id, Menu.NONE, title);

		int showAsAction = (alwaysShow ? MenuItem.SHOW_AS_ACTION_ALWAYS : MenuItem.SHOW_AS_ACTION_IF_ROOM);
		if (icon != -1) {
			oRet.setIcon(icon);
		}

		if (id == BUTTON_SYNC_ID) {
			oRet.setShowAsAction(showAsAction | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		} else {
			oRet.setShowAsAction(showAsAction);
		}

		return oRet;
	}

	private void addSyncSpinner(MenuItem item, IFormMenuItemStateProvider stateProvider, SyncState intialSelection) {
		// SyncSpinnerAdapter adapter = new SyncSpinnerAdapter(layoutInflater,
		// SYNC_SPINNER_DRAWABLE_MAP);
		// SyncSpinner syncSpinner = new SyncSpinner(context, layoutInflater,
		// intialSelection, adapter, stateProvider);
		// item.setActionView(syncSpinner.getSpinner());
	}

	public void refreshSaveItem(Menu menu, boolean enabled, IDrawableResourceProvider drawableProvider) {
		refreshItem(menu, enabled, BUTTON_SAVE_PAGE_ID, drawableProvider);
	}

	public void refreshSignItem(Menu menu, boolean enabled, IDrawableResourceProvider drawableProvider) {
		refreshIcon(menu, enabled, BUTTON_SIGN_PAGE_ID, drawableProvider);
	}

	public void refreshEmailItem(Menu menu, boolean enabled, IDrawableResourceProvider drawableProvider) {
		refreshItem(menu, enabled, BUTTON_EMAIL_FORM_ID, drawableProvider);
	}

	public void refreshPrintItem(Menu menu, boolean enabled, IDrawableResourceProvider drawableProvider) {
		refreshItem(menu, enabled, BUTTON_PRINT_FORM_ID, drawableProvider);
	}

	public void refreshTheSeekBar(Menu menu, String title) {
		refreshTitle(menu, BUTTON_PAGE_SEEKBAR_DIALOG_ID, title);
	}

	public void refreshDeleteItem(Menu menu, boolean enabled, IDrawableResourceProvider drawableProvider) {
		refreshItem(menu, enabled, BUTTON_DELETE_FORM, drawableProvider);
	}

	public void refreshSyncStateItem(Menu menu, boolean enabled) {
		MenuItem item = menu.findItem(BUTTON_SYNC_ID);
		final Spinner spinner = (Spinner) item.getActionView();
		spinner.setEnabled(enabled);
		// ((SyncSpinnerAdapter) spinner.getAdapter()).setEnabled(enabled);
	}

	public void refreshSyncStateItemSelection(Menu menu, SyncState syncState) {
		MenuItem item = menu.findItem(BUTTON_SYNC_ID);
		final Spinner spinner = (Spinner) item.getActionView();
		spinner.setSelection(syncState.getValue());
	}

	private void refreshItem(Menu menu, boolean enabled, int itemId, IDrawableResourceProvider drawableProvider) {
		refreshEnabled(menu, enabled, itemId);
		refreshIcon(menu, enabled, itemId, drawableProvider);
	}

	private void refreshEnabled(Menu menu, boolean enabled, int itemId) {
		menu.findItem(itemId).setEnabled(enabled);
	}

	private void refreshIcon(Menu menu, boolean enabled, int itemId, IDrawableResourceProvider drawableProvider) {
		menu.findItem(itemId).setIcon(drawableProvider.getResourceId(itemId, enabled));
	}

	private void refreshTitle(Menu menu, int itemId, String title) {
		menu.findItem(itemId).setTitle(title);
	}

	public SyncState getSyncState(Menu menu) {
		// MenuItem item = menu.findItem(BUTTON_SYNC_ID);
		// Spinner spinner = (Spinner) item.getActionView();
		// SyncStateItem selectedItem = (SyncStateItem)
		// spinner.getSelectedItem();
		// return selectedItem.getSyncState();
		return SyncState.SYNC;
	}
}
