package a01085867.book.data;

/**
 * Created by scirka on 2017-10-04.
 */
public class Purchase {
	public static final int ATTRIBUTE_COUNT = 4;

	private long id;
	private long customerId;
	private long bookId;
	private float price;

	public static class Builder {
		// Required parameters
		private long id;
		private long customerId;
		private long bookId;
		private float price;

		public Builder(long id, long customerId, long bookId, float price) {
			this.id = id;
			this.customerId = customerId;
			this.bookId = bookId;
			this.price = price;
		}

		public Purchase build() {
			return new Purchase(this);
		}
	}

	/**
	 * @param builder
	 */
	public Purchase(Builder builder) {
		this.id = builder.id;
		this.customerId = builder.customerId;
		this.bookId = builder.bookId;
		this.price = builder.price;
	}

	/**
	 * @return the attributeCount
	 */
	public static int getAttributeCount() {
		return ATTRIBUTE_COUNT;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @return the customerId
	 */
	public long getCustomerId() {
		return customerId;
	}

	/**
	 * @return the bookid
	 */
	public long getBookId() {
		return bookId;
	}

	/**
	 * @return the price
	 */
	public float getPrice() {
		return price;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Purchase [customerId=" + customerId + ", bookId=" + bookId + ", price=" + price + "]";
	}

}
