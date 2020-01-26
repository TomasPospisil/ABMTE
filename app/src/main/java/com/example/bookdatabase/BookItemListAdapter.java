package com.example.bookdatabase;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import java.lang.*;

public class BookItemListAdapter extends ArrayAdapter {

    public final MainActivity activity;
    public String[] images;
    public String[] bookNames;
    public String[] authorNames;
    public float[] ratings;

    private final String noPictureTag = "no-pic";

    public BookItemListAdapter(Activity activity, String[] images, String[] bookNames,
                               String[] authorNames, float[] ratings) {

        super(activity, R.layout.book_item, bookNames);

        this.activity = (MainActivity)activity;
        this.images = images;
        this.bookNames = bookNames;
        this.authorNames = authorNames;
        this.ratings = ratings;
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
        RatingBar ratingBarField = rowView.findViewById(R.id.ratingBar_listView);

        if (FileTools.Exists(images[position], activity)) {
            Bitmap imgBitmap = BitmapFactory.decodeFile(FileTools.getAbsolutePath(images[position], activity));
            imageIdsField.setImageBitmap(imgBitmap);
        } else {
            imageIdsField.setImageResource(R.mipmap.image_book_unknown_foreground);
        }

        bookNamesField.setText(bookNames[position]);
        authorNamesField.setText(authorNames[position]);
        ratingBarField.setRating(ratings[position]);

        return rowView;
    }

    public void OnShortClickEvent(AdapterView<?> parent, final int position, final long id) {
        Intent intent = new Intent(activity, DetailActivity.class);
        intent.putExtra("id", (int)id);
        activity.startActivity(intent);
    }

    public boolean OnLongClickEvent(AdapterView<?> parent, int position, final long id) {
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
        return true;
    }
}
