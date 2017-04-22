/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nackademin.rest.test;

/**
 *
 * @author testautomatisering
 */
public class GlobVar {
    //this class stores all static variables for relatively easy editing of test data and base url
    public static final String BASE_URL = "http://localhost:8080/librarytest/rest/";
    
    public static Integer dummyBookId;
    public static Integer dummyAuthorId;
    public static String dummyAuthorName = "dummyAuthorName";
    public static String secondDummyAuthorName = "AnotherDummyAuthorName";
    public static Integer thirdDummyAuthorId;
    public static String thirdDummyAuthorName = "AndYetAThirdDummyAuthorName";
    
    public static String dummyBookDescription = "dummyTestDescription";
    public static String dummyBookTitle = "dummyTestTitle";
    public static String dummyBookIsbn = "dummyIsbn";
    public static Integer dummyBookNbOfPage = 7357;
    
    public static String secondDummyBookDescription = "AnotherDummyTestDescription";
    public static String secondDummyBookTitle = "AnotherDummyTestDescription";
    public static String secondDummyBookIsbn = "AnotherDummyIsbn";
    public static Integer secondDummyBookNbOfPage = 123123;
    
    
    
}
