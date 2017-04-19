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
public class LibraryRestTestTest {

    
    public LibraryRestTestTest() {
    }
        
    @BeforeClass
    public static void MakeMockBookAndMockAuthor(){
        Response makeMocksResponse = BeforeAndAfterOperations.MakeMockBookAndMockAuthor();
        assertEquals("The status code should be: 201",  201, makeMocksResponse.statusCode());
        
        Response addMockAuthorToMockBook = BeforeAndAfterOperations.AddMockAuthorToMockBook();
        assertEquals("The status code should be: 200",  200, addMockAuthorToMockBook.statusCode());
    }
    
    @AfterClass
    public static void RemoveMockBookAndMockAuthor(){
        Response removeResponse = BeforeAndAfterOperations.RemoveTestBookAndTestAuthor();
        assertEquals("The status code should be: 204",  204, removeResponse.statusCode());  
    }
    
    
    @Test
    public void testCreateNewBook(){
        
        Book book = new Book();
        book.setDescription("Hello");
        book.setTitle("Happy");
        book.setIsbn("123123");
        book.setNbOfPage(2356);
        SingleBook singleBook = new SingleBook(book);
        
        Response response = given().contentType(ContentType.JSON).body(singleBook).log().all().post(GlobVar.BASE_URL+"books").prettyPeek();
        assertEquals("The status code should be: 201",  201, response.statusCode());
        
        Book verifyBook = new BookOperations().fetchLastBook();
        assertEquals("The books description should be: Hello",  book.getDescription(), verifyBook.getDescription());       
        assertEquals("The books title should be: Happy",  book.getTitle(), verifyBook.getTitle());       
        assertEquals("The books isbn should be: 123123",  book.getIsbn(), verifyBook.getIsbn());       
        assertEquals("The books page count should be: 2356",  book.getNbOfPage(), verifyBook.getNbOfPage());       
        
        //Response deleteResponse = new BookOperations().deleteLastBook();
        //assertEquals("The status code should be: 204",  204, deleteResponse.statusCode());
    }
    
    //@Test this method will not work currently due to the api not supporting posting a new book with an author
    public void testCreateBookWithAuthor(){
        BookOperations bookOperations = new BookOperations();
        AuthorOperations authorOperations = new AuthorOperations();
        
        //Response postAuthorResponse = authorOperations.createRandomAuthorWithRandomId();
        // assertEquals("status code should be 201",  201, postAuthorResponse.statusCode());
        
        Response authorResponse = authorOperations.getAuthor(GlobVar.mockAuthorId); //from(authorOperations.getLatestJsonString()).getString("author.name");
        String authorName = authorResponse.body().jsonPath().getString("author.name");
        Integer authorId = authorResponse.body().jsonPath().getInt("author.id");
        
        Response postResponse = bookOperations.createBookWithAuthor("Hello", "123123", 2356, "Happy", authorName, authorId);
        assertEquals("status code should be 201",  201, postResponse.statusCode());
        
    
        String expectedTitle = from(bookOperations.getLatestJsonString()).getString("book.title");
        String expectedDescription = from(bookOperations.getLatestJsonString()).getString("book.description");
        String expectedIsbn = from(bookOperations.getLatestJsonString()).getString("book.isbn");
        
        Response getResponse = new BookOperations().getAllBooks();
        String fetchedTitle = getResponse.jsonPath().getString("books.book[-1].title");
        String fetchedDescription = getResponse.jsonPath().getString("books.book[-1].description");
        String fetchedIsbn = getResponse.jsonPath().getString("books.book[-1].isbn");
        
    
        assertEquals(expectedTitle, fetchedTitle);
        assertEquals(expectedDescription, fetchedDescription);
        assertEquals(expectedIsbn, fetchedIsbn);      
        
        //Response deleteResponse = new BookOperations().deleteLastBook();
        //assertEquals("The status code should be: 204",  204, deleteResponse.statusCode());
    }
       
    @Test
    public void testDeleteBook(){
        Response postResponse = new BookOperations().createRandomBook();
        assertEquals("The status code should be: 201",  201, postResponse.statusCode());
        
        Response getResponse = new BookOperations().getAllBooks();
        int fetchedId = getResponse.jsonPath().getInt("books.book[-1].id");
        
        Response deleteResponse = new BookOperations().deleteBook(fetchedId);
        assertEquals("delete method should return status code: 204", 204, deleteResponse.getStatusCode());
        
        Response getDeletedBookResponse = new BookOperations().getBookById(fetchedId);
        assertEquals("fetching deleted book should return status code: 404",  404, getDeletedBookResponse.statusCode());
        
    }
    
