package com.example.bookdatabase;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    String booksInfoFile = "books-info";
    String[] bookArray = {"Book 1","Book 2","Book 3 ","Book 4","Book 5","Book 6" };

    String[] authorArray = {
            "John Doe",
            "Mellisa Doe",
            "Rupert Marty",
            "Ho Yin",
            "Harold & Kummar",
            "John Doe"
    };

    int[] imageArray = {R.mipmap.image_book_unknown_foreground,
            R.mipmap.image_book_unknown_foreground,
            R.mipmap.image_book_unknown_foreground,
            R.mipmap.image_book_unknown_foreground,
            R.mipmap.image_book_unknown_foreground,
            R.mipmap.image_book_unknown_foreground};

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        AppendFAB();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        CreateBooksView();

    }

    private void AppendFAB() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setImageResource(android.R.drawable.ic_input_add);
        fab.setColorFilter(Color.WHITE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void CreateBooksView() {
        File file = new File(getFilesDir(), booksInfoFile);
        //WriteFile();
        List<String> books = GetBooks();

        String bookNames[] = new String[books.size()];
        String bookAuthors[] = new String[books.size()];
        int bookImages[] = new int[books.size()];

        for (int i = 0; i < books.size(); ++i) {
            String[] nodes = books.get(i).split("\\t");
            bookNames[i] = nodes[0];
            bookAuthors[i] = nodes[1];
            bookImages[i] = R.mipmap.image_book_unknown_foreground;
        }

        if (!file.exists()) {
            CreateFile(file);
            return;
        }

        BookItemListAdapter x = new BookItemListAdapter(this, imageArray, bookArray, authorArray);
        //BookItemListAdapter x = new BookItemListAdapter(this, bookImages, bookNames, bookAuthors);
        listView = findViewById(R.id.list_books);
        listView.setAdapter(x);
    }

    private List<String> GetBooks() {
        List<String> books = new ArrayList<>();

        try {
            FileInputStream fileInputStream = new FileInputStream (new File(getFilesDir(), booksInfoFile));
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String line;
            while ( (line = bufferedReader.readLine()) != null )
            {
                books.add(line);
            }
            fileInputStream.close();
            bufferedReader.close();
        }
        catch(Exception e) {
            Log.d(getResources().getString(R.string.app_name), e.getMessage());
        }
        return books;
    }

    private void WriteFile() {

        FileOutputStream outputStream;
        String[] data = new String[] {
                "Book Name 1 \tAuthor Name 1\r\n",
                "Book Name 2 \tAuthor Name 2\r\n",
                "Book Name 3 \tAuthor Name 3\r\n",
                "Book Name 4 \tAuthor Name 4\r\n",
                "Book Name 5 \tAuthor Name 5\r\n"};
        try {
            outputStream = openFileOutput(booksInfoFile, Context.MODE_APPEND);
            for (String s : data) {
                outputStream.write(s.getBytes());
            }
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void CreateFile(File file) {
        try {
            file.createNewFile();
        } catch (Exception e) {
            Log.d(getResources().getString(R.string.app_name), e.getMessage());
        }
    }
}
