package id.ac.bisnisdirektori;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import id.ac.bisnisdirektori.admin.Server;
import id.ac.bisnisdirektori.review.AdapterListReview;
import id.ac.bisnisdirektori.review.ReviewActivity;

public class ProductMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    SharedPreferences sharedpreferences;

    ArrayList<HashMap<String, String>> list_loc;

    public static final String TAG_ID = "id";
    String id, DetailAPI, lo, la;
    int IOConnect = 0;
    private String id_data, nama_bisnis, latitude, longitude;
    Double lat, lon;
    private RequestQueue requestQueue;
    private StringRequest stringRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        sharedpreferences = getSharedPreferences(LoginActivity.my_shared_preferences, Context.MODE_PRIVATE);

        id = sharedpreferences.getString("id_user", "null");

        Intent iGet = getIntent();
        id_data = iGet.getStringExtra("id_data");
        nama_bisnis = iGet.getStringExtra("nama_bisnis");
        latitude = iGet.getStringExtra("latitude");
        longitude = iGet.getStringExtra("longitude");
        lat = Double.parseDouble(latitude);
        lon = Double.parseDouble(longitude);

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng loc = new LatLng(lat, lon);
        mMap.addMarker(new MarkerOptions().position(loc).title(nama_bisnis));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(lat, lon))  //here latitude and longtitude are null as no value is assigned to them    // Sets the center of the map to Mountain View
                .zoom(18)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
}