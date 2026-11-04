package com.example.exam;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView tvAvailableBooks;
    private TextView tvBorrowedBooks;
    private Button btnViewBooks;
    private Button btnAddBook;
    private Button btnBorrowBook;
    private Button btnReturnBook;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Views
        tvAvailableBooks = findViewById(R.id.tvAvailableBooks);
        tvBorrowedBooks = findViewById(R.id.tvBorrowedBooks);
        btnViewBooks = findViewById(R.id.btnViewBooks);
        btnAddBook = findViewById(R.id.btnAddBook);
        btnBorrowBook = findViewById(R.id.btnBorrowBook);
        btnReturnBook = findViewById(R.id.btnReturnBook);

        // Initialize Database Helper
        dbHelper = new DatabaseHelper(this);

        // Navigation listeners
        btnViewBooks.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ViewBooksActivity.class);
            startActivity(intent);
        });

        btnAddBook.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddBookActivity.class);
            startActivity(intent);
        });

        btnBorrowBook.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, BorrowBookActivity.class);
            startActivity(intent);
        });

        btnReturnBook.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ReturnBookActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateBookCounts();
    }

    private void updateBookCounts() {
        int availableCount = dbHelper.getAvailableBooksCount();
        int borrowedCount = dbHelper.getBorrowedBooksCount();

        tvAvailableBooks.setText(String.valueOf(availableCount));
        tvBorrowedBooks.setText(String.valueOf(borrowedCount));
    }
}
