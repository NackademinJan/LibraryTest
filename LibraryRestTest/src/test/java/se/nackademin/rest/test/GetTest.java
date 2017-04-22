/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nackademin.rest.test;

import org.junit.Test;

import com.jayway.restassured.response.Response;
import static org.junit.Assert.*;
import se.nackademin.rest.test.model.Book;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import se.nackademin.rest.test.model.AllAuthors;
import se.nackademin.rest.test.model.AllBooks;
import se.nackademin.rest.test.model.Author;

/**
 *
 * @author testautomatisering
 */
public class GetTest {

    
    public GetTest() {
    }
    
    @BeforeClass //this method creates a dummy book, dummy author and adds the author to the book to be used during test executions
    public static void MaketheDummyBookAndDummyAuthor(){
        Response makeDummysResponse = BeforeAndAfterOperations.makeDummyBookAndDummyAuthor();
        assertEquals("The status code should be: 201",  201, makeDummysResponse.statusCode());
        assertEquals("response body should be blank",  "", makeDummysResponse.body().print());
        
        Response addDummyAuthorToDummyBook = BeforeAndAfterOperations.addDummyAuthorToDummyBook();
        assertEquals("The status code should be: 200",  200, addDummyAuthorToDummyBook.statusCode());
        assertEquals("response body should be blank",  "", addDummyAuthorToDummyBook.body().print());
    }
    
    @AfterClass //this method removes the dummies created by the previous method
    public static void RemovetheDummyBookAndDummyAuthor(){
        Response removeResponse = BeforeAndAfterOperations.removeDummyBookAndDummyAuthor();
        assertEquals("The status code should be: 204",  204, removeResponse.statusCode());  
        assertEquals("response body should be blank",  "", removeResponse.body().print());
    }
    
    
    
    @Test //this test tries to perform a get-request on the api for a list of all books in the system and then verifies that we get the right statuscode (200), the response body is not blank and our dummy book is in the response we get
    public void testGetAllBooks(){
        //this part fetches a list of all books
        Response response = new BookOperations().getAllBooks();
        assertEquals("The status code should be: 200",  200, response.statusCode());
        assertNotEquals("The book list should not be empty", "", response.body().print());
        
        //the next parts fetches the Dummybook from the list as a book.class object and verifies that it has the right attributes
        AllBooks books = response.jsonPath().getObject("books", AllBooks.class);
        Book DummyBook = books.getBookfromBooks(GlobVar.dummyBookId);
        
        assertEquals("The books description should be: " + GlobVar.dummyBookDescription,  GlobVar.dummyBookDescription, DummyBook.getDescription());       
        assertEquals("The books title should be: " + GlobVar.dummyBookTitle,  GlobVar.dummyBookTitle, DummyBook.getTitle());       
        assertEquals("The books isbn should be: " + GlobVar.dummyBookIsbn,  GlobVar.dummyBookIsbn, DummyBook.getIsbn());       
        assertEquals("The books page count should be: " + GlobVar.dummyBookNbOfPage,  GlobVar.dummyBookNbOfPage, DummyBook.getNbOfPage());       
        
        //the part below gets the same Dummybook as a response and verifies that it contains the name of that book's author
        Response grabAuthorResponse = new BookOperations().getBookById(GlobVar.dummyBookId);
        String authorName = GlobVar.dummyAuthorName;
        assertNotEquals("The book list should not be empty", "", response.body().print()); 
        assertTrue("The book list should contain the name of our test author", grabAuthorResponse.body().print().contains(authorName)); 
        
        /* //this part commented out because I need help figuring out how to check the author in a book.class object
        if( uncertainAuthor.getClass().isInstance(Author.class)){
            Author DummyAuthor = (Author) DummyBook.getAuthor();
            assertEquals("Author name should be: " + GlobVar.DummyAuthorName, GlobVar.DummyAuthorName, (String) DummyAuthor.getName());
            System.out.println("huzzah");
        } else if(uncertainAuthor.getClass().isInstance(AllAuthors.class)){
            AllAuthors DummyAuthor = (AllAuthors) DummyBook.getAuthor();
            assertEquals("Author name should be: " + GlobVar.DummyAuthorName, GlobVar.DummyAuthorName, (String) DummyAuthor.getAuthorfromAuthors(GlobVar.DummyAuthorId).getName());
        }
        */        
    }
    
