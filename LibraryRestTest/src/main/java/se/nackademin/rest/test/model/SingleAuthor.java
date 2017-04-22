/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nackademin.rest.test.model;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Note, this class does not work as intended currently due to difficulties in parsing json vauge objects to two or more object types
 * @author testautomatisering
 */
public class SingleAuthor {
    private Object author;
    
    public SingleAuthor(Author author){
        this.author = author;
    }
    
    public SingleAuthor(ArrayList<Author> author){
        this.author = author;
    }

    /**
     * @return the author
     */
    public Object getAuthor() {
        return author;
    }

    /**
     * @param author the author to set
     */
    public void setAuthor(Author author) {
        this.author = author;
    }
       
    public void setAuthor(ArrayList<Author> author) {
        this.author = author;
    }
    
    
    public void addAuthor(Author author) {
        ArrayList<Author> temp = (ArrayList<Author>) this.author;
        temp.add(author);
        this.author = temp;
    }
    
    public void removeAuthor(Author author) {
        ArrayList<Author> temp = (ArrayList<Author>) this.author;
        boolean removedAuthor = temp.remove(author);
        if(removedAuthor)this.author = temp;
    }
    
    public Author getAuthorfromAuthors(Integer id){
        Author wantedAuthor = null;
        for(Author n: (ArrayList<Author>)author){
            Integer authorId = n.getId();
            
            if(Objects.equals(authorId, id)){
                
                wantedAuthor = n;
            }
            
        }
        return wantedAuthor;        
    }
}
    
    
    
