//package com.example.fashionshop.Map;
//
//import android.os.AsyncTask;
//
//import java.io.BufferedReader;
//import java.io.InputStream;
//import java.net.HttpURLConnection;
//import java.net.URL;
//
//public class FetchURL extends AsyncTask<String, Void, String> {
//
//    private final MapsActivity activity;
//
//    public FetchURL(MapsActivity activity) {
//        this.activity = activity;
//    }
//
//    @Override
//    protected String doInBackground(String... strings) {
//        try {
//            URL url = new URL(strings[0]);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.connect();
//
//            InputStream is = conn.getInputStream();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
//
//            StringBuilder sb = new StringBuilder();
//            String line;
//            while ((line = reader.readLine()) != null) sb.append(line);
//
//            return sb.toString();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    @Override
//    protected void onPostExecute(String result) {
//        new ParserTask(activity).execute(result);
//    }
//}
