package copilot.module.forms.menu;

import copilot.app.data.RefForm.SyncState;

public interface IFormMenuItemStateProvider {
	public boolean canSave();

	public String getShowPageMessage();

	public boolean shouldSyncStateSpinnerBeEnabled();

	public boolean isReadOnly();

	public boolean isPKPositive();

	public boolean canMail();

	public boolean canPrint();

	public boolean canDeleteForm();

	public SyncState syncState();

	public boolean isDrawing();

	public boolean isDivertedState();

	public boolean isSavedForm();

	public void onSyncStateChanged();

}
