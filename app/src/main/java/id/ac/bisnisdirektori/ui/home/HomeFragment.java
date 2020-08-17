package id.ac.bisnisdirektori.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
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
//        CardView cardView = (CardView) root.findViewById(R.id.card_product);
//        cardView.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                Intent intent = new Intent(getActivity(), DetailProduct.class);
//                startActivity(intent);
//            }
//        });
        CardView kategori = (CardView) root.findViewById(R.id.card_category);
        kategori.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getActivity(), DetailCategory.class);
                startActivity(intent);
            }
        });
        sharedpreferences = getContext().getSharedPreferences(LoginActivity.my_shared_preferences, Context.MODE_PRIVATE);

        id = getActivity().getIntent().getStringExtra(TAG_ID);
        mail = getActivity().getIntent().getStringExtra(TAG_EMAIL);

        swipeRefreshLayout = root.findViewById (R.id.swipeRefreshLayout);

        ListTop = root.findViewById (R.id.ListTop);
//        edtKeyword = root.findViewById(R.id.edtKeyword);
//        btnSearch = root.findViewById(R.id.btnSearch);
//        txtAlert = root.findViewById (R.id.txtAlert);
//        final String keyword = edtKeyword.getText().toString();

        cla = new adapterListTop(getActivity());
        ListAPI2 = ADMIN_PANEL_URL + "/get_list_top.php";
        Log.d("TAG", "url: "+ListAPI);
        new getDataTask().execute();


//        btnSearch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //sharedpreferences
//
//                String keyword = edtKeyword.getText().toString();
//                cla = new adapterList (getActivity());
//                new getDataTask().execute ();
//
//                ListAPI2 = ADMIN_PANEL_URL + "/get_list_top3.php"+"?keyword="+keyword;
//                ListTop.setVisibility (View.VISIBLE);
//                ListTop.setAdapter (cla);
//
//            }
//        });

        //Click List Event to Detail
        ListTop.setOnItemClickListener (new AdapterView.OnItemClickListener () {
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                // TODO Auto-generated method stub
                // go to menu detail page
                Intent iDetail = new Intent (getActivity(), DetailProduct.class);
                iDetail.putExtra ("ID", id_data.get (position));
                startActivity (iDetail);
            }
        });

        //Get List Data Bisnis Event to Display with Refresh Layout
        ListTop.setOnScrollListener (new AbsListView.OnScrollListener () {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                boolean enable = false;

                if (ListTop != null && ListTop.getChildCount () > 0) {
                    boolean firstItemVisible = ListTop.getFirstVisiblePosition () == 0;
                    boolean topOfFirstItemVisible = ListTop.getChildAt (0).getTop () == 0;
                    enable = firstItemVisible && topOfFirstItemVisible;
                }
//                swipeRefreshLayout.setEnabled (enable);
            }
        });

        //Refreshig List Data Bisnis Event
        swipeRefreshLayout.setOnRefreshListener (new SwipeRefreshLayout.OnRefreshListener () {
            @Override
            public void onRefresh() {
                new Handler().postDelayed (new Runnable () {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing (false);
                        IOConnect = 0;
                        ListTop.invalidateViews ();
                        clearData ();
                        new getDataTask().execute();
                    }
                }, 3000);
            }
        });

        return root;
    }


    void clearData() {
        id_data.clear ();
        nama_bisnis.clear ();
        kategori.clear ();
        price.clear();
        alamat.clear ();
        foto.clear ();
    }

    // asynctask class to handle parsing json in background
    public class getDataTask extends AsyncTask<Void, Void, Void> {
        // show progressbar first

        getDataTask() {
            conMgr = (ConnectivityManager) getActivity().getSystemService (Context.CONNECTIVITY_SERVICE);
            {
                if (conMgr.getActiveNetworkInfo () != null
                        && conMgr.getActiveNetworkInfo ().isAvailable ()
                        && conMgr.getActiveNetworkInfo ().isConnected ()) {
//                    if (!prgLoading.isShown ()) {
//                        prgLoading.setVisibility (View.VISIBLE);
//                        txtAlert.setVisibility (View.GONE);
//                    }

                } else {

                }
            }

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            // parse json data from server in background
            parseJSONData ();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            // when finish parsing, hide progressbar
//            prgLoading.setVisibility (View.GONE);
            // if internet connection and data available show data on list
            // otherwise, show alert text

            if ((id_data.size () > 0) && (IOConnect == 0)) {

                ListTop.setVisibility (View.VISIBLE);
                ListTop.setAdapter (cla);

            } else {
                ListTop.setVisibility (View.VISIBLE);
                Toast.makeText (getActivity(), "No Internet Connection",
                        Toast.LENGTH_LONG).show ();
//                txtAlert.setVisibility (View.VISIBLE);

            }
        }

    }


    public void parseJSONData() {
        clearData ();
        //sharedpreferences

        String url = Server.URL+"get_list_top.php";


        try {
            // request data from Category API


            HttpClient client = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout (client.getParams (), 15000);
            HttpConnectionParams.setSoTimeout (client.getParams (), 15000);
            HttpUriRequest request = new HttpGet(ListAPI2);
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

                id_data.add (staff.getString ("id_data"));
                nama_bisnis.add (staff.getString ("nama_bisnis"));
                kategori.add (staff.getString ("kategori"));
                price.add(staff.getString("price"));
                alamat.add (staff.getString ("alamat"));
                foto.add (staff.getString ("foto"));

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
