package com.example.bookdatabase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private BookItemListAdapter bookItemListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        AddListeners();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

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
        String booksInfoFile = "books-info";
        if (!FileTools.Exists(booksInfoFile, this)) {
            FileTools.CreateFile(booksInfoFile, this);
        }

        List<String> books = FileTools.GetLinesFromFile(booksInfoFile, this);

        String bookNames[] = new String[books.size()];
        String bookAuthors[] = new String[books.size()];
        float ratings[] = new float[books.size()];
        String bookImages[] = new String[books.size()];

        for (int i = 0; i < books.size(); ++i) {
            String[] nodes = books.get(i).split("\\t");
            bookNames[i] = nodes[0];
            bookAuthors[i] = nodes[1];
            ratings[i] = Float.valueOf(nodes[2]);
            bookImages[i] = nodes[4];
        }

        bookItemListAdapter = new BookItemListAdapter(this, bookImages, bookNames, bookAuthors, ratings);
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
