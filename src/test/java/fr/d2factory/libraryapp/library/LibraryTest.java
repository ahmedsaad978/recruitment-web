package fr.d2factory.libraryapp.library;

import fr.d2factory.libraryapp.book.Book;
import fr.d2factory.libraryapp.book.BookRepository;
import fr.d2factory.libraryapp.book.ISBN;

import fr.d2factory.libraryapp.member.Resident;
import fr.d2factory.libraryapp.member.Student;

import org.junit.Before;
import org.junit.Test;



import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
public class LibraryTest {
    private Library library ;
    private BookRepository bookRepository;
    
    @Before
    public void setup(){
        //TODO instantiate the library and the repository
    	bookRepository=new BookRepository();
    	List<Book> books=new ArrayList<>();
    	JSONParser parser = new JSONParser();
    	//read the json file and extract the books
    	try {
    		Object obj=parser.parse(new FileReader("src/test/resources/books.json"));
    		JSONArray jsonArray=(JSONArray) obj;
    		for(int i=0;i<4;i++) {
    			JSONObject jsonobject = (JSONObject) jsonArray.get(i);
    			String title=(String) jsonobject.get("title");
    			String author=(String) jsonobject.get("author");
    			Map isbn=(Map) jsonobject.get("isbn");
    			long  isbnCode = (long) isbn.get("isbnCode");
    			ISBN isbnCodeBook = new ISBN(isbnCode);
    			Book book=new Book(title,author,isbnCodeBook);
    			books.add(book);
    		}
    		}
    	catch(FileNotFoundException e) {e.printStackTrace();}
    	catch(IOException e) {e.printStackTrace();}
    	catch(Exception e) {e.printStackTrace();}
    	//put the books in availableBooks
    	bookRepository.addBooks(books);
    	 }
    	 
        //TODO add some test books (use BookRepository#addBooks)
        //TODO to help you a file called books.json is available in src/test/resources
    

    @Test
    public void member_can_borrow_a_book_if_book_is_available(){ 
        //isbn code for a book which is not available 
        long isbn_code =7777777777777L;
        Book find_book_available = bookRepository.findBook(isbn_code);
        assertEquals(find_book_available,null);
    }

   @Test
    public void borrowed_book_is_no_longer_available(){
        
		//fail("Implement me");
    	LocalDate borrowedAt = LocalDate.of(2019, 4, 20);
    	//Create a book which is available in the Map availableBooks
    	ISBN i=new ISBN(46578964513L);
    	Book book=new Book("Harry Potter","J.K. Rowling",i);
    	bookRepository.saveBookBorrow(book, borrowedAt);
    	Book find_book=bookRepository.findBook(46578964513L);
    	assertEquals(find_book,null);
    }

    @Test
    public void residents_are_taxed_10cents_for_each_day_they_keep_a_book(){
    	float wallet=5;
    	//Create an object Resident
    	Resident resident= new Resident(bookRepository,wallet);
    	//the resident pay for 25 days
    	resident.payBook(25);
    	assertEquals(wallet-resident.getWallet(),2.5,0.1);
    	
    }

    @Test
    public void students_pay_10_cents_the_first_30days(){
        //fail("Implement me");
        
    	float wallet=40;
    	//Create an object student in his 1st year
    	Student student_1st= new Student(1,bookRepository,wallet);
    	//Create an object Student in his 2nd year
    	Student student_2nd=new Student(2,bookRepository,wallet);
    	//the students pay for 30 days
    	student_1st.payBook(30);
    	student_2nd.payBook(30);
    	double w_1st = student_1st.getWallet();
    	double w_2nd=student_2nd.getWallet();
    	assertEquals(wallet-w_1st,1.5,0.1);   //for the student in 1st year
    	assertEquals(wallet-w_2nd,3.0,0.1);   // for the student in 2nd year
    }

    @Test
    public void students_in_1st_year_are_not_taxed_for_the_first_15days(){
        //fail("Implement me");
    	float wallet=30;
    	Student student_1st= new Student(1,bookRepository,wallet);
    	student_1st.payBook(15);
    	double w_1st = student_1st.getWallet();
    	assertEquals(wallet-w_1st,0,0.1);
    }

