package campusreserve.testutil;

import java.time.LocalDateTime;

public final class TestUtil {
	private TestUtil() {
	}

	public static LocalDateTime futureMinutes(int minutes) {
		return LocalDateTime.now().plusMinutes(minutes);
	}
}

