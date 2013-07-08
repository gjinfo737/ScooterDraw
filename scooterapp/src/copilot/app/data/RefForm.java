package copilot.app.data;

import java.io.Serializable;

public class RefForm implements Serializable, Comparable<RefForm>, Cloneable {

	private static final long serialVersionUID = -2080135393453215565L;
	private static long sNextPrimaryKey = -1;

	public enum SyncState {
		DO_NOT_SYNC(0), SYNC(1), FAVORITE(2);

		private final int id;

		SyncState(int id) {
			this.id = id;
		}

		public static SyncState getEnum(int id) {
			SyncState[] values = values();
			for (SyncState syncState : values) {
				if (syncState.getValue() == id)
					return syncState;
			}
			return SYNC;
		}

		public int getValue() {
			return id;
		}

		public String getFriendlyName() {
			switch (this) {
			case DO_NOT_SYNC:
				return "Do Not Sync";
			case FAVORITE:
				return "Favorite";
			default:
			case SYNC:
				return "Sync";
			}
		}

	}

	@Override
	public int compareTo(RefForm another) {
		// TODO Auto-generated method stub
		return 0;
	}

}
