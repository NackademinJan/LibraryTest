/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nackademin.rest.test;

import static com.jayway.restassured.RestAssured.given;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import se.nackademin.rest.test.model.Book;
import se.nackademin.rest.test.model.SingleBook;

/**
 *
 * @author testautomatisering
 */
public class BeforeAndAfterOperations {

    
    public static Response makeDummyBookAndDummyAuthor(){
        Response postBookResponse = new BookOperations().createBookWithInput(GlobVar.dummyBookDescription, GlobVar.dummyBookIsbn, GlobVar.dummyBookNbOfPage, GlobVar.dummyBookTitle);
        if(!( 201 == postBookResponse.getStatusCode() )) return postBookResponse;
        Response lastBookResponse = new BookOperations().getAllBooks();
        GlobVar.dummyBookId = lastBookResponse.jsonPath().getInt("books.book[-1].id");
        
        Response postAuthorResponse = new AuthorOperations().createAuthor(GlobVar.dummyAuthorName);
        if(!( 201 == postAuthorResponse.getStatusCode() )) return postAuthorResponse;
        Response authorResponse = new AuthorOperations().getAllAuthors();
        GlobVar.dummyAuthorId = authorResponse.jsonPath().getInt("authors.author[-1].id");
        return postAuthorResponse;
        
    } 
    public static Response addDummyAuthorToDummyBook(){
        Response authorOfBookResponse = new BookOperations().addAuthorToBook(GlobVar.dummyAuthorName, GlobVar.dummyAuthorId, GlobVar.dummyBookId);  
        return authorOfBookResponse; 
    }
   
    public static Response removeDummyBookAndDummyAuthor(){
        Response deleteBookResponse = new BookOperations().deleteBook(GlobVar.dummyBookId);
        if(!( 204 == deleteBookResponse.getStatusCode() )) return deleteBookResponse;
        
        Response deleteAuthorResponse = new AuthorOperations().deleteAuthor(GlobVar.dummyAuthorId);
        return deleteAuthorResponse;
    }
    
    
    
}
