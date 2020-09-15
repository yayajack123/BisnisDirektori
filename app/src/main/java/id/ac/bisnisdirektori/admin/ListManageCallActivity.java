package id.ac.bisnisdirektori.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;

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

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import id.ac.bisnisdirektori.R;

public class ListManageCallActivity extends AppCompatActivity {

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
    ListView ListEvent;
    ProgressBar prgLoading;
    SwipeRefreshLayout swipeRefreshLayout = null;
    EditText edtKeyword;
    ImageButton btnSearch;
    TextView txtAlert;
    adapterListCall cla;
    ConnectivityManager conMgr;

    public static ArrayList<String> id_data = new ArrayList<String> ();
    public static ArrayList<String> nama_bisnis = new ArrayList<String> ();
    public static ArrayList<String> no_telp = new ArrayList<String> ();
    public static ArrayList<String> email = new ArrayList<String> ();
    public static ArrayList<String> website = new ArrayList<String> ();
    public static ArrayList<String> price = new ArrayList<String> ();
    public static ArrayList<String> kategori = new ArrayList<String> ();
    public static ArrayList<String> alamat = new ArrayList<String> ();
    public static ArrayList<String> latitude = new ArrayList<String> ();
    public static ArrayList<String> longitude = new ArrayList<String> ();
    public static ArrayList<String> otherinfo = new ArrayList<String> ();
    public static ArrayList<String> foto = new ArrayList<String> ();
    public static ArrayList<String> id_admin = new ArrayList<String> ();
    String ListAPI, ListAPI2;
    int IOConnect = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_list_manage_call);

        swipeRefreshLayout = findViewById (R.id.swipeRefreshLayout);
        //sharedpreferences
        sharedpreferences = getSharedPreferences (LoginAdminActivity.my_shared_preferences, Context.MODE_PRIVATE);
        //get data
        id = getIntent ().getStringExtra (TAG_ID);
        //set data if data null
        id = sharedpreferences.getString (TAG_ID, null);
        //convert id to string id1
        String id1 = id;

        swipeRefreshLayout.setColorSchemeResources (R.color.orange, R.color.green, R.color.blue);
        prgLoading = findViewById (R.id.prgLoading);
        ListEvent = findViewById (R.id.ListBisnis);
        edtKeyword = findViewById(R.id.edtKeyword);
        btnSearch = findViewById(R.id.btnSearch);
        txtAlert = findViewById (R.id.txtAlert);
        cla = new adapterListCall (ListManageCallActivity.this);
        ListAPI2 = ADMIN_PANEL_URL + "/bd_get_all_list_call3.php"+"?id_admin="+id1;
        Log.d("TAG", "url: "+ListAPI);
        new getDataTask ().execute ();

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //sharedpreferences
                sharedpreferences = getSharedPreferences (LoginAdminActivity.my_shared_preferences, Context.MODE_PRIVATE);
                //get data
                id = getIntent ().getStringExtra (TAG_ID);
                //set data if data null
                id = sharedpreferences.getString (TAG_ID, null);
                //convert id to string id1
                String id1 = id;

                String keyword = edtKeyword.getText().toString();
                cla = new adapterListCall (ListManageCallActivity.this);
                new getDataTask ().execute ();

                ListAPI2 = ADMIN_PANEL_URL + "/bd_get_all_list_call3.php"+"?keyword="+keyword+"&&id_admin="+id1;
                ListEvent.setVisibility (View.VISIBLE);
                ListEvent.setAdapter (cla);

            }
        });


        //Click List Event to Detail
        ListEvent.setOnItemClickListener (new AdapterView.OnItemClickListener () {
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                // TODO Auto-generated method stub
                // go to menu detail page
                Intent iDetail = new Intent (ListManageCallActivity.this, ListCallActivity.class);
                iDetail.putExtra ("id_data", id_data.get (position));
                startActivity (iDetail);
            }
        });

        //Click Button to Outlet Activity
        Button btnDashboard = (Button) findViewById (R.id.btnDashboard);
        btnDashboard.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (ListManageCallActivity.this, HomeAdminActivity.class);
                startActivity (intent);
            }
        });


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
                        new getDataTask ().execute ();
                    }
                }, 3000);
            }
        });

    }

    void clearData() {
        id_data.clear ();
        nama_bisnis.clear ();
        no_telp.clear ();
        email.clear ();
        alamat.clear ();
        price.clear();
        website.clear ();
        otherinfo.clear ();
        foto.clear ();
        id_admin.clear ();
//        rating.clear ();
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
            sharedpreferences = getSharedPreferences (LoginAdminActivity.my_shared_preferences, Context.MODE_PRIVATE);
            //get data
            id = getIntent ().getStringExtra (TAG_ID);
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
            sharedpreferences = getSharedPreferences (LoginAdminActivity.my_shared_preferences, Context.MODE_PRIVATE);
            //get data
            id = getIntent ().getStringExtra (TAG_ID);
            //set data if data null
            id = sharedpreferences.getString (TAG_ID, null);
            //convert id to string id1
            String id1 = id;

            if ((id_data.size () > 0) && (IOConnect == 0)) {

                ListEvent.setVisibility (View.VISIBLE);
                ListEvent.setAdapter (cla);

            } else {
                ListEvent.setVisibility (View.VISIBLE);
                Toast.makeText (getApplicationContext (), "Not Found Data",
                        Toast.LENGTH_LONG).show ();
                txtAlert.setVisibility (View.VISIBLE);

            }
        }

    }


    public void parseJSONData() {
        clearData ();
        //sharedpreferences
        sharedpreferences = getSharedPreferences (LoginAdminActivity.my_shared_preferences, Context.MODE_PRIVATE);
        //get data
        id = getIntent ().getStringExtra (TAG_ID);
        //set data if data null
        id = sharedpreferences.getString (TAG_ID, null);
        String url = Server.URL+"bd_get_all_list.php?id="+id;


        try {
            // request data from Category API

            sharedpreferences = getSharedPreferences (LoginAdminActivity.my_shared_preferences, Context.MODE_PRIVATE);
            //get data
            id = getIntent ().getStringExtra (TAG_ID);
            //set data if data null
            id = sharedpreferences.getString (TAG_ID, null);
            //convert id to string id1
            String id1 = id;

            HttpClient client = new DefaultHttpClient ();
            HttpConnectionParams.setConnectionTimeout (client.getParams (), 15000);
            HttpConnectionParams.setSoTimeout (client.getParams (), 15000);
            HttpUriRequest request = new HttpGet (ListAPI2);
            HttpResponse response = client.execute (request);
            InputStream atomInputStream = response.getEntity ().getContent ();
            BufferedReader in = new BufferedReader (new InputStreamReader (atomInputStream));
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

                id_data.add (staff.getString ("id_data"));
                nama_bisnis.add (staff.getString ("nama_bisnis"));
                no_telp.add (staff.getString ("no_telp"));
                email.add (staff.getString ("email"));
                alamat.add (staff.getString ("alamat"));
                price.add(staff.getString("price"));
                website.add (staff.getString ("website"));
                otherinfo.add (staff.getString ("otherinfo"));
                foto.add (staff.getString ("foto"));
                id_admin.add (staff.getString ("id_admin"));
//                rating.add(staff.getString ("rating"));

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