    @Test
    public void testmakeauthor(){
        Response response = new AuthorOperations().createAuthor("MockTestFörfattare SkaRaderasAutomatiskt");
        assertEquals("The status code should be: 201",  201, response.statusCode());
        
        Response deleteResponse = new AuthorOperations().deleteLastAuthor();
        assertEquals("The status code should be: 204",  204, deleteResponse.statusCode());  
    }   
    
    /*
    //@Test
    public void testCreateNewBookWithAuthor(){
        Book Authorfetch = given().accept(ContentType.JSON).get(GlobVar.BASE_URL+"books/1").jsonPath().getObject("book", Book.class);
        Book book = new Book();
        book.setDescription("Hello");
        book.setTitle("happy");
        book.setIsbn("123123");
        book.setNbOfPage(2356);
        book.setAuthor(Authorfetch.getAuthor());
        
        SingleBook singleBook = new SingleBook(book);
        Response response = given().contentType(ContentType.JSON).body(singleBook).log().all().post(GlobVar.BASE_URL+"books").prettyPeek();
        assertEquals("status code should be 201",  201, response.statusCode());
        System.out.println("Status code: " + response.getStatusCode());  
        
        //Response deleteResponse = new BookOperations().deleteLastBook();
        //assertEquals("status code should be 204",  204, deleteResponse.statusCode());      
    }
    
    //@Test
    public void testAddNewBookWithAuthor(){
        BookOperations bookOperations = new BookOperations();
        AuthorOperations authorOperations = new AuthorOperations();
        
        //Response postAuthorResponse = authorOperations.createRandomAuthorWithRandomId();
        // assertEquals("status code should be 201",  201, postAuthorResponse.statusCode());
        String authorTitle = "Margaret Atwood"; //from(authorOperations.getLatestJsonString()).getString("author.name");
        Integer authorId = 1.0;//from(authorOperations.getLatestJsonString()).getInt("author.id");
        
        Response postResponse = bookOperations.createRandomBookWithAuthor(authorTitle, authorId);
        assertEquals("status code should be 201",  201, postResponse.statusCode());
        
    
        String expectedTitle = from(bookOperations.getLatestJsonString()).getString("book.title");
        String expectedDescription = from(bookOperations.getLatestJsonString()).getString("book.description");
        String expectedIsbn = from(bookOperations.getLatestJsonString()).getString("book.isbn");
        
        Response getResponse = new BookOperations().getAllBooks();
        String fetchedTitle = getResponse.jsonPath().getString("books.book[-1].title");
        String fetchedDescription = getResponse.jsonPath().getString("books.book[-1].description");
        String fetchedIsbn = getResponse.jsonPath().getString("books.book[-1].isbn");
        
    
        assertEquals(expectedTitle, fetchedTitle);
        assertEquals(expectedDescription, fetchedDescription);
        assertEquals(expectedIsbn, fetchedIsbn);
        
    }
    /*
    
    @Test
    public void testAddNewBook(){
        BookOperations bookOperations = new BookOperations();
        Response postResponse = bookOperations.createRandomBook();
        assertEquals("status code should be 201",  201, postResponse.statusCode());
        
    
        String expectedTitle = from(bookOperations.getLatestJsonString()).getString("book.title");
        String expectedDescription = from(bookOperations.getLatestJsonString()).getString("book.description");
        String expectedIsbn = from(bookOperations.getLatestJsonString()).getString("book.isbn");
        
        Response getResponse = new BookOperations().getAllBooks();
        String fetchedTitle = getResponse.jsonPath().getString("books.book[-1].title");
        String fetchedDescription = getResponse.jsonPath().getString("books.book[-1].description");
        String fetchedIsbn = getResponse.jsonPath().getString("books.book[-1].isbn");
        
    
        assertEquals(expectedTitle, fetchedTitle);
        assertEquals(expectedDescription, fetchedDescription);
        assertEquals(expectedIsbn, fetchedIsbn);
        
    }
    */
    /*       //String title = response.body().jsonPath().getString("books.book.title");
        String title = response.body().jsonPath().getString("books.book.title");
        String authorName = response.body().jsonPath().getString("books.book.author.name");
        System.out.println("The author name: " + authorName);
    */
    


    
}
