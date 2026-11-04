package com.example.exam;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Library.db";
    private static final int DATABASE_VERSION = 1;

    // Table Name
    public static final String TABLE_BOOKS = "books";

    // Table Columns
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_BOOK_NAME = "book_name";
    public static final String COLUMN_AUTHOR_NAME = "author_name";
    public static final String COLUMN_TOTAL_QUANTITY = "total_quantity";
    public static final String COLUMN_AVAILABLE_QUANTITY = "available_quantity";

    // Create Table Query
    private static final String CREATE_TABLE_BOOKS = "CREATE TABLE " + TABLE_BOOKS + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_BOOK_NAME + " TEXT, "
            + COLUMN_AUTHOR_NAME + " TEXT, "
            + COLUMN_TOTAL_QUANTITY + " INTEGER, "
            + COLUMN_AVAILABLE_QUANTITY + " INTEGER" + ");";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_BOOKS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKS);
        onCreate(db);
    }

    // Add a new book to the database
    public boolean addBook(String bookName, String authorName, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_BOOK_NAME, bookName);
        values.put(COLUMN_AUTHOR_NAME, authorName);
        values.put(COLUMN_TOTAL_QUANTITY, quantity);
        values.put(COLUMN_AVAILABLE_QUANTITY, quantity);

        long result = db.insert(TABLE_BOOKS, null, values);
        db.close();
        return result != -1;
    }

    // Get all books from the database
    public List<Book> getAllBooks() {
        List<Book> bookList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_BOOKS;
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOK_NAME));
                String author = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_AUTHOR_NAME));
                int totalQty = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TOTAL_QUANTITY));
                int availableQty = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_AVAILABLE_QUANTITY));

                Book book = new Book(id, name, author, totalQty, availableQty);
                bookList.add(book);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return bookList;
    }

    // Get only books that have available copies to borrow (available_quantity > 0)
    public List<Book> getAvailableBooks() {
        List<Book> bookList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_BOOKS + " WHERE " + COLUMN_AVAILABLE_QUANTITY + " > 0";
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOK_NAME));
                String author = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_AUTHOR_NAME));
                int totalQty = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TOTAL_QUANTITY));
                int availableQty = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_AVAILABLE_QUANTITY));

                Book book = new Book(id, name, author, totalQty, availableQty);
                bookList.add(book);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return bookList;
    }

    // Get only books that currently have borrowed copies (available_quantity < total_quantity)
    public List<Book> getBorrowedBooks() {
        List<Book> bookList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_BOOKS + " WHERE " + COLUMN_AVAILABLE_QUANTITY + " < " + COLUMN_TOTAL_QUANTITY;
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOK_NAME));
                String author = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_AUTHOR_NAME));
                int totalQty = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TOTAL_QUANTITY));
                int availableQty = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_AVAILABLE_QUANTITY));

                Book book = new Book(id, name, author, totalQty, availableQty);
                bookList.add(book);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return bookList;
    }

    // Get a specific book by ID
    public Book getBookById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_BOOKS,
                new String[]{COLUMN_ID, COLUMN_BOOK_NAME, COLUMN_AUTHOR_NAME, COLUMN_TOTAL_QUANTITY, COLUMN_AVAILABLE_QUANTITY},
                COLUMN_ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null);

        Book book = null;
        if (cursor != null && cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOK_NAME));
            String author = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_AUTHOR_NAME));
            int totalQty = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TOTAL_QUANTITY));
            int availableQty = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_AVAILABLE_QUANTITY));

            book = new Book(id, name, author, totalQty, availableQty);
            cursor.close();
        }
        db.close();
        return book;
    }

    // Get total number of books across all titles (Sum of total_quantity)
    public int getTotalBooksCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT SUM(" + COLUMN_TOTAL_QUANTITY + ") FROM " + TABLE_BOOKS;
        Cursor cursor = db.rawQuery(query, null);
        int total = 0;
        if (cursor.moveToFirst()) {
            total = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return total;
    }

    // Get total number of available books (Sum of available_quantity)
    public int getAvailableBooksCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT SUM(" + COLUMN_AVAILABLE_QUANTITY + ") FROM " + TABLE_BOOKS;
        Cursor cursor = db.rawQuery(query, null);
        int available = 0;
        if (cursor.moveToFirst()) {
            available = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return available;
    }

    // Get total number of borrowed books (Total Books - Available Books)
    public int getBorrowedBooksCount() {
        return getTotalBooksCount() - getAvailableBooksCount();
    }

    // Borrow a book (decrease available_quantity by 1)
    public boolean borrowBook(int bookId) {
        Book book = getBookById(bookId);
        if (book != null && book.getAvailableQuantity() > 0) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_AVAILABLE_QUANTITY, book.getAvailableQuantity() - 1);

            int rowsAffected = db.update(TABLE_BOOKS, values, COLUMN_ID + "=?", new String[]{String.valueOf(bookId)});
            db.close();
            return rowsAffected > 0;
        }
        return false;
    }

    // Return a book (increase available_quantity by 1)
    public boolean returnBook(int bookId) {
        Book book = getBookById(bookId);
        if (book != null && book.getAvailableQuantity() < book.getTotalQuantity()) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_AVAILABLE_QUANTITY, book.getAvailableQuantity() + 1);

            int rowsAffected = db.update(TABLE_BOOKS, values, COLUMN_ID + "=?", new String[]{String.valueOf(bookId)});
            db.close();
            return rowsAffected > 0;
        }
        return false;
    }
}
