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
public class PutTest {

    
    public PutTest() {
    }
        
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
    public void testPutBook(){
        Integer newBookId = GlobVar.mockBookId;
        Book book = new Book();
        book.setDescription(GlobVar.secondMockBookDescription);
        book.setTitle(GlobVar.secondMockBookTitle);
        book.setIsbn(GlobVar.secondMockBookIsbn);
        book.setNbOfPage(GlobVar.secondMockBookNbOfPage);
        book.setId(newBookId);
        SingleBook singleBook = new SingleBook(book);
        
        Response response = given().contentType(ContentType.JSON).body(singleBook).log().all().put(GlobVar.BASE_URL+"books");
        assertEquals("The status code should be: 200",  200, response.statusCode());
        assertEquals("response body should be blank", "", response.body().print());
        
        Book verifyBook = new BookOperations().fetchBookById(GlobVar.mockBookId);
        assertEquals("The books description should be: " + GlobVar.secondMockBookDescription,  book.getDescription(), verifyBook.getDescription());       
        assertEquals("The books title should be: " + GlobVar.secondMockBookTitle,  book.getTitle(), verifyBook.getTitle());       
        assertEquals("The books isbn should be: " + GlobVar.secondMockBookIsbn,  book.getIsbn(), verifyBook.getIsbn());       
        assertEquals("The books page count should be: " + GlobVar.secondMockBookNbOfPage,  book.getNbOfPage(), verifyBook.getNbOfPage());       
        
        Response unPutResponse = new BookOperations().updateBookWithAuthor(newBookId, GlobVar.mockBookDescription, GlobVar.mockBookIsbn, GlobVar.mockBookNbOfPage, GlobVar.mockBookTitle, GlobVar.mockAuthorName, GlobVar.mockAuthorId);
        assertEquals("The status code should be: 200",  200, unPutResponse.statusCode());
        assertEquals("response body should be blank", "", unPutResponse.body().print());
    }
    
    @Test
    public void testInvalidPutBookWithNotPreviouslyExistingBook(){
        Integer newBookId = GlobVar.mockBookId +1;
        Book book = new Book();
        book.setDescription(GlobVar.secondMockBookDescription);
        book.setTitle(GlobVar.secondMockBookTitle);
        book.setIsbn(GlobVar.secondMockBookIsbn);
        book.setNbOfPage(GlobVar.secondMockBookNbOfPage);
        book.setId(newBookId);
        SingleBook singleBook = new SingleBook(book);
        
        Response response = given().contentType(ContentType.JSON).body(singleBook).log().all().put(GlobVar.BASE_URL+"books");
        assertEquals("The status code should be: 404",  404, response.statusCode());
        assertEquals("response body should be blank", "", response.body().print());
    }
    
    @Test
    public void testPutBookWithNewAuthorExistingInSystem(){
        BookOperations bookOperations = new BookOperations();
        AuthorOperations authorOperations = new AuthorOperations();
        
        Integer bookId = GlobVar.mockBookId;
        Integer newAuthorId = GlobVar.mockAuthorId;
        String newAuthorName = GlobVar.mockAuthorName;
      
        Response response = bookOperations.updateBookWithAuthor(bookId, GlobVar.secondMockBookDescription, GlobVar.secondMockBookIsbn, GlobVar.secondMockBookNbOfPage, GlobVar.secondMockBookTitle, newAuthorName, newAuthorId);
        assertEquals("The status code should be: 200",  200, response.statusCode());
        assertEquals("response body should be blank", "", response.body().print());
        
        String expectedTitle = from(bookOperations.getLatestJsonString()).getString("book.title");
        String expectedDescription = from(bookOperations.getLatestJsonString()).getString("book.description");
        String expectedIsbn = from(bookOperations.getLatestJsonString()).getString("book.isbn");
        Integer expectedNbOfPage = from(bookOperations.getLatestJsonString()).getInt("book.nbOfPage");
        String expectedAuthorName = from(bookOperations.getLatestJsonString()).getString("book.author.name");
        Integer expectedAuthorId = from(bookOperations.getLatestJsonString()).getInt("book.author.id");
        
        Response verifyResponse = new BookOperations().getBookById(bookId);
        String verifyTitle = verifyResponse.jsonPath().getString("book.title");
        String verifyDescription = verifyResponse.jsonPath().getString("book.description");
        String verifyIsbn = verifyResponse.jsonPath().getString("book.isbn");
        Integer verifyNbOfPage = verifyResponse.jsonPath().getInt("book.nbOfPage");
        String verifyAuthorName = verifyResponse.jsonPath().getString("book.author.name");
        Integer verifyAuthorId = verifyResponse.jsonPath().getInt("book.author.id");
        
        assertEquals(expectedTitle, verifyTitle);
        assertEquals(expectedDescription, verifyDescription);
        assertEquals(expectedIsbn, verifyIsbn);
        assertEquals(expectedNbOfPage, verifyNbOfPage);
        assertEquals(expectedAuthorName, verifyAuthorName);
        assertEquals(expectedAuthorId, verifyAuthorId);
        
        Response unPutResponse = new BookOperations().updateBookWithAuthor(bookId, GlobVar.mockBookDescription, GlobVar.mockBookIsbn, GlobVar.mockBookNbOfPage, GlobVar.mockBookTitle, GlobVar.mockAuthorName, GlobVar.mockAuthorId);
        assertEquals("The status code should be: 200",  200, unPutResponse.statusCode());
        assertEquals("response body should be blank", "", unPutResponse.body().print());
        
    }
    
