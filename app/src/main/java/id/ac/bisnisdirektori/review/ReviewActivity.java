package id.ac.bisnisdirektori.review;

import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
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
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
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
    String id, DetailAPI;
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

        DetailAPI = Server.URL + "detail_product.php?id_data=" + id_data;

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

        if(rata==null){
            rt.setRating(Float.parseFloat("0"));
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
        new getDataTask().execute();
    }

    public class getDataTask extends AsyncTask<Void, Void, Void> {
        // show progressbar first
        @Override
        protected Void doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            // parse json data from server in background
            parseJSONData();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            // when finish parsing, hide progressbar
            // if internet connection and data available show data
            // otherwise, show alert text
            if (IOConnect == 0) {

                title.setText(nama_bisnis);
                category.setText(kategori);
                location.setText(alamat);
                open.setText(opentime);
                harga.setText(price);
                jumlah.setText(jumlah_review);

                if(rata=="null"){
                    rt.setRating(Float.parseFloat("0"));
                    ratarata.setText("0");
                }else{
                    rt.setRating(Float.parseFloat(rata));
                    ratarata.setText(rata);
                }

            }
        }
    }
    public void parseJSONData() {
        try {
            HttpClient client = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(client.getParams(), 15000);
            HttpConnectionParams.setSoTimeout(client.getParams(), 15000);
            HttpUriRequest request = new HttpGet(DetailAPI);
            HttpResponse response = client.execute(request);
            InputStream atomInputStream = response.getEntity().getContent();
            BufferedReader in = new BufferedReader(new InputStreamReader(atomInputStream));
            String line;
            String str = "";
            while ((line = in.readLine()) != null) {
                str += line;
            }
            JSONObject json = new JSONObject(str);
            JSONArray data = json.getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {
                JSONObject object = data.getJSONObject(i);
                JSONObject detail = object.getJSONObject("detail");
                nama_bisnis = detail.getString("nama_bisnis");
                price = detail.getString("price");
                opentime = detail.getString("opentime");
                kategori = detail.getString("kategori");
                alamat = detail.getString("alamat");
                jumlah_review = detail.getString("jumlah_review");
                rata = detail.getString("rata");
            }
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            IOConnect = 1;
            e.printStackTrace();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}