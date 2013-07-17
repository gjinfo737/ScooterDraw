package com.drawstuff.drawer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.R.drawable;
import android.view.Menu;
import android.view.MenuItem;

import com.drawshtuff.drawer.view.DrawView;
import com.drawstuff.drawer.DrawableMenuItem.MenuItemState;

@SuppressWarnings("serial")
public class DrawMenu {

	private static final int ID_PEN = 1000;
	private static final int ID_ERASER = 1001;
	private final Menu menu;
	private final DrawView drawView;
	private static final Map<MenuItemState, Integer> penItemDrawables = new HashMap<MenuItemState, Integer>() {
		{
			put(MenuItemState.ENABLED, drawable.btn_radio);
			put(MenuItemState.DISABLED, drawable.btn_star_big_off);
			put(MenuItemState.SELECTED, drawable.btn_star_big_on);
		}
	};
	private static final Map<MenuItemState, Integer> eraserItemDrawables = new HashMap<MenuItemState, Integer>() {
		{
			put(MenuItemState.ENABLED, drawable.btn_default);
			put(MenuItemState.DISABLED, drawable.btn_default_small);
			put(MenuItemState.SELECTED, drawable.btn_plus);
		}
	};
	private List<DrawableMenuItem> drawableMenuItems;

	public DrawMenu(Menu menu, DrawView drawView) {
		this.menu = menu;
		this.drawView = drawView;
		addItems();
	}

	private void addItems() {
		drawableMenuItems = new ArrayList<DrawableMenuItem>();
		drawableMenuItems.add(addNewMenuItem(true, ID_PEN, penItemDrawables));
		drawableMenuItems.add(addNewMenuItem(true, ID_ERASER, eraserItemDrawables));
	}

	private DrawableMenuItem addNewMenuItem(boolean enabledState, int id, Map<MenuItemState, Integer> icons) {
		MenuItem item = menu.add(Menu.NONE, id, Menu.NONE, "");
		item.setEnabled(enabledState);
		item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return new DrawableMenuItem(item, icons, this);
	}

	public boolean onMenuItemClick(DrawableMenuItem drawableMenuItem) {
		int itemId = drawableMenuItem.getItemId();
		MenuItemState itemState = drawableMenuItem.getItemState();
		resetOthers(itemId);
		switch (itemId) {
		case ID_PEN:
			if (itemState.equals(MenuItemState.SELECTED)) {
				drawView.onPenStart();
			} else {
				drawView.onPenEnd();
			}
			break;
		case ID_ERASER:
			if (itemState.equals(MenuItemState.SELECTED)) {
				drawView.onEraseStart();
			} else {
				drawView.onEraseEnd();
			}
			break;

		default:
			break;
		}
		return false;
	}

	private void resetOthers(int itemId) {
		for (DrawableMenuItem dmi : drawableMenuItems) {
			if (dmi.getItemId() != itemId && dmi.getItemState().equals(MenuItemState.SELECTED)) {
				dmi.setItemState(MenuItemState.ENABLED);
			}
		}
	}
}
