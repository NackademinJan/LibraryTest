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

/**
 *
 * @author testautomatisering
 */
public class AuthorOperations {
    private String jsonString = "";
    
    
    public Response getAllAuthors(){
        String resourceName = "authors";
        Response getResponse = given().accept(ContentType.JSON).get(GlobVar.BASE_URL + resourceName);
        return getResponse;
    }
    public Response getAuthor(int id){
        String resourceName = "authors/"+id;
        Response response = given().accept(ContentType.JSON).get(GlobVar.BASE_URL+resourceName);
        return response;
    }
   
    public Response createAuthor(String name){
        String resourceName = "authors";
        
        
        
        String postBodyTemplate = 
                        "{\n" 
                    +   "\"author\":\n" 
                    +   "  {\n" 
                    +   "    \"name\":\"%s\",\n" 
                    +   "  }\n" 
                    +   "}";
        String postBody= String.format(postBodyTemplate, name);
        jsonString = postBody;
        Response postResponse = given().contentType(ContentType.JSON).body(postBody).post(GlobVar.BASE_URL + resourceName);
        return postResponse;
    }
    
    
    
    public Response createAuthorWithId(String name, Integer id){
        String resourceName = "authors";
        
        
        
        String postBodyTemplate = 
                        "{\n" 
                    +   "\"author\":\n" 
                    +   "  {\n" 
                    +   "    \"name\":\"%s\",\n" 
                    +   "    \"id\":%s\n"
                    +   "  }\n" 
                    +   "}";
        String postBody= String.format(postBodyTemplate, name, id);
        jsonString = postBody;
        Response postResponse = given().contentType(ContentType.JSON).body(postBody).post(GlobVar.BASE_URL + resourceName);
        return postResponse;
    }
    
    public Response createRandomAuthor(){
        String resourceName = "authors";
        String name = UUID.randomUUID().toString();
        
        
        String postBodyTemplate = 
                        "{\n" 
                    +   "\"author\":\n" 
                    +   "  {\n" 
                    +   "    \"name\":\"%s\",\n" 
                    +   "  }\n" 
                    +   "}";
        String postBody= String.format(postBodyTemplate, name);
        jsonString = postBody;
        Response postResponse = given().contentType(ContentType.JSON).body(postBody).post(GlobVar.BASE_URL + resourceName);
        return postResponse;
    }
    
    public Response createRandomAuthorWithRandomId(){
        String resourceName = "authors";
        String name = UUID.randomUUID().toString();
        //id blir en slumpvald siffra mellan 500 och 1000 som författarens nya id, borde inte ge problem om man inte har mer än 500 författare i databasen
        Integer id = (int)(Math.random() * ((1000 - 500)- 1));
        
        String postBodyTemplate = 
                        "{\n" 
                    +   "\"author\":\n" 
                    +   "  {\n" 
                    +   "    \"name\":\"%s\",\n" 
                    +   "    \"id\":%s\n" 
                    +   "  }\n" 
                    +   "}";
        String postBody= String.format(postBodyTemplate, name, id);
        jsonString = postBody;
        Response postResponse = given().contentType(ContentType.JSON).body(postBody).post(GlobVar.BASE_URL + resourceName);
        return postResponse;
    }
    
    
    public Response updateAuthor(String authorName, Integer authorId){
        String resourceName = "authors";
        
        
        String putBodyTemplate = 
                        "{\n" 
                    +   "\"author\":\n" 
                    +   "  {\n" 
                    +   "    \"name\":\"%s\",\n" 
                    +   "    \"id\":%s\n" 
                    +   "  }\n" 
                    +   "}";
                
        String putBody= String.format(putBodyTemplate, authorName, authorId);
        jsonString = putBody;
        Response putResponse = given().contentType(ContentType.JSON).body(putBody).put(GlobVar.BASE_URL + resourceName);
        return putResponse;
    }
    // this method should always return status code 400 and should not update an author
    public Response invalidUpdateAuthorWithoutAuthorId(String authorName){
        String resourceName = "authors";
        
        
        String putBodyTemplate = 
                        "{\n" 
                    +   "\"author\":\n" 
                    +   "  {\n" 
                    +   "    \"name\":\"%s\",\n" 
                    +   "  }\n" 
                    +   "}";
                
        String putBody= String.format(putBodyTemplate, authorName);
        jsonString = putBody;
        Response putResponse = given().contentType(ContentType.JSON).body(putBody).put(GlobVar.BASE_URL + resourceName);
        return putResponse;
    }
    
    public Response deleteLastAuthor(){   
        Response getResponse = new AuthorOperations().getAllAuthors();
        int fetchedId = getResponse.jsonPath().getInt("authors.author[-1].id");
        Response deleteResponse = new AuthorOperations().deleteAuthor(fetchedId);
        return deleteResponse;
    }
    
    public Response deleteAuthor(int id){
        String deleteResourceName = "authors/"+id;
        
        Response deleteResponse = delete(GlobVar.BASE_URL + deleteResourceName);
        return deleteResponse;
    }
    
    public String getLatestJsonString(){
        return jsonString;
    }
    
}
