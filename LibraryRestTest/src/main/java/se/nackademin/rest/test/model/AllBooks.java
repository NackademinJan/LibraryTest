/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nackademin.rest.test.model;

import java.util.ArrayList;
import java.util.Objects;

/**
 *
 * @author testautomatisering
 */
public class AllBooks {
    private ArrayList<Book> book; 

    /**
     * @return the books
     */
    public ArrayList<Book> getBooks() {
        return book;
    }

    /**
     * @param book the books to set
     */
    public void setBooks(ArrayList<Book> book) {
        this.book = book;
    }
    
    public Book getBookfromBooks(Integer id){
        Book wantedBook = null;
        for(Book n: book){
            Integer bookId = n.getId();
            
            if(Objects.equals(bookId, id)){
                
                wantedBook = n;
            }
            
        }
        return wantedBook;        
    }
    
}
