package fr.d2factory.libraryapp.member;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import fr.d2factory.libraryapp.book.Book;
import fr.d2factory.libraryapp.book.BookRepository;
import fr.d2factory.libraryapp.book.ISBN;
import fr.d2factory.libraryapp.library.HasLateBooksException;
import fr.d2factory.libraryapp.library.Library;

public class Student extends Member implements Library{
	
	private int year;
	private BookRepository bookRepository;
	
	public Student(int year, BookRepository bookRepository,float wallet) {
		super(wallet);
		this.year = year;
		this.bookRepository = bookRepository;
	}
	@Override
	public void payBook(int numberOfDays) {
		// TODO Auto-generated method stub
		double pay;
		float w=this.getWallet();
		
		
		//if the student keep the book less then 30 days
		if (numberOfDays<30) {
			// check if the student is in his 1st year  
			if (year==1) {
				numberOfDays=Math.max(numberOfDays-15, 0); 
			}
			pay=numberOfDays*0.10;
		}
		//if the student keep the book more then 30 days
		else {
			// check if the student is in his 1st year 
			if (year==1) {
				pay=((numberOfDays-30)*0.15)+15*0.10;
			}
			else {
				pay=((numberOfDays-30)*0.15)+30*0.10;
			}
			
		}
		// update the wallet of the student
		w=(float) (w-pay);
		this.setWallet(w);
	}
	@Override
	public Book borrowBook(long isbnCode, Member member, LocalDate borrowedAt) throws HasLateBooksException {
		// TODO Auto-generated method stub
		//if the book is unavailable in the bookRepository then the student can't borrow the book
		Book book = bookRepository.findBook(isbnCode);
		
		if(book==null) {
			
			return null;
		}
		else {
			
			Collection<LocalDate> key_date = member.getBorrowedBooksByMember().values();			
			Iterator<LocalDate> it=key_date.iterator(); // create an iterator that iterate if the resident has any books borrowed
			boolean flag = true;
			while(it.hasNext() && flag==true) {
				//Period intervalPeriod=Period.between(it.next(), borrowedAt);
				//int days = intervalPeriod.getDays();
				LocalDate borrowed_date = it.next();
				//Calculate the number of days between borrowedAt and borrowedDate to determine if the resident is late or not.
				int days = (borrowedAt.getDayOfYear()-borrowed_date.getDayOfYear()+(borrowedAt.getYear()-borrowed_date.getYear())*365);
				
				
				if(days>30) {
					flag=false;
					throw new HasLateBooksException("Has Late books !!"); 
				}
				
			}
			//If the resident isn't late
			if(flag==true) {
				bookRepository.saveBookBorrow(book, borrowedAt);
				member.getBorrowedBooksByMember().put(book,borrowedAt);
			}
		}
		return null;
		
	}
	@Override
	public void returnBook(Book book, Member member) {
		//get the date of borrow of the book
		LocalDate date_borrow = bookRepository.findBorrowedBookDate(book);
		//Calculate the number of days
		int numberOfDays = (LocalDate.now().getDayOfYear()-date_borrow.getDayOfYear()+(LocalDate.now().getYear()-date_borrow.getYear())*365);
		payBook(numberOfDays);
		//Remove this book of borrowedBooksByMember
		member.removeBorrowedBook(book);
		
		List<Book> books=new ArrayList<>();
		books.add(book);
		// add this book to availableBooks
		bookRepository.addBooks(books);
	}

}
