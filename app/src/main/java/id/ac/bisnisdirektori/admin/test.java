//package id.ac.bisnisdirektori.admin;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.content.res.Configuration;
//import android.graphics.Bitmap;
//import android.graphics.drawable.BitmapDrawable;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.squareup.picasso.Callback;
//import com.squareup.picasso.Picasso;
//
//import org.apache.http.HttpResponse;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.client.methods.HttpUriRequest;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.params.HttpConnectionParams;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.net.MalformedURLException;
//import java.util.HashMap;
//
//import androidx.palette.graphics.Palette;
//import id.ac.bisnisdirektori.R;
//
//public class AdminProfileActivity extends AppCompatActivity {
//
//    ImageView imgPreview;
//    EditText txt_id, txt_email,txt_fullname, txt_phonenumber, txt_password;
//    Button update, changePhoto;
//    int IOConnect = 0;
//    private String id;
//    public static String ADMIN_PANEL_URL = "https://www.pantaucovid19.net/";
//    public static final String KEY_ID = "id";
//    public static final String KEY_EMAIL = "email";
//    public static final String KEY_FULLNAME = "fullname";
//    public static final String KEY_PHONENUMBER= "phonenumber";
//    public static final String KEY_ADDRESS = "address";
//    public static final String KEY_PASSWORD = "password";
//    public static final String KEY_FOTO = "foto";
//    public static final String URL_UPDATE = "https://www.pantaucovid19.net/bd_update_profile_admin.php";
//
//    String email, fullname, phonenumber, password, foto;
//    String DetailAPI;
//
//    String tag_json_obj = "json_obj_req";
//    SharedPreferences sharedpreferences;
//    Boolean session = false;
//
//    public static final String my_shared_preferences = "my_shared_preferences";
//    public static final String session_status = "session_status";
//
//    public static final String TAG_ID = "id";
//    public static final String TAG_EMAIL = "email";
//    public static final String TAG_FULLNAME = "fullname";
//    public static final String TAG_PHONENUMBER = "phonenumber";
//    public static final String TAG_PASSWORD = "password";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_admin_profile);
//
//        //Deklarasi antara variabel dengan id field xml
//        txt_id = (EditText) findViewById(R.id.id_admin);
//        txt_email = (EditText) findViewById(R.id.email_admin);
//        txt_fullname = (EditText) findViewById(R.id.fullname_admin);
//        txt_phonenumber = (EditText) findViewById(R.id.phonenumber_admin);
//        txt_password = (EditText) findViewById(R.id.password_admin);
//        update = findViewById(R.id.btn_update);
//
//
//        update.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                uploadUpdate();
//            }
//        });
//
//
//        //sharedpreferences
//        sharedpreferences = getSharedPreferences(LoginAdminActivity.my_shared_preferences, Context.MODE_PRIVATE);
//
//        //get data
//        id = getIntent().getStringExtra(TAG_ID);
//        email = getIntent().getStringExtra(TAG_EMAIL);
//        fullname = getIntent().getStringExtra(TAG_FULLNAME);
//        phonenumber = getIntent().getStringExtra(TAG_PHONENUMBER);
//        password = getIntent().getStringExtra(TAG_PASSWORD);
////
//        //set data if data null
//        id = sharedpreferences.getString(TAG_ID, null);
//        email = sharedpreferences.getString(TAG_EMAIL, null);
//        fullname = sharedpreferences.getString(TAG_FULLNAME, null);
//        phonenumber = sharedpreferences.getString(TAG_PHONENUMBER, null);
//        password = sharedpreferences.getString(TAG_PASSWORD, null);
//
//        //set data
////        txt_id.setText(TAG_ID);
////        txt_email.setText(TAG_EMAIL);
////        txt_fullname.setText(TAG_FULLNAME);
////        txt_phonenumber.setText(TAG_PHONENUMBER);
////        txt_password.setText(TAG_PASSWORD);
//        txt_id.setText("" + id);
//        txt_email.setText("" + email);
//        txt_fullname.setText("" + fullname);
//        txt_phonenumber.setText("" + phonenumber);
//        txt_password.setText("" + password);
//
//        new AdminProfileActivity.getDataTask ().execute();
//
//
//    }
//
//
//
//
//
//
//    //variabel txt buat ngelempar variabel ke database
//
//    public class getDataTask extends AsyncTask<Void, Void, Void> {
//        // show progressbar first
//        @Override
//        protected Void doInBackground(Void... arg0) {
//            // TODO Auto-generated method stub
//            // parse json data from server in background
////            parseJSONData ();
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void result) {
//            // TODO Auto-generated method stub
//            // when finish parsing, hide progressbar
//            // if internet connection and data available show data
//            // otherwise, show alert text
//            if ((email != null) && IOConnect == 0) {
////                coordinatorLayout.setVisibility(View.VISIBLE);
////                Picasso.with(getApplicationContext()).load(ADMIN_PANEL_URL + "/" + foto).placeholder(R.drawable.logogeografis).into(imgPreview, new Callback () {
////                    @Override
////                    public void onSuccess() {
////                        Bitmap bitmap = ((BitmapDrawable) imgPreview.getDrawable()).getBitmap();
////                        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
////                            @Override
////                            public void onGenerated(Palette palette) {
////                            }
////                        });
////                    }
////
////                    @Override
////                    public void onError() {
////                    }
////                });
//                txt_fullname.setText(fullname);
//                txt_phonenumber.setText(phonenumber);
//                txt_password.setText(password);
//
//            }
//        }
//    }
//
////    // method to parse json data from server
////    public void parseJSONData() {
////        try {
////            // request data from menu detail API
////            HttpClient client = new DefaultHttpClient ();
////            HttpConnectionParams.setConnectionTimeout(client.getParams(), 15000);
////            HttpConnectionParams.setSoTimeout(client.getParams(), 15000);
////            HttpUriRequest request = new HttpGet (DetailAPI);
////            HttpResponse response = client.execute(request);
////            InputStream atomInputStream = response.getEntity().getContent();
////            BufferedReader in = new BufferedReader(new InputStreamReader (atomInputStream));
////            String line;
////            String str = "";
////            while ((line = in.readLine()) != null) {
////                str += line;
////            }
////            // parse json data and store into tax and currency variables
////            JSONObject json = new JSONObject(str);
////            JSONArray data = json.getJSONArray("data"); // this is the "items: [ ] part
////            for (int i = 0; i < data.length(); i++) {
////                JSONObject object = data.getJSONObject(i);
////                JSONObject detail = object.getJSONObject("Staff_detail");
////                fullname = detail.getString("fullname");
////                phonenumber = detail.getString("phonenumber");
////                password = detail.getString("password");
////
////            }
////        } catch (MalformedURLException e) {
////            // TODO Auto-generated catch block
////            e.printStackTrace();
////        } catch (IOException e) {
////            // TODO Auto-generated catch block
////            IOConnect = 1;
////            e.printStackTrace();
////        } catch (JSONException e) {
////            // TODO Auto-generated catch block
////            e.printStackTrace();
////        }
////    }
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//    private void uploadUpdate() {
//        final String email = txt_email.getText().toString().trim();
//        final String fullname = txt_fullname.getText().toString().trim();
//        final String phonenumber = txt_phonenumber.getText().toString().trim();
//        final String password = txt_password.getText().toString().trim();
//
//        class uploadWorkKnowledge extends AsyncTask<Void, Void, String> {
//            ProgressDialog loading;
//
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//                loading = ProgressDialog.show(AdminProfileActivity.this, "Updating...", "Wait...", false, false);
//            }
//
//            @Override
//            protected void onPostExecute(String s) {
//                super.onPostExecute(s);
//                loading.dismiss();
//                if (s != null) {
//                    Toast.makeText(AdminProfileActivity.this, "Success", Toast.LENGTH_LONG).show();
//                    startActivity(new Intent (AdminProfileActivity.this, AdminProfileActivity.class));
//
//                } else {
//                    Toast.makeText(AdminProfileActivity.this, "Error", Toast.LENGTH_LONG).show();
//                }
//            }
//
//            @Override
//            protected String doInBackground(Void... params) {
//                HashMap<String, String> hashMap = new HashMap<>();
//                hashMap.put(TAG_ID, id);
//                hashMap.put(TAG_EMAIL, email);
//                hashMap.put(TAG_FULLNAME, fullname);
//                hashMap.put(TAG_PHONENUMBER, phonenumber);
//                hashMap.put(TAG_PASSWORD, password);
//                RequestHandler rh = new RequestHandler();
//                String s = rh.sendPostRequest(URL_UPDATE, hashMap);
//                return s;
//            }
//        }
//        uploadWorkKnowledge ue = new uploadWorkKnowledge();
//        ue.execute();
//    }
//
//
//
//
//}