    @Test
    public void students_pay_15cents_for_each_day_they_keep_a_book_after_the_initial_30days(){
        //fail("Implement me");	
    	float wallet=40;
    	
    	//Student 1st year
    	Student student_1st= new Student(1,bookRepository,wallet);
    	student_1st.payBook(40);
    	double w_1st = student_1st.getWallet();
    	//the student in the 1st year is taxe 3euro for 40 days
    	assertEquals(wallet-w_1st,3,0.1);
    	
    	Student student_2nd=new Student(2,bookRepository,wallet);
    	student_2nd.payBook(40);
    	double w_2nd=student_2nd.getWallet();
    	//the student in the 2nd year is taxed 4.5euro for 40 days
    	assertEquals(wallet-w_2nd,4.5,0.1);
    	
    }

    @Test
    public void residents_pay_20cents_for_each_day_they_keep_a_book_after_the_initial_60days(){
        //fail("Implement me");
        
    	float wallet=15;
    	Resident resident= new Resident(bookRepository,wallet);
    	resident.payBook(70);
    	float w = resident.getWallet();
    	// the resident in pay 8 euro for 70days
    	assertEquals(wallet-resident.getWallet(),8,0.0001);
    	
    }

    @Test(expected = HasLateBooksException.class)
    public void members_cannot_borrow_book_if_they_have_late_books(){
        //fail("Implement me");
    	LocalDate borrowedAtResident = LocalDate.of(2019,7, 20);
    	ISBN i=new ISBN(3326456467846L);
    	Book book=new Book("Around the world in 80 days","Jules Verne",i);
    	
    	//Resident
    	Resident resident= new Resident(bookRepository,20);
    	// add book in the map BorrowedBooksByMember
    	resident.addBorrowedBooksByMember(book, borrowedAtResident);
    	//resident.boorwBook will throw exception HasLateBooksException
    	resident.borrowBook(968787565445L, resident, LocalDate.now());
    	
    	//Student
    	Student student=new Student(4,bookRepository,20);
    	// add book in the map BorrowedBooksByMember
    	//student.addBorrowedBooksByMember(book, borrowedAtResident);
    	//student.boorowBook will throw exception HasLateBooksException
    	//student.borrowBook(46578964513L, student, LocalDate.now());
    	
    	
    }
    
    @Test
    public void members_can_borrow_book_if_they_dont_have_late_books() {
    	
    	//Resident
    	Resident resident= new Resident(bookRepository,20);
    	resident.borrowBook(46578964513L, resident, LocalDate.now());
    	//we add a book borrowed by resident so the size of BorrowedBooksByMember of Resident is 1
    	assertEquals(resident.getBorrowedBooksByMember().size(),1);
    	
    	//Student
    	Student student=new Student(4,bookRepository,20);
    	student.borrowBook(968787565445L,student, LocalDate.now());
    	//we add a book borrowed by student so the size of BorrowedBooksByMember of student is 1
    	assertEquals(student.getBorrowedBooksByMember().size(),1);
    	
    	//we removed from availableBooks 2 books, so size of availableBooks is 2
    	assertEquals(bookRepository.availableBooksReturn().size(),2);
    }
    
    
    @Test
    public void member_return_book_to_the_library() {
    	
    	//Resident 
    	Resident resident= new Resident(bookRepository,20);
    	ISBN i=new ISBN(465789453149L);
    	Book book=new Book("La peau de chagrin","Balzac",i);
    	LocalDate borrowedAt= LocalDate.of(2019,9, 20);
    	resident.borrowBook(465789453149L, resident, borrowedAt);
    	resident.returnBook(book, resident);
    	//we remove the returned book from the map: BorrowedBooksByMember
    	assertEquals(resident.getBorrowedBooksByMember().size(),0);
    	// we add the returned book to the map :AvailableBooks
    	assertEquals(bookRepository.availableBooksReturn().size(),4);
    	
    	//Student
    	Student student=new Student(4,bookRepository,20);
    	ISBN j=new ISBN(968787565445L);
    	Book bookStudent=new Book("Catch 22","Joseph Heller",j);
    	LocalDate borrowedAtStudent= LocalDate.of(2019,5, 4);
    	student.borrowBook(968787565445L, student, borrowedAtStudent);
    	student.returnBook(bookStudent, student);
    	//we remove the returned book from the map: BorrowedBooksByMember
    	assertEquals(student.getBorrowedBooksByMember().size(),0);
    	// we add the returned book to the map :AvailableBooks
    	assertEquals(bookRepository.availableBooksReturn().size(),4);
    }
}
