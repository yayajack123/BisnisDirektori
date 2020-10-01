package id.ac.bisnisdirektori.review;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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

import id.ac.bisnisdirektori.LoginActivity;
import id.ac.bisnisdirektori.R;

import id.ac.bisnisdirektori.admin.DetailPhotoProductActivity;
import id.ac.bisnisdirektori.admin.Server;


public class ListPhotoReviewActivity extends AppCompatActivity {

    private String id_data1, id_foto1;
    EditText edtIdData;
    Button submit;
    ImageView imgView, uploadPhoto;
    private int GALLERY = 1, CAMERA = 2;
    Bitmap bitmap, decoded;

    int success;
    private static final String TAG = ListPhotoReviewActivity.class.getSimpleName();
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private String KEY_FOTO = "foto";
    private String KEY_IDDATA = "id_data";
    private static final String KEY_EMPTY = "";
    String tag_json_obj = "json_obj_req";
    String strFoto, strIdData;


    //Shared Preferences from Login Admin
    public final static String TAG_ID = "id";
    String id;
    SharedPreferences sharedpreferences;

    private RequestQueue requestQueue;
    private StringRequest stringRequest;

    ArrayList<HashMap<String, String>> list_data;

    public static String ADMIN_PANEL_URL = "https://www.pantaucovid19.net/";
    public static String AccessKey = "12345";
    // database path configuration
    @SuppressLint("SdCardPath")
    public static String DBPath = "/data/data/com.devatapixel.crud/databases/";
    GridView ListEvent;
    ProgressBar prgLoading;
    SwipeRefreshLayout swipeRefreshLayout = null;
    EditText edtKeyword;
    ImageButton btnSearch;
    TextView txtAlert;
    FloatingActionButton addImage;
    AdapterListPhotoReview cla;
    ConnectivityManager conMgr;

    public static ArrayList<String> foto = new ArrayList<String> ();
    public static ArrayList<String> id_data = new ArrayList<String> ();
    public static ArrayList<String> id_review = new ArrayList<String> ();
    String ListAPI;
    int IOConnect = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_photo_review);

        Intent iGet = getIntent();
        id_data1 = iGet.getStringExtra ("id_data");
        String idData = id_data1;
        getSupportActionBar().setTitle(" ");
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_baseline_arrow_back_24);
        upArrow.setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        swipeRefreshLayout = findViewById (R.id.swipeRefreshLayout);
        //sharedpreferences
        sharedpreferences = getSharedPreferences (LoginActivity.my_shared_preferences, Context.MODE_PRIVATE);
        //get data
//        id = getIntent().getStringExtra (TAG_ID);
        //set data if data null
        id = sharedpreferences.getString("id_member", "null");
        //convert id to string id1
        String id1 = id;

        swipeRefreshLayout.setColorSchemeResources (R.color.orange, R.color.green, R.color.blue);
        prgLoading = findViewById (R.id.prgLoading);
        ListEvent = findViewById (R.id.ListPhotoReview);
        txtAlert = findViewById (R.id.txtAlert);
        cla = new AdapterListPhotoReview (ListPhotoReviewActivity.this);
        ListAPI = ADMIN_PANEL_URL + "/get_list_photo_review.php?id_data="+idData;
        Log.d("TAG", "url: "+ListAPI);
        new getDataTask().execute();


        //Get List Data Bisnis Event to Display with Refresh Layout
        ListEvent.setOnScrollListener (new AbsListView.OnScrollListener () {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                boolean enable = false;

                if (ListEvent != null && ListEvent.getChildCount () > 0) {
                    boolean firstItemVisible = ListEvent.getFirstVisiblePosition () == 0;
                    boolean topOfFirstItemVisible = ListEvent.getChildAt (0).getTop () == 0;
                    enable = firstItemVisible && topOfFirstItemVisible;
                }
                swipeRefreshLayout.setEnabled (enable);
            }
        });

        //Refreshig List Data Bisnis Event
        swipeRefreshLayout.setOnRefreshListener (new SwipeRefreshLayout.OnRefreshListener () {
            @Override
            public void onRefresh() {
                new Handler ().postDelayed (new Runnable () {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing (false);
                        IOConnect = 0;
                        ListEvent.invalidateViews ();
                        clearData ();
                        new getDataTask().execute ();
                    }
                }, 3000);
            }
        });


