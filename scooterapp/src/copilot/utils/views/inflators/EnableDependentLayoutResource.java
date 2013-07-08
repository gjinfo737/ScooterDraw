package copilot.utils.views.inflators;

public class EnableDependentLayoutResource {

	private int enabledLayout;

	private int disableDrawable;

	public EnableDependentLayoutResource(int enabledLayout, int disableLayout) {
		this.enabledLayout = enabledLayout;
		this.disableDrawable = disableLayout;
	}

	public int getDrawable(boolean enabled) {
		return enabled ? enabledLayout : disableDrawable;
	}

}
