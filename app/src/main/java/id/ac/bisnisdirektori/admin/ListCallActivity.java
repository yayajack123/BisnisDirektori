package id.ac.bisnisdirektori.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import id.ac.bisnisdirektori.R;

public class ListCallActivity extends AppCompatActivity {

    private String id_data1, id_foto1;
    EditText edtIdData;
    Button submit;
    ImageView imgView, uploadPhoto;
    private int GALLERY = 1, CAMERA = 2;
    Bitmap bitmap, decoded;
    private String UPLOAD_URL = "https://www.pantaucovid19.net/bd_upload_photo_product.php";
    public static final String URL_DELETE = "https://www.pantaucovid19.net/bd_delete_product.php?id_foto=";
    int success;
    private static final String TAG = InformationActivity.class.getSimpleName();
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private String KEY_FOTO = "foto";
    private String KEY_FULLNAME= "fullname";
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
    ListView ListEvent;
    ProgressBar prgLoading;
    SwipeRefreshLayout swipeRefreshLayout = null;
    EditText edtKeyword;
    ImageButton btnSearch;
    TextView txtAlert;
    FloatingActionButton addImage;
    adapterListDetailCall cla;
    ConnectivityManager conMgr;

    public static ArrayList<String> id_call_user = new ArrayList<String> ();
    public static ArrayList<String> id_user = new ArrayList<String> ();
    public static ArrayList<String> fullname = new ArrayList<String> ();
    public static ArrayList<String> phonenumber = new ArrayList<String> ();
    public static ArrayList<String> address= new ArrayList<String> ();
    public static ArrayList<String> id_data = new ArrayList<String> ();
    public static ArrayList<String> foto = new ArrayList<String> ();
    public static ArrayList<String> time = new ArrayList<String> ();
    String ListAPI;
    int IOConnect = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_list_call);


        // Intent to detail with id data
        Intent iGet = getIntent ();
        id_data1 = iGet.getStringExtra ("id_data");
        String idData = id_data1;



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
        ListEvent = findViewById (R.id.ListCall);
        txtAlert = findViewById (R.id.txtAlert);
        cla = new adapterListDetailCall (ListCallActivity.this);
        ListAPI = ADMIN_PANEL_URL + "/bd_get_all_list_call_user.php?id_data="+idData;
        Log.d("TAG", "url: "+ListAPI);
        new getDataTask ().execute ();


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



//        ListEvent.setOnItemClickListener (new AdapterView.OnItemClickListener () {
//            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
//                                    long arg3) {
//                // TODO Auto-generated method stub
//                // go to menu detail page
//
//                Intent iDetail = new Intent (ListPhotoActivity.this, DetailPhotoProductActivity.class);
//                iDetail.putExtra ("id_foto", id_foto.get (position));
//                iDetail.putExtra ("id_data", id_data.get (position));
//                startActivity (iDetail);
////                Toast.makeText (getApplicationContext (), "Id Data",
////                        Toast.LENGTH_LONG).show ();
//
//            }
//        });




    }



    void clearData() {
        id_call_user.clear ();
        id_user.clear ();
        fullname.clear();
        phonenumber.clear();
        address.clear();
        id_data.clear ();
        foto.clear ();
        time.clear ();
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
            HttpUriRequest request = new HttpGet (ListAPI);
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

                id_call_user.add (staff.getString ("id_call_user"));
                id_user.add (staff.getString ("id_user"));
                fullname.add(staff.getString ("fullname"));
                phonenumber.add(staff.getString ("phonenumber"));
                address.add(staff.getString ("address"));
                id_data.add (staff.getString ("id_data"));
                foto.add (staff.getString ("foto"));
                time.add (staff.getString ("time"));

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