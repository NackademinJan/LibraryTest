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
import static org.junit.Assert.*;
import static com.jayway.restassured.path.json.JsonPath.*;
import se.nackademin.rest.test.model.Book;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import se.nackademin.rest.test.model.SingleBook;
/**
 *
 * @author testautomatisering
 */
public class PutTest {

    
    public PutTest() {
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

    
    
    @Test //this test attempts to update our dummybook with new data, verifies that we get the right response statuscode (200) and a blank response body. it then verifies that the book's information has been updated and finally reverts the data as part of cleanup
    public void testPutBook(){
        Integer newBookId = GlobVar.dummyBookId;
        Book book = new Book();
        book.setDescription(GlobVar.secondDummyBookDescription);
        book.setTitle(GlobVar.secondDummyBookTitle);
        book.setIsbn(GlobVar.secondDummyBookIsbn);
        book.setNbOfPage(GlobVar.secondDummyBookNbOfPage);
        book.setId(newBookId);
        SingleBook singleBook = new SingleBook(book);
        
        Response response = given().contentType(ContentType.JSON).body(singleBook).log().all().put(GlobVar.BASE_URL+"books");
        assertEquals("The status code should be: 200",  200, response.statusCode());
        assertEquals("response body should be blank", "", response.body().print());
        
        Book verifyBook = new BookOperations().fetchBookById(GlobVar.dummyBookId);
        assertEquals("The books description should be: " + GlobVar.secondDummyBookDescription,  book.getDescription(), verifyBook.getDescription());       
        assertEquals("The books title should be: " + GlobVar.secondDummyBookTitle,  book.getTitle(), verifyBook.getTitle());       
        assertEquals("The books isbn should be: " + GlobVar.secondDummyBookIsbn,  book.getIsbn(), verifyBook.getIsbn());       
        assertEquals("The books page count should be: " + GlobVar.secondDummyBookNbOfPage,  book.getNbOfPage(), verifyBook.getNbOfPage());       
        
        Response unPutResponse = new BookOperations().unPutBook(newBookId);
        assertEquals("The status code should be: 200",  200, unPutResponse.statusCode());
        assertEquals("response body should be blank", "", unPutResponse.body().print());
    }
    
    @Test //this test verifies that we cannot attempt to update a book that does not already exist in the system. that we get the right response statuscode (404) and a blank response body
    public void testInvalidPutBookWithNotPreviouslyExistingBook(){
        Integer newBookId = GlobVar.dummyBookId +1;
        Book book = new Book();
        book.setDescription(GlobVar.secondDummyBookDescription);
        book.setTitle(GlobVar.secondDummyBookTitle);
        book.setIsbn(GlobVar.secondDummyBookIsbn);
        book.setNbOfPage(GlobVar.secondDummyBookNbOfPage);
        book.setId(newBookId);
        SingleBook singleBook = new SingleBook(book);
        
        Response response = given().contentType(ContentType.JSON).body(singleBook).log().all().put(GlobVar.BASE_URL+"books");
        assertEquals("The status code should be: 404",  404, response.statusCode());
        assertEquals("response body should be blank", "", response.body().print());
    }
    
    @Test ////this test attempts to update our dummybook with new data, including the author, verifies that we get the right response statuscode (200) and a blank response body. it then verifies that the book's information has been updated and finally reverts the data as part of cleanup
    public void testPutBookWithNewAuthorExistingInSystem(){
        BookOperations bookOperations = new BookOperations();
        AuthorOperations authorOperations = new AuthorOperations();
        
        Integer bookId = GlobVar.dummyBookId;
        Integer newAuthorId = GlobVar.dummyAuthorId;
        String newAuthorName = GlobVar.dummyAuthorName;
      
        Response response = bookOperations.updateBookWithAuthor(bookId, GlobVar.secondDummyBookDescription, GlobVar.secondDummyBookIsbn, GlobVar.secondDummyBookNbOfPage, GlobVar.secondDummyBookTitle, newAuthorName, newAuthorId);
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
        
        Response unPutResponse = new BookOperations().unPutBook(bookId);
        assertEquals("The status code should be: 200",  200, unPutResponse.statusCode());
        assertEquals("response body should be blank", "", unPutResponse.body().print());
    }
    
    @Test //this test verifies that you cannot update a book with a new author that doesnt already exist in the system, that we get the appropriate response statuscode (400) and a blank response body
          //This test while testing the current version of the system returns 200 and somehow produces a new author with the given id and name which should NOT happen!!!
    public void testInvalidPutBookWithNewAuthorNotPreviouslyExistingInSystem(){
        Integer BookId = GlobVar.dummyBookId;
        Integer badAuthorId = GlobVar.dummyAuthorId + 1000;
        String badAuthorName = GlobVar.secondDummyAuthorName;
        
        Response response = new BookOperations().updateBookWithAuthor(BookId, GlobVar.secondDummyBookDescription, GlobVar.secondDummyBookIsbn, GlobVar.secondDummyBookNbOfPage, GlobVar.secondDummyBookTitle, badAuthorName, badAuthorId);
        
        assertEquals("The status code should be: 400",  400, response.statusCode());
        assertEquals("response body should be blank", "", response.body().print());   
    }
    
    @Test //this test verifies that we cannot update a book with an author that does exist in the system if we do not include the authorId in the request. we get the appropriate response statuscode (400) and response body message
    public void testInvalidPutBookWithNewAuthorPreviouslyExistingInSystemButNoAuthorId(){
        Integer BookId = GlobVar.dummyBookId;
        String AuthorName = GlobVar.secondDummyAuthorName;
        
        Response response = new BookOperations().invalidUpdateBookWithAuthorButNoAuthorId(BookId, GlobVar.secondDummyBookDescription, GlobVar.secondDummyBookIsbn, GlobVar.secondDummyBookNbOfPage, GlobVar.secondDummyBookTitle, AuthorName);
        
        assertEquals("The status code should be: 400",  400, response.statusCode());
        assertEquals("response body should be Book contained an author with no id field set", "Book contained an author with no id field set.", response.body().print());
    }
    
    
    
    @Test //this test verifies that we cannot perform a put request to a book's bookId directly, that we get the right response statuscode (405) and a blank response body
    public void testForbiddenPutToBooksId(){
        String resourceName = "books/"+GlobVar.dummyBookId;
        Response response = given().accept(ContentType.JSON).put(GlobVar.BASE_URL+resourceName);
        assertEquals("The status code should be: 405, method not allowed",  405, response.statusCode());  
        assertEquals("response body should be blank",  "", response.body().print());
    }
    
    
    
    @Test //this test verifies that we cannot perform a put request to an author's authorId directly, that we get the right response statuscode (405) and a blank response body
    public void testForbiddenPutToBooksByAuthorAuthorId(){
        String resourceName = "books/byauthor/"+GlobVar.dummyAuthorId;
        Response response = given().accept(ContentType.JSON).put(GlobVar.BASE_URL+resourceName);
        assertEquals("The status code should be: 405, method not allowed",  405, response.statusCode());
        assertEquals("response body should be blank",  "", response.body().print());
    }
    
    
    
    @Test //this test attempts to create a new dummy author then update an existing dummybook with that author, verifying that we get the right response statuscode (200) and a blank response body. it then restores the dummybook and deletes the new dummy author as part of cleanup
    public void testPutNewAuthorPreviouslyExistingInSystemInExistingBook(){
        BookOperations bookOperations = new BookOperations();
        AuthorOperations authorOperations = new AuthorOperations();
        
        Response authorResponse = authorOperations.createAuthor(GlobVar.thirdDummyAuthorName);
        assertEquals("The status code should be: 201",  201, authorResponse.statusCode());
        assertEquals("response body should be blank", "", authorResponse.body().print());
        
        Response getAuthorIdResponse = authorOperations.getAllAuthors();
        Integer authorId = getAuthorIdResponse.jsonPath().getInt("authors.author[-1].id");
        String authorName = GlobVar.thirdDummyAuthorName;
      
        Integer bookId = GlobVar.dummyBookId;
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
        //the next two parts restore the dummybook to default variables and, if a new author was made, deletes that author)
        Response unPutResponse = new BookOperations().unPutBook(bookId);
        assertEquals("The status code should be: 200",  200, unPutResponse.statusCode());
        assertEquals("response body should be blank", "", unPutResponse.body().print());
        
        if(authorResponse.statusCode() == 201){
            Response deleteAuthorResponse = authorOperations.deleteAuthor(authorId);
            assertEquals("The status code should be: 204",  204, deleteAuthorResponse.statusCode());
            assertEquals("response body should be blank", "", deleteAuthorResponse.body().print());
        }
    }
    
    @Test //this test verifies that you cannot update a book that exists in the system with an author that does not exist in the system and that we get the appropriate response statuscode (400) and response body message
    public void testInvalidPutNewAuthorNotPreviouslyExistingInSystemInExistingBook(){
        Integer bookId = GlobVar.dummyBookId;
        Integer badAuthorId = GlobVar.dummyAuthorId + 1000;
        String badAuthorName = GlobVar.thirdDummyAuthorName;
        
      
        Response response = new BookOperations().updateABooksAuthors(bookId, badAuthorName, badAuthorId);
        assertEquals("The status code should be: 400",  400, response.statusCode());
        assertEquals("response body should be Author does not exist in database.", "Author does not exist in database.", response.body().print());
        
    }
    
    @Test  //this test verifies that you cannot update a book that exists in the system with an author that exists if you do not include the authorId in the post request. it also verifies that we get the appropriate response statuscode (400) and response body message
    public void testInvalidPutNewAuthorPreviouslyExistingInSystemInExistingBookButNoAuthorId(){
        Integer bookId = GlobVar.dummyBookId;
        String badAuthorName = GlobVar.thirdDummyAuthorName;
        
        Response response = new BookOperations().invalidUpdateABooksAuthors(bookId, badAuthorName);
        assertEquals("The status code should be: 400",  400, response.statusCode());
        assertEquals("response body should be Author must have id field set.", "Author must have id field set.", response.body().print());   
    }
    
    
    
    @Test //this test tries to update an author that exists in the system with a new name, verifies that we get the right response statuscode (200) and a blank response body
    public void testPutExistingAuthorInAuthors(){
        Response putAuthorResponse = new AuthorOperations().updateAuthor(GlobVar.thirdDummyAuthorName, GlobVar.dummyAuthorId);
        assertEquals("The status code should be: 200",  200, putAuthorResponse.statusCode());
        assertEquals("response body should be blank", "", putAuthorResponse.body().print());
        
        Response unPutResponse =  new AuthorOperations().updateAuthor(GlobVar.dummyAuthorName, GlobVar.dummyAuthorId);
        assertEquals("The status code should be: 200",  200, unPutResponse.statusCode());
        assertEquals("response body should be blank", "", unPutResponse.body().print());    
    }
    
    @Test //this test verifies that we cannot update an existing author with a new name if we do not include an authorId with the request, 
          //this test should Probably return 400 for an improperly format on the put request, but apparently it just fails to find what author to update and a 400 code is not required by the api documentation
    public void testInvalidPutExistingAuthorInAuthorsWithNoAuthorId(){
        Response putAuthorResponse = new AuthorOperations().invalidUpdateAuthorWithoutAuthorId(GlobVar.thirdDummyAuthorName);
        assertEquals("The status code should be: 404",  404, putAuthorResponse.statusCode());
        assertEquals("response body should be blank", "", putAuthorResponse.body().print());
    }
    
    @Test //this test verifies that you cannot update an author with new information if that author does not exist in the system already. Also that we get the right response statuscode(404) and a blank response body
    public void testInvalidPutNotPreviouslyExistingAuthorInAuthors(){
        Response putAuthorResponse = new AuthorOperations().updateAuthor(GlobVar.thirdDummyAuthorName, GlobVar.thirdDummyAuthorId);
        assertEquals("The status code should be: 404",  404, putAuthorResponse.statusCode());
        assertEquals("response body should be blank", "", putAuthorResponse.body().print());
    }
    
    
    
    @Test //this test verifies that you cannot perform a put request to authors directly to authorId and that we get the right response statuscode (405) for trying along with a blank response body
    public void ForbiddentestPutAuthorById(){
        String resourceName = "authors/"+GlobVar.dummyAuthorId;
        Response response = given().accept(ContentType.JSON).put(GlobVar.BASE_URL+resourceName);
        assertEquals("The status code should be: 405, method not allowed",  405, response.statusCode());  
        assertEquals("response body should be blank",  "", response.body().print());
    }
    
    
    
}
