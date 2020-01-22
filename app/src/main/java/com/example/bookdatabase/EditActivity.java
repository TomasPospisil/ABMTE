package com.example.bookdatabase;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class EditActivity extends AppCompatActivity {

    private int bookId;
    private final String fileName = "books-info";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("BookDatabase", "got here");
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
                            finish();
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
        String bookName = ((TextView) findViewById(R.id.label_book_name_edit)).getText().toString();
        String authorName = ((TextView) findViewById(R.id.authorName_edit)).getText().toString();

        List<String> line = new ArrayList<>();
        line.add(bookName + " \t" + authorName);

        FileTools.AppendLinesToFile(fileName, line,this);
    }

    private void UpdateBook() {
        String bookName = ((TextView) findViewById(R.id.label_book_name_edit)).getText().toString();
        String authorName = ((TextView) findViewById(R.id.authorName_edit)).getText().toString();

        String line = bookName + "  \t" + authorName;

        List<String> backup = FileTools.GetLinesFromFile(fileName, this);
        List<String> actual = FileTools.GetLinesFromFile(fileName, this);
        actual.set(bookId, line);

        FileTools.RemoveFile(fileName, this);

        if (!FileTools.AppendLinesToFile(fileName, actual, this)) {
            FileTools.AppendLinesToFile(fileName, backup, this);
            ShowErrorDialog();
        } else {
            finish();
        }

    }

    private void ShowErrorDialog() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Something went wrong :(")
                .setMessage("An unexpected error occurred during saving book.")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .show();
    }
}

