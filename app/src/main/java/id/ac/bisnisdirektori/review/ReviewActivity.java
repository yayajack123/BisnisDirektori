package id.ac.bisnisdirektori.review;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import id.ac.bisnisdirektori.DetailProduct;
import id.ac.bisnisdirektori.LoginActivity;
import id.ac.bisnisdirektori.R;
import id.ac.bisnisdirektori.admin.Server;
import id.ac.bisnisdirektori.ui.home.HomeFragment;
import id.ac.bisnisdirektori.ui.home.adapterListTop;

public class ReviewActivity extends AppCompatActivity {

    SharedPreferences sharedpreferences;

    private RecyclerView rev;
    ArrayList<HashMap<String, String>> list_rev;

    public static final String TAG_ID = "id";
    String id;
    int IOConnect = 0;
    private String id_data, nama_bisnis, kategori, alamat, opentime, price, jumlah_review, rata;

    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    private android.widget.RatingBar RatingBar;
    private TextView rate, title, category, location, open, harga, jumlah, ratarata;
    private RatingBar rt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        sharedpreferences = getSharedPreferences(LoginActivity.my_shared_preferences, Context.MODE_PRIVATE);

        id = sharedpreferences.getString("id_user", "null");

        Intent iGet = getIntent();
        id_data = iGet.getStringExtra("id_data");
        nama_bisnis = iGet.getStringExtra("nama_bisnis");
        kategori = iGet.getStringExtra("kategori");
        alamat = iGet.getStringExtra("alamat");
        opentime = iGet.getStringExtra("opentime");
        price = iGet.getStringExtra("price");
        jumlah_review = iGet.getStringExtra("jumlah_review");
        rata = iGet.getStringExtra("rata");

        String url = Server.URL+"get_list_review.php?id_data="+id_data;

        Button add_rvw = (Button) findViewById(R.id.btn_writereview);
        add_rvw.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(ReviewActivity.this, PostReviewActivity.class);
                intent.putExtra("id_data", id_data);
                intent.putExtra("nama_bisnis", nama_bisnis);
                startActivity(intent);
            }
        });

        title = (TextView) findViewById(R.id.title_product);
        category = (TextView) findViewById(R.id.cat_product);
        location = (TextView) findViewById(R.id.loc_product);
        open = (TextView) findViewById(R.id.open_time);
        harga = (TextView) findViewById(R.id.price_product);
        jumlah = (TextView) findViewById(R.id.total_review);
        ratarata = (TextView) findViewById(R.id.txt_rating_product);
        rt = (RatingBar) findViewById(R.id.rating_product);

        title.setText(nama_bisnis);
        category.setText(kategori);
        location.setText(alamat);
        open.setText(opentime);
        harga.setText(price);
        jumlah.setText(jumlah_review);
        ratarata.setText(rata);
        
//        rt.setRating(Float.parseFloat(rata));
        if(rata==null){
            rt.setRating(Float.parseFloat("null"));
        }else{
            rt.setRating(Float.parseFloat(rata));
        }


        rev = (RecyclerView) findViewById(R.id.ListRvw);
        rev.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));


        requestQueue = Volley.newRequestQueue(this.getApplicationContext());

        list_rev = new ArrayList<>();


        stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response ", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("rev");
                    for (int a = 0; a < jsonArray.length(); a++) {
                        JSONObject json = jsonArray.getJSONObject(a);
                        HashMap<String, String> map = new HashMap<>();
                        map.put("id_review", json.getString("id_review"));
                        map.put("fullname", json.getString("fullname"));
                        map.put("rate", json.getString("rate"));
                        map.put("review", json.getString("review"));
                        map.put("tanggal", json.getString("tanggal"));
                        map.put("foto", json.getString("foto"));
                        list_rev.add(map);
                        AdapterListReview adapter = new AdapterListReview(ReviewActivity.this, list_rev);
                        rev.setAdapter(adapter);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(HomeFragment.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(stringRequest);
    }
}