package id.ac.bisnisdirektori;

import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.images.ImageManager;
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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;

import id.ac.bisnisdirektori.admin.Server;
import id.ac.bisnisdirektori.review.ListPhotoProductActivity;
import id.ac.bisnisdirektori.review.ListPhotoReviewActivity;
import id.ac.bisnisdirektori.review.PostReviewActivity;
import id.ac.bisnisdirektori.review.ReviewActivity;

public class DetailProduct extends AppCompatActivity {

    SharedPreferences sharedpreferences;

    public static final String TAG_ID = "id";
    public static final String TAG_EMAIL = "email";
    private ImageView img;
    private TextView nm, cat, tp, loc, tm, pr, loc2, cat2, pr2, em, web, ot, tr, rr, la, lo;
    private RatingBar rb;
    String nama_bisnis, no_telp, email, website, opentime, price, kategori, alamat, otherinfo, foto, jumlah_review, rata, latitude, longitude;
    String id;
    String url_detail;
    String DetailAPI;
    int IOConnect = 0;
    private String id_data;

    private RequestQueue requestQueue;
    private StringRequest stringRequest;

    ArrayList<HashMap<String, String>> list_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);

        sharedpreferences = getSharedPreferences(LoginActivity.my_shared_preferences, Context.MODE_PRIVATE);

        id = sharedpreferences.getString("id_member", "null");

        Intent iGet = getIntent();
        id_data = iGet.getStringExtra("id_data");

        DetailAPI = Server.URL + "detail_product.php?id_data=" + id_data;

        img = (ImageView)findViewById(R.id.photo_product);
        nm = (TextView)findViewById(R.id.name_product);
        cat = (TextView)findViewById(R.id.cat_product);
        tp = (TextView) findViewById(R.id.telp_product);
        loc = (TextView) findViewById(R.id.loc_product);
        tm = (TextView) findViewById(R.id.time_product);
        pr = (TextView) findViewById(R.id.price_product);
        loc2 = (TextView) findViewById(R.id.loc2_product);
        cat2 = (TextView) findViewById(R.id.cat2_product);
        pr2 = (TextView) findViewById(R.id.price2_product);
        em = (TextView) findViewById(R.id.email_product);
        web = (TextView) findViewById(R.id.web_product);
        ot = (TextView) findViewById(R.id.otherinfo_product);
        tr = (TextView) findViewById(R.id.total_review);
        rr = (TextView) findViewById(R.id.text_review);
        rb = (RatingBar) findViewById(R.id.rating_product);
        la = (TextView) findViewById(R.id.latitude_pro);
        lo = (TextView) findViewById(R.id.longitude_pro);

        requestQueue = Volley.newRequestQueue(DetailProduct.this);

        TextView rvw = (TextView) findViewById(R.id.review);
        rvw.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(DetailProduct.this, ReviewActivity.class);
                intent.putExtra("id_data", id_data);
                startActivity(intent);
            }
        });

        TextView dir = (TextView) findViewById(R.id.direction);
        dir.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(DetailProduct.this, ProductMapsActivity.class);
                intent.putExtra("id_data", id_data);
                intent.putExtra("nama_bisnis", nama_bisnis);
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                startActivity(intent);
            }
        });

        Button list_rvw = (Button) findViewById(R.id.btn_review);
        list_rvw.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(DetailProduct.this, ReviewActivity.class);
                intent.putExtra("id_data", id_data);
                startActivity(intent);
            }
        });

        Button list_ptrvw = (Button) findViewById(R.id.btn_photo);
        list_ptrvw.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(DetailProduct.this, ListPhotoReviewActivity.class);
                intent.putExtra("id_data", id_data);
                startActivity(intent);
            }
        });

        Button list_menu = (Button) findViewById(R.id.btn_menu);
        list_menu.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(DetailProduct.this, ListPhotoProductActivity.class);
                intent.putExtra("id_data", id_data);
                startActivity(intent);
            }
        });

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

                Picasso.with(getApplicationContext()).load(Server.URL + "/" + foto).placeholder(R.drawable.kategori).into(img, new Callback() {
                    @Override
                    public void onSuccess() {
                        Bitmap bitmap = ((BitmapDrawable) img.getDrawable()).getBitmap();
                        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(Palette palette) {
                            }
                        });
                    }

                    @Override
                    public void onError() {
                    }
                });

                nm.setText(nama_bisnis);
                cat.setText(kategori);
                tp.setText(no_telp);
                loc.setText(alamat);
                tm.setText(opentime);
                pr.setText(price);
                loc2.setText(alamat);
                cat2.setText(kategori);
                pr2.setText(price);
                em.setText(email);
                web.setText(website);
                ot.setText(otherinfo);
                tr.setText(jumlah_review);
                la.setText(latitude);
                lo.setText(longitude);

                if(rata=="null"){
                    rb.setRating(Float.parseFloat("0"));
                    rr.setText("0");
                }else{
                    rb.setRating(Float.parseFloat(rata));
                    rr.setText(rata);
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
                foto = detail.getString("foto");
                nama_bisnis = detail.getString("nama_bisnis");
                no_telp = detail.getString("no_telp");
                email = detail.getString("email");
                website = detail.getString("website");
                price = detail.getString("price");
                opentime = detail.getString("opentime");
                kategori = detail.getString("kategori");
                alamat = detail.getString("alamat");
                otherinfo = detail.getString("otherinfo");
                jumlah_review = detail.getString("jumlah_review");
                rata = detail.getString("rata");
                latitude = detail.getString("latitude");
                longitude = detail.getString("longitude");
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

    @Override
    public void onConfigurationChanged(final Configuration newConfig) {
        // Ignore orientation change to keep activity from restarting
        super.onConfigurationChanged(newConfig);
    }
}