package fr.d2factory.libraryapp.member;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import fr.d2factory.libraryapp.book.Book;
import fr.d2factory.libraryapp.book.ISBN;
import fr.d2factory.libraryapp.library.Library;

/**
 * A member is a person who can borrow and return books to a {@link Library}
 * A member can be either a student or a resident
 */
public abstract class Member {
    /**
     * An initial sum of money the member has
     */
	
    private float wallet;
    
    //each member has a map that contains all the books that now have
    private Map<Book,LocalDate> borrowedBooksByMember = new HashMap<>();
    
    public Member(float wallet) {
		//super();
		this.wallet = wallet;
	}

   
    
    /**
     * The member should pay their books when they are returned to the library
     *
     * @param numberOfDays the number of days they kept the book
     */
    public abstract void payBook(int numberOfDays);

    public float getWallet() {
        return wallet;
    }

    public void setWallet(float wallet) {
        this.wallet = wallet;
    }

	public Map<Book, LocalDate> getBorrowedBooksByMember() {
		return borrowedBooksByMember;
	}

	public void addBorrowedBooksByMember(Book book, LocalDate borrowedAt) {
		this.borrowedBooksByMember.put(book, borrowedAt);
	}
	
	
	public LocalDate findDate(Book book) {
		Iterator<Entry<Book,LocalDate>> it=borrowedBooksByMember.entrySet().iterator();
    	while(it.hasNext()) {
    		 Map.Entry<Book,LocalDate> mapElement = (Map.Entry<Book,LocalDate>)it.next();
    		 long isbn = mapElement.getKey().getISBN().getIsbnCode();
    		 if(isbn==book.getISBN().getIsbnCode()) {
    			 return  mapElement.getValue();
    		 } 
    	}
    	return null;	
	}
	
	public void removeBorrowedBook(Book book) {
		
		Iterator<Entry<Book,LocalDate>> it=borrowedBooksByMember.entrySet().iterator();  //Create iterator it to browse the borrowedBooksByMember Map.
    	while(it.hasNext()) {
    		 Map.Entry<Book,LocalDate> mapElement = (Map.Entry<Book,LocalDate>)it.next();
    		 long isbn = mapElement.getKey().getISBN().getIsbnCode(); // get the ISBN of the key of MapElement
    		 
    		 if(isbn==book.getISBN().getIsbnCode()) {
    			 LocalDate  bookToRemove=mapElement.getValue();// Create local variable BookToRemove which contains the value of the book to remove
    			 Book  isbnToRemove=mapElement.getKey();// Create local variable isbnToRemove which contains the value of the ISBN to remove
    			 it.remove(); //Remove of borrowedBooksByMember
    		 } 
	}
	}  
}
