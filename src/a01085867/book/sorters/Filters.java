package a01085867.book.sorters;

public class Filters {

	private static boolean byAuthor;
	private static boolean descending;
	private static boolean byJoinDate;
	private static boolean byLastName;
	private static boolean byCustomerId;
	private static boolean byTitle;

	/**
	 * @return the byAuthor
	 */
	public static boolean isByAuthor() {
		return byAuthor;
	}

	/**
	 * @param byAuthor
	 *            the byAuthor to set
	 */
	public static void setByAuthor(boolean byAuthor) {
		Filters.byAuthor = byAuthor;
	}

	/**
	 * @return the descending
	 */
	public static boolean isDescending() {
		return descending;
	}

	/**
	 * @param descending
	 *            the descending to set
	 */
	public static void setDescending(boolean descending) {
		Filters.descending = descending;
	}

	/**
	 * @return the byJoinDate
	 */
	public static boolean isByJoinDate() {
		return byJoinDate;
	}

	/**
	 * @param byJoinDate
	 *            the byJoinDate to set
	 */
	public static void setByJoinDate(boolean byJoinDate) {
		Filters.byJoinDate = byJoinDate;
	}

	/**
	 * @return the byLastName
	 */
	public static boolean isByLastName() {
		return byLastName;
	}

	/**
	 * @param byLastName
	 *            the byLastName to set
	 */
	public static void setByLastName(boolean byLastName) {
		Filters.byLastName = byLastName;
	}

	/**
	 * @return the byCustomerId
	 */
	public static boolean isByCustomerId() {
		return byCustomerId;
	}

	/**
	 * @param byCustomerId
	 *            the byCustomerId to set
	 */
	public static void setByCustomerId(boolean byCustomerId) {
		Filters.byCustomerId = byCustomerId;
	}

	/**
	 * @return the byTitle
	 */
	public static boolean isByTitle() {
		return byTitle;
	}

	/**
	 * @param byTitle
	 *            the byTitle to set
	 */
	public static void setByTitle(boolean byTitle) {
		Filters.byTitle = byTitle;
	}

}
