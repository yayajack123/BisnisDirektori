package id.ac.bisnisdirektori.admin;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

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
import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.palette.graphics.Palette;
import id.ac.bisnisdirektori.R;

public class DetailInformationActivity extends AppCompatActivity {

    ImageView imgPreview;
    EditText txtNama, txtNotelp, txtEmail, txtWebsite, txtOpentime, txtPrice, txtKategori, txtAlamat, txtLatitude, txtLongitude, txtOtherinfo, editTextId;
    Button update, delete;
    CoordinatorLayout coordinatorLayout;
    int IOConnect = 0;
    String nama_bisnis, no_telp, email, website, opentime, price, kategori, alamat, latitude, longitude, otherinfo, foto;
    String DetailAPI;
    private String id_data;
    public static String ADMIN_PANEL_URL = "https://www.pantaucovid19.net/";
    public static String AccessKey = "12345";
    public static final String KEY_IDDATA = "id_data";
    public static final String KEY_NAMA_BISNIS = "nama_bisnis";
    public static final String KEY_NOTELP = "no_telp";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_WEBSITE = "website";
    public static final String KEY_OPENTIME = "opentime";
    public static final String KEY_PRICE = "price";
    public static final String KEY_KATEGORI = "kategori";
    public static final String KEY_ALAMAT = "alamat";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_OTHERINFO = "otherinfo";
    public static final String URL_UPDATE = "https://www.pantaucovid19.net/bd_update.php";
    public static final String URL_DELETE = "https://www.pantaucovid19.net/bd_delete.php?id_data=";
    public static final String EMP_ID = "emp_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_detail_information);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_content);
        imgPreview = findViewById(R.id.imgPreview);
        txtNama = findViewById(R.id.businessname_admin);
        txtNotelp = findViewById(R.id.phonenumber_admin);
        txtEmail = findViewById(R.id.email_admin);
        txtWebsite= findViewById(R.id.web_admin);
        txtOpentime = findViewById(R.id.opentime_admin);
        txtPrice = findViewById(R.id.price_admin);
        txtKategori = findViewById(R.id.kategori_admin);
        txtAlamat = findViewById(R.id.alamat_admin);
        txtLatitude = findViewById(R.id.latitude_admin);
        txtLongitude = findViewById(R.id.longitude_admin);
        txtOtherinfo = findViewById(R.id.otherinfo_admin);