    //@Test //This test currently returns 200 and somehow produces a new author with the given id and name which should NOT happen!!!
    public void testInvalidPutBookWithNewAuthorNotPreviouslyExistingInSystem(){
        Integer newBookId = GlobVar.mockBookId;
        Integer newAuthorId = GlobVar.mockAuthorId + 1000;
        String newAuthorName = GlobVar.secondMockAuthorName;
        
        Response response = new BookOperations().updateBookWithAuthor(newBookId, GlobVar.secondMockBookDescription, GlobVar.secondMockBookIsbn, GlobVar.secondMockBookNbOfPage, GlobVar.secondMockBookTitle, newAuthorName, newAuthorId);
        
        assertEquals("The status code should be: 400",  400, response.statusCode());
        assertEquals("response body should be blank", "", response.body().print());
        
    }
    
    @Test 
    public void testInvalidPutBookWithNewAuthorPreviouslyExistingInSystemButNoAuthorId(){
        Integer newBookId = GlobVar.mockBookId;
        String newAuthorName = GlobVar.secondMockAuthorName;
        
        Response response = new BookOperations().invalidUpdateBookWithAuthorButNoAuthorId(newBookId, GlobVar.secondMockBookDescription, GlobVar.secondMockBookIsbn, GlobVar.secondMockBookNbOfPage, GlobVar.secondMockBookTitle, newAuthorName);
        
        assertEquals("The status code should be: 400",  400, response.statusCode());
        assertEquals("response body should be Book contained an author with no id field set", "Book contained an author with no id field set.", response.body().print());
    }
    
    @Test
    public void testForbiddenPutToBooksId(){
        String resourceName = "books/"+GlobVar.mockBookId;
        Response response = given().accept(ContentType.JSON).put(GlobVar.BASE_URL+resourceName);
        assertEquals("The status code should be: 405, method not allowed",  405, response.statusCode());  
        assertEquals("response body should be blank",  "", response.body().print());
    }
    
    @Test
    public void testForbiddenPutToBooksByAuthorAuthorId(){
        String resourceName = "books/byauthor"+GlobVar.mockAuthorId;
        Response response = given().accept(ContentType.JSON).put(GlobVar.BASE_URL+resourceName);
        assertEquals("The status code should be: 405, method not allowed",  405, response.statusCode());
        assertEquals("response body should be blank",  "", response.body().print());
    }
    
