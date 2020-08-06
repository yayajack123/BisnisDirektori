package id.ac.bisnisdirektori.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import id.ac.bisnisdirektori.LoginActivity;
import id.ac.bisnisdirektori.R;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


import id.ac.bisnisdirektori.RegisterActivity;
import id.ac.bisnisdirektori.admin.Server;

public class LoginAdminActivity extends AppCompatActivity {

    ProgressDialog pDialog;
    Button login_admin;
    EditText email_admin, password_admin;
    Intent intent;

    int success;
    ConnectivityManager conMgr;

    private String url = Server.URL + "login.php";
    private static final String TAG = LoginAdminActivity.class.getSimpleName();
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    public final static String TAG_ID = "id";
    public final static String TAG_EMAIL = "email";
    public final static String TAG_FULLNAME = "fullname";
    public final static String TAG_PHONENUMBER = "phonenumber";
    public final static String TAG_ADDRESS = "address";
    public final static String TAG_PASSWORD = "password";

    String tag_json_obj = "json_obj_req";

    SharedPreferences sharedpreferences;
    Boolean session = false;
    String id, email, fullname, phonenumber, address, password;
    public static final String my_shared_preferences = "my_shared_preferences";
    public static final String session_status = "session_status";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_admin);

        //Button
        TextView register_admin = (TextView) findViewById(R.id.register_admin);
        register_admin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getBaseContext(), RegisterAdminActivity.class);
                startActivity(intent);
            }
        });

        Button login_user = (Button) findViewById(R.id.login_user);
        login_user.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        Button register_user = (Button) findViewById(R.id.register_user);
        register_user.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getBaseContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });


        //Configuration Connection
        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        {
            if (conMgr.getActiveNetworkInfo()!= null
                    && conMgr.getActiveNetworkInfo().isAvailable()
                    && conMgr.getActiveNetworkInfo().isConnected()) {

            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection",
                        Toast.LENGTH_LONG).show();
            }
        }

        login_admin = (Button) findViewById(R.id.login_admin);
        email_admin = (EditText) findViewById(R.id.email_admin);
        password_admin = (EditText) findViewById(R.id.password_admin);

        // Cek session login jika TRUE maka langsung buka MainActivity
        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);

        id = sharedpreferences.getString(TAG_ID, null);
        email = sharedpreferences.getString(TAG_EMAIL, null);
        fullname = sharedpreferences.getString(TAG_FULLNAME, null);
        phonenumber = sharedpreferences.getString(TAG_PHONENUMBER, null);
        address = sharedpreferences.getString(TAG_ADDRESS, null);
        password = sharedpreferences.getString (TAG_PASSWORD, null);



        if (session) {
            Intent intent = new Intent(LoginAdminActivity.this, HomeAdminActivity.class);
            intent.putExtra(TAG_ID, id);
            intent.putExtra(TAG_EMAIL, email);
            intent.putExtra(TAG_FULLNAME, fullname);
            intent.putExtra(TAG_PHONENUMBER, phonenumber);
            intent.putExtra(TAG_ADDRESS, address);
            intent.putExtra(TAG_PASSWORD, password);
            finish();
            startActivity(intent);
        }

        login_admin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String email = email_admin.getText().toString();
                String password = password_admin.getText().toString();

                // mengecek kolom yang kosong
                if (email.trim().length() > 0 && password.trim().length() > 0) {
                    if (conMgr.getActiveNetworkInfo() != null
                            && conMgr.getActiveNetworkInfo().isAvailable()
                            && conMgr.getActiveNetworkInfo().isConnected()) {
                        checkLogin(email, password);
                    } else {
                        Toast.makeText(getApplicationContext() ,"No Internet Connection", Toast.LENGTH_LONG).show();
                    }
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext() ,"Kolom tidak boleh kosong", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    private void checkLogin(final String email, final String password) {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Logging in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Check for error node in json
                    if (success == 1) {
                        String email = jObj.getString(TAG_EMAIL);
                        String id = jObj.getString(TAG_ID);
                        String fullname = jObj.getString(TAG_FULLNAME);
                        String phonenumber = jObj.getString(TAG_PHONENUMBER);
                        String address = jObj.getString(TAG_ADDRESS);
                        String password = jObj.getString(TAG_PASSWORD);

                        Log.e("Successfully Login!", jObj.toString());

                        Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                        // menyimpan login ke session
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putBoolean(session_status, true);
                        editor.putString(TAG_ID, id);
                        editor.putString(TAG_EMAIL, email);
                        editor.putString(TAG_FULLNAME, fullname);
                        editor.putString(TAG_PHONENUMBER, phonenumber);
                        editor.putString(TAG_ADDRESS, address);
                        editor.putString(TAG_PASSWORD, password);
                        editor.apply();

                        // Memanggil main activity
                        Intent intent = new Intent(LoginAdminActivity.this, HomeAdminActivity.class);
                        intent.putExtra(TAG_ID, id);
                        intent.putExtra(TAG_EMAIL, email);
                        intent.putExtra(TAG_FULLNAME, fullname);
                        intent.putExtra(TAG_PHONENUMBER, phonenumber);
                        intent.putExtra(TAG_ADDRESS, address);
                        intent.putExtra(TAG_PASSWORD, password);
//                        finish();
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(),
                                jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error : " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();

                hideDialog();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String> ();
                params.put("email", email);
                params.put("password", password);


                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


}