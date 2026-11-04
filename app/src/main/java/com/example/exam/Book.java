package com.example.exam;

public class Book {
    private int id;
    private String bookName;
    private String authorName;
    private int totalQuantity;
    private int availableQuantity;

    public Book(int id, String bookName, String authorName, int totalQuantity, int availableQuantity) {
        this.id = id;
        this.bookName = bookName;
        this.authorName = authorName;
        this.totalQuantity = totalQuantity;
        this.availableQuantity = availableQuantity;
    }

    public Book(String bookName, String authorName, int totalQuantity, int availableQuantity) {
        this.bookName = bookName;
        this.authorName = authorName;
        this.totalQuantity = totalQuantity;
        this.availableQuantity = availableQuantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(int availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public int getBorrowedQuantity() {
        return totalQuantity - availableQuantity;
    }

    @Override
    public String toString() {
        return bookName + " by " + authorName + " (Available: " + availableQuantity + "/" + totalQuantity + ")";
    }
}