    @Test //this method tests put functionality for /books/{book_id}/authors
    public void testPutNewAuthorPreviouslyExistingInSystemInExistingBook(){
        BookOperations bookOperations = new BookOperations();
        AuthorOperations authorOperations = new AuthorOperations();
        
        Response authorResponse = authorOperations.createAuthor(GlobVar.thirdMockAuthorName);
        assertEquals("The status code should be: 201",  201, authorResponse.statusCode());
        assertEquals("response body should be blank", "", authorResponse.body().print());
        
        Response getAuthorIdResponse = authorOperations.getAllAuthors();
        Integer authorId = getAuthorIdResponse.jsonPath().getInt("authors.author[-1].id");
        String authorName = GlobVar.thirdMockAuthorName;
      
        Integer bookId = GlobVar.mockBookId;
        Response response = bookOperations.updateABooksAuthors(bookId, authorName, authorId);
        assertEquals("The status code should be: 200",  200, response.statusCode());
        assertEquals("response body should be blank", "", response.body().print());
        
        String expectedAuthorName = from(bookOperations.getLatestJsonString()).getString("authors.author.name");
        Integer expectedAuthorId = from(bookOperations.getLatestJsonString()).getInt("authors.author.id");
        
        Response verifyResponse = new BookOperations().getBookById(bookId);
        String verifyAuthorName = verifyResponse.jsonPath().getString("book.author.name");
        Integer verifyAuthorId = verifyResponse.jsonPath().getInt("book.author.id");
        
        assertEquals(expectedAuthorName, verifyAuthorName);
        assertEquals(expectedAuthorId, verifyAuthorId);
        
        Response unPutResponse = new BookOperations().updateBookWithAuthor(bookId, GlobVar.mockBookDescription, GlobVar.mockBookIsbn, GlobVar.mockBookNbOfPage, GlobVar.mockBookTitle, GlobVar.mockAuthorName, GlobVar.mockAuthorId);
        assertEquals("The status code should be: 200",  200, unPutResponse.statusCode());
        assertEquals("response body should be blank", "", unPutResponse.body().print());
        
        Response deleteAuthorResponse = authorOperations.deleteAuthor(authorId);
        assertEquals("The status code should be: 204",  204, deleteAuthorResponse.statusCode());
        assertEquals("response body should be blank", "", deleteAuthorResponse.body().print());
    }
    
    @Test 
    public void testInvalidPutNewAuthorNotPreviouslyExistingInSystemInExistingBook(){
        Integer bookId = GlobVar.mockBookId;
        Integer badAuthorId = GlobVar.mockAuthorId + 1000;
        String badAuthorName = GlobVar.thirdMockAuthorName;
        
      
        Response response = new BookOperations().updateABooksAuthors(bookId, badAuthorName, badAuthorId);
        assertEquals("The status code should be: 400",  400, response.statusCode());
        assertEquals("response body should be Author does not exist in database.", "Author does not exist in database.", response.body().print());
        
    }
    
    @Test 
    public void testInvalidPutNewAuthorPreviouslyExistingInSystemInExistingBookButNoAuthorId(){
        Integer bookId = GlobVar.mockBookId;
        Integer badAuthorId = GlobVar.mockAuthorId + 1000;
        String badAuthorName = GlobVar.thirdMockAuthorName;
        
        Response response = new BookOperations().invalidUpdateABooksAuthors(bookId, badAuthorName);
        assertEquals("The status code should be: 400",  400, response.statusCode());
        assertEquals("response body should be Author must have id field set.", "Author must have id field set.", response.body().print());   
    }
    
    
    @Test
    public void testPutExistingAuthorInAuthors(){
        Response putAuthorResponse = new AuthorOperations().updateAuthor(GlobVar.thirdMockAuthorName, GlobVar.mockAuthorId);
        assertEquals("The status code should be: 200",  200, putAuthorResponse.statusCode());
        assertEquals("response body should be blank", "", putAuthorResponse.body().print());
        
         Response unPutResponse =  new AuthorOperations().updateAuthor(GlobVar.mockAuthorName, GlobVar.mockAuthorId);
        assertEquals("The status code should be: 200",  200, unPutResponse.statusCode());
        assertEquals("response body should be blank", "", unPutResponse.body().print());    
    }
    
    @Test //this method should Probably return 400 for an improperly format on the put request, but apparently it just fails to find what author to update and a 400 code is not required by the api documentation
    public void testInvalidPutExistingAuthorInAuthorsWithNoAuthorId(){
        Response putAuthorResponse = new AuthorOperations().invalidUpdateAuthorWithoutAuthorId(GlobVar.thirdMockAuthorName);
        assertEquals("The status code should be: 404",  404, putAuthorResponse.statusCode());
        assertEquals("response body should be blank", "", putAuthorResponse.body().print());
    }
    
    @Test
    public void testInvalidPutNotPreviouslyExistingAuthorInAuthors(){
        Response putAuthorResponse = new AuthorOperations().updateAuthor(GlobVar.thirdMockAuthorName, GlobVar.thirdMockAuthorId);
        assertEquals("The status code should be: 404",  404, putAuthorResponse.statusCode());
        assertEquals("response body should be blank", "", putAuthorResponse.body().print());
    }
    
    
    @Test
    public void ForbiddentestPutAuthorById(){
        String resourceName = "authors/"+GlobVar.mockAuthorId;
        Response response = given().accept(ContentType.JSON).put(GlobVar.BASE_URL+resourceName);
        assertEquals("The status code should be: 405, method not allowed",  405, response.statusCode());  
        assertEquals("response body should be blank",  "", response.body().print());
    }
    
   
}
