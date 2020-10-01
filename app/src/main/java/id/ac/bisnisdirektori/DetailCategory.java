package id.ac.bisnisdirektori;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

import id.ac.bisnisdirektori.admin.Server;


public class DetailCategory extends AppCompatActivity {
    SharedPreferences sharedpreferences;

    private RecyclerView cat;


    private RequestQueue requestQueue;
    private StringRequest stringRequest;

    TextView txt_id, txt_email;
    String id, mail, kategori;

    public static final String TAG_ID = "id";
    public static String ADMIN_PANEL_URL = "https://www.pantaucovid19.net/";
    public static String AccessKey = "12345";
    // database path configuration
    @SuppressLint("SdCardPath")
    public static String DBPath = "/data/data/com.devatapixel.crud/databases/";
    ArrayList<HashMap<String, String>> list_cat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_category);

        sharedpreferences = getSharedPreferences(LoginActivity.my_shared_preferences, Context.MODE_PRIVATE);

        id = getIntent().getStringExtra(TAG_ID);
        Intent iGet = getIntent();
        kategori = iGet.getStringExtra("kategori");
        String url = Server.URL+"get_list_cat.php?kategori="+kategori;

        cat = (RecyclerView) findViewById(R.id.ListCat);
        cat.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        TextView judul = (TextView) findViewById(R.id.judulCat);
        judul.setText(kategori);

        TextView desc = (TextView) findViewById(R.id.descCat);
        desc.setText("Most-loved and highly-reviewed "+kategori+" on Bali, by and for the lovers of the Island of Gods");

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        list_cat = new ArrayList<>();

        stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response ", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("top");
                    for (int a = 0; a < jsonArray.length(); a++) {
                        JSONObject json = jsonArray.getJSONObject(a);
                        HashMap<String, String> map = new HashMap<>();
                        map.put("id_data", json.getString("id_data"));
                        map.put("nama_bisnis", json.getString("nama_bisnis"));
                        map.put("kategori", json.getString("kategori"));
                        map.put("alamat", json.getString("alamat"));
                        map.put("no_telp", json.getString("no_telp"));
                        map.put("foto", json.getString("foto"));
                        map.put("rata", json.getString("rata"));
                        map.put("jumlah_review", json.getString("jumlah_review"));
                        list_cat.add(map);
                        AdapterListProductCategory adapter = new AdapterListProductCategory(DetailCategory.this, list_cat);
                        cat.setAdapter(adapter);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DetailCategory.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(stringRequest);

        cat.addOnItemTouchListener(new RecyclerTouchListener(DetailCategory.this, cat, new ClickListener() {
            @Override
            public void onDataClick(View view, int position) {
                Intent intent = new Intent(DetailCategory.this, DetailProduct.class);
                intent.putExtra("id_data", list_cat.get(position).get("id_data"));
                startActivity(intent);
            }

            @Override
            public void onDataLongClick(View view, int position) {
//                Toast.makeText(getActivity(),"Kepencet Lama "+position,Toast.LENGTH_LONG).show();
            }
        }));

    }
    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{
        private GestureDetector mGestureDetector;
        private DetailCategory.ClickListener mClickListener;


        public RecyclerTouchListener(final Context context, final RecyclerView recyclerView, final DetailCategory.ClickListener clickListener) {
            this.mClickListener = clickListener;
            mGestureDetector = new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(),e.getY());
                    if (child!=null && clickListener!=null){
                        clickListener.onDataLongClick(child,recyclerView.getChildAdapterPosition(child));
                    }
                    super.onLongPress(e);
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child!=null && mClickListener!=null && mGestureDetector.onTouchEvent(e)){
                mClickListener.onDataClick(child,rv.getChildAdapterPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
    public static interface ClickListener{
        public void onDataClick(View view, int position);
        public void onDataLongClick(View view, int position);
    }
}