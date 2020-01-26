package com.example.bookdatabase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    private int bookId;
    private final String fileName = "books-info";
    private final String noPictureTag = "no-pic";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        bookId = getIntent().getIntExtra("id", -1);

        SetListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        FillData(bookId);
    }

    private void FillData(int id) {
        String line = FileTools.GetNthLineFromFile(fileName, id, this);
        String[] nodes = line.split("\\t");

        ((TextView) findViewById(R.id.label_book_name_detail)).setText(nodes[0]);
        ((TextView) findViewById(R.id.authorName_detail)).setText(nodes[1]);
        ((RatingBar) findViewById(R.id.ratingBar)).setRating(Float.valueOf(nodes[2]));
        ((CheckBox) findViewById(R.id.checkBox_detail)).setChecked(Boolean.parseBoolean(nodes[3]));

        try {
            if (FileTools.Exists(nodes[4], this)) {
                Log.d("BookDatabase", "exists: " + nodes[4]);
                Bitmap imgBitmap = BitmapFactory.decodeFile(FileTools.getAbsolutePath(nodes[4], this));
                ((ImageView) findViewById(R.id.imageView_detail)).setImageBitmap(imgBitmap);
            } else {
                Log.d("BookDatabase", "does not exist: " + nodes[4]);
                ((ImageView) findViewById(R.id.imageView_detail)).setImageResource(R.mipmap.image_book_unknown_foreground);
            }
        }
        catch (Exception e) {
            Log.d("BookDatabase", e.getMessage());
            return;
        }


        ((TextView) findViewById(R.id.content_detail)).setText(nodes[5].trim());

        CheckBox read = (findViewById(R.id.checkBox_detail));
        read.setText(read.isChecked() ? R.string.checkbox_label_read : R.string.checkbox_label_not_read);
    }

    private void SetListeners() {
        final Button getBack = findViewById(R.id.button_getBack);
        getBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                OnGetBackButtonClick();
            }
        });

        final Button edit = findViewById(R.id.button_edit);
        edit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                OnEditButtonClick();
            }
        });
    }

    private void OnGetBackButtonClick() {
        finish();
    }

    private void OnEditButtonClick() {
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra("id", bookId);
        startActivity(intent);
    }
}
