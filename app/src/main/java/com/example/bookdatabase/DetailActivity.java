package com.example.bookdatabase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        bookId = getIntent().getIntExtra("id", -1);

        SetListeners();
        //FillData(bookId);
    }

    @Override
    protected void onResume() {
        Log.d("BookDatabase", "onresume");
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
        ((TextView) findViewById(R.id.content_detail)).setText(nodes[4]);
        ((ImageView) findViewById(R.id.imageView_detail)).setImageResource(R.mipmap.image_book_unknown_foreground);

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
