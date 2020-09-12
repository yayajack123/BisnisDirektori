package id.ac.bisnisdirektori.ui.dashboard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import id.ac.bisnisdirektori.LoginActivity;
import id.ac.bisnisdirektori.R;
import id.ac.bisnisdirektori.admin.Server;
import id.ac.bisnisdirektori.ui.home.HomeFragment;
import id.ac.bisnisdirektori.ui.home.adapterListTop;

public class DashboardFragment extends Fragment {

    SharedPreferences sharedpreferences;

    private RecyclerView exp;


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
    ArrayList<HashMap<String, String>> list_explore;

    private DashboardViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        sharedpreferences = getContext().getSharedPreferences(LoginActivity.my_shared_preferences, Context.MODE_PRIVATE);
        String url = Server.URL+"get_list_explore.php";
        id = getActivity().getIntent().getStringExtra(TAG_ID);
        mail = getActivity().getIntent().getStringExtra(TAG_EMAIL);

        exp = (RecyclerView) root.findViewById(R.id.ListExplore);
        exp.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));


        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

        list_explore = new ArrayList<>();


        stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response ", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("exp");
                    for (int a = 0; a < jsonArray.length(); a++) {
                        JSONObject json = jsonArray.getJSONObject(a);
                        HashMap<String, String> map = new HashMap<>();
                        map.put("id_review", json.getString("id_review"));
                        map.put("nama_bisnis", json.getString("nama_bisnis"));
                        map.put("fullname", json.getString("fullname"));
                        map.put("review", json.getString("review"));
                        map.put("tanggal", json.getString("tanggal"));
                        map.put("foto_user", json.getString("foto_user"));
                        map.put("foto_review", json.getString("foto_review"));
                        list_explore.add(map);
                        AdapterListExplore adapter = new AdapterListExplore(DashboardFragment.this, list_explore);
                        exp.setAdapter(adapter);

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

        return root;
    }
}
