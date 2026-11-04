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

public class ReturnBookActivity extends AppCompatActivity {

    private Spinner spinnerBooks;
    private LinearLayout layoutBookDetails;
    private TextView tvDetailAuthor;
    private TextView tvDetailBorrowed;
    private TextView tvNoBooksMsg;
    private Button btnReturn;

    private DatabaseHelper dbHelper;
    private List<Book> bookList;
    private Book selectedBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_book);

        spinnerBooks = findViewById(R.id.spinnerBooks);
        layoutBookDetails = findViewById(R.id.layoutBookDetails);
        tvDetailAuthor = findViewById(R.id.tvDetailAuthor);
        tvDetailBorrowed = findViewById(R.id.tvDetailBorrowed);
        tvNoBooksMsg = findViewById(R.id.tvNoBooksMsg);
        btnReturn = findViewById(R.id.btnReturn);

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

        btnReturn.setOnClickListener(v -> performReturn());
    }

    private void loadBooksSpinner() {
        bookList = dbHelper.getBorrowedBooks();

        if (bookList.isEmpty()) {
            spinnerBooks.setVisibility(View.GONE);
            layoutBookDetails.setVisibility(View.GONE);
            tvNoBooksMsg.setVisibility(View.VISIBLE);
            btnReturn.setEnabled(false);
        } else {
            spinnerBooks.setVisibility(View.VISIBLE);
            tvNoBooksMsg.setVisibility(View.GONE);
            btnReturn.setEnabled(true);

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
            tvDetailBorrowed.setText("Currently Borrowed: " + book.getBorrowedQuantity() + " / " + book.getTotalQuantity());
        }
    }

    private void performReturn() {
        if (selectedBook == null) {
            Toast.makeText(this, "Please select a book", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedBook.getAvailableQuantity() >= selectedBook.getTotalQuantity()) {
            Toast.makeText(this, "Cannot return: All copies of this book are already in the library!", Toast.LENGTH_LONG).show();
            return;
        }

        boolean success = dbHelper.returnBook(selectedBook.getId());
        if (success) {
            Toast.makeText(this, "Book returned successfully!", Toast.LENGTH_SHORT).show();
            // Reload updated book list
            loadBooksSpinner();
        } else {
            Toast.makeText(this, "Failed to return book. Try again.", Toast.LENGTH_SHORT).show();
        }
    }
}
