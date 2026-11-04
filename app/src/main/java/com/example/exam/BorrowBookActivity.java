package com.example.exam;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class BorrowBookActivity extends AppCompatActivity {

    private Spinner spinnerBooks;
    private LinearLayout layoutBookDetails;
    private TextView tvDetailAuthor;
    private TextView tvDetailAvailable;
    private TextView tvNoBooksMsg;
    private Button btnBorrow;

    private DatabaseHelper dbHelper;
    private List<Book> bookList;
    private Book selectedBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow_book);

        spinnerBooks = findViewById(R.id.spinnerBooks);
        layoutBookDetails = findViewById(R.id.layoutBookDetails);
        tvDetailAuthor = findViewById(R.id.tvDetailAuthor);
        tvDetailAvailable = findViewById(R.id.tvDetailAvailable);
        tvNoBooksMsg = findViewById(R.id.tvNoBooksMsg);
        btnBorrow = findViewById(R.id.btnBorrow);

        dbHelper = new DatabaseHelper(this);

        loadBooksSpinner();

        spinnerBooks.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (bookList != null && position < bookList.size()) {
                    selectedBook = bookList.get(position);
                    updateBookDetails(selectedBook);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedBook = null;
            }
        });

        btnBorrow.setOnClickListener(v -> performBorrow());
    }

    private void loadBooksSpinner() {
        bookList = dbHelper.getAvailableBooks();

        if (bookList.isEmpty()) {
            spinnerBooks.setVisibility(View.GONE);
            layoutBookDetails.setVisibility(View.GONE);
            tvNoBooksMsg.setVisibility(View.VISIBLE);
            btnBorrow.setEnabled(false);
        } else {
            spinnerBooks.setVisibility(View.VISIBLE);
            tvNoBooksMsg.setVisibility(View.GONE);
            btnBorrow.setEnabled(true);

            ArrayAdapter<Book> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, bookList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerBooks.setAdapter(adapter);

            if (!bookList.isEmpty()) {
                selectedBook = bookList.get(0);
                updateBookDetails(selectedBook);
            }
        }
    }

    private void updateBookDetails(Book book) {
        if (book != null) {
            layoutBookDetails.setVisibility(View.VISIBLE);
            tvDetailAuthor.setText("Author: " + book.getAuthorName());
            tvDetailAvailable.setText("Available Copies: " + book.getAvailableQuantity() + " / " + book.getTotalQuantity());
        }
    }

    private void performBorrow() {
        if (selectedBook == null) {
            Toast.makeText(this, "Please select a book", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedBook.getAvailableQuantity() <= 0) {
            Toast.makeText(this, "Cannot borrow: No copies available for this book!", Toast.LENGTH_LONG).show();
            return;
        }

        boolean success = dbHelper.borrowBook(selectedBook.getId());
        if (success) {
            Toast.makeText(this, "Book borrowed successfully!", Toast.LENGTH_SHORT).show();
            // Reload updated book list
            loadBooksSpinner();
        } else {
            Toast.makeText(this, "Failed to borrow book. Try again.", Toast.LENGTH_SHORT).show();
        }
    }
}
