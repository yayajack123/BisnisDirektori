package id.ac.bisnisdirektori.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import androidx.palette.graphics.Palette;
import id.ac.bisnisdirektori.MainActivity;
import id.ac.bisnisdirektori.R;
import id.ac.bisnisdirektori.admin.Server;
import id.ac.bisnisdirektori.ui.notifications.NotificationsFragment;

import static id.ac.bisnisdirektori.admin.ListInformationActivity.foto;
import static id.ac.bisnisdirektori.admin.Server.URL;

public class AdminProfileActivity extends AppCompatActivity {

    SharedPreferences sharedpreferences;

    private RequestQueue requestQueue;
    private StringRequest stringRequest;

    ArrayList<HashMap<String, String>> list_data;

    EditText txt_id, txt_email,txt_fullname, txt_phonenumber,txt_address;
    TextView txt_top_name_admin, txt_top_email_admin, changefotoadmin;

    String id;
    Button update;
    String DetailAPI;

    Boolean session = false;
    int IOConnect = 0;

    public static String ADMIN_PANEL_URL = "https://www.pantaucovid19.net/";
    public static final String TAG_ID = "id";
    public static final String TAG_EMAIL = "email";
    public static final String TAG_FULLNAME = "fullname";
    public static final String TAG_PHONENUMBER = "phonenumber";
    public static final String TAG_ADDRESS = "address";
    public static final String TAG_FOTO = "foto";

    public static final String URL_UPDATE = "https://www.pantaucovid19.net/bd_update_profile_admin.php";

    private int GALLERY = 1, CAMERA = 2;
    Bitmap bitmap, decoded;
    ImageView imgPreview, imgChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile);

        sharedpreferences = getSharedPreferences(LoginAdminActivity.my_shared_preferences, Context.MODE_PRIVATE);
        id = getIntent ().getStringExtra (TAG_ID);
        //set data if data null
        id = sharedpreferences.getString (TAG_ID, null);

        String url = Server.URL+"bd_profile_admin.php?id="+id;

        //Deklarasi antara variabel dengan id field xml
        txt_id = (EditText) findViewById(R.id.id_admin);
        txt_email = (EditText) findViewById(R.id.email_admin);
        txt_fullname = (EditText) findViewById(R.id.fullname_admin);
        txt_phonenumber = (EditText) findViewById(R.id.phonenumber_admin);
        txt_address = (EditText) findViewById(R.id.address_admin);
        txt_top_name_admin = (TextView) findViewById(R.id.top_name_admin);
        txt_top_email_admin = (TextView) findViewById(R.id.top_email_admin);

        imgPreview = (ImageView) findViewById (R.id.imgPreview);
        changefotoadmin = findViewById (R.id.change_photo_admin);


        update = findViewById(R.id.btn_update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadUpdate();
            }
        });

        requestQueue = Volley.newRequestQueue(AdminProfileActivity.this);
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
                    txt_fullname.setText(list_data.get(0).get("fullname"));
                    txt_email.setText(list_data.get(0).get("email"));
                    txt_phonenumber.setText(list_data.get(0).get("phonenumber"));
                    txt_address.setText(list_data.get(0).get("address"));
                    txt_top_name_admin.setText(list_data.get(0).get("fullname"));
                    txt_top_email_admin.setText(list_data.get(0).get("email"));





                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AdminProfileActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(stringRequest);

    }




    //Update Profile Admin
    private void uploadUpdate() {
        final String email = txt_email.getText().toString().trim();
        final String fullname = txt_fullname.getText().toString().trim();
        final String phonenumber = txt_phonenumber.getText().toString().trim();
        final String address = txt_address.getText().toString().trim();


        class uploadWorkKnowledge extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(AdminProfileActivity.this, "Updating...", "Wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                if (s != null) {
                    Toast.makeText(AdminProfileActivity.this, "Update Profile Successfully", Toast.LENGTH_LONG).show();
                    startActivity(new Intent (AdminProfileActivity.this, OutletDetailsActivity.class));

                } else {
                    Toast.makeText(AdminProfileActivity.this, "Error", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(TAG_ID, id);
                hashMap.put(TAG_EMAIL, email);
                hashMap.put(TAG_FULLNAME, fullname);
                hashMap.put(TAG_PHONENUMBER, phonenumber);
                hashMap.put(TAG_ADDRESS, address);
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(URL_UPDATE, hashMap);
                return s;
            }
        }
        uploadWorkKnowledge ue = new uploadWorkKnowledge();
        ue.execute();
    }

}