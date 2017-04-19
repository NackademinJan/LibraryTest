/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nackademin.rest.test.model;

/**
 *
 * @author testautomatisering
 */
public class Book {
    private Object author;
    private Integer id;
    private String description;
    private Integer nbOfPage;
    private String title;
    private String isbn;

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the nbOfPage
     */
    public Integer getNbOfPage() {
        return nbOfPage;
    }

    /**
     * @param nbOfPage the nbOfPage to set
     */
    public void setNbOfPage(Integer nbOfPage) {
        this.nbOfPage = nbOfPage;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the isbn
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * @param isbn the isbn to set
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
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
    public void setAuthor(Object author) {
        this.author = author;
    }

}
