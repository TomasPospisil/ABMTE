package com.example.bookdatabase;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BookItemListAdapter extends ArrayAdapter {

    private final Activity activity;
    private final int[] imageIds;
    private final String[] bookNames;
    private final String[] authorNames;

    private boolean deleteMode = false;

    public BookItemListAdapter(Activity activity, int[] imageIds, String[] bookNames, String[] authorNames) {

        super(activity, R.layout.book_item, bookNames);

        this.activity = activity;
        this.imageIds = imageIds;
        this.bookNames = bookNames;
        this.authorNames = authorNames;

        LayoutInflater inflater = activity.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.book_item, null,true);
        LinearLayout item = (LinearLayout) rowView.findViewById(R.id.mainBookItem);

        item.setOnLongClickListener(pressHoldListener);
        //cb.setOnTouchListener(pressTouchListener);
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.book_item, null,true);

        ImageView imageIdsField = (ImageView) rowView.findViewById(R.id.img_book);
        TextView bookNamesField = (TextView) rowView.findViewById(R.id.textView_book_name);
        TextView authorNamesField = (TextView) rowView.findViewById(R.id.textView_book_author);

        imageIdsField.setImageResource(imageIds[position]);
        bookNamesField.setText(bookNames[position]);
        authorNamesField.setText(authorNames[position]);

        return rowView;
    }

    private View.OnLongClickListener pressHoldListener = new View.OnLongClickListener() {

        @Override
        public boolean onLongClick(View pView) {
            /*LayoutInflater inflater = activity.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.book_item, null,true);
            CheckBox cb = (CheckBox) rowView.findViewById(R.id.checkBox_to_delete);

            // Do something when your hold starts here.
            deleteMode = true;
            //mFrameLayout.setBackgroundResource(R.drawable.trash_can);

            cb.setVisibility(View.INVISIBLE);*/
            Log.d("BookDatabase", "LONG CLICKED");
            return true;
        }
    };
}
