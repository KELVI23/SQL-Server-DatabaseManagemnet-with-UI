
package a01085867.book;

import java.io.IOException;

import a00123456.book.BookStore;

public class BookRunner {

	public static void main(String[] args) throws IOException {

		new BookRunner().run();

	}

	private void run() throws IOException {

		System.err.println("RUN three reports -c -b -p........");
		String[] programAgrs_1 = {"-c", "-b", "-p"};
		BookStore.main(programAgrs_1);
		System.err.println("PAUSE 1-------------");
		System.in.read();
		System.in.read();
		
		System.err.println("RUN Customer report -c -J -d........");
		String[] programAgrs_2 = {"-c", "-J", "-d"};
		BookStore.main(programAgrs_2);
		System.err.println("PAUSE 2-------------");
		System.in.read();
		System.in.read();
			
		System.err.println("RUN Book report -b -A........");
		String[] programAgrs_3 = {"-b", "-A"};
		BookStore.main(programAgrs_3);
		System.err.println("PAUSE 3-------------");
		System.in.read();
		System.in.read();
		
		System.err.println("RUN Purchase report -p -T -t........");
		String[] programAgrs_4 = {"-p", "-T", "-t"};
		BookStore.main(programAgrs_4);
		System.err.println("PAUSE 4-------------");
		System.in.read();
		System.in.read();	
		
		System.err.println("RUN Purchase report -p -C=XXXX -t -T -d........");
		String[] programAgrs_5 = {"-p", "-C=8000", "-t", "-T", "-d"};
		BookStore.main(programAgrs_5);
			
	}

}
