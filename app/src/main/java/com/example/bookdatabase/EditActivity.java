package com.example.bookdatabase;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class EditActivity extends AppCompatActivity {

    private int bookId;
    private final String fileName = "books-info";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        bookId = getIntent().getIntExtra("id", -1);

        SetListeners();

        if (bookId == -1) {
            return;
        }

        FillData(bookId);
    }

    private void FillData(int id) {
        String line = FileTools.GetNthLineFromFile(fileName, id, this);
        String[] nodes = line.split("\\t");

        ((TextView) findViewById(R.id.label_book_name_edit)).setText(nodes[0]);
        ((TextView) findViewById(R.id.authorName_edit)).setText(nodes[1]);
        ((RatingBar) findViewById(R.id.ratingBar_edit)).setRating(Float.valueOf(nodes[2]));
        ((CheckBox) findViewById(R.id.checkBox_edit)).setChecked(Boolean.parseBoolean(nodes[3]));
        ((TextView) findViewById(R.id.content_edit)).setText(nodes[4].trim());
        ((ImageView) findViewById(R.id.imageView_edit)).setImageResource(R.mipmap.image_book_unknown_foreground);
    }

    private void SetListeners() {
        final Button cancel = findViewById(R.id.button_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                OnCancelButtonClick();
            }
        });

        final Button save = findViewById(R.id.button_save);
        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                OnSaveButtonClick();
            }
        });
    }

    private void OnCancelButtonClick() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Cancel operation?")
                .setMessage("Are you sure you want to cancel operation?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) { }
                })
                .show();
    }

    private void OnSaveButtonClick() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Save book?")
                .setMessage("Are you sure you want to save the book?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (bookId == -1) {
                            SaveNewBook();
                        } else {
                            UpdateBook();
                        }
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) { }
                })
                .show();
    }

    private void SaveNewBook() {
        String newLine = getLineForSavingToFile();
        if (newLine == null) {
            return;
        }

        List<String> line = new ArrayList<>();
        line.add(newLine);

        FileTools.AppendLinesToFile(fileName, line,this);
        finish();
    }

    private void UpdateBook() {
        String newLine = getLineForSavingToFile();
        if (newLine == null) {
            return;
        }

        List<String> backup = FileTools.GetLinesFromFile(fileName, this);
        List<String> actual = FileTools.GetLinesFromFile(fileName, this);
        actual.set(bookId, newLine);

        FileTools.RemoveFile(fileName, this);

        if (!FileTools.AppendLinesToFile(fileName, actual, this)) {
            FileTools.AppendLinesToFile(fileName, backup, this);
            ShowErrorDialog("An unexpected error occurred during saving book.");
        }
        finish();

    }

    private String getLineForSavingToFile() {
        String bookName = ((TextView) findViewById(R.id.label_book_name_edit)).getText().toString();
        String authorName = ((TextView) findViewById(R.id.authorName_edit)).getText().toString();
        String rating = String.valueOf(((RatingBar)findViewById(R.id.ratingBar_edit)).getRating());
        String isRead = Boolean.toString(((CheckBox) findViewById(R.id.checkBox_edit)).isChecked());
        String comment = ((TextView) findViewById(R.id.content_edit)).getText().toString();

        if (bookName.trim().isEmpty() || authorName.trim().isEmpty()) {
            ShowErrorDialog("Book or author is empty!");
            return null;
        }

        return bookName + "\t" + authorName + "\t" + rating + "\t" + isRead + "\t" +
                (!comment.isEmpty() ? comment : " ");
    }

    private void ShowErrorDialog(String message) {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Something went wrong :(")
                .setMessage(message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialogInterface, int i) { }
                })
                .show();

    }
}

