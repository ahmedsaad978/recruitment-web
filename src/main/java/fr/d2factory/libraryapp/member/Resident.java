package fr.d2factory.libraryapp.member;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import fr.d2factory.libraryapp.book.Book;
import fr.d2factory.libraryapp.book.BookRepository;
import fr.d2factory.libraryapp.book.ISBN;
import fr.d2factory.libraryapp.library.HasLateBooksException;
import fr.d2factory.libraryapp.library.Library;

public class Resident extends Member implements Library{
	
	private BookRepository bookRepository;
	
	public Resident(BookRepository bookRepository,float wallet){
		super(wallet);
		this.bookRepository = bookRepository;
	}

	@Override
	public void payBook(int numberOfDays) {
		// TODO Auto-generated method stub
		double pay;
		float w=this.getWallet();
		//if the resident keep the book less then 60 days
		if (numberOfDays < 60) {
			pay=numberOfDays*0.10;
		}
		//if the resident keep the book more than 60 days
		else {										
			pay=((numberOfDays-60)*0.20)+60*0.10;
		}
		// update the wallet of the resident 
		w=(float) (w-pay);
		this.setWallet(w);
	}

	@Override
	public Book borrowBook(long isbnCode, Member member, LocalDate borrowedAt) throws HasLateBooksException {
		// TODO Auto-generated method stub
		//if the book is unavailable in the bookRepository then the resident can't borrow the book
		Book book = bookRepository.findBook(isbnCode);
		if(book==null) {
			return null;
		}
		else {
			
			Collection<LocalDate> key_date = member.getBorrowedBooksByMember().values();			
			Iterator<LocalDate> it=key_date.iterator(); // create an iterator that iterate if the resident has any books borrowed
			boolean flag = true;
			while(it.hasNext() && flag==true) {
				LocalDate borrowedDate = it.next();
				//Calculate the number of days between borrowedAt and borrowedDate to determine if the resident is late or not.
				int days = (borrowedAt.getDayOfYear()-borrowedDate.getDayOfYear()+(borrowedAt.getYear()-borrowedDate.getYear())*365);
				if(days>60) {
					flag =false;
					throw new HasLateBooksException("Has Late books !!");
				}
				
			}
			//If the resident isn't late
			if(flag==true) {
				bookRepository.saveBookBorrow(book, borrowedAt); 
				member.getBorrowedBooksByMember().put(book,borrowedAt);//save the book and the date in the Map borrowedBooksByMember
			}
		}
		
		return null;
	}

	@Override
	public void returnBook(Book book, Member member) {
		// TODO Auto-generated method stub
		//get the date of borrow of the book
		LocalDate dateBorrow = bookRepository.findBorrowedBookDate(book);
		//Calculate the number of days
		int numberOfDays = (LocalDate.now().getDayOfYear()-dateBorrow.getDayOfYear()+(LocalDate.now().getYear()-dateBorrow.getYear())*365);
		payBook(numberOfDays);
		//Remove this book of borrowedBooksByMember
		member.removeBorrowedBook(book); 
		List<Book> books=new ArrayList<>();
		books.add(book);
		// add this book to availableBooks
		bookRepository.addBooks(books);
		
	}

}
