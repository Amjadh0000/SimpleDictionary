package com.example.simpledictionary;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton fabAdd;
    ArrayList<Word> wordList;
    WordAdapter adapter;

    final int ADD_REQUEST = 1;
    final int EDIT_REQUEST = 2;
    final String KEY_WORDS = "words";
    final String KEY_MEANINGS = "meanings";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        fabAdd = findViewById(R.id.fabAdd);
        wordList = new ArrayList<>();

        if (savedInstanceState != null) {
            ArrayList<String> words = savedInstanceState.getStringArrayList(KEY_WORDS);
            ArrayList<String> meanings = savedInstanceState.getStringArrayList(KEY_MEANINGS);

            if (words != null && meanings != null) {
                for (int i = 0; i < words.size(); i++) {
                    wordList.add(new Word(words.get(i), meanings.get(i)));
                }
            }
        }

        adapter = new WordAdapter(wordList, new WordAdapter.OnWordClickListener() {
            @Override
            public void onItemClick(int position) {
                Word word = wordList.get(position);
                Intent intent = new Intent(MainActivity.this, AddEditActivity.class);
                intent.putExtra("word", word.getWord());
                intent.putExtra("meaning", word.getMeaning());
                intent.putExtra("position", position);
                startActivityForResult(intent, EDIT_REQUEST);
            }

            @Override
            public void onItemLongClick(int position) {
                wordList.remove(position);
                adapter.notifyItemRemoved(position);
                Toast.makeText(MainActivity.this, "A word is deleted", Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEditActivity.class);
            startActivityForResult(intent, ADD_REQUEST);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            String word = data.getStringExtra("word");
            String meaning = data.getStringExtra("meaning");
            int position = data.getIntExtra("position", -1);

            if (requestCode == ADD_REQUEST) {
                wordList.add(new Word(word, meaning));
                adapter.notifyItemInserted(wordList.size() - 1);
                Toast.makeText(this, "A word is added", Toast.LENGTH_SHORT).show();
            } else if (requestCode == EDIT_REQUEST && position != -1) {
                wordList.set(position, new Word(word, meaning));
                adapter.notifyItemChanged(position);
                Toast.makeText(this, "A word is updated", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        ArrayList<String> words = new ArrayList<>();
        ArrayList<String> meanings = new ArrayList<>();

        for (Word item : wordList) {
            words.add(item.getWord());
            meanings.add(item.getMeaning());
        }

        outState.putStringArrayList(KEY_WORDS, words);
        outState.putStringArrayList(KEY_MEANINGS, meanings);
    }
}
