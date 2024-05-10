package com.example.appstart;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.HashSet;

public class NoteEditorActivity extends AppCompatActivity {
    int noteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        EditText editText = findViewById(R.id.editText);

        // Fetch data that is passed from MainActivity
        Intent intent = getIntent();

        // Accessing the data using key and value
        noteId = intent.getIntExtra("noteId", -1);
        if (noteId != -1) {
            editText.setText(MainActivity.notes.get(noteId));
        } else {

            MainActivity.notes.add("");
            noteId = MainActivity.notes.size() - 1;
            MainActivity.arrayAdapter.notifyDataSetChanged();

        }

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // add your code here
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                MainActivity.notes.set(noteId, String.valueOf(charSequence));
                MainActivity.arrayAdapter.notifyDataSetChanged();
                // Creating Object of SharedPreferences to store data in the phone
                // Save to Firestore
                String noteText = String.valueOf(charSequence);
                db.collection("notes").document("note_" + noteId)
                        .set(new HashMap<String, Object>() {{
                            put("text", noteText);
                        }})
                        .addOnSuccessListener(aVoid -> {
                            // Note saved successfully

                            Utils.showToast(NoteEditorActivity.this,"successfuly added");
                        })
                        .addOnFailureListener(e -> {
                            // Handle failure
                            Utils.showToast(NoteEditorActivity.this,"Error saving note: ");
                        });
            }
               /* SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);
                HashSet<String> set = new HashSet(MainActivity.notes);
                sharedPreferences.edit().putStringSet("notes", set).apply();
            }*/

            @Override
            public void afterTextChanged(Editable editable) {
                // add your code here
            }
        });
        // Find the Validation button by its ID
        Button validationButton = findViewById(R.id.validation);

        // Set an OnClickListener for the Validation button
        validationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace this with your main activity class
                Intent intent = new Intent(NoteEditorActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
