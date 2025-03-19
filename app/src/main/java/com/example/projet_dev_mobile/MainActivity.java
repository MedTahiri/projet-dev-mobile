package com.example.projet_dev_mobile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.example.projet_dev_mobile.module.Profile;
import com.example.projet_dev_mobile.service.ApiService;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    EditText Nom, Prenom, Classe, Remarques;
    ImageView Profile;
    Button Enregistere, Appel, Notes;

    Profile profile;

    // chose id from 1 to 10 for generate random profile info
    private static final String API_URL_FOR_GET_PROFILE = "https://medtahiri.pythonanywhere.com/api/profile/?id=4";

    private static final String API_URL_FOR_UPDATE_PROFILE = "https://medtahiri.pythonanywhere.com/api/profile/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Nom = (EditText) findViewById(R.id.Nom);
        Prenom = (EditText) findViewById(R.id.Prenom);
        Classe = (EditText) findViewById(R.id.Classe);
        Remarques = (EditText) findViewById(R.id.Remarques);

        Profile = (ImageView) findViewById(R.id.Profile);


        Enregistere = (Button) findViewById(R.id.Enregistere);
        Appel = (Button) findViewById(R.id.Appel);
        Notes = (Button) findViewById(R.id.Notes);

        getProfileInfo();

        Enregistere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Profile updateProfile = new Profile(
                        profile.id,
                        Nom.getText().toString(),
                        Prenom.getText().toString(),
                        profile.image,
                        profile.email,
                        profile.phone,
                        Classe.getText().toString(),
                        Remarques.getText().toString()
                );
                updateProfileInfo(updateProfile);
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        Appel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + profile.phone));
                startActivity(intent);
            }
        });

        Notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NotesActivity.class);
                intent.putExtra("profile_id",profile.id);
                intent.putExtra("profile_fullname",profile.firstname+" "+profile.lastname);
                startActivity(intent);
            }
        });

    }

    public void getProfileInfo() {
        // Create a new Thread to run the network request
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Perform the network request in the background thread
                String response = ApiService.makeRequest(API_URL_FOR_GET_PROFILE, "GET", new Object());

                // Once the network call is complete, post the result to the main thread
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (response != null && !response.isEmpty()) {
                            try {
                                // Parse the JSON response as an object, not an array
                                JSONObject obj = new JSONObject(response);

                                profile = new Profile(
                                        obj.getInt("id"),
                                        obj.getString("firstname"),
                                        obj.getString("lastname"),
                                        obj.getString("image"),
                                        obj.getString("email"),
                                        obj.getString("phone"),
                                        obj.getString("class_name"),
                                        obj.getString("remarque")
                                );

                                // Set values to UI elements
                                Nom.setText(profile.firstname);
                                Prenom.setText(profile.lastname);
                                Classe.setText(profile.class_name);
                                Remarques.setText(profile.remarque);
                                new Thread(() -> {
                                    try {
                                        URL imageUrl = new URL(profile.image);
                                        HttpURLConnection connection = (HttpURLConnection) imageUrl.openConnection();
                                        connection.connect();

                                        InputStream inputStream = connection.getInputStream();
                                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                                        // Update the UI on the main thread
                                        new Handler(Looper.getMainLooper()).post(() -> {
                                            Profile.setImageBitmap(bitmap);
                                        });

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }).start();

                            } catch (JSONException e) {
                                Toast.makeText(MainActivity.this, "Error parsing the response.", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(MainActivity.this, "Empty or null response from the server.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).start(); // Start the thread
    }

    public void updateProfileInfo(Profile updateProfile){
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Perform the network request in the background thread
                String response = ApiService.makeRequest(API_URL_FOR_UPDATE_PROFILE, "PUT", updateProfile);

                // Once the network call is complete, post the result to the main thread
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (response != null && !response.isEmpty()) {
                            Toast.makeText(MainActivity.this, "ok!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Empty or null response from the server.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).start();
    }
}