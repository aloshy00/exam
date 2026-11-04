package com.example.exam;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddBookActivity extends AppCompatActivity {

    private EditText etBookName;
    private EditText etAuthorName;
    private EditText etQuantity;
    private Button btnSaveBook;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        etBookName = findViewById(R.id.etBookName);
        etAuthorName = findViewById(R.id.etAuthorName);
        etQuantity = findViewById(R.id.etQuantity);
        btnSaveBook = findViewById(R.id.btnSaveBook);

        dbHelper = new DatabaseHelper(this);

        btnSaveBook.setOnClickListener(v -> saveBook());
    }

    private void saveBook() {
        String bookName = etBookName.getText().toString().trim();
        String authorName = etAuthorName.getText().toString().trim();
        String quantityStr = etQuantity.getText().toString().trim();

        if (TextUtils.isEmpty(bookName)) {
            Toast.makeText(this, "Please enter book name", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(authorName)) {
            Toast.makeText(this, "Please enter author name", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(quantityStr)) {
            Toast.makeText(this, "Please enter total quantity", Toast.LENGTH_SHORT).show();
            return;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(quantityStr);
            if (quantity <= 0) {
                Toast.makeText(this, "Quantity must be greater than 0", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid quantity number", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isInserted = dbHelper.addBook(bookName, authorName, quantity);
        if (isInserted) {
            Toast.makeText(this, "Book added successfully!", Toast.LENGTH_SHORT).show();
            finish(); // Close activity and return to Home
        } else {
            Toast.makeText(this, "Failed to add book. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }
}
