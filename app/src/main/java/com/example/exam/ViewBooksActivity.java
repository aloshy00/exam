package com.example.exam;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class ViewBooksActivity extends AppCompatActivity {

    private ListView listViewBooks;
    private TextView tvEmpty;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_books);

        listViewBooks = findViewById(R.id.listViewBooks);
        tvEmpty = findViewById(R.id.tvEmpty);
        dbHelper = new DatabaseHelper(this);

        loadBooks();
    }

    private void loadBooks() {
        List<Book> bookList = dbHelper.getAllBooks();

        if (bookList.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            listViewBooks.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            listViewBooks.setVisibility(View.VISIBLE);
            BookAdapter adapter = new BookAdapter(this, bookList);
            listViewBooks.setAdapter(adapter);
        }
    }
}
