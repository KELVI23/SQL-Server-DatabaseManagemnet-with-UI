package a01085867.book.sorters;

import java.util.Comparator;

import a01085867.book.data.Customer;

public class CustomerSorter {

	public static class CompareByJoinedDate implements Comparator<Customer> {
		@Override
		public int compare(Customer customer1, Customer customer2) {
			return customer1.getJoinedDate().compareTo(customer2.getJoinedDate());
		}
	}

	public static class CompareByJoinedDateDescending implements Comparator<Customer> {
		@Override
		public int compare(Customer customer1, Customer customer2) {
			return customer2.getJoinedDate().compareTo(customer1.getJoinedDate());
		}
	}

	public static class CompareByCustomerID implements Comparator<Customer> {
		@Override
		public int compare(Customer customer1, Customer customer2) {
			return (int) (customer1.getId() - customer2.getId());
		}
	}

}
