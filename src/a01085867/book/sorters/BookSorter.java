package a01085867.book.sorters;

import java.util.Comparator;

import a01085867.book.data.Book;

public class BookSorter {

	public static class CompareByAuthor implements Comparator<Book> {
		@Override
		public int compare(Book book1, Book book2) {
			return book1.getAuthors().compareToIgnoreCase(book2.getAuthors());
		}
	}

	public static class CompareByAuthorDescending implements Comparator<Book> {
		@Override
		public int compare(Book book1, Book book2) {
			return -book1.getAuthors().compareToIgnoreCase(book2.getAuthors());
		}
	}

	public static class CompareBookbyID implements Comparator<Book> {
		@Override
		public int compare(Book book1, Book book2) {
			return (int) (book1.getId() - book2.getId());

		}
	}

}
