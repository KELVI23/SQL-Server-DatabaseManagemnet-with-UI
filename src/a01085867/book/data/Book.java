/**
 * Project: Books
 * File: Book.java
 */

package a01085867.book.data;

/**
 * @author Sam Cirka, A00123456
 */
public class Book {

	public static final int ATTRIBUTE_COUNT = 8;

	private long id;
	private String isbn;
	private String authors;
	private int year;
	private String title;
	private float rating;
	private int ratingsCount;
	private String imageUrl;

	public static class Builder {
		// Required parameters
		private long id;

		// Optional parameters
		private String isbn;
		private String authors;
		private int year;
		private String title;
		private float rating;
		private int ratingsCount;
		private String imageUrl;

		public Builder(long id) {
			this.id = id;
		}

		/**
		 * @param isbn
		 *            the isbn to set
		 */
		public Builder setIsbn(String isbn) {
			this.isbn = isbn;

			return this;
		}

		/**
		 * @param authors
		 *            the authors to set
		 */
		public Builder setAuthors(String authors) {
			this.authors = authors;

			return this;
		}

		/**
		 * @param year
		 *            the year to set
		 */
		public Builder setYear(int year) {
			this.year = year;

			return this;
		}

		/**
		 * @param title
		 *            the title to set
		 */
		public Builder setTitle(String title) {
			this.title = title;

			return this;
		}

		/**
		 * @param rating
		 *            the rating to set
		 */
		public Builder setRating(float rating) {
			this.rating = rating;

			return this;
		}

		/**
		 * @param ratingsCount
		 *            the ratingsCount to set
		 */
		public Builder setRatingsCount(int ratingsCount) {
			this.ratingsCount = ratingsCount;

			return this;
		}

		/**
		 * @param imageUrl
		 *            the imageUrl to set
		 */
		public Builder setImageUrl(String imageUrl) {
			this.imageUrl = imageUrl;

			return this;
		}

		public Book build() {
			return new Book(this);
		}

	}

	/**
	 * Default constructor
	 */
	private Book(Builder builder) {
		this.id = builder.id;
		this.isbn = builder.isbn;
		this.authors = builder.authors;
		this.year = builder.year;
		this.title = builder.title;
		this.rating = builder.rating;
		this.ratingsCount = builder.ratingsCount;
		this.imageUrl = builder.imageUrl;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @return the isbn
	 */
	public String getIsbn() {
		return isbn;
	}

	/**
	 * @param isbn
	 *            the isbn to set
	 */
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	/**
	 * @return the authors
	 */
	public String getAuthors() {
		return authors;
	}

	/**
	 * @param authors
	 *            the authors to set
	 */
	public void setAuthors(String authors) {
		this.authors = authors;
	}

	/**
	 * @return the year
	 */
	public int getYear() {
		return year;
	}

	/**
	 * @param year
	 *            the year to set
	 */
	public void setYear(int year) {
		this.year = year;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the rating
	 */
	public float getRating() {
		return rating;
	}

	/**
	 * @param rating
	 *            the rating to set
	 */
	public void setRating(float rating) {
		this.rating = rating;
	}

	/**
	 * @return the ratingsCount
	 */
	public int getRatingsCount() {
		return ratingsCount;
	}

	/**
	 * @param ratingsCount
	 *            the ratingsCount to set
	 */
	public void setRatingsCount(int ratingsCount) {
		this.ratingsCount = ratingsCount;
	}

	/**
	 * @return the imageUrl
	 */
	public String getImageUrl() {
		return imageUrl;
	}

	/**
	 * @param imageUrl
	 *            the imageUrl to set
	 */
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	/**
	 * Get the attribute count used for input validation.
	 *
	 * @return the attribute count
	 */
	public static int getAttributeCount() {
		return ATTRIBUTE_COUNT;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Book{" + "id='" + id + '\'' + ", isbn='" + isbn + '\'' + ", authors='" + authors + '\'' + ", year=" + year + ", title='" + title
				+ '\'' + ", rating=" + rating + '}';
	}
}