    @Test //this test uses my help class AllBooks to fetch all books in the list, then find a single book from the response and verify that it has the right title
    public void testFetchAllBooks(){
        AllBooks books = new BookOperations().fetchAllBooks();
        Book fetchedBook = books.getBookfromBooks(GlobVar.dummyBookId);
        assertEquals("The books description should be: " + GlobVar.dummyBookDescription,  GlobVar.dummyBookDescription, fetchedBook.getDescription());       
        assertEquals("The books title should be: " + GlobVar.dummyBookTitle,  GlobVar.dummyBookTitle, fetchedBook.getTitle());       
        assertEquals("The books isbn should be: " + GlobVar.dummyBookIsbn,  GlobVar.dummyBookIsbn, fetchedBook.getIsbn());       
        assertEquals("The books page count should be: " + GlobVar.dummyBookNbOfPage,  GlobVar.dummyBookNbOfPage, fetchedBook.getNbOfPage());       
        
    }
    
    
    
    @Test //this test fetches our dummy book from the system and verifies the response statuscode (200) and that we got the right book
    public void testGetBookById(){
        Response response = new BookOperations().getBookById(GlobVar.dummyBookId);
        assertEquals("The status code should be: 200",  200, response.statusCode());        
        assertEquals("Book title should be: " + GlobVar.dummyBookTitle,  GlobVar.dummyBookTitle, response.body().jsonPath().getString("book.title"));
        assertEquals("The books description should be: " + GlobVar.dummyBookDescription,  GlobVar.dummyBookDescription, response.body().jsonPath().getString("book.description"));       
        assertEquals("The books title should be: " + GlobVar.dummyBookTitle,  GlobVar.dummyBookTitle, response.body().jsonPath().getString("book.title"));       
        assertEquals("The books isbn should be: " + GlobVar.dummyBookIsbn,  GlobVar.dummyBookIsbn, response.body().jsonPath().getString("book.isbn"));       
        assertEquals("The books page count should be: " + GlobVar.dummyBookNbOfPage,  GlobVar.dummyBookNbOfPage, (Integer)response.body().jsonPath().getInt("book.nbOfPage"));
    }
    
    @Test //this test verifies that trying to retrieve a book that does not exist in the system returns the right statuscode (404) and a blank response body
    public void testInvalidGetBookByIdReturns404(){
        Response response = new BookOperations().getBookById(GlobVar.dummyBookId + 1);
        assertEquals("status code returned should be: 404",  404, response.statusCode());
        assertEquals("response body should be blank", "", response.body().print());
    }

    @Test //this test is essentially the same as testGetBookById but uses the book.class object instead of json style responses
    public void testFetchBook(){
        Book book = new BookOperations().fetchBookById(GlobVar.dummyBookId);
        assertEquals("book id should be: "+GlobVar.dummyBookId, GlobVar.dummyBookId, book.getId());    
        assertEquals("The books description should be: " + GlobVar.dummyBookDescription,  GlobVar.dummyBookDescription, book.getDescription());       
        assertEquals("The books title should be: " + GlobVar.dummyBookTitle,  GlobVar.dummyBookTitle, book.getTitle());       
        assertEquals("The books isbn should be: " + GlobVar.dummyBookIsbn,  GlobVar.dummyBookIsbn, book.getIsbn());       
        assertEquals("The books page count should be: " + GlobVar.dummyBookNbOfPage,  GlobVar.dummyBookNbOfPage, book.getNbOfPage());       
        
    }
    
    
    
    @Test  //this test gets a single book written by an author specified by their authorId, then verifies the response statuscode (200) and that we got the right book
    public void testGetBookByAuthor(){
        Response response = new BookOperations().getBookByAuthor(GlobVar.dummyAuthorId);
        assertEquals("The status code should be: 200",  200, response.statusCode());
        assertEquals("The books description should be: " + GlobVar.dummyBookDescription,  GlobVar.dummyBookDescription, response.body().jsonPath().getString("books.book.description"));       
        assertEquals("The books title should be: " + GlobVar.dummyBookTitle,  GlobVar.dummyBookTitle, response.body().jsonPath().getString("books.book.title"));       
        assertEquals("The books isbn should be: " + GlobVar.dummyBookIsbn,  GlobVar.dummyBookIsbn, response.body().jsonPath().getString("books.book.isbn"));       
        assertEquals("The books page count should be: " + GlobVar.dummyBookNbOfPage,  GlobVar.dummyBookNbOfPage, (Integer)response.body().jsonPath().getInt("books.book.nbOfPage"));
        
    }
    
