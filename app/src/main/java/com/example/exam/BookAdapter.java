package com.example.exam;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class BookAdapter extends ArrayAdapter<Book> {

    private final Context context;
    private final List<Book> bookList;

    public BookAdapter(Context context, List<Book> bookList) {
        super(context, 0, bookList);
        this.context = context;
        this.bookList = bookList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_book, parent, false);
        }

        Book book = bookList.get(position);

        TextView tvBookId = convertView.findViewById(R.id.tvBookId);
        TextView tvBookName = convertView.findViewById(R.id.tvBookName);
        TextView tvAuthorName = convertView.findViewById(R.id.tvAuthorName);
        TextView tvTotalQuantity = convertView.findViewById(R.id.tvTotalQuantity);
        TextView tvAvailableQuantity = convertView.findViewById(R.id.tvAvailableQuantity);

        if (book != null) {
            tvBookId.setText("ID: #" + book.getId());
            tvBookName.setText(book.getBookName());
            tvAuthorName.setText("Author: " + book.getAuthorName());
            tvTotalQuantity.setText("Total Qty: " + book.getTotalQuantity());
            tvAvailableQuantity.setText("Available: " + book.getAvailableQuantity());
        }

        return convertView;
    }
}
