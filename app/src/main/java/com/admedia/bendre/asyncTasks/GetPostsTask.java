package com.admedia.bendre.asyncTasks;

import android.os.AsyncTask;

import com.admedia.bendre.util.EndpointConstants;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.HttpsURLConnection;

public class GetPostsTask extends AsyncTask<String, Void, String> {
    private String endpointUrl = EndpointConstants.POSTS_URL;

    public interface AsyncResponse {
        void processFinish(String output);
    }

    private AsyncResponse delegate;

    public GetPostsTask(AsyncResponse delegate) {
        this.delegate = delegate;
    }

    protected void onPreExecute() {
    }

    protected String doInBackground(String... args) {
        try
        {
            String category = args[0];
           // endpointUrl += "?categories=" + category;
            URL url = new URL(endpointUrl);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(30000 /* milliseconds */);
            conn.setConnectTimeout(30000 /* milliseconds */);

            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8));
            writer.write("");

            writer.flush();
            writer.close();
            os.close();

            int responseCode = conn.getResponseCode();

            //if the server respond by 200, we will get the data and use it
            if (responseCode == HttpsURLConnection.HTTP_OK)
            {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = in.readLine()) != null)
                {
                    sb.append(line);
                }

                in.close();

                return sb.toString();
            }

            return "";
        }
        catch (Exception e)
        {
            String msg = e.getMessage();
            return "Exception: " + msg;
        }
    }

    @Override
    protected void onPostExecute(String result) {
        delegate.processFinish(result);
    }

}