        update = findViewById(R.id.btn_update);
        delete = findViewById(R.id.btn_delete);
        editTextId = findViewById(R.id.editTextId);
        editTextId.setText(id_data);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadUpdate();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDelete();
            }
        });
        Intent iGet = getIntent();
        id_data = iGet.getStringExtra("ID");
        DetailAPI = ADMIN_PANEL_URL + "/bd_detail_list.php" + "?accesskey=" + AccessKey + "&ID=" + id_data;
        new getDataTask().execute();


    }

    public class getDataTask extends AsyncTask<Void, Void, Void> {
        // show progressbar first
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
            // if internet connection and data available show data
            // otherwise, show alert text
            if ((nama_bisnis != null) && IOConnect == 0) {
//                coordinatorLayout.setVisibility(View.VISIBLE);
                Picasso.with(getApplicationContext()).load(ADMIN_PANEL_URL + "/" + foto).placeholder(R.drawable.logogeografis).into(imgPreview, new Callback () {
                    @Override
                    public void onSuccess() {
                        Bitmap bitmap = ((BitmapDrawable) imgPreview.getDrawable()).getBitmap();
                        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(Palette palette) {
                            }
                        });
                    }

                    @Override
                    public void onError() {
                    }
                });
                txtNama.setText(nama_bisnis);
                txtNotelp.setText(no_telp);
                txtEmail.setText(email);
                txtWebsite.setText(website);
                txtOpentime.setText(opentime);
                txtPrice.setText(price);
                txtKategori.setText(kategori);
                txtAlamat.setText(alamat);
                txtLatitude.setText(latitude);
                txtLongitude.setText(longitude);
                txtOtherinfo.setText(otherinfo);

            }
        }
    }

    // method to parse json data from server
    public void parseJSONData() {
        try {
            // request data from menu detail API
            HttpClient client = new DefaultHttpClient ();
            HttpConnectionParams.setConnectionTimeout(client.getParams(), 15000);
            HttpConnectionParams.setSoTimeout(client.getParams(), 15000);
            HttpUriRequest request = new HttpGet (DetailAPI);
            HttpResponse response = client.execute(request);
            InputStream atomInputStream = response.getEntity().getContent();
            BufferedReader in = new BufferedReader(new InputStreamReader (atomInputStream));
            String line;
            String str = "";
            while ((line = in.readLine()) != null) {
                str += line;
            }
            // parse json data and store into tax and currency variables
            JSONObject json = new JSONObject(str);
            JSONArray data = json.getJSONArray("data"); // this is the "items: [ ] part
            for (int i = 0; i < data.length(); i++) {
                JSONObject object = data.getJSONObject(i);
                JSONObject detail = object.getJSONObject("Staff_detail");
                foto = detail.getString("foto");
                nama_bisnis = detail.getString("nama_bisnis");
                no_telp = detail.getString("no_telp");
                email = detail.getString("email");
                website = detail.getString("website");
                opentime = detail.getString("opentime");
                price = detail.getString("price");
                kategori = detail.getString("kategori");
                alamat = detail.getString("alamat");
                latitude = detail.getString("latitude");
                longitude = detail.getString("longitude");
                otherinfo = detail.getString("otherinfo");

            }
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            IOConnect = 1;
            e.printStackTrace();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    @Override
    public void onConfigurationChanged(final Configuration newConfig) {
        // Ignore orientation change to keep activity from restarting
        super.onConfigurationChanged(newConfig);
    }

    private void uploadUpdate() {
        final String nama_bisnis = txtNama.getText().toString().trim();
        final String no_telp = txtNotelp.getText().toString().trim();
        final String email = txtEmail.getText().toString().trim();
        final String website = txtWebsite.getText().toString().trim();
        final String opentime = txtOpentime.getText().toString().trim();
        final String price = txtPrice.getText().toString().trim();
        final String kategori = txtKategori.getText().toString().trim();
        final String alamat = txtAlamat.getText().toString().trim();
        final String latitude = txtLatitude.getText().toString().trim();
        final String longitude = txtLongitude.getText().toString().trim();
        final String otherinfo = txtOtherinfo.getText().toString().trim();

        class uploadWorkKnowledge extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(DetailInformationActivity.this, "Updating...", "Wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                if (s != null) {
                    Toast.makeText(DetailInformationActivity.this, "Success", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(DetailInformationActivity.this, ListInformationActivity.class));

                } else {
                    Toast.makeText(DetailInformationActivity.this, "Error", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(KEY_IDDATA, id_data);
                hashMap.put(KEY_NAMA_BISNIS, nama_bisnis);
                hashMap.put(KEY_NOTELP, no_telp);
                hashMap.put(KEY_EMAIL, email);
                hashMap.put(KEY_WEBSITE, website);
                hashMap.put(KEY_OPENTIME, opentime);
                hashMap.put(KEY_PRICE, price);
                hashMap.put(KEY_KATEGORI, kategori);
                hashMap.put(KEY_ALAMAT, alamat);
                hashMap.put(KEY_LATITUDE, latitude);
                hashMap.put(KEY_LONGITUDE, longitude);
                hashMap.put(KEY_OTHERINFO, otherinfo);
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(URL_UPDATE, hashMap);
                return s;
            }
        }
        uploadWorkKnowledge ue = new uploadWorkKnowledge();
        ue.execute();
    }

    private void delete() {
        class Delete extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(DetailInformationActivity.this, "Updating...", "Wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(DetailInformationActivity.this, s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(URL_DELETE, id_data);
                return s;
            }
        }
        Delete de = new Delete();
        de.execute();
    }

    private void confirmDelete() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Yakin ?");
        alertDialogBuilder.setPositiveButton("Ya",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        delete();
                        startActivity(new Intent(DetailInformationActivity.this, ListInformationActivity.class));
                    }
                });
        alertDialogBuilder.setNegativeButton("Tidak",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }





}