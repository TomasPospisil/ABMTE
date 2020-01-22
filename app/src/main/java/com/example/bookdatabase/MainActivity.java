package com.example.bookdatabase;

import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    BookItemListAdapter bookItemListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        AppendFAB();
        AddListeners();
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

    private void AddListeners() {
        final Button addBook = findViewById(R.id.button_addNewBook);
        addBook.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                OnAddBook();
            }
        });
    }

    private void OnAddBook() {
        Intent intent = new Intent(this, EditActivity.class);
        startActivity(intent);
    }

    public void CreateBooksView() {
        Log.d("BookDatabase", "Creating new!");
        String booksInfoFile = "books-info";
        if (!FileTools.Exists(booksInfoFile, this)) {
            FileTools.CreateFile(booksInfoFile, this);
        }

        //FileTools.RemoveFile(booksInfoFile, this);
        //FileTools.WriteTestDataToFile(booksInfoFile, this);
        List<String> books = FileTools.GetLinesFromFile(booksInfoFile, this);

        String bookNames[] = new String[books.size()];
        String bookAuthors[] = new String[books.size()];
        int bookImages[] = new int[books.size()];

        for (int i = 0; i < books.size(); ++i) {
            String[] nodes = books.get(i).split("\\t");
            bookNames[i] = nodes[0];
            bookAuthors[i] = nodes[1];
            bookImages[i] = R.mipmap.image_book_unknown_foreground;
        }

        bookItemListAdapter = new BookItemListAdapter(this, bookImages, bookNames, bookAuthors);
        listView = findViewById(R.id.list_books);
        listView.setAdapter(bookItemListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bookItemListAdapter.OnShortClickEvent(parent, position, id);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return bookItemListAdapter.OnLongClickEvent(parent, position, id);
            }
        });
    }

}