    /*
     *  detta test kommer inte ge rätt utslag då rest-api returnerar 200 när dett test körs, 
     *  api dokumentationen specifierar inte heller att man ska kunna få fel på detta vis, 
     *  men det verkar som ett misstag så behåller detta test så ni kan se hur det blir.
     */
    @Test //this test verifies that trying to retrieve a book using an authorId that does not exist in the system returns the right statuscode (404) and a blank response body
    public void testInvalidGetBookbyAuthorReturns404(){
        Response response = new BookOperations().getBookByAuthor(GlobVar.dummyAuthorId + 1);
        assertEquals("The status code should be: 404",  404, response.statusCode());
        assertEquals("response body should be blank", "", response.body().print());
    }
    

    
    @Test //this test gets a single author from a book retrieved by its bookId and verifies the response statuscode (200) and that we got the right author
    public void testGetAuthorByBook(){
        Response response = new BookOperations().getAuthorByBook(GlobVar.dummyBookId);
        assertEquals("The status code should be: 200",  200, response.statusCode());
        assertEquals("Author name should be " + GlobVar.dummyAuthorName,  GlobVar.dummyAuthorName, response.body().jsonPath().getString("authors.author.name"));
    }
    
    @Test //this test verifies that trying to retrieve an author using a bookId that does not exist in the system returns the right statuscode (404) and a blank response body
    public void testInvalidGetAuthorByBook(){
        Response response = new BookOperations().getAuthorByBook(GlobVar.dummyBookId + 1);
        assertEquals("The status code should be: 404",  404, response.statusCode());
        assertEquals("response body should be blank", "", response.body().print());
    }
    
    
    
    @Test //this test tries to perform a get-request on the api for a list of all authors in the system and then verifies that we get the right statuscode (200), the response body is not blank and our dummy author is in the response we get
    public void testGetAllAuthors(){
        Response response = new AuthorOperations().getAllAuthors();
        assertEquals("The status code should be: 200",  200, response.statusCode());
        Response authorResponse = new AuthorOperations().getAuthor(GlobVar.dummyAuthorId);
        String authorName = authorResponse.body().jsonPath().getString("author.name");
        assertNotEquals("The book list should not be empty", "", response.body().print()); 
        assertTrue("The book list should contain the name of our test author", response.body().print().contains(authorName));
    }
    
    @Test //this test uses my help class AllAuthors to fetch all authors in the list, then find a single author from the response and verify that it has the right name
    public void testFetchAllAuthors(){
        AllAuthors  authors = new AuthorOperations().fetchAllAuthors();
        Author fetchedAuthor = authors.getAuthorfromAuthors(GlobVar.dummyAuthorId);
        assertEquals("Author name should be: " + GlobVar.dummyAuthorName,  GlobVar.dummyAuthorName, fetchedAuthor.getName());
    }
    
    
    
    @Test //this test fetches our dummy author from the system and verifies the response statuscode (200) and that we got the right author
    public void testGetAuthorById(){
        Response response = new AuthorOperations().getAuthor(GlobVar.dummyAuthorId);
        assertEquals("The status code should be: 200",  200, response.statusCode());
        assertEquals("Author Name should be: " + GlobVar.dummyAuthorName,  GlobVar.dummyAuthorName, response.body().jsonPath().getString("author.name"));
    }
    
    @Test //this test verifies that trying to retrieve an author using an authorId that does not exist in the system returns the right statuscode (404) and a blank response body
    public void testInvalidGetAuthorById(){
        Response response = new AuthorOperations().getAuthor(GlobVar.dummyAuthorId + 1);
        assertEquals("The status code should be: 404",  404, response.statusCode());
        assertEquals("response body should be blank", "", response.body().print());
    }
    @Test //this test uses my Author.class helpclass to fetch a specific author based on their authorId and verifies that we get the right author.
    public void testFetchAuthorById(){
        Author author = new AuthorOperations().fetchAuthor(GlobVar.dummyAuthorId);
        assertEquals("Author name should be: " + GlobVar.dummyAuthorName,  GlobVar.dummyAuthorName, author.getName());
    }
    
    
    
}
