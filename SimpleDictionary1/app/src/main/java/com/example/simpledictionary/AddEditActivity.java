package com.example.simpledictionary;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddEditActivity extends AppCompatActivity {

    EditText editWord, editMeaning;
    Button btnSave;
    TextView txtScreenTitle;
    int position = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

        txtScreenTitle = findViewById(R.id.txtScreenTitle);
        editWord = findViewById(R.id.editWord);
        editMeaning = findViewById(R.id.editMeaning);
        btnSave = findViewById(R.id.btnSave);

        Intent oldIntent = getIntent();

        if (oldIntent.hasExtra("word")) {
            txtScreenTitle.setText("Edit Word");
            editWord.setText(oldIntent.getStringExtra("word"));
            editMeaning.setText(oldIntent.getStringExtra("meaning"));
            position = oldIntent.getIntExtra("position", -1);
        }

        btnSave.setOnClickListener(v -> {
            String word = editWord.getText().toString().trim();
            String meaning = editMeaning.getText().toString().trim();

            if (word.isEmpty() || meaning.isEmpty()) {
                Toast.makeText(this, "Enter word and meaning", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent resultIntent = new Intent();
            resultIntent.putExtra("word", word);
            resultIntent.putExtra("meaning", meaning);
            resultIntent.putExtra("position", position);

            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
}
