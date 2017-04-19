/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nackademin.rest.test;

import static com.jayway.restassured.RestAssured.delete;
import static com.jayway.restassured.RestAssured.given;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import java.util.Random;
import java.util.UUID;
import se.nackademin.rest.test.model.Book;
import se.nackademin.rest.test.model.SingleBook;

/**
 *
 * @author testautomatisering
 */
public class BookOperations {
    private String jsonString = "";
    
    
    public Response getAllBooks(){
        String resourceName = "books";
        Response getResponse = given().accept(ContentType.JSON).get(GlobVar.BASE_URL + resourceName);
        return getResponse;
    }
    public Response getBookById(int id){
        String resourceName = "books/"+id;
        Response response = given().accept(ContentType.JSON).get(GlobVar.BASE_URL+resourceName);
        return response;
    }
    
    public Book fetchBookById(int id){
        Book fetchBook = given().accept(ContentType.JSON).get(GlobVar.BASE_URL+"books/"+id).jsonPath().getObject("book", Book.class);
        return  fetchBook;
    }
    
    public Response getBookByAuthor(int id){
        String resourceName = "books/byauthor/"+id;
        Response response = given().accept(ContentType.JSON).get(GlobVar.BASE_URL+resourceName);
        return response;
    }
    public Response getAuthorByBook(int id){
        String resourceName = "books/"+id+"/authors";
        Response response = given().accept(ContentType.JSON).get(GlobVar.BASE_URL+resourceName);
        return response;
    }
    
    public Response addAuthorToBook(String authorName, Integer authorId, Integer bookId){
        String resourceName = "books/"+bookId+"/authors";
        
        
        
        String postBodyTemplate = 
                        "{\n" 
                    +   "\"author\":\n" 
                    +   "  {\n" 
                    +   "    \"name\":\"%s\",\n" 
                    +   "    \"id\":%s\n"
                    +   "  }\n" 
                    +   "}";
        String postBody= String.format(postBodyTemplate, authorName, authorId);
        jsonString = postBody;
        Response postResponse = given().contentType(ContentType.JSON).body(postBody).post(GlobVar.BASE_URL + resourceName);
        return postResponse;
    }
    
    public Response addAuthorToBookWithoutAuthorId(String authorName, Integer bookId){
        String resourceName = "books/"+bookId+"/authors";
         
        String postBodyTemplate = 
                        "{\n" 
                    +   "\"author\":\n" 
                    +   "  {\n" 
                    +   "    \"name\":\"%s\",\n" 
                    +   "  }\n" 
                    +   "}";
        String postBody= String.format(postBodyTemplate, authorName);
        jsonString = postBody;
        Response postResponse = given().contentType(ContentType.JSON).body(postBody).post(GlobVar.BASE_URL + resourceName);
        return postResponse;
    }
    //This class is createBookWithInput using the Book.java class, need to make a author.java class to help handle author objects
    public Response makeBookWithInput(String description, String isbn, Integer nbOfPage, String title){
        Book book = new Book();
        book.setDescription(description);
        book.setTitle(title);
        book.setIsbn(isbn);
        book.setNbOfPage(nbOfPage);
        SingleBook singleBook = new SingleBook(book);
        Response postBookResponse = given().contentType(ContentType.JSON).body(singleBook).post(GlobVar.BASE_URL+"books");
        return postBookResponse;
    }
    public Response createBookWithInput(String description, String isbn, Integer nbOfPage, String title){
        String resourceName = "books";
        String postBodyTemplate = 
                        "{\n" 
                    +   "\"book\":\n" 
                    +   "  {\n" 
                    +   "    \"description\":\"%s\",\n" 
                    +   "    \"isbn\":\"%s\",\n" 
                    +   "    \"nbOfPage\":\"%s\",\n" 
                    +   "    \"title\":\"%s\"\n" 
                    +   "  }\n" 
                    +   "}";
        String postBody= String.format(postBodyTemplate, description, isbn, nbOfPage, title);
        jsonString = postBody;
        Response postResponse = given().contentType(ContentType.JSON).body(postBody).post(GlobVar.BASE_URL + resourceName);
        return postResponse;
    }
    
        public Response createBookWithInputAndId(Integer Id, String description, String isbn, Integer nbOfPage, String title){
        String resourceName = "books";
        String postBodyTemplate = 
                        "{\n" 
                    +   "\"book\":\n" 
                    +   "  {\n" 
                    +   "    \"description\":\"%s\",\n" 
                    +   "    \"isbn\":\"%s\",\n" 
                    +   "    \"nbOfPage\":\"%s\",\n" 
                    +   "    \"title\":\"%s\",\n"
                    +   "    \"id\":%s\n"
                    +   "  }\n" 
                    +   "}";
        String postBody= String.format(postBodyTemplate, description, isbn, nbOfPage, title, Id);
        jsonString = postBody;
        Response postResponse = given().contentType(ContentType.JSON).body(postBody).post(GlobVar.BASE_URL + resourceName);
        return postResponse;
    }
    
