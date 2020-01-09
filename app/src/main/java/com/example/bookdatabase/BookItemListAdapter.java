package com.example.bookdatabase;

import android.app.Activity;
import android.content.DialogInterface;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import java.lang.*;

public class BookItemListAdapter extends ArrayAdapter {

    public final MainActivity activity;
    public int[] imageIds;
    public String[] bookNames;
    public String[] authorNames;

    public BookItemListAdapter(Activity activity, int[] imageIds, String[] bookNames, String[] authorNames) {

        super(activity, R.layout.book_item, bookNames);

        this.activity = (MainActivity)activity;
        this.imageIds = imageIds;
        this.bookNames = bookNames;
        this.authorNames = authorNames;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        super.registerDataSetObserver(observer);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        super.unregisterDataSetObserver(observer);
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.book_item, null,true);

        ImageView imageIdsField = rowView.findViewById(R.id.img_book);
        TextView bookNamesField = rowView.findViewById(R.id.textView_book_name);
        TextView authorNamesField = rowView.findViewById(R.id.textView_book_author);

        imageIdsField.setImageResource(imageIds[position]);
        bookNamesField.setText(bookNames[position]);
        authorNamesField.setText(authorNames[position]);

        return rowView;
    }

    public void OnShortClickEvent(AdapterView<?> parent, final int position, final long id) {
        String selectedItem = (String) parent.getItemAtPosition(position);
        Log.d("BookDatabase", "clicked from Books: " + selectedItem);

        new AlertDialog.Builder(activity)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("DELETING BOOK")
                .setMessage("Are you sure you want to remove book " + bookNames[(int)id] + "?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d("BookDatabase", "Yes, delete it!");
                        FileTools.RemoveNthLineFromFile("books-info", (int)id, activity);
                        activity.CreateBooksView();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) { }
                })
                .show();
    }

    public boolean OnLongClickEvent(AdapterView<?> parent, int position, long id) {
        String selectedItem = (String) parent.getItemAtPosition(position);
        Log.d("BookDatabase", "clicked from Books: " + selectedItem);
        return true;
    }
}
