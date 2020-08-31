package id.ac.bisnisdirektori;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.Map;

import id.ac.bisnisdirektori.admin.AppController;
import id.ac.bisnisdirektori.admin.Server;

import static id.ac.bisnisdirektori.admin.Server.URL;

public class EditProfileUserActivity extends AppCompatActivity {

    SharedPreferences sharedpreferences;
    public static final String my_shared_preferences = "my_shared_preferences";
    public static final String session_status = "session_status";

    private RequestQueue requestQueue;
    private StringRequest stringRequest;

    ArrayList<HashMap<String, String>> list_data;

    private EditText fullname, address, phonenumber, email;

    String id, url_update;
    Button edit, update;
    int success;


    Boolean session = false;

    private static final String TAG = EditProfileUserActivity.class.getSimpleName();
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    String tag_json_obj = "json_obj_req";

    public static final String TAG_ID = "id";
    public static final String TAG_EMAIL = "email";
    public static final String TAG_FULLNAME = "fullname";
    public static final String TAG_PHONENUMBER = "phonenumber";
    public static final String TAG_ADDRESS = "address";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_user);

        sharedpreferences = getSharedPreferences(LoginActivity.my_shared_preferences, Context.MODE_PRIVATE);

        id = sharedpreferences.getString("id", "null");
        String url = Server.URL+"profileuser.php?id="+id;
        url_update = Server.URL+"updateprofileuser.php?id="+id;

        fullname = (EditText) findViewById(R.id.txt_fullname);
        email = (EditText) findViewById(R.id.txt_email);
        phonenumber = (EditText) findViewById(R.id.txt_phonenumber);
        address = (EditText) findViewById(R.id.txt_address);

        requestQueue = Volley.newRequestQueue(EditProfileUserActivity.this);

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
                        map.put("fullname", json.getString("fullname"));
                        map.put("email", json.getString("email"));
                        map.put("phonenumber", json.getString("phonenumber"));
                        map.put("address", json.getString("address"));
                        list_data.add(map);
                    }
                    fullname.setText(list_data.get(0).get("fullname"));
                    email.setText(list_data.get(0).get("email"));
                    phonenumber.setText(list_data.get(0).get("phonenumber"));
                    address.setText(list_data.get(0).get("address"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EditProfileUserActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(stringRequest);

        edit = findViewById(R.id.editProfile);
        update = findViewById(R.id.updateProfile);
        // fungsi floating action button memanggil form biodata
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fullname.setEnabled(true);
                email.setEnabled(true);
                phonenumber.setEnabled(true);
                address.setEnabled(true);

                edit.setVisibility(View.GONE);
                update.setVisibility(View.VISIBLE);
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fullname.setEnabled(true);
                email.setEnabled(true);
                phonenumber.setEnabled(true);
                address.setEnabled(true);

                String fn = fullname.getText().toString();
                String em = email.getText().toString();
                String np = phonenumber.getText().toString();
                String ad = address.getText().toString();

                checkUpdate(id, fn, em, np, ad);

                fullname.setEnabled(false);
                email.setEnabled(false);
                phonenumber.setEnabled(false);
                address.setEnabled(false);

                edit.setVisibility(View.VISIBLE);
                update.setVisibility(View.GONE);
            }
        });

    }
    private void checkUpdate(final String id_member, final String fn, final String em, final String np, final String ad){

        StringRequest strReq = new StringRequest(Request.Method.POST, url_update, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Register Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Check for error node in json
                    if (success == 1) {

                        Log.e("Successfully Register!", jObj.toString());

//                        Toast.makeText(getActivity().getApplicationContext(),
//                                jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();


                    } else {
//                        Toast.makeText(getActivity().getApplicationContext(),
//                                jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
//                Toast.makeText(getActivity().getApplicationContext(),
//                        error.getMessage(), Toast.LENGTH_LONG).show();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id);
                params.put("fullname", fn);
                params.put("email", em);
                params.put("phonenumber", np);
                params.put("address", ad);


                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);

    }
}