package fr.d2factory.libraryapp.book;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;




/**
 * The book repository emulates a database via 2 HashMaps
 */
public class BookRepository {
    private Map<ISBN, Book> availableBooks = new HashMap<>();
    private Map<Book, LocalDate> borrowedBooks = new HashMap<>();
 
    public void addBooks(List<Book> books){
        //TODO implement the missing feature
    	for(int i=0;i<books.size();i++) {
    		Book book = books.get((i));
    		try {
    			availableBooks.put(book.isbn, book);
    		}
    		catch(NullPointerException npe) {npe.printStackTrace();}
    		
    	}
    }

    public Book findBook(long isbnCode)  {
        //TODO implement the missing feature
    	//browse the map and return the book that has isbnCode
    	Iterator<Entry<ISBN, Book>> it=availableBooks.entrySet().iterator();  //Create iterator it
    	while(it.hasNext()) {
    		 Map.Entry<ISBN, Book> mapElement = (Map.Entry<ISBN, Book>)it.next();
    		 long isbn = mapElement.getKey().isbnCode;  //get the key of the mapElement
    		 if(isbn==isbnCode) {
    			 return (Book) mapElement.getValue();  // return the value of the key isbnCode
    		 } 
    	}
    	return null;
    	 }

    public void saveBookBorrow(Book book, LocalDate borrowedAt){
        //TODO implement the missing feature
    	borrowedBooks.put(book,borrowedAt);     //save the borrowed book in the map borrowedBooks
    	Iterator<Entry<ISBN, Book>> it=availableBooks.entrySet().iterator(); //Create iterator it 
    	while(it.hasNext()) {
    		 Map.Entry<ISBN, Book> mapElement = (Map.Entry<ISBN, Book>)it.next();
    		 long isbn = mapElement.getKey().isbnCode;  //get the isbnCode of the key of the mapElement
    		 
    		 if(isbn==book.isbn.isbnCode) {
    			 //availableBooks.remove(isbnToRemove,bookToRemove);
    			 it.remove();
    		 } 
    	}  	
    }

    public LocalDate findBorrowedBookDate(Book book) {
        //TODO implement the missing feature
    	Iterator<Entry<Book, LocalDate>> it=borrowedBooks.entrySet().iterator(); //Create iterator it
    	while(it.hasNext()) {
    		 Map.Entry<Book, LocalDate> mapElement = (Map.Entry<Book, LocalDate>)it.next();
    		 long isbn = mapElement.getKey().isbn.isbnCode;
    		 if(isbn==book.isbn.isbnCode) {
    			 return (LocalDate) mapElement.getValue();  //// return the value of the key :the date when the book is borrowed
    		 } 
    	}
    	return null;
    }
    
    public Map<ISBN, Book> availableBooksReturn() {
    	return this.availableBooks;
    }
}
