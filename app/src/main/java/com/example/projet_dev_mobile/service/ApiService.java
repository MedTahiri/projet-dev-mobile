package com.example.projet_dev_mobile.service;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiService {
    public static String makeRequest(String apiUrl, String method, Object data) {
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod(method);

            if (method.equals("POST") || method.equals("PUT") || method.equals("DELETE")) {
                String jsonData = new Gson().toJson(data);
                System.out.println("Request Payload: " + jsonData);
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(jsonData.getBytes("UTF-8"));
                outputStream.close();
            }

            connection.connect();

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder result = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            reader.close();
            return result.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
