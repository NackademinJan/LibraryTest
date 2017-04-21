/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nackademin.rest.test;

import org.junit.Test;
import static com.jayway.restassured.RestAssured.*;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import java.util.UUID;
import static org.junit.Assert.*;
import static com.jayway.restassured.path.json.JsonPath.*;
import se.nackademin.rest.test.model.Book;
import static com.jayway.restassured.RestAssured.*;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import se.nackademin.rest.test.model.AllBooks;
import se.nackademin.rest.test.model.SingleBook;
/**
 *
 * @author testautomatisering
 */
public class DeleteTest {
    
        @BeforeClass
    public static void MaketheMockBookAndMockAuthor(){
        Response makeMocksResponse = BeforeAndAfterOperations.makeMockBookAndMockAuthor();
        assertEquals("The status code should be: 201",  201, makeMocksResponse.statusCode());
        assertEquals("response body should be blank",  "", makeMocksResponse.body().print());
        
        Response addMockAuthorToMockBook = BeforeAndAfterOperations.addMockAuthorToMockBook();
        assertEquals("The status code should be: 200",  200, addMockAuthorToMockBook.statusCode());
        assertEquals("response body should be blank",  "", addMockAuthorToMockBook.body().print());
    }
    
    @AfterClass
    public static void RemovetheMockBookAndMockAuthor(){
        Response removeResponse = BeforeAndAfterOperations.removeTestBookAndTestAuthor();
        assertEquals("The status code should be: 204",  204, removeResponse.statusCode());  
        assertEquals("response body should be blank",  "", removeResponse.body().print());
    }
    
    
    @Test
    public void testForbiddenDeleteBooks(){
        String resourceName = "books";
        Response response = given().accept(ContentType.JSON).delete(GlobVar.BASE_URL+resourceName);
        assertEquals("The status code should be: 405, method not allowed",  405, response.statusCode());  
        assertEquals("response body should be blank",  "", response.body().print());
    }
    
    
    
    @Test 
    public void testDeleteBookWithAuthor(){
        BookOperations bookOperations = new BookOperations();
        AuthorOperations authorOperations = new AuthorOperations();
        
        Response authorResponse = authorOperations.getAuthor(GlobVar.mockAuthorId); 
        String authorName = authorResponse.body().jsonPath().getString("author.name");
        Integer authorId = authorResponse.body().jsonPath().getInt("author.id");
        
        Response postResponse = bookOperations.createBookWithAuthor(GlobVar.secondMockBookDescription, GlobVar.secondMockBookIsbn, GlobVar.secondMockBookNbOfPage, GlobVar.secondMockBookTitle, authorName, authorId);
        assertEquals("status code should be 201",  201, postResponse.statusCode());
        assertEquals("response body should be blank", "", postResponse.body().prettyPrint());
        
        String expectedTitle = from(bookOperations.getLatestJsonString()).getString("book.title");
        String expectedDescription = from(bookOperations.getLatestJsonString()).getString("book.description");
        String expectedIsbn = from(bookOperations.getLatestJsonString()).getString("book.isbn");
        Integer expectedNbOfPage = from(bookOperations.getLatestJsonString()).getInt("book.nbOfPage");
        String expectedAuthorName = from(bookOperations.getLatestJsonString()).getString("book.author.name");
        Integer expectedAuthorId = from(bookOperations.getLatestJsonString()).getInt("book.author.id");
        
        Response verifyResponse = new BookOperations().getAllBooks();
        String verifyTitle = verifyResponse.jsonPath().getString("books.book[-1].title");
        String verifyDescription = verifyResponse.jsonPath().getString("books.book[-1].description");
        String verifyIsbn = verifyResponse.jsonPath().getString("books.book[-1].isbn");
        Integer verifyNbOfPage = verifyResponse.jsonPath().getInt("books.book[-1].nbOfPage");
        String verifyAuthorName = verifyResponse.jsonPath().getString("books.book[-1].author.name");
        Integer verifyAuthorId = verifyResponse.jsonPath().getInt("books.book[-1].author.id");
        
        assertEquals(expectedTitle, verifyTitle);
        assertEquals(expectedDescription, verifyDescription);
        assertEquals(expectedIsbn, verifyIsbn);
        assertEquals(expectedNbOfPage, verifyNbOfPage); 
        assertEquals(expectedAuthorName, verifyAuthorName); 
        assertEquals(expectedAuthorId, verifyAuthorId); 
        
        //everything before this line verifies that we have created a new book with attatched author that we can then try to delete.
        
        //the part below tries to delete the book if one was created
        if(postResponse.statusCode() == 201){
            Integer verifyBookId = verifyResponse.jsonPath().getInt("books.book[-1].id");
            Response deleteResponse = new BookOperations().deleteBook(verifyBookId);
            assertEquals("The status code should be: 204",  204, deleteResponse.statusCode());
            assertEquals("response body should be blank",  "", deleteResponse.body().print());
        
            //and this part veirfies that the book is gone
            Response postDeleteBookResponse = new BookOperations().getBookById(verifyBookId);
            assertEquals("The status code should be: 404",  404, postDeleteBookResponse.statusCode());
            assertEquals("response body should be blank",  "", postDeleteBookResponse.body().print());
        
            //and this part verifies that the author was not deleted from the system along with the book!
            Response postDeleteBookButNotAuthorResponse = authorOperations.getAuthor(GlobVar.mockAuthorId); 
            assertEquals("The status code should be: 200",  200, postDeleteBookButNotAuthorResponse.statusCode());
            assertEquals("response body should be: " + authorName,  authorName, postDeleteBookButNotAuthorResponse.body().jsonPath().getString("author.name"));
            Integer postDeleteBookButNotAuthorId = postDeleteBookButNotAuthorResponse.body().jsonPath().getInt("author.id");
            assertEquals("response body should be: " + authorId,  authorId, postDeleteBookButNotAuthorId);
        }
    }
    