    //author must have been created prior to running this method)
    public Response createBookWithAuthor(String description, String isbn, Integer nbOfPage, String title, String authorName, Integer authorId){
        String resourceName = "books";
        
        
        String postBodyTemplate = 
                        "{\n" 
                    +   "\"book\":\n" 
                    +   "  {\n" 
                    +   "    \"description\":\"%s\",\n" 
                    +   "    \"isbn\":\"%s\",\n" 
                    +   "    \"nbOfPage\":\"%s\",\n"
                    +   "    \"title\":\"%s\",\n" 
                    +   "    \"author\":\n" 
                    +   "    {\n" 
                    +   "      \"name\":\"%s\",\n" 
                    +   "      \"id\":%s\n" 
                    +   "    }\n" 
                    +   "  }\n" 
                    +   "}";
                
        String postBody= String.format(postBodyTemplate, description, isbn, nbOfPage, title, authorName, authorId);
        jsonString = postBody;
        Response postResponse = given().contentType(ContentType.JSON).body(postBody).post(GlobVar.BASE_URL + resourceName);
        return postResponse;        
    }
    
    // this method should always produce a response with statuscode 400, it should not successfully post a book
    public Response invalidCreateBookWithAuthorNameButNoAuthorId(String description, String isbn, Integer nbOfPage, String title, String authorName){
        String resourceName = "books";
        
        
        String postBodyTemplate = 
                        "{\n" 
                    +   "\"book\":\n" 
                    +   "  {\n" 
                    +   "    \"description\":\"%s\",\n" 
                    +   "    \"isbn\":\"%s\",\n" 
                    +   "    \"nbOfPage\":\"%s\",\n"
                    +   "    \"title\":\"%s\",\n" 
                    +   "    \"author\":\n" 
                    +   "    {\n" 
                    +   "      \"name\":\"%s\",\n" 
                    +   "    }\n" 
                    +   "  }\n" 
                    +   "}";
                
        String postBody= String.format(postBodyTemplate, description, isbn, nbOfPage, title, authorName);
        jsonString = postBody;
        Response postResponse = given().contentType(ContentType.JSON).body(postBody).post(GlobVar.BASE_URL + resourceName);
        return postResponse;        
    }
    
    public Response createRandomBook(){
        String resourceName = "books";
        String title = UUID.randomUUID().toString();
        String description = UUID.randomUUID().toString();
        String isbn = UUID.randomUUID().toString();
        Integer nbOfPage = new Random().nextInt(500);
        
        String postBodyTemplate = 
                        "{\n" 
                    +   "\"book\":\n" 
                    +   "  {\n" 
                    +   "    \"description\":\"%s\",\n" 
                    +   "    \"isbn\":\"%s\",\n" 
                    +   "    \"nbOfPage\":\"%s\",\n" 
                    +   "    \"title\":\"%s\"\n" 
                    +   "  }\n" 
                    +   "}";
        String postBody= String.format(postBodyTemplate, description, isbn, nbOfPage, title);
        jsonString = postBody;
        Response postResponse = given().contentType(ContentType.JSON).body(postBody).post(GlobVar.BASE_URL + resourceName);
        return postResponse;
    }
    //the below method will not work due to the api not supporting creating  a book with an author and author id regardless if they exist already or not in defiance of documentation
    public Response createRandomBookWithAuthor(String authorName, Integer authorId){
        String resourceName = "books";
        String title = UUID.randomUUID().toString();
        String description = UUID.randomUUID().toString();
        String isbn = UUID.randomUUID().toString();
        String name = authorName;
        Integer id = (int)authorId;
        
        String postBodyTemplate = 
                        "{\n" 
                    +   "\"book\":\n" 
                    +   "  {\n" 
                    +   "    \"author\":\n" 
                    +   "    {\n" 
                    +   "      \"name\":\"%s\",\n" 
                    +   "      \"id\":%s\n" 
                    +   "    }\n" 
                    +   "    \"description\":\"%s\",\n" 
                    +   "    \"isbn\":\"%s\",\n" 
                    +   "    \"nbOfPage\":\"%s\",\n"
                    +   "    \"title\":\"%s\"\n" 
                    +   "  }\n" 
                    +   "}";
                
        String postBody= String.format(postBodyTemplate, description, isbn, new Random().nextInt(500), title, authorName, authorId);
        jsonString = postBody;
        Response postResponse = given().contentType(ContentType.JSON).body(postBody).post(GlobVar.BASE_URL + resourceName);
        return postResponse;        
    }
    
    
    public Response updateBookWithAuthor(Integer bookId, String description, String isbn, Integer nbOfPage, String title, String authorName, Integer authorId){
        String resourceName = "books";
        
        
        String putBodyTemplate = 
                        "{\n" 
                    +   "\"book\":\n" 
                    +   "  {\n" 
                    +   "    \"description\":\"%s\",\n" 
                    +   "    \"isbn\":\"%s\",\n" 
                    +   "    \"nbOfPage\":\"%s\",\n"
                    +   "    \"title\":\"%s\",\n" 
                    +   "    \"id\":%s,\n"                
                    +   "    \"author\":\n" 
                    +   "    {\n" 
                    +   "      \"name\":\"%s\",\n" 
                    +   "      \"id\":%s\n" 
                    +   "    }\n" 
                    +   "  }\n" 
                    +   "}";
                
        String putBody= String.format(putBodyTemplate, description, isbn, nbOfPage, title, bookId, authorName, authorId);
        jsonString = putBody;
        Response putResponse = given().contentType(ContentType.JSON).body(putBody).put(GlobVar.BASE_URL + resourceName);
        return putResponse;        
    }
     // this method should always produce a response with statuscode 400, it should not successfully update a book
    public Response invalidUpdateBookWithAuthorButNoAuthorId(Integer bookId, String description, String isbn, Integer nbOfPage, String title, String authorName){
        String resourceName = "books";
        
        
        String putBodyTemplate = 
                        "{\n" 
                    +   "\"book\":\n" 
                    +   "  {\n" 
                    +   "    \"description\":\"%s\",\n" 
                    +   "    \"isbn\":\"%s\",\n" 
                    +   "    \"nbOfPage\":\"%s\",\n"
                    +   "    \"title\":\"%s\",\n"  
                    +   "    \"author\":\n" 
                    +   "    {\n" 
                    +   "      \"name\":\"%s\",\n" 
                    +   "      \"id\":%s\n" 
                    +   "    }\n" 
                    +   "  }\n" 
                    +   "}";
                
        String putBody= String.format(putBodyTemplate, description, isbn, nbOfPage, title, bookId, authorName);
        jsonString = putBody;
        Response putResponse = given().contentType(ContentType.JSON).body(putBody).put(GlobVar.BASE_URL + resourceName);
        return putResponse;        
    }
    
