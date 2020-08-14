package id.ac.bisnisdirektori.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import id.ac.bisnisdirektori.MainActivity;
import id.ac.bisnisdirektori.R;
import id.ac.bisnisdirektori.admin.Server;
import id.ac.bisnisdirektori.ui.notifications.NotificationsFragment;

import static id.ac.bisnisdirektori.admin.Server.URL;

public class AdminPasswordActivity extends AppCompatActivity {

    SharedPreferences sharedpreferences;

    private RequestQueue requestQueue;
    private StringRequest stringRequest;

    ArrayList<HashMap<String, String>> list_data;

    EditText txt_password;
    TextView txt_pass_old;

    String id;
    Button update_pass;

    public static final String TAG_ID = "id";
    public static final String TAG_PASSWORD = "password";

    public static final String URL_UPDATE = "https://www.pantaucovid19.net/bd_update_password_admin.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_password);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        sharedpreferences = getSharedPreferences(LoginAdminActivity.my_shared_preferences, Context.MODE_PRIVATE);
        id = getIntent ().getStringExtra (TAG_ID);
        //set data if data null
        id = sharedpreferences.getString (TAG_ID, null);

        String url = Server.URL+"bd_password_admin.php?id="+id;

        //Deklarasi antara variabel dengan id field xml
        txt_password = (EditText) findViewById(R.id.new_password_admin);
        txt_pass_old = (TextView) findViewById(R.id.password_admin);

        update_pass = findViewById(R.id.btn_update_pass);
        update_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadUpdate();
            }
        });

        requestQueue = Volley.newRequestQueue(AdminPasswordActivity.this);

        list_data = new ArrayList<HashMap<String, String>>();

        stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("user");
                    for (int a = 0; a < jsonArray.length(); a ++){
                        JSONObject json = jsonArray.getJSONObject(a);
                        HashMap<String, String> map  = new HashMap<String, String>();
                        map.put("id", json.getString("id"));
                        map.put("password", json.getString("password"));
                        list_data.add(map);
                    }
                    txt_pass_old.setText(list_data.get(0).get("password"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AdminPasswordActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(stringRequest);


    }


    //Update Password Admin
    private void uploadUpdate() {

        final String password = txt_password.getText().toString().trim();

        class uploadWorkKnowledge extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(AdminPasswordActivity.this, "Updating...", "Wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                if (s != null) {
                    Toast.makeText(AdminPasswordActivity.this, "Update Password Successfully", Toast.LENGTH_LONG).show();
                    startActivity(new Intent (AdminPasswordActivity.this, OutletDetailsActivity.class));

                } else {
                    Toast.makeText(AdminPasswordActivity.this, "Error", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(TAG_ID, id);
                hashMap.put(TAG_PASSWORD, password);
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(URL_UPDATE, hashMap);
                return s;
            }
        }
        uploadWorkKnowledge ue = new uploadWorkKnowledge();
        ue.execute();
    }

}