    @Test 
    public void testDeleteBookWithNoAuthor(){
        BookOperations bookOperations = new BookOperations();
        
        Response postResponse = bookOperations.createBookWithInput(GlobVar.secondMockBookDescription, GlobVar.secondMockBookIsbn, GlobVar.secondMockBookNbOfPage, GlobVar.secondMockBookTitle);
        assertEquals("status code should be 201",  201, postResponse.statusCode());
        assertEquals("response body should be blank", "", postResponse.body().prettyPrint());
        
        String expectedTitle = from(bookOperations.getLatestJsonString()).getString("book.title");
        String expectedDescription = from(bookOperations.getLatestJsonString()).getString("book.description");
        String expectedIsbn = from(bookOperations.getLatestJsonString()).getString("book.isbn");
        Integer expectedNbOfPage = from(bookOperations.getLatestJsonString()).getInt("book.nbOfPage");
        
        Response verifyResponse = new BookOperations().getAllBooks();
        String verifyTitle = verifyResponse.jsonPath().getString("books.book[-1].title");
        String verifyDescription = verifyResponse.jsonPath().getString("books.book[-1].description");
        String verifyIsbn = verifyResponse.jsonPath().getString("books.book[-1].isbn");
        Integer verifyNbOfPage = verifyResponse.jsonPath().getInt("books.book[-1].nbOfPage");
        
        assertEquals(expectedTitle, verifyTitle);
        assertEquals(expectedDescription, verifyDescription);
        assertEquals(expectedIsbn, verifyIsbn);
        assertEquals(expectedNbOfPage, verifyNbOfPage); 
        
        //everything before this line verifies that we have created a new book that we can then try to delete
        
        //the part below tries to delete the book if one was created
        if(postResponse.statusCode() == 201){
            Integer verifyBookId = verifyResponse.jsonPath().getInt("books.book[-1].id");
            Response deleteResponse = new BookOperations().deleteBook(verifyBookId);
            assertEquals("The status code should be: 204",  204, deleteResponse.statusCode());
            assertEquals("response body should be blank",  "", deleteResponse.body().print());
        
            //and this part veirfies that the book is gone
            Response postDeleteBookResponse = new BookOperations().getBookById(verifyBookId);
            assertEquals("The status code should be: 404",  404, postDeleteBookResponse.statusCode());
            assertEquals("response body should be blank",  "", postDeleteBookResponse.body().print());
        }
    }
    
    @Test 
    public void testInvalidDeleteBookWithNoExistingBook(){
        
        //this part veirfies that the book we are about to delete does not exist in the system
        Response bookResponse = new BookOperations().getAllBooks();
        Integer nonExistingBookId = bookResponse.jsonPath().getInt("books.book[-1].id") + 100;
        Response verifyNoBookResponse = new BookOperations().getBookById(nonExistingBookId);
        assertEquals("The status code should be: 404",  404, verifyNoBookResponse.statusCode());
        assertEquals("response body should be blank",  "", verifyNoBookResponse.body().print());
        
        //the part below tries to delete a book if that book does not exist in the system
        if(verifyNoBookResponse.statusCode() == 404){
        Response deleteResponse = new BookOperations().deleteBook(nonExistingBookId);
        assertEquals("The status code should be: 404",  404, deleteResponse.statusCode());
        assertEquals("response body should be blank",  "", deleteResponse.body().print());
        }
    }
    
    
    @Test
    public void testForbiddenDeleteBooksByAuthorId(){
        String resourceName = "books/byauthor/"+GlobVar.mockAuthorId;
        Response response = given().accept(ContentType.JSON).delete(GlobVar.BASE_URL+resourceName);
        assertEquals("The status code should be: 405, method not allowed",  405, response.statusCode());
        assertEquals("response body should be blank",  "", response.body().print());
    }
    
    @Test
    public void testForbiddenDeleteAuthorByBookId(){
        String resourceName = "books/"+GlobVar.mockAuthorId +"/authors";
        Response response = given().accept(ContentType.JSON).delete(GlobVar.BASE_URL+resourceName);
        assertEquals("The status code should be: 405, method not allowed",  405, response.statusCode());
        assertEquals("response body should be blank",  "", response.body().print());
    }
    
    
    @Test
    public void testForbiddenDeleteAuthors(){
        String resourceName = "authors";
        Response response = given().accept(ContentType.JSON).delete(GlobVar.BASE_URL+resourceName);
        assertEquals("The status code should be: 405, method not allowed",  405, response.statusCode());  
        assertEquals("response body should be blank",  "", response.body().print());
    }
    
    
    
    @Test
    public void testDeleteAuthorById(){
        AuthorOperations authorOperations = new AuthorOperations();
        
        Response postResponse = authorOperations.createAuthor("Another MockAuthor");
        assertEquals("status code should be 201",  201, postResponse.statusCode());
        assertEquals("response body should be blank", "", postResponse.body().print());
        
        String expectedName = from(authorOperations.getLatestJsonString()).getString("author.name");
        
        Response getResponse = authorOperations.getAllAuthors();
        String verifyAuthorName = getResponse.body().jsonPath().getString("authors.author[-1].name");
        assertEquals(expectedName, verifyAuthorName);
        
        
        //this part above verifies that we have created an author and, if so, tries to delete it
        if(postResponse.statusCode() == 201){
        Integer verifyAuthorId = getResponse.body().jsonPath().getInt("authors.author[-1].id");
        Response delResponse = authorOperations.deleteAuthor(verifyAuthorId);
        assertEquals("status code should be 204",  204, delResponse.statusCode());
        assertEquals("response body should be blank", "", delResponse.body().print());
        
        //the part below verifies that the author is gone
        Response postDeleteAuthorResponse = new AuthorOperations().getAuthor(verifyAuthorId);
        assertEquals("The status code should be: 404",  404, postDeleteAuthorResponse.statusCode());
        assertEquals("response body should be blank",  "", postDeleteAuthorResponse.body().print());
        }
    }
    
    @Test
    public void testInvalidDeleteAuthorById(){
        //this part veirfies that the Author we are about to delete does not exist in the system
        Response authorResponse = new AuthorOperations().getAllAuthors();
        Integer nonExistingAuthorId = authorResponse.jsonPath().getInt("authors.author[-1].id") + 100;
        Response verifyNoAuthorResponse = new AuthorOperations().getAuthor(nonExistingAuthorId);
        assertEquals("The status code should be: 404",  404, verifyNoAuthorResponse.statusCode());
        assertEquals("response body should be blank",  "", verifyNoAuthorResponse.body().print());
        
        //the part below tries to delete a book if that book does not exist in the system
        if(verifyNoAuthorResponse.statusCode() == 404){
            Response deleteResponse = new AuthorOperations().deleteAuthor(nonExistingAuthorId);
        assertEquals("The status code should be: 404",  404, deleteResponse.statusCode());
        assertEquals("response body should be blank",  "", deleteResponse.body().print());
        }
        
    }
    
}
    