    public Response updateABooksAuthors(Integer bookId, String authorName, Integer authorId){
        String resourceName = "books/" +bookId+ "/authors";
        
        
        String putBodyTemplate = 
                        "{\n" 
                    +   "    \"authors\": {\n" 
                    +   "        \"author\": {\n" 
                    +   "            \"id\": %s,\n" 
                    +   "            \"name\": \"%s\"\n" 
                    +   "        }\n" 
                    +   "    }\n" 
                    +   "}";
                
        String putBody= String.format(putBodyTemplate, authorId, authorName);
        jsonString = putBody;
        Response putResponse = given().contentType(ContentType.JSON).body(putBody).put(GlobVar.BASE_URL + resourceName);
        return putResponse;        
    }
    
    public Response invalidUpdateABooksAuthors(Integer bookId, String authorName){
        String resourceName = "books/" +bookId+ "/authors";
        
        
        String putBodyTemplate = 
                        "{\n" 
                    +   "    \"authors\": {\n" 
                    +   "        \"author\": {\n" 
                    +   "            \"name\": \"%s\"\n" 
                    +   "        }\n" 
                    +   "    }\n" 
                    +   "}";
                
        String putBody= String.format(putBodyTemplate, authorName);
        jsonString = putBody;
        Response putResponse = given().contentType(ContentType.JSON).body(putBody).put(GlobVar.BASE_URL + resourceName);
        return putResponse;        
    }
    
    public String getLatestJsonString(){
        return jsonString;
    }
    
    public Book fetchLastBook(){
        Response getResponse = new BookOperations().getAllBooks();
        int fetchlastBookId = getResponse.jsonPath().getInt("books.book[-1].id");
        Book fetchBook = given().accept(ContentType.JSON).get(GlobVar.BASE_URL+"books/"+fetchlastBookId).jsonPath().getObject("book", Book.class);
        return  fetchBook;
    }
    
    public Response getLastBook(){
        Response getResponse = new BookOperations().getAllBooks();
        int lastBookId = getResponse.jsonPath().getInt("books.book[-1].id");
        Response lastBookResponse = new BookOperations().getBookById(lastBookId);
        return  lastBookResponse;
    }
    
    public Response deleteLastBook(){   
        Response getResponse = new BookOperations().getAllBooks();
        int deleteLastBookId = getResponse.jsonPath().getInt("books.book[-1].id");
        Response deleteLastBookResponse = new BookOperations().deleteBook(deleteLastBookId);
        return deleteLastBookResponse;
    }
    
    public Response deleteBook(int id){
        String deleteResourceName = "books/"+id;
        
        Response deleteResponse = delete(GlobVar.BASE_URL + deleteResourceName);
        return deleteResponse;
    }
}
