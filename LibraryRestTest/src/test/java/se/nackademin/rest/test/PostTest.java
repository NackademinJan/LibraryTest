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
    public static void MaketheMockBookAndMockAuthor(){
        Response makeMocksResponse = BeforeAndAfterOperations.makeMockBookAndMockAuthor();
        assertEquals("The status code should be: 201",  201, makeMocksResponse.statusCode());
        assertEquals("response body should be blank",  "", makeMocksResponse.body().print());
        
        Response addMockAuthorToMockBook = BeforeAndAfterOperations.addMockAuthorToMockBook();
        assertEquals("The status code should be: 200",  200, addMockAuthorToMockBook.statusCode());
        assertEquals("response body should be blank",  "", addMockAuthorToMockBook.body().print());
    }
    
    @AfterClass //this method removes the dummies created by the previous method
    public static void RemovetheMockBookAndMockAuthor(){
        Response removeResponse = BeforeAndAfterOperations.removeTestBookAndTestAuthor();
        assertEquals("The status code should be: 204",  204, removeResponse.statusCode());  
        assertEquals("response body should be blank",  "", removeResponse.body().print());
    }

    
    @Test //This test tries to post a new book to the system and verifies that we get the right responsecode (201), a blank responsebody and then verifies that the new book with included variables are in the system
    public void testPostBook(){
        Book book = new Book();
        book.setDescription(GlobVar.secondMockBookDescription);
        book.setTitle(GlobVar.secondMockBookTitle);
        book.setIsbn(GlobVar.secondMockBookIsbn);
        book.setNbOfPage(GlobVar.secondMockBookNbOfPage);
        SingleBook singleBook = new SingleBook(book);
        
        Response response = given().contentType(ContentType.JSON).body(singleBook).log().all().post(GlobVar.BASE_URL+"books");
        assertEquals("The status code should be: 201",  201, response.statusCode());
        assertEquals("response body should be blank", "", response.body().print());
        
        Book verifyBook = new BookOperations().fetchLastBook();
        assertEquals("The books description should be: " + GlobVar.secondMockBookDescription,  book.getDescription(), verifyBook.getDescription());       
        assertEquals("The books title should be: " + GlobVar.secondMockBookTitle,  book.getTitle(), verifyBook.getTitle());       
        assertEquals("The books isbn should be: " + GlobVar.secondMockBookIsbn,  book.getIsbn(), verifyBook.getIsbn());       
        assertEquals("The books page count should be: " + GlobVar.secondMockBookNbOfPage,  book.getNbOfPage(), verifyBook.getNbOfPage());       
        
        if(response.statusCode() == 201){
            Response deleteResponse = new BookOperations().deleteLastBook();
            assertEquals("The status code should be: 204",  204, deleteResponse.statusCode());
            assertEquals("response body should be blank", "", deleteResponse.body().print());
        }
        
    }
    
    @Test //this test does not use the book class but is otherwise equivalent to testPostBookWithSpecificNewId and was written mainly to see if the api woulld allow calls of the text format that "BookOperations().createBookWithInputAndId" uses to be posted.
    public void testPostBookWithSpecificNewIdWithoutBookClass(){
        Response getResponse = new BookOperations().getAllBooks();
        Integer newId = GlobVar.mockBookId + 1;
        Response response = new BookOperations().createBookWithInputAndId(newId, GlobVar.secondMockBookDescription, GlobVar.secondMockBookIsbn, GlobVar.secondMockBookNbOfPage, GlobVar.secondMockBookTitle);
        
        assertEquals("The status code should be: 201",  201, response.statusCode());
        assertEquals("response body should be blank", "", response.body().print());
        
        Book verifyBook = new BookOperations().fetchLastBook();
        assertEquals("The books description should be: " + GlobVar.secondMockBookDescription,  GlobVar.secondMockBookDescription, verifyBook.getDescription());       
        assertEquals("The books title should be: " + GlobVar.secondMockBookTitle,  GlobVar.secondMockBookTitle, verifyBook.getTitle());       
        assertEquals("The books isbn should be: " + GlobVar.secondMockBookIsbn,  GlobVar.secondMockBookIsbn, verifyBook.getIsbn());       
        assertEquals("The books page count should be: " + GlobVar.secondMockBookNbOfPage,  GlobVar.secondMockBookNbOfPage, verifyBook.getNbOfPage());       
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
        book.setDescription(GlobVar.secondMockBookDescription);
        book.setTitle(GlobVar.secondMockBookTitle);
        book.setIsbn(GlobVar.secondMockBookIsbn);
        book.setNbOfPage(GlobVar.secondMockBookNbOfPage);
        book.setId(newId);
        SingleBook singleBook = new SingleBook(book);
        
        Response response = given().contentType(ContentType.JSON).body(singleBook).log().all().post(GlobVar.BASE_URL+"books");
        assertEquals("The status code should be: 201",  201, response.statusCode());
        assertEquals("response body should be blank", "", response.body().print());
        
        Book verifyBook = new BookOperations().fetchLastBook();
        assertEquals("The books description should be: " + GlobVar.secondMockBookDescription,  book.getDescription(), verifyBook.getDescription());       
        assertEquals("The books title should be: " + GlobVar.secondMockBookTitle,  book.getTitle(), verifyBook.getTitle());       
        assertEquals("The books isbn should be: " + GlobVar.secondMockBookIsbn,  book.getIsbn(), verifyBook.getIsbn());       
        assertEquals("The books page count should be: " + GlobVar.secondMockBookNbOfPage,  book.getNbOfPage(), verifyBook.getNbOfPage());       
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
        book.setDescription(GlobVar.secondMockBookDescription);
        book.setTitle(GlobVar.secondMockBookTitle);
        book.setIsbn(GlobVar.secondMockBookIsbn);
        book.setNbOfPage(GlobVar.secondMockBookNbOfPage);
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
        book.setDescription(GlobVar.secondMockBookDescription);
        book.setTitle(GlobVar.secondMockBookTitle);
        book.setIsbn(GlobVar.secondMockBookIsbn);
        book.setNbOfPage(GlobVar.secondMockBookNbOfPage);
        book.setAuthor(authorResponse);
        SingleBook singleBook = new SingleBook(book);
        
        Response response = given().contentType(ContentType.JSON).body(singleBook).log().all().post(GlobVar.BASE_URL+"books").prettyPeek();
        assertEquals("The status code should be: 201",  201, response.statusCode());
        assertEquals("response body should be blank", "", response.body().prettyPrint());
    
        Book verifyBook = new BookOperations().fetchLastBook();
         assertEquals("The books description should be: " + GlobVar.secondMockBookDescription,  book.getDescription(), verifyBook.getDescription());       
        assertEquals("The books title should be: " + GlobVar.secondMockBookTitle,  book.getTitle(), verifyBook.getTitle());       
        assertEquals("The books isbn should be: " + GlobVar.secondMockBookIsbn,  book.getIsbn(), verifyBook.getIsbn());       
        assertEquals("The books page count should be: " + GlobVar.secondMockBookNbOfPage,  book.getNbOfPage(), verifyBook.getNbOfPage());       
        // this one needs work! assertEquals("The books author should be: " + book.getAuthor(),  book.getAuthor(), verifyBook.getAuthor());       
    
        assertEquals("The books description should be: " + GlobVar.secondMockBookDescription,  book.getDescription(), verifyBook.getDescription());       
        assertEquals("The books title should be: " + GlobVar.secondMockBookTitle,  book.getTitle(), verifyBook.getTitle());       
        assertEquals("The books isbn should be: " + GlobVar.secondMockBookIsbn,  book.getIsbn(), verifyBook.getIsbn());       
        assertEquals("The books page count should be: " + GlobVar.secondMockBookNbOfPage,  book.getNbOfPage(), verifyBook.getNbOfPage());       
    
        Response deleteResponse = new BookOperations().deleteLastBook();
        assertEquals("The status code should be: 204",  204, deleteResponse.statusCode());
    }
   */
    
    @Test //this test verifies that trying to post a book with an author but no authorId returns the right statuscode (400) and appropriate response body message
    public void testInvalidPostBookWithAuthorNameButNoAuthorId(){
        Response authorResponse = new AuthorOperations().getAuthor(GlobVar.mockAuthorId); 
        String authorName = authorResponse.body().jsonPath().getString("author.name");
        Response postResponse = new BookOperations().invalidCreateBookWithAuthorNameButNoAuthorId(GlobVar.secondMockBookDescription, GlobVar.secondMockBookIsbn, GlobVar.secondMockBookNbOfPage, GlobVar.secondMockBookTitle, authorName);
        assertEquals("The status code should be: 400",  400, postResponse.statusCode());
        assertEquals("response body should be Book contained an author with no id field set.",  "Book contained an author with no id field set.", postResponse.body().print());
    }
    
    @Test //this test verifies that trying to post a new book with an authorId that does not already exists in the system returns the right statuscode (400) and appropriate response body message
    public void testInvalidPostBookWithAuthorNameButWrongAuthorId(){
        Response authorResponse = new AuthorOperations().getAuthor(GlobVar.mockAuthorId); 
        String authorName = authorResponse.body().jsonPath().getString("author.name");
        Integer authorId = authorResponse.body().jsonPath().getInt("author.id") + 1;
        Response postResponse = new BookOperations().createBookWithAuthor(GlobVar.secondMockBookDescription, GlobVar.secondMockBookIsbn, GlobVar.secondMockBookNbOfPage, GlobVar.secondMockBookTitle, authorName, authorId);
        assertEquals("The status code should be: 400",  400, postResponse.statusCode()); 
        assertEquals("response body should be Author does not exist in database.",  "Author does not exist in database.", postResponse.body().print());
    }
    
    @Test //this test verifies that trying to post a new book with a valid authorId but the wrong authorName for that id returns the right statuscode (400) and appropriate response body message
    public void testInvalidPostBookWithAuthorIDButWrongAuthorName(){
        Response authorResponse = new AuthorOperations().getAuthor(GlobVar.mockAuthorId); 
        String authorName = authorResponse.body().jsonPath().getString("author.name") + "blarg"; 
        Integer authorId = authorResponse.body().jsonPath().getInt("author.id");
        Response postResponse = new BookOperations().createBookWithAuthor(GlobVar.secondMockBookDescription, GlobVar.secondMockBookIsbn, GlobVar.secondMockBookNbOfPage, GlobVar.secondMockBookTitle, authorName, authorId);
        assertEquals("The status code should be: 400",  400, postResponse.statusCode()); 
        assertEquals("response body should be Author does not exist in database.",  "Author does not exist in database.", postResponse.body().print());
    }
    
    
    @Test
    public void testForbiddenPostToBooksId(){
        String resourceName = "books/"+GlobVar.mockBookId;
        Response response = given().accept(ContentType.JSON).post(GlobVar.BASE_URL+resourceName);
        assertEquals("The status code should be: 405, method not allowed",  405, response.statusCode());  
        assertEquals("response body should be blank",  "", response.body().print());
    }
    
    
    @Test
    public void testForbiddenPostToBooksByAuthorAuthorId(){
        String resourceName = "books/byauthor/"+GlobVar.mockAuthorId;
        Response response = given().accept(ContentType.JSON).post(GlobVar.BASE_URL+resourceName);
        assertEquals("The status code should be: 405, method not allowed",  405, response.statusCode());
        assertEquals("response body should be blank",  "", response.body().print());
    }
    
    
    @Test
    public void testPostAuthorToBook(){
        BookOperations bookOperations = new BookOperations();
        AuthorOperations authorOperations = new AuthorOperations();
        
        //this part makes a new book and verifies that it was created)
        Response postResponse = bookOperations.createBookWithInput(GlobVar.secondMockBookDescription, GlobVar.secondMockBookIsbn, GlobVar.secondMockBookNbOfPage, GlobVar.secondMockBookTitle);
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
            Response authorResponse = authorOperations.getAuthor(GlobVar.mockAuthorId); 
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
    
    @Test
    public void testInvalidPostAuthorToBookTwice(){
        BookOperations bookOperations = new BookOperations();
        AuthorOperations authorOperations = new AuthorOperations();
        
        //this part makes a new book and verifies that it was created)
        Response postResponse = bookOperations.createBookWithInput(GlobVar.secondMockBookDescription, GlobVar.secondMockBookIsbn, GlobVar.secondMockBookNbOfPage, GlobVar.secondMockBookTitle);
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
            Response authorResponse = authorOperations.getAuthor(GlobVar.mockAuthorId); 
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
    
    @Test
    public void testInvalidPostAuthorToBookWithNoAuthorId(){
        BookOperations bookOperations = new BookOperations();
        AuthorOperations authorOperations = new AuthorOperations();
        
        //this part makes a new book and verifies that it was created)
        Response postResponse = bookOperations.createBookWithInput(GlobVar.secondMockBookDescription, GlobVar.secondMockBookIsbn, GlobVar.secondMockBookNbOfPage, GlobVar.secondMockBookTitle);
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
            Response authorResponse = authorOperations.getAuthor(GlobVar.mockAuthorId); 
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
    
    @Test
    public void testInvalidPostAuthorToBookWithNonxistingBook(){
        BookOperations bookOperations = new BookOperations();
        AuthorOperations authorOperations = new AuthorOperations();    
        
        //this part adds an existing author to a nonexisting book which should not work
        Response authorResponse = authorOperations.getAuthor(GlobVar.mockAuthorId); 
        String authorName = authorResponse.body().jsonPath().getString("author.name");
        Integer authorId = authorResponse.body().jsonPath().getInt("author.id");
        Integer newBookId = new BookOperations().getLastBook().jsonPath().getInt("book.id") + 1;
        
        Response postAuthorToBookResponse = new BookOperations().addAuthorToBook(authorName, authorId, newBookId);
        assertEquals("The status code should be: 404",  404, postAuthorToBookResponse.statusCode());
        assertEquals("response body should be blank", "", postAuthorToBookResponse.body().print());
    }
    
    
    @Test
    public void testPostAuthor(){
        AuthorOperations authorOperations = new AuthorOperations();
        
        Response postResponse = authorOperations.createAuthor("Another MockAuthor");
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
    
    @Test
    public void testPostAuthorWithSpecificNewAuthorId(){
        AuthorOperations authorOperations = new AuthorOperations();
        
        Response postResponse = authorOperations.createAuthorWithId("Another MockAuthor", GlobVar.mockAuthorId + 1);
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
    
    @Test
    public void testPostAuthorWithExistingAuthorId(){
        Response postResponse = new AuthorOperations().createAuthorWithId("Another MockAuthor", GlobVar.mockAuthorId);
        assertEquals("status code should be 400",  400, postResponse.statusCode());
        assertEquals("response body should be Author was null.", "Author was null.", postResponse.body().print());
    }
    
    
    @Test
    public void testForbiddenPostToAuthorsId(){
        String resourceName = "books/"+GlobVar.mockAuthorId;
        Response response = given().accept(ContentType.JSON).post(GlobVar.BASE_URL+resourceName);
        assertEquals("The status code should be: 405, method not allowed",  405, response.statusCode());
        assertEquals("response body should be blank", "", response.body().print());
    }
    
    
}
