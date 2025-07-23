//package com.example.fashionshop.Map;
//
//import android.graphics.Color;
//import android.os.AsyncTask;
//
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.PolylineOptions;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//public class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
//
//    private final MapsActivity activity;
//
//    public ParserTask(MapsActivity activity) {
//        this.activity = activity;
//    }
//
//    @Override
//    protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
//        JSONObject jObject;
//        List<List<HashMap<String, String>>> routes = null;
//
//        try {
//            jObject = new JSONObject(jsonData[0]);
//            DirectionsJSONParser parser = new DirectionsJSONParser();
//            routes = parser.parse(jObject);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return routes;
//    }
//
//    @Override
//    protected void onPostExecute(List<List<HashMap<String, String>>> result) {
//        ArrayList<LatLng> points;
//        PolylineOptions lineOptions = null;
//
//        for (List<HashMap<String, String>> path : result) {
//            points = new ArrayList<>();
//            lineOptions = new PolylineOptions();
//
//            for (HashMap<String, String> point : path) {
//                double lat = Double.parseDouble(point.get("lat"));
//                double lng = Double.parseDouble(point.get("lng"));
//                points.add(new LatLng(lat, lng));
//            }
//
//            lineOptions.addAll(points);
//            lineOptions.width(10);
//            lineOptions.color(Color.BLUE);
//            lineOptions.geodesic(true);
//        }
//
//        if (lineOptions != null) {
//            activity.mMap.addPolyline(lineOptions);
//        }
//    }
//}
