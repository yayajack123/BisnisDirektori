package id.ac.bisnisdirektori;

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

import id.ac.bisnisdirektori.admin.AdminPasswordActivity;
import id.ac.bisnisdirektori.admin.LoginAdminActivity;
import id.ac.bisnisdirektori.admin.OutletDetailsActivity;
import id.ac.bisnisdirektori.admin.RequestHandler;
import id.ac.bisnisdirektori.admin.Server;
import id.ac.bisnisdirektori.ui.notifications.NotificationsFragment;

public class UserPasswordActivity extends AppCompatActivity {
    SharedPreferences sharedpreferences;

    private RequestQueue requestQueue;
    private StringRequest stringRequest;

    ArrayList<HashMap<String, String>> list_data;

    EditText txt_password, txt_pass_old, txt_confirm_password, txt_pass_old_confirm;

    String id, pass, confirm, old, old_confirm;
    Button update_pass;

    public static final String TAG_ID = "id";
    public static final String TAG_PASSWORD = "password";

    public static final String URL_UPDATE = "https://www.pantaucovid19.net/update_password_user.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_password);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        sharedpreferences = getSharedPreferences(LoginActivity.my_shared_preferences, Context.MODE_PRIVATE);

        id = sharedpreferences.getString("id", "null");

        String url = Server.URL+"password_user.php?id="+id;

        //Deklarasi antara variabel dengan id field xml
        txt_password = (EditText) findViewById(R.id.new_password_user);
        txt_pass_old = (EditText) findViewById(R.id.password_user);
        txt_pass_old_confirm = (EditText) findViewById(R.id.password_user_confirm);
        txt_confirm_password = (EditText) findViewById(R.id.confirm_new_password_user);

        pass = txt_password.getText().toString().trim();
        confirm = txt_confirm_password.getText().toString().trim();
        old = txt_pass_old.getText().toString().trim();
        old_confirm = txt_pass_old_confirm.getText().toString().trim();



        update_pass = findViewById(R.id.btn_update_pass);
        update_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((old.equals(old_confirm)) && (pass.equals(confirm))){
                    uploadUpdate();
                }else{
                    Toast.makeText(UserPasswordActivity.this, "Password salah", Toast.LENGTH_SHORT).show();
                }
            }
        });

        requestQueue = Volley.newRequestQueue(UserPasswordActivity.this);

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
                    txt_pass_old_confirm.setText(list_data.get(0).get("password"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UserPasswordActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(stringRequest);
    }

    private void uploadUpdate() {

        final String password = txt_password.getText().toString().trim();

        class uploadWorkKnowledge extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(UserPasswordActivity.this, "Updating...", "Wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                if (s != null) {
                    Toast.makeText(UserPasswordActivity.this, "Update Password Successfully", Toast.LENGTH_LONG).show();
//                    startActivity(new Intent(UserPasswordActivity.this, UserPasswordActivity.class));
                } else {
                    Toast.makeText(UserPasswordActivity.this, "Error", Toast.LENGTH_LONG).show();
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