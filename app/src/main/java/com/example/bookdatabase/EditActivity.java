package com.example.bookdatabase;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.media.MediaRecorder.VideoSource.CAMERA;

public class EditActivity extends AppCompatActivity {

    private int bookId;
    private final String fileName = "books-info";

    private int GALLERY = 1;
    private String imagePath;
    private final String noPictureTag = "no-pic";

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

        if (FileTools.Exists(nodes[4], this)) {
            Bitmap imgBitmap = BitmapFactory.decodeFile(FileTools.getAbsolutePath(nodes[4], this));
            ((ImageView) findViewById(R.id.imageView_edit)).setImageBitmap(imgBitmap);
        } else {
            imagePath = noPictureTag;
            ((ImageView) findViewById(R.id.imageView_edit)).setImageResource(R.mipmap.image_book_unknown_foreground);
        }

        ((TextView) findViewById(R.id.content_edit)).setText(nodes[5].trim());
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

        final ImageView image = findViewById(R.id.imageView_edit);
        image.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                OnImageViewClick();
            }
        });
    }

    private void OnImageViewClick() {
        Log.d("BookDatabase", "image clicked");

        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {"Select photo from gallery", "Capture photo from camera" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallery();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    private void choosePhotoFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }

        ImageView imageview = findViewById(R.id.imageView_edit);
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    imagePath = FileTools.saveImage(bitmap, this);
                    Log.d("BookDatabase", "From gallery saved to: " + imagePath);
                    imageview.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else /*if (requestCode == CAMERA)*/ {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            imageview.setImageBitmap(thumbnail);
            imagePath = FileTools.saveImage(thumbnail, this);
            Log.d("BookDatabase", "From camera saved to: " + imagePath);
        }
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

        Log.d("BookDatabase", "Saving:" + newLine);

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

        Log.d("BookDatabase", "Updating:" + newLine);

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

        return bookName + "\t" + authorName + "\t" + rating + "\t" + isRead + "\t"
                + imagePath + "\t" + (!comment.isEmpty() ? comment : " ");
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

