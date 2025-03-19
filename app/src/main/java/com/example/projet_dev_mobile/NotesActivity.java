package com.example.projet_dev_mobile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.projet_dev_mobile.adapter.NoteAdapter;
import com.example.projet_dev_mobile.module.Note;
import com.example.projet_dev_mobile.module.Profile;
import com.example.projet_dev_mobile.service.ApiService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NotesActivity extends AppCompatActivity {


    ListView listView;
    Button Ajoute,Retour;

    int profileId ;

    private static final String API_URL_FOR_GET_NOTE = "https://medtahiri.pythonanywhere.com/api/notes/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notes);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        listView = (ListView) findViewById(R.id.Listview);
        Ajoute = (Button) findViewById(R.id.Ajoute);
        Retour = (Button) findViewById(R.id.Retour);

        profileId = getIntent().getIntExtra("profile_id",-1);

        getAllNote(profileId);

        Ajoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotesActivity.this,NoteActivity.class);
                intent.putExtra("profile_id",profileId);
                intent.putExtra("profile_fullname",getIntent().getStringExtra("profile_fullname"));
                startActivity(intent);
            }
        });

        Retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotesActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void getAllNote(int id) {
        // Create a new Thread to run the network request
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Perform the network request in the background thread
                String response = ApiService.makeRequest(API_URL_FOR_GET_NOTE+"?id="+id, "GET", new Object());

                // Once the network call is complete, post the result to the main thread
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (response != null && !response.isEmpty()) {
                            try {
                                JSONArray jsonArray = new JSONArray(response);

                                ArrayList<Note> notes = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    Note note = new Note(
                                            Integer.parseInt(jsonArray.getJSONObject(i).getString("id")),
                                            Integer.parseInt(jsonArray.getJSONObject(i).getString("student_id")),
                                            jsonArray.getJSONObject(i).getString("matiere"),
                                            jsonArray.getJSONObject(i).getString("score"),
                                            Boolean.parseBoolean(jsonArray.getJSONObject(i).getString("status"))
                                    );
                                    notes.add(note);
                                }

                                NoteAdapter adapter = new NoteAdapter(NotesActivity.this,notes);
                                listView.setAdapter(adapter);

                            } catch (JSONException e) {
                                Toast.makeText(NotesActivity.this, "Error parsing the response.", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(NotesActivity.this, "Empty or null response from the server.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).start();
    }

}