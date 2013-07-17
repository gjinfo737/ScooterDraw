package com.drawstuff.drawer;

import java.util.Map;

import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;

public class DrawableMenuItem implements OnMenuItemClickListener {

	private final MenuItem item;
	private final Map<MenuItemState, Integer> icons;
	private MenuItemState itemState;
	private boolean isSelected;
	private DrawMenu drawMenu;

	public enum MenuItemState {
		SELECTED, DISABLED, ENABLED
	}

	public DrawableMenuItem(MenuItem item, Map<MenuItemState, Integer> icons, DrawMenu drawMenu) {
		this.item = item;
		this.icons = icons;
		this.item.setOnMenuItemClickListener(this);
		itemState = item.isEnabled() ? MenuItemState.ENABLED : MenuItemState.DISABLED;
		this.drawMenu = drawMenu;
		refreshIcon();
	}

	@Override
	public boolean onMenuItemClick(MenuItem item) {
		if (item.isEnabled()) {
			toggleSelect();
		} else {
			setDisabled();
		}
		drawMenu.onMenuItemClick(this);
		return true;
	}

	private void setDisabled() {
		itemState = MenuItemState.DISABLED;
		refreshIcon();
	}

	private void toggleSelect() {
		if (isSelected)
			itemState = MenuItemState.ENABLED;
		else
			itemState = MenuItemState.SELECTED;
		isSelected = !isSelected;
		refreshIcon();
	}

	public MenuItemState getItemState() {
		return itemState;
	}

	public void setItemState(MenuItemState itemState) {
		this.itemState = itemState;
		isSelected = itemState == MenuItemState.SELECTED;
		refreshIcon();
	}

	private void refreshIcon() {
		item.setIcon(icons.get(itemState));
	}

	public int getItemId() {
		return item.getItemId();
	}

}
