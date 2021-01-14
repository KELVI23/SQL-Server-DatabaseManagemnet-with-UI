/**
 * Project: Books
 * File: Customer.java
 */

package a01085867.book.data;

import java.time.LocalDate;

/**
 * @author Sam Cirka, A00123456
 */
public class Customer {

	public static final int ATTRIBUTE_COUNT = 9;

	private long id;
	private String firstName;
	private String lastName;
	private String street;
	private String city;
	private String postalCode;
	private String phone;
	private String emailAddress;
	private LocalDate joinedDate;

	public static class Builder {
		// Required parameters
		private final long id;
		private final String phone;

		// Optional parameters
		private String firstName;
		private String lastName;
		private String street;
		private String city;
		private String postalCode;
		private String emailAddress;
		private LocalDate joinedDate;

		public Builder(long id, String phone) {
			this.id = id;
			this.phone = phone;
		}

		/**
		 * @param firstName
		 *            the firstName to set
		 */
		public Builder setFirstName(String firstName) {
			this.firstName = firstName;
			return this;
		}

		/**
		 * @param lastName
		 *            the lastName to set
		 */
		public Builder setLastName(String lastName) {
			this.lastName = lastName;
			return this;
		}

		/**
		 * @param street
		 *            the street to set
		 */
		public Builder setStreet(String street) {
			this.street = street;
			return this;
		}

		/**
		 * @param city
		 *            the city to set
		 */
		public Builder setCity(String city) {
			this.city = city;
			return this;
		}

		/**
		 * @param postalCode
		 *            the postalCode to set
		 */
		public Builder setPostalCode(String postalCode) {
			this.postalCode = postalCode;
			return this;
		}

		/**
		 * @param emailAddress
		 *            the emailAddress to set
		 */
		public Builder setEmailAddress(String emailAddress) {
			this.emailAddress = emailAddress;
			return this;
		}

		/**
		 * Set the joined date
		 *
		 * @param year
		 *            the year, includes the century, ex. 1967
		 * @param month
		 *            the month - must be 1-based
		 * @param day
		 *            the day of the month - 1-based
		 */
		public Builder setJoinedDate(int year, int month, int day) {
			joinedDate = LocalDate.of(year, month, day);
			return this;
		}

		/**
		 * Set the joined date
		 *
		 * @param date
		 *            the local date
		 */
		public Builder setJoinedDate(LocalDate date) {
			joinedDate = date;
			return this;
		}

		public Customer build() {
			return new Customer(this);
		}
	}

	/**
	 * Default Constructor
	 */
	private Customer(Builder builder) {
		id = builder.id;
		firstName = builder.firstName;
		lastName = builder.lastName;
		street = builder.street;
		city = builder.city;
		postalCode = builder.postalCode;
		phone = builder.phone;
		emailAddress = builder.emailAddress;
		joinedDate = builder.joinedDate;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName
	 *            the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName
	 *            the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the street
	 */
	public String getStreet() {
		return street;
	}

	/**
	 * @param street
	 *            the street to set
	 */
	public void setStreet(String street) {
		this.street = street;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city
	 *            the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the postalCode
	 */
	public String getPostalCode() {
		return postalCode;
	}

	/**
	 * @param postalCode
	 *            the postalCode to set
	 */
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone
	 *            the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * @param emailAddress
	 *            the emailAddress to set
	 */
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	/**
	 * @return the emailAddress
	 */
	public String getEmailAddress() {
		return emailAddress;
	}

	/**
	 * Set the joined date
	 *
	 * @param date
	 *            the local date
	 */
	public void setJoinedDate(LocalDate joinedDate) {
		this.joinedDate = joinedDate;
	}

	/**
	 * Set the joined date
	 *
	 * @param year
	 *            the year, includes the century, ex. 1967
	 * @param month
	 *            the month - must be 1-based
	 * @param day
	 *            the day of the month - 1-based
	 */
	public void setJoinedDate(int year, int month, int day) {
		joinedDate = LocalDate.of(year, month, day);
	}

	public LocalDate getJoinedDate() {
		return joinedDate;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Customer [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", street=" + street + ", city=" + city
				+ ", postalCode=" + postalCode + ", phone=" + phone + ", emailAddress=" + emailAddress + ", joinedDate=" + joinedDate + "]";
	}

}
