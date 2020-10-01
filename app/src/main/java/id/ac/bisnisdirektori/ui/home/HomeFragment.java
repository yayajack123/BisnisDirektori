package id.ac.bisnisdirektori.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

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

import id.ac.bisnisdirektori.DetailCategory;
import id.ac.bisnisdirektori.DetailProduct;
import id.ac.bisnisdirektori.LoginActivity;
import id.ac.bisnisdirektori.R;
import id.ac.bisnisdirektori.admin.DetailInformationActivity;
import id.ac.bisnisdirektori.admin.ListInformationActivity;
import id.ac.bisnisdirektori.admin.LoginAdminActivity;
import id.ac.bisnisdirektori.admin.Server;
import id.ac.bisnisdirektori.admin.adapterList;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    SharedPreferences sharedpreferences;

    private RecyclerView top;


    private RequestQueue requestQueue;
    private StringRequest stringRequest;

    TextView txt_id, txt_email;
    String id, mail;

    public static final String TAG_ID = "id";
    public static final String TAG_EMAIL = "email";

    public static String ADMIN_PANEL_URL = "https://www.pantaucovid19.net/";
    public static String AccessKey = "12345";
    // database path configuration
    @SuppressLint("SdCardPath")
    public static String DBPath = "/data/data/com.devatapixel.crud/databases/";
    ListView ListTop;
    ArrayList<HashMap<String, String>> list_top;
    ProgressBar prgLoading;
    SwipeRefreshLayout swipeRefreshLayout = null;
    EditText edtKeyword;
    ImageButton btnSearch;
    TextView txtAlert;
    adapterListTop cla;
    ConnectivityManager conMgr;

    public static ArrayList<String> id_data = new ArrayList<String> ();
    public static ArrayList<String> nama_bisnis = new ArrayList<String> ();
    public static ArrayList<String> no_telp = new ArrayList<String> ();
    public static ArrayList<String> email = new ArrayList<String> ();
    public static ArrayList<String> website = new ArrayList<String> ();
    public static ArrayList<String> opentime = new ArrayList<String> ();
    public static ArrayList<String> price = new ArrayList<String> ();
    public static ArrayList<String> kategori = new ArrayList<String> ();
    public static ArrayList<String> alamat = new ArrayList<String> ();
    public static ArrayList<String> latitude = new ArrayList<String> ();
    public static ArrayList<String> longitude = new ArrayList<String> ();
    public static ArrayList<String> otherinfo = new ArrayList<String> ();
    public static ArrayList<String> foto = new ArrayList<String> ();
    String ListAPI, ListAPI2, keyword;
    int IOConnect = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        CardView kat_musik = (CardView) root.findViewById(R.id.cat_musik);
        kat_musik.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getActivity(), DetailCategory.class);
                intent.putExtra("kategori", "Barang");
                startActivity(intent);
            }
        });
        CardView kat_makanan = (CardView) root.findViewById(R.id.cat_makanan);
        kat_makanan.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getActivity(), DetailCategory.class);
                intent.putExtra("kategori", "Makanan");
                startActivity(intent);
            }
        });
        CardView kat_jasa = (CardView) root.findViewById(R.id.cat_otomotif);
        kat_jasa.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getActivity(), DetailCategory.class);
                intent.putExtra("kategori", "Jasa");
                startActivity(intent);
            }
        });
        sharedpreferences = getContext().getSharedPreferences(LoginActivity.my_shared_preferences, Context.MODE_PRIVATE);
        String url = Server.URL+"get_list_top.php";
        id = getActivity().getIntent().getStringExtra(TAG_ID);
//        mail = getActivity().getIntent().getStringExtra(TAG_EMAIL);


//        edtKeyword = root.findViewById(R.id.edtKeyword);
//        btnSearch = root.findViewById(R.id.btnSearch);
//        txtAlert = root.findViewById (R.id.txtAlert);
//        final String keyword = edtKeyword.getText().toString();

        top = (RecyclerView) root.findViewById(R.id.ListTop);
        top.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));


        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

        list_top = new ArrayList<>();


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
                        list_top.add(map);
                        adapterListTop adapter = new adapterListTop(HomeFragment.this, list_top);
                        top.setAdapter(adapter);

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

        top.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), top, new ClickListener() {
            @Override
            public void onDataClick(View view, int position) {
                Intent intent = new Intent(getActivity(), DetailProduct.class);
                intent.putExtra("id_data", list_top.get(position).get("id_data"));
                startActivity(intent);
            }

            @Override
            public void onDataLongClick(View view, int position) {
//                Toast.makeText(getActivity(),"Kepencet Lama "+position,Toast.LENGTH_LONG).show();
            }
        }));

        return root;
    }


//    void clearData() {
//        id_data.clear ();
//        nama_bisnis.clear ();
//        kategori.clear ();
//        price.clear();
//        alamat.clear ();
//        foto.clear ();
//    }

    // asynctask class to handle parsing json in background

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{
        private GestureDetector mGestureDetector;
        private ClickListener mClickListener;


        public RecyclerTouchListener(final Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
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
