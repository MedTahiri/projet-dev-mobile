package com.example.projet_dev_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.projet_dev_mobile.module.createNote;
import com.example.projet_dev_mobile.service.ApiService;

public class NoteActivity extends AppCompatActivity {

    EditText Matier, Score;
    Button Enregistere,Retour;

    TextView FullName;

    int profile_id;
    String profile_fullname;

    private static final String API_URL_FOR_CREATE_NOTE = "https://medtahiri.pythonanywhere.com/api/note/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_note);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        FullName = (TextView) findViewById(R.id.FullName);
        Matier = (EditText) findViewById(R.id.Input_Matiere);
        Score = (EditText) findViewById(R.id.Input_Score);

        Enregistere = (Button) findViewById(R.id.Enregistere);
        Retour = (Button) findViewById(R.id.Retour);

        profile_id = getIntent().getIntExtra("profile_id", -1);
        profile_fullname = getIntent().getStringExtra("profile_fullname");

        FullName.setText(profile_fullname);

        Enregistere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNote note = new createNote(profile_id, Matier.getText().toString(), Score.getText().toString(), Integer.parseInt(Score.getText().toString()) >= 10);
                createNewNote(note, profile_id);
            }
        });

        Retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NoteActivity.this, NotesActivity.class);
                intent.putExtra("profile_id",profile_id);
                startActivity(intent);
            }
        });

    }

    public void createNewNote(createNote note, int id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String response = ApiService.makeRequest(API_URL_FOR_CREATE_NOTE + "?id=" + id, "POST", note);
                System.out.println("Response: " + response);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (response != null && !response.isEmpty()) {
                            Intent intent = new Intent(NoteActivity.this, NotesActivity.class);
                            intent.putExtra("profile_id",profile_id);
                            startActivity(intent);
                        } else {
                            Toast.makeText(NoteActivity.this, "Empty or null response from the server.", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        }).start();
    }

}