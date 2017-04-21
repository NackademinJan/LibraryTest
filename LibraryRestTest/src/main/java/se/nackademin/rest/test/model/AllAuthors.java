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
public class AllAuthors {
    private ArrayList<Author> author; 

    /**
     * @return the books
     */
    public ArrayList<Author> getAuthors() {
        return author;
    }

    /**
     * @param author
     */
    public void setAuthors(ArrayList<Author> author) {
        this.author = author;
    }
    
    public void addAuthor(Author author) {
        ArrayList<Author> temp = this.author;
        temp.add(author);
        this.author = temp;
    }
    
    public void removeAuthor(Author author) {
        ArrayList<Author> temp = this.author;
        boolean removedAuthor = temp.remove(author);
        if(removedAuthor)this.author = temp;
    }
    
    public Author getAuthorfromAuthors(Integer id){
        Author wantedAuthor = null;
        for(Author n: author){
            Integer authorId = n.getId();
            
            if(Objects.equals(authorId, id)){
                
                wantedAuthor = n;
            }
            
        }
        return wantedAuthor;        
    }
}
