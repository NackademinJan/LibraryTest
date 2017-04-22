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
public class PostTest {

    
    public PostTest() {
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

    
    
    @Test //This test tries to post a new book to the system and verifies that we get the right responsecode (201), a blank responsebody and then verifies that the new book with included variables are in the system
    public void testPostBook(){
        Book book = new Book();
        book.setDescription(GlobVar.secondDummyBookDescription);
        book.setTitle(GlobVar.secondDummyBookTitle);
        book.setIsbn(GlobVar.secondDummyBookIsbn);
        book.setNbOfPage(GlobVar.secondDummyBookNbOfPage);
        SingleBook singleBook = new SingleBook(book);
        
        Response response = given().contentType(ContentType.JSON).body(singleBook).log().all().post(GlobVar.BASE_URL+"books");
        assertEquals("The status code should be: 201",  201, response.statusCode());
        assertEquals("response body should be blank", "", response.body().print());
        
        Book verifyBook = new BookOperations().fetchLastBook();
        assertEquals("The books description should be: " + GlobVar.secondDummyBookDescription,  book.getDescription(), verifyBook.getDescription());       
        assertEquals("The books title should be: " + GlobVar.secondDummyBookTitle,  book.getTitle(), verifyBook.getTitle());       
        assertEquals("The books isbn should be: " + GlobVar.secondDummyBookIsbn,  book.getIsbn(), verifyBook.getIsbn());       
        assertEquals("The books page count should be: " + GlobVar.secondDummyBookNbOfPage,  book.getNbOfPage(), verifyBook.getNbOfPage());       
        
        if(response.statusCode() == 201){
            Response deleteResponse = new BookOperations().deleteLastBook();
            assertEquals("The status code should be: 204",  204, deleteResponse.statusCode());
            assertEquals("response body should be blank", "", deleteResponse.body().print());
        }
        
    }
    
    @Test //this test does not use the book class but is otherwise equivalent to testPostBookWithSpecificNewId and was written mainly to see if the api woulld allow calls of the text format that "BookOperations().createBookWithInputAndId" uses to be posted.
    public void testPostBookWithSpecificNewIdWithoutBookClass(){
        Response getResponse = new BookOperations().getAllBooks();
        Integer newId = GlobVar.dummyBookId + 1;
        Response response = new BookOperations().createBookWithInputAndId(newId, GlobVar.secondDummyBookDescription, GlobVar.secondDummyBookIsbn, GlobVar.secondDummyBookNbOfPage, GlobVar.secondDummyBookTitle);
        
        assertEquals("The status code should be: 201",  201, response.statusCode());
        assertEquals("response body should be blank", "", response.body().print());
        
        Book verifyBook = new BookOperations().fetchLastBook();
        assertEquals("The books description should be: " + GlobVar.secondDummyBookDescription,  GlobVar.secondDummyBookDescription, verifyBook.getDescription());       
        assertEquals("The books title should be: " + GlobVar.secondDummyBookTitle,  GlobVar.secondDummyBookTitle, verifyBook.getTitle());       
        assertEquals("The books isbn should be: " + GlobVar.secondDummyBookIsbn,  GlobVar.secondDummyBookIsbn, verifyBook.getIsbn());       
        assertEquals("The books page count should be: " + GlobVar.secondDummyBookNbOfPage,  GlobVar.secondDummyBookNbOfPage, verifyBook.getNbOfPage());       
        assertEquals("The books id should be: " + newId,  newId, verifyBook.getId()); 
        
        if(response.statusCode() == 201){
            Response deleteResponse = new BookOperations().deleteLastBook();
            assertEquals("The status code should be: 204",  204, deleteResponse.statusCode());
            assertEquals("response body should be blank", "", deleteResponse.body().print());
        }
    }
    
    @Test //this test attempts to post a book with a specific bookid that does not previously exist in the system, verifies that we get the right statuscode (201) and blank response body. It then verifies that the new book is in the system
    public void testPostBookwithSpecificNewId(){
        Response getResponse = new BookOperations().getAllBooks();
        int newId = getResponse.jsonPath().getInt("books.book[-1].id") + 1;
        Book book = new Book();
        book.setDescription(GlobVar.secondDummyBookDescription);
        book.setTitle(GlobVar.secondDummyBookTitle);
        book.setIsbn(GlobVar.secondDummyBookIsbn);
        book.setNbOfPage(GlobVar.secondDummyBookNbOfPage);
        book.setId(newId);
        SingleBook singleBook = new SingleBook(book);
        
        Response response = given().contentType(ContentType.JSON).body(singleBook).log().all().post(GlobVar.BASE_URL+"books");
        assertEquals("The status code should be: 201",  201, response.statusCode());
        assertEquals("response body should be blank", "", response.body().print());
        
        Book verifyBook = new BookOperations().fetchLastBook();
        assertEquals("The books description should be: " + GlobVar.secondDummyBookDescription,  book.getDescription(), verifyBook.getDescription());       
        assertEquals("The books title should be: " + GlobVar.secondDummyBookTitle,  book.getTitle(), verifyBook.getTitle());       
        assertEquals("The books isbn should be: " + GlobVar.secondDummyBookIsbn,  book.getIsbn(), verifyBook.getIsbn());       
        assertEquals("The books page count should be: " + GlobVar.secondDummyBookNbOfPage,  book.getNbOfPage(), verifyBook.getNbOfPage());       
        assertEquals("The books page count should be: " + newId,  book.getId(), verifyBook.getId()); 
        
        if(response.statusCode() == 201){
            Response deleteResponse = new BookOperations().deleteLastBook();
            assertEquals("The status code should be: 204",  204, deleteResponse.statusCode());
            assertEquals("response body should be blank", "", deleteResponse.body().print());
        }
    }
    
    @Test //this test verifies that trying to post a book with a bookId that already exists in the system returns the right statuscode (400) and appropriate response body message
    public void testInvalidPostBookwithExistingID(){
        Response getResponse = new BookOperations().getAllBooks();
        int lastId = getResponse.jsonPath().getInt("books.book[-1].id");
        Book book = new Book();
        book.setDescription(GlobVar.secondDummyBookDescription);
        book.setTitle(GlobVar.secondDummyBookTitle);
        book.setIsbn(GlobVar.secondDummyBookIsbn);
        book.setNbOfPage(GlobVar.secondDummyBookNbOfPage);
        book.setId(lastId);
        SingleBook singleBook = new SingleBook(book);
        
        Response response = given().contentType(ContentType.JSON).body(singleBook).log().all().post(GlobVar.BASE_URL+"books");
        assertEquals("The status code should be: 400",  400, response.statusCode());
        assertEquals("response body should be Book was null.",  "Book was null.", response.body().print());
    }
    
    
    @Test //this test attempts to post a book with an author that exists in the system then verifies that we get the right response statuscode (201) and a blank response body. then verifies that the new book (with author data) is in the system
    public void testPostBookWithAuthor(){
        BookOperations bookOperations = new BookOperations();
        AuthorOperations authorOperations = new AuthorOperations();
        
        Response authorResponse = authorOperations.getAuthor(GlobVar.dummyAuthorId); 
        String authorName = authorResponse.body().jsonPath().getString("author.name");
        Integer authorId = authorResponse.body().jsonPath().getInt("author.id");
        
        Response postResponse = bookOperations.createBookWithAuthor(GlobVar.secondDummyBookDescription, GlobVar.secondDummyBookIsbn, GlobVar.secondDummyBookNbOfPage, GlobVar.secondDummyBookTitle, authorName, authorId);
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
        
        if(postResponse.statusCode() == 201){
            Response deleteResponse = new BookOperations().deleteLastBook();
            assertEquals("The status code should be: 204",  204, deleteResponse.statusCode());
            assertEquals("response body should be blank", "", deleteResponse.body().print());
        }
    }
    
    /*
    @Test //This test remains incomplete because im not sure how to work with a book.class author-object
    public void testPostBookWithAuthorUsingBookClass(){
        Response getResponse = new BookOperations().getAllBooks();
        int lastId = getResponse.jsonPath().getInt("books.book[-1].id");
        Response authorResponse = new AuthorOperations().getAuthor(lastId);
        Object Author = authorResponse.getBody().jsonPath().getList(basePath)
        
        Book book = new Book();
        book.setDescription(GlobVar.secondDummyBookDescription);
        book.setTitle(GlobVar.secondDummyBookTitle);
        book.setIsbn(GlobVar.secondDummyBookIsbn);
        book.setNbOfPage(GlobVar.secondDummyBookNbOfPage);
        book.setAuthor(authorResponse);
        SingleBook singleBook = new SingleBook(book);
        
        Response response = given().contentType(ContentType.JSON).body(singleBook).log().all().post(GlobVar.BASE_URL+"books").prettyPeek();
        assertEquals("The status code should be: 201",  201, response.statusCode());
        assertEquals("response body should be blank", "", response.body().prettyPrint());
    
        Book verifyBook = new BookOperations().fetchLastBook();
         assertEquals("The books description should be: " + GlobVar.secondDummyBookDescription,  book.getDescription(), verifyBook.getDescription());       
        assertEquals("The books title should be: " + GlobVar.secondDummyBookTitle,  book.getTitle(), verifyBook.getTitle());       
        assertEquals("The books isbn should be: " + GlobVar.secondDummyBookIsbn,  book.getIsbn(), verifyBook.getIsbn());       
        assertEquals("The books page count should be: " + GlobVar.secondDummyBookNbOfPage,  book.getNbOfPage(), verifyBook.getNbOfPage());       
        // this one needs work! assertEquals("The books author should be: " + book.getAuthor(),  book.getAuthor(), verifyBook.getAuthor());       
    
        assertEquals("The books description should be: " + GlobVar.secondDummyBookDescription,  book.getDescription(), verifyBook.getDescription());       
        assertEquals("The books title should be: " + GlobVar.secondDummyBookTitle,  book.getTitle(), verifyBook.getTitle());       
        assertEquals("The books isbn should be: " + GlobVar.secondDummyBookIsbn,  book.getIsbn(), verifyBook.getIsbn());       
        assertEquals("The books page count should be: " + GlobVar.secondDummyBookNbOfPage,  book.getNbOfPage(), verifyBook.getNbOfPage());       
    
        Response deleteResponse = new BookOperations().deleteLastBook();
        assertEquals("The status code should be: 204",  204, deleteResponse.statusCode());
    }
   */
    
    @Test //this test verifies that trying to post a book with an author but no authorId returns the right statuscode (400) and appropriate response body message
    public void testInvalidPostBookWithAuthorNameButNoAuthorId(){
        Response authorResponse = new AuthorOperations().getAuthor(GlobVar.dummyAuthorId); 
        String authorName = authorResponse.body().jsonPath().getString("author.name");
        Response postResponse = new BookOperations().invalidCreateBookWithAuthorNameButNoAuthorId(GlobVar.secondDummyBookDescription, GlobVar.secondDummyBookIsbn, GlobVar.secondDummyBookNbOfPage, GlobVar.secondDummyBookTitle, authorName);
        assertEquals("The status code should be: 400",  400, postResponse.statusCode());
        assertEquals("response body should be Book contained an author with no id field set.",  "Book contained an author with no id field set.", postResponse.body().print());
    }
    
    @Test //this test verifies that trying to post a new book with an authorId that does not already exists in the system returns the right statuscode (400) and appropriate response body message
    public void testInvalidPostBookWithAuthorNameButWrongAuthorId(){
        Response authorResponse = new AuthorOperations().getAuthor(GlobVar.dummyAuthorId); 
        String authorName = authorResponse.body().jsonPath().getString("author.name");
        Integer authorId = authorResponse.body().jsonPath().getInt("author.id") + 1;
        Response postResponse = new BookOperations().createBookWithAuthor(GlobVar.secondDummyBookDescription, GlobVar.secondDummyBookIsbn, GlobVar.secondDummyBookNbOfPage, GlobVar.secondDummyBookTitle, authorName, authorId);
        assertEquals("The status code should be: 400",  400, postResponse.statusCode()); 
        assertEquals("response body should be Author does not exist in database.",  "Author does not exist in database.", postResponse.body().print());
    }
    
    @Test //this test verifies that trying to post a new book with a valid authorId but the wrong authorName for that id returns the right statuscode (400) and appropriate response body message
    public void testInvalidPostBookWithAuthorIDButWrongAuthorName(){
        Response authorResponse = new AuthorOperations().getAuthor(GlobVar.dummyAuthorId); 
        String authorName = authorResponse.body().jsonPath().getString("author.name") + "blarg"; 
        Integer authorId = authorResponse.body().jsonPath().getInt("author.id");
        Response postResponse = new BookOperations().createBookWithAuthor(GlobVar.secondDummyBookDescription, GlobVar.secondDummyBookIsbn, GlobVar.secondDummyBookNbOfPage, GlobVar.secondDummyBookTitle, authorName, authorId);
        assertEquals("The status code should be: 400",  400, postResponse.statusCode()); 
        assertEquals("response body should be Author does not exist in database.",  "Author does not exist in database.", postResponse.body().print());
    }
    
    
    
    @Test //this test verifies that you cannot perform a post request to a given book by its bookId
    public void testForbiddenPostToBooksId(){
        String resourceName = "books/"+GlobVar.dummyBookId;
        Response response = given().accept(ContentType.JSON).post(GlobVar.BASE_URL+resourceName);
        assertEquals("The status code should be: 405, method not allowed",  405, response.statusCode());  
        assertEquals("response body should be blank",  "", response.body().print());
    }
    
    
    @Test //this test verifies that you cannot perform a post request to a given book by its author's authorId
    public void testForbiddenPostToBooksByAuthorAuthorId(){
        String resourceName = "books/byauthor/"+GlobVar.dummyAuthorId;
        Response response = given().accept(ContentType.JSON).post(GlobVar.BASE_URL+resourceName);
        assertEquals("The status code should be: 405, method not allowed",  405, response.statusCode());
        assertEquals("response body should be blank",  "", response.body().print());
    }
    
    
    
    @Test //this test tries to create a new book, then attempts to add our existing dummy author to that book and 
    public void testPostAuthorToBook(){
        BookOperations bookOperations = new BookOperations();
        AuthorOperations authorOperations = new AuthorOperations();
        
        //this part makes a new book and verifies that it was created)
        Response postResponse = bookOperations.createBookWithInput(GlobVar.secondDummyBookDescription, GlobVar.secondDummyBookIsbn, GlobVar.secondDummyBookNbOfPage, GlobVar.secondDummyBookTitle);
        assertEquals("status code should be 201",  201, postResponse.statusCode());
        assertEquals("response body should be blank", "", postResponse.body().print());
        
        String expectedTitle = from(bookOperations.getLatestJsonString()).getString("book.title");
        String expectedDescription = from(bookOperations.getLatestJsonString()).getString("book.description");
        String expectedIsbn = from(bookOperations.getLatestJsonString()).getString("book.isbn");
        
        Response getResponse = bookOperations.getAllBooks();
        String fetchedTitle = getResponse.jsonPath().getString("books.book[-1].title");
        String fetchedDescription = getResponse.jsonPath().getString("books.book[-1].description");
        String fetchedIsbn = getResponse.jsonPath().getString("books.book[-1].isbn");
        
        assertEquals(expectedTitle, fetchedTitle);
        assertEquals(expectedDescription, fetchedDescription);
        assertEquals(expectedIsbn, fetchedIsbn);      
        
        //this part adds an existing author to the new book if a new book was successfully created and verifies that the author has been added to said book and that we get the right response statuscode (200) and finally deletes the book as part of cleanup
        if(postResponse.statusCode() == 201){
            Response authorResponse = authorOperations.getAuthor(GlobVar.dummyAuthorId); 
            String authorName = authorResponse.body().jsonPath().getString("author.name");
            Integer authorId = authorResponse.body().jsonPath().getInt("author.id");
            Integer newBookId = new BookOperations().getLastBook().jsonPath().getInt("book.id");
        
            Response postAuthorToBookResponse = new BookOperations().addAuthorToBook(authorName, authorId, newBookId);
            assertEquals("The status code should be: 200",  200, postAuthorToBookResponse.statusCode());
            assertEquals("response body should be blank", "", postAuthorToBookResponse.body().print());  
        
            Response getAuthorResponse = bookOperations.getBookById(newBookId);
            String verifyAuthorName = getAuthorResponse.jsonPath().getString("book.author.name");
            Integer verifyAuthorId = getAuthorResponse.jsonPath().getInt("book.author.id");
        
            assertEquals(authorName, verifyAuthorName);
            assertEquals(authorId, verifyAuthorId);
        }
        
        // this bit cleans up the mess if there is a mess to clean up
        if(postResponse.statusCode() == 201){
            Response deleteResponse = new BookOperations().deleteLastBook();
            assertEquals("The status code should be: 204",  204, deleteResponse.statusCode());
            assertEquals("response body should be blank", "", deleteResponse.body().print());
        }
    }
    
    @Test //this test verifies that you cannot post an author to a book that already has that author on its list of authors, it first creates a new dummybook, adds our dummy author to it, then tries to add the same author again. Then verifies that we get the right response statuscode (400) and response body message. Finally it deletes the new dummybook as part of cleanup
    public void testInvalidPostAuthorToBookTwice(){
        BookOperations bookOperations = new BookOperations();
        AuthorOperations authorOperations = new AuthorOperations();
        
        //this part makes a new book and verifies that it was created)
        Response postResponse = bookOperations.createBookWithInput(GlobVar.secondDummyBookDescription, GlobVar.secondDummyBookIsbn, GlobVar.secondDummyBookNbOfPage, GlobVar.secondDummyBookTitle);
        assertEquals("status code should be 201",  201, postResponse.statusCode());
        assertEquals("response body should be blank", "", postResponse.body().print());
        
        String expectedTitle = from(bookOperations.getLatestJsonString()).getString("book.title");
        String expectedDescription = from(bookOperations.getLatestJsonString()).getString("book.description");
        String expectedIsbn = from(bookOperations.getLatestJsonString()).getString("book.isbn");
        
        Response getResponse = bookOperations.getAllBooks();
        String fetchedTitle = getResponse.jsonPath().getString("books.book[-1].title");
        String fetchedDescription = getResponse.jsonPath().getString("books.book[-1].description");
        String fetchedIsbn = getResponse.jsonPath().getString("books.book[-1].isbn");
        
        assertEquals(expectedTitle, fetchedTitle);
        assertEquals(expectedDescription, fetchedDescription);
        assertEquals(expectedIsbn, fetchedIsbn);      
        
        //this part adds an existing author to the new book if a new book was successfully created and verifies that the author has been added to said book
        if(postResponse.statusCode() == 201){
            Response authorResponse = authorOperations.getAuthor(GlobVar.dummyAuthorId); 
            String authorName = authorResponse.body().jsonPath().getString("author.name");
            Integer authorId = authorResponse.body().jsonPath().getInt("author.id");
            Integer newBookId = new BookOperations().getLastBook().jsonPath().getInt("book.id");
        
            Response postAuthorToBookResponse = new BookOperations().addAuthorToBook(authorName, authorId, newBookId);
            assertEquals("The status code should be: 200",  200, postAuthorToBookResponse.statusCode());
            assertEquals("response body should be blank", "", postAuthorToBookResponse.body().print());  
        
            Response getAuthorResponse = bookOperations.getBookById(newBookId);
            String verifyAuthorName = getAuthorResponse.jsonPath().getString("book.author.name");
            Integer verifyAuthorId = getAuthorResponse.jsonPath().getInt("book.author.id");
        
            assertEquals(authorName, verifyAuthorName);
            assertEquals(authorId, verifyAuthorId);
            //this part tries to post the same author to the book again which should not work
            Response postAuthorToBookTwiceResponse = new BookOperations().addAuthorToBook(authorName, authorId, newBookId);
            assertEquals("The status code should be: 400",  400, postAuthorToBookTwiceResponse.statusCode());
            assertEquals("response body should be Author is already author of this book.", "Author is already author of this book.", postAuthorToBookTwiceResponse.body().print());
        }
        
        // this bit cleans up the mess if there is a mess to clean up
        if(postResponse.statusCode() == 201){
            Response deleteResponse = new BookOperations().deleteLastBook();
            assertEquals("The status code should be: 204",  204, deleteResponse.statusCode());
            assertEquals("response body should be blank", "", deleteResponse.body().print());
        }
    }
    
    @Test //this test verifies that you cannot post an author to a book without including an authorId, it also verifies that we get the appropriate response statuscode (400) and body message. the new dummybook created is then deleted as part of cleanup
    public void testInvalidPostAuthorToBookWithNoAuthorId(){
        BookOperations bookOperations = new BookOperations();
        AuthorOperations authorOperations = new AuthorOperations();
        
        //this part makes a new book and verifies that it was created)
        Response postResponse = bookOperations.createBookWithInput(GlobVar.secondDummyBookDescription, GlobVar.secondDummyBookIsbn, GlobVar.secondDummyBookNbOfPage, GlobVar.secondDummyBookTitle);
        assertEquals("status code should be 201",  201, postResponse.statusCode());
        assertEquals("response body should be blank", "", postResponse.body().print());
        
        String expectedTitle = from(bookOperations.getLatestJsonString()).getString("book.title");
        String expectedDescription = from(bookOperations.getLatestJsonString()).getString("book.description");
        String expectedIsbn = from(bookOperations.getLatestJsonString()).getString("book.isbn");
        
        Response getResponse = bookOperations.getAllBooks();
        String fetchedTitle = getResponse.jsonPath().getString("books.book[-1].title");
        String fetchedDescription = getResponse.jsonPath().getString("books.book[-1].description");
        String fetchedIsbn = getResponse.jsonPath().getString("books.book[-1].isbn");
        
        assertEquals(expectedTitle, fetchedTitle);
        assertEquals(expectedDescription, fetchedDescription);
        assertEquals(expectedIsbn, fetchedIsbn);      
        
        //this part tries, if a new book was created, adding an author without his authorId to the new book and verifies that this does not work
        if(postResponse.statusCode() == 201){
            Response authorResponse = authorOperations.getAuthor(GlobVar.dummyAuthorId); 
            String authorName = authorResponse.body().jsonPath().getString("author.name");
            Integer newBookId = new BookOperations().getLastBook().jsonPath().getInt("book.id");
        
            Response postAuthorToBookResponse = new BookOperations().invalidAddAuthorToBookWithoutAuthorId(authorName, newBookId);
            assertEquals("The status code should be: 400",  400, postAuthorToBookResponse.statusCode());
            assertEquals("response body should be Author must have id field set.", "Author must have id field set.", postAuthorToBookResponse.body().print());
        }
        // this bit cleans up the mess if there is a mess to clean up
        if(postResponse.statusCode() == 201){
            Response deleteResponse = new BookOperations().deleteLastBook();
            assertEquals("The status code should be: 204",  204, deleteResponse.statusCode());
            assertEquals("response body should be blank", "", deleteResponse.body().print());
        }
    }
    
    
    @Test //this test verifies that you cannot add an author that exists in the system to a book that does not exist in said system, that we get the appropriate response statuscode (404) and body message
    public void testInvalidPostAuthorToBookWithNonxistingBook(){
        BookOperations bookOperations = new BookOperations();
        AuthorOperations authorOperations = new AuthorOperations();    
        
        //this part adds an existing author to a nonexisting book which should not work
        Response authorResponse = authorOperations.getAuthor(GlobVar.dummyAuthorId); 
        String authorName = authorResponse.body().jsonPath().getString("author.name");
        Integer authorId = authorResponse.body().jsonPath().getInt("author.id");
        Integer newBookId = new BookOperations().getLastBook().jsonPath().getInt("book.id") + 1;
        
        Response postAuthorToBookResponse = new BookOperations().addAuthorToBook(authorName, authorId, newBookId);
        assertEquals("The status code should be: 404",  404, postAuthorToBookResponse.statusCode());
        assertEquals("response body should be blank", "", postAuthorToBookResponse.body().print());
    }
    
    
    
    @Test //this test attempts to post a new dummy author to the system, verifies that we get the right response statuscode (201) and a blank response body. it then deletes the new author
    public void testPostAuthor(){
        AuthorOperations authorOperations = new AuthorOperations();
        
        Response postResponse = authorOperations.createAuthor("Another DummyAuthor");
        assertEquals("status code should be 201",  201, postResponse.statusCode());
        assertEquals("response body should be blank", "", postResponse.body().print());
        
        String expectedName = from(authorOperations.getLatestJsonString()).getString("author.name");
        
        Response getResponse = authorOperations.getAllAuthors();
        String fetchedName = getResponse.body().jsonPath().getString("authors.author[-1].name");
        assertEquals(expectedName, fetchedName);
        
        if(postResponse.statusCode() == 201){
            Response delResponse = authorOperations.deleteLastAuthor();
            assertEquals("status code should be 204",  204, delResponse.statusCode());
            assertEquals("response body should be blank", "", delResponse.body().print());
        }
        
    }
    
    @Test //this test attempts to post a new author with a specified authorid that does not exist in the system, verifies that we get the right response statuscode (201) and that the response body is blank, it then deletes the author as part of cleanup
    public void testPostAuthorWithSpecificNewAuthorId(){
        AuthorOperations authorOperations = new AuthorOperations();
        
        Response postResponse = authorOperations.createAuthorWithId("Another DummyAuthor", GlobVar.dummyAuthorId + 1);
        assertEquals("status code should be 201",  201, postResponse.statusCode());
        assertEquals("response body should be blank", "", postResponse.body().print());
        
        String expectedName = from(authorOperations.getLatestJsonString()).getString("author.name");
        Integer expectedId = from(authorOperations.getLatestJsonString()).getInt("author.id");
        
        Response getResponse = authorOperations.getAllAuthors();
        String fetchedAuthorName = getResponse.body().jsonPath().getString("authors.author[-1].name");
        Integer fetchedAuthorId = getResponse.jsonPath().getInt("authors.author[-1].id");
        
        assertEquals(expectedName, fetchedAuthorName);
        assertEquals(expectedId, fetchedAuthorId);
        
        if(postResponse.statusCode() == 201){
            Response delResponse = authorOperations.deleteLastAuthor();
            assertEquals("status code should be 204",  204, delResponse.statusCode());
            assertEquals("response body should be blank", "", delResponse.body().print());
        }
    }
    
    @Test //this test verifies that you cannot post an author to the system using an authorId that already exists, that we get back the right response statuscode (400) and appropriate response body message
    public void testInvalidPostAuthorWithExistingAuthorId(){
        Response postResponse = new AuthorOperations().createAuthorWithId("Another DummyAuthor", GlobVar.dummyAuthorId);
        assertEquals("status code should be 400",  400, postResponse.statusCode());
        assertEquals("response body should be Author was null.", "Author was null.", postResponse.body().print());
    }
    
    
    @Test //this test verifies that we cannot perform a post request to an author's authorId directly, that we get the right response statuscode (405) and response body
    public void testForbiddenPostToAuthorsId(){
        String resourceName = "books/"+GlobVar.dummyAuthorId;
        Response response = given().accept(ContentType.JSON).post(GlobVar.BASE_URL+resourceName);
        assertEquals("The status code should be: 405, method not allowed",  405, response.statusCode());
        assertEquals("response body should be blank", "", response.body().print());
    }
    
    
    
}