//        edtIdData = findViewById(R.id.idDataProduct);
//        edtIdData.setText(idData);


        ListEvent.setOnItemClickListener (new AdapterView.OnItemClickListener () {
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                // TODO Auto-generated method stub
                // go to menu detail page

                Intent iDetail = new Intent (ListPhotoReviewActivity.this, ImageReviewActivity.class);
                iDetail.putExtra ("id_review", id_review.get (position));
                iDetail.putExtra ("foto", foto.get (position));
                iDetail.putExtra ("id_data", id_data.get (position));
                startActivity (iDetail);

            }
        });
    }

    void clearData() {
        foto.clear ();
        id_data.clear ();
        id_review.clear ();
    }

    // asynctask class to handle parsing json in background
    public class getDataTask extends AsyncTask<Void, Void, Void> {
        // show progressbar first

        getDataTask() {
            conMgr = (ConnectivityManager) getSystemService (Context.CONNECTIVITY_SERVICE);
            {
                if (conMgr.getActiveNetworkInfo () != null
                        && conMgr.getActiveNetworkInfo ().isAvailable ()
                        && conMgr.getActiveNetworkInfo ().isConnected ()) {
                    if (!prgLoading.isShown ()) {
                        prgLoading.setVisibility (View.VISIBLE);
                        txtAlert.setVisibility (View.GONE);
                    }

                } else {

                }
            }

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            // parse json data from server in background
            sharedpreferences = getSharedPreferences (LoginActivity.my_shared_preferences, Context.MODE_PRIVATE);
            //get data
            id = getIntent().getStringExtra (TAG_ID);
            //set data if data null
            id = sharedpreferences.getString (TAG_ID, null);
            //convert id to string id1
            String id1 = id;

            parseJSONData ();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            // when finish parsing, hide progressbar
            prgLoading.setVisibility (View.GONE);
            // if internet connection and data available show data on list
            // otherwise, show alert text

            //sharedpreferences
            sharedpreferences = getSharedPreferences (LoginActivity.my_shared_preferences, Context.MODE_PRIVATE);
            //get data
            id = getIntent().getStringExtra (TAG_ID);
            //set data if data null
            id = sharedpreferences.getString (TAG_ID, null);
            //convert id to string id1
            String id1 = id;

            if ((id_data.size () > 0) && (IOConnect == 0)) {

                ListEvent.setVisibility (View.VISIBLE);
                ListEvent.setAdapter (cla);

            } else {
                ListEvent.setVisibility (View.VISIBLE);
                txtAlert.setVisibility (View.VISIBLE);

            }
        }

    }


    public void parseJSONData() {
        clearData ();
        //sharedpreferences
        sharedpreferences = getSharedPreferences (LoginActivity.my_shared_preferences, Context.MODE_PRIVATE);
        //get data
        id = getIntent ().getStringExtra (TAG_ID);
        //set data if data null
        id = sharedpreferences.getString (TAG_ID, null);


        try {
            // request data from Category API

            sharedpreferences = getSharedPreferences (LoginActivity.my_shared_preferences, Context.MODE_PRIVATE);
            //get data
            id = getIntent().getStringExtra (TAG_ID);
            //set data if data null
            id = sharedpreferences.getString (TAG_ID, null);
            //convert id to string id1
            String id1 = id;

            HttpClient client = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout (client.getParams (), 15000);
            HttpConnectionParams.setSoTimeout (client.getParams (), 15000);
            HttpUriRequest request = new HttpGet(ListAPI);
            HttpResponse response = client.execute (request);
            InputStream atomInputStream = response.getEntity ().getContent ();
            BufferedReader in = new BufferedReader (new InputStreamReader(atomInputStream));
            String line;
            String str ="";
            while ((line = in.readLine ()) != null) {
                str += line;
            }
            // parse json data and store into arraylist variables
            JSONObject json = new JSONObject (str);
            JSONArray data = json.getJSONArray ("data");

            for (int i = 0; i < data.length (); i++) {
                JSONObject object = data.getJSONObject (i);
                JSONObject staff = object.getJSONObject ("Staff");
                id_review.add (staff.getString ("id_review"));
                foto.add (staff.getString ("foto"));
                id_data.add (staff.getString ("id_data"));

            }
        }

        catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        catch (IOException e) {
            // TODO Auto-generated catch block
            IOConnect = 1;
            e.printStackTrace();
        }

        catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}