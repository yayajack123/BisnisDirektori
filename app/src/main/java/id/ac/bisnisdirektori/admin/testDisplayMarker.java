//package com.dedykuncoro.googlemapapimysql;
//
//import android.support.v4.app.FragmentActivity;
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.android.volley.Request;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;
//import com.dedykuncoro.googlemapapimysql.app.AppController;
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.MapFragment;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.model.CameraPosition;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.Marker;
//import com.google.android.gms.maps.model.MarkerOptions;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
///**
// * Created by Kuncoro on 15/06/2017.
// */
//public class MainActivity extends FragmentActivity implements OnMapReadyCallback {
//
//    MapFragment mapFragment;
//    GoogleMap gMap;
//    MarkerOptions markerOptions = new MarkerOptions();
//    CameraPosition cameraPosition;
//    LatLng center, latLng;
//    String title;
//
//    public static final String ID = "id";
//    public static final String TITLE = "nama";
//    public static final String LAT = "lat";
//    public static final String LNG = "lng";
//
//    private String url = "http://wisatademak.dedykuncoro.com/Main/json_wisata";
//
//    String tag_json_obj = "json_obj_req";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
//
//    }
//
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        gMap = googleMap;
//
//        // Mengarahkan ke alun-alun Demak
//        center = new LatLng(-6.894796, 110.638413);
//        cameraPosition = new CameraPosition.Builder().target(center).zoom(10).build();
//        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//
//        getMarkers();
//    }
//
//    private void addMarker(LatLng latlng, final String title) {
//        markerOptions.position(latlng);
//        markerOptions.title(title);
//        gMap.addMarker(markerOptions);
//
//        gMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
//            @Override
//            public void onInfoWindowClick(Marker marker) {
//                Toast.makeText(getApplicationContext(), marker.getTitle(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    // Fungsi get JSON marker
//    private void getMarkers() {
//        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//
//            @Override
//            public void onResponse(String response) {
//                Log.e("Response: ", response.toString());
//
//                try {
//                    JSONObject jObj = new JSONObject(response);
//                    String getObject = jObj.getString("wisata");
//                    JSONArray jsonArray = new JSONArray(getObject);
//
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject jsonObject = jsonArray.getJSONObject(i);
//                        title = jsonObject.getString(TITLE);
//                        latLng = new LatLng(Double.parseDouble(jsonObject.getString(LAT)), Double.parseDouble(jsonObject.getString(LNG)));
//
//                        // Menambah data marker untuk di tampilkan ke google map
//                        addMarker(latLng, title);
//                    }
//
//                } catch (JSONException e) {
//                    // JSON error
//                    e.printStackTrace();
//                }
//
//            }
//        }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e("Error: ", error.getMessage());
//                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
//            }
//        });
//
//        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
//    }
//
//}