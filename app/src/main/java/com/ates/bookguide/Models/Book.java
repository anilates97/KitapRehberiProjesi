package com.ates.bookguide.Models;

import com.google.firebase.firestore.PropertyName;

import java.io.Serializable;

public class Book implements Serializable {

    @PropertyName("bookName")
    private String bookName;

    @PropertyName("bookDescription")
    private String bookDescription;

    @PropertyName("bookCategory")
    private  String bookCategory;

    @PropertyName("bookImage")
    private  String bookImage;

    public Book(String bookName, String bookDescription, String bookCategory, String bookImage) {
        this.bookName = bookName;
        this.bookDescription = bookDescription;
        this.bookCategory = bookCategory;
        this.bookImage = bookImage;
    }


    public Book(){

    }


    @PropertyName("bookName")
    public String getBookName() {
        return bookName;
    }

    @PropertyName("bookName")
    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    @PropertyName("bookDescription")
    public String getBookDescription() {
        return bookDescription;
    }

    @PropertyName("bookDescription")
    public void setBookDescription(String bookDescription) {
        this.bookDescription = bookDescription;
    }

    @PropertyName("bookCategory")
    public String getBookCategory() {
        return bookCategory;
    }

    @PropertyName("bookCategory")
    public void setBookCategory(String bookCategory) {
        this.bookCategory = bookCategory;
    }

    @PropertyName("bookImage")
    public String getBookImage() {
        return bookImage;
    }

    @PropertyName("bookImage")
    public void setBookImage(String bookImage) {
        this.bookImage = bookImage;
    }

    @Override
    public String toString() {
        return "Book{" +
                "bookName='" + bookName + '\'' +
                ", bookDescription='" + bookDescription + '\'' +
                ", bookCategory='" + bookCategory + '\'' +
                ", bookImage='" + bookImage + '\'' +
                '}';
    }
}
