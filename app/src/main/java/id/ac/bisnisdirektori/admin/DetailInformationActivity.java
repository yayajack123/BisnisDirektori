package id.ac.bisnisdirektori.admin;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.palette.graphics.Palette;
import id.ac.bisnisdirektori.MainActivity;
import id.ac.bisnisdirektori.R;

public class DetailInformationActivity extends AppCompatActivity {

    //Initialize Variable
    GoogleMap gMaps;
    String title;
    LatLng center, latLng;
    String tag_json_obj = "json_obj_req";

    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient client;



    //    ImageView imgView;
    private int GALLERY = 1, CAMERA = 2;
    Bitmap bitmap, decoded;
    ImageView imgPreview, imgChange;
    EditText txtNama, txtNotelp, txtEmail, txtWebsite, txtOpentime, txtPrice, txtKategori, txtAlamat, txtLatitude, txtLongitude, txtOtherinfo,txtFoto,editTextId;
    Button update, delete, changePhoto;
    CoordinatorLayout coordinatorLayout;
    int IOConnect = 0;
    String nama_bisnis, no_telp, email, website, opentime, price, kategori, alamat, latitude, longitude, otherinfo, foto, id_admin;
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
    public static final String KEY_FOTO = "foto";
    public static final String URL_UPDATE = "https://www.pantaucovid19.net/bd_update.php";
    public static final String URL_DELETE = "https://www.pantaucovid19.net/bd_delete.php?id_data=";
    public static final String EMP_ID = "emp_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_detail_information);


        //Obtain the SupportMapFragment
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager ().findFragmentById (R.id.google_map);
        client = LocationServices.getFusedLocationProviderClient (this);

        if (ActivityCompat.checkSelfPermission (DetailInformationActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            getCurrentLocation ();
        } else {
            ActivityCompat.requestPermissions (DetailInformationActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }


        coordinatorLayout = (CoordinatorLayout) findViewById (R.id.main_content);
        imgPreview = findViewById (R.id.imgPreview);

        txtNama = findViewById (R.id.businessname_admin);
        txtNotelp = findViewById (R.id.phonenumber_admin);
        txtEmail = findViewById (R.id.email_admin);
        txtWebsite = findViewById (R.id.web_admin);
        txtOpentime = findViewById (R.id.opentime_admin);
        txtPrice = findViewById (R.id.price_admin);
        txtKategori = findViewById (R.id.kategori_admin);
        txtAlamat = findViewById (R.id.alamat_admin);
        txtLatitude = findViewById (R.id.latitude_admin);
        txtLongitude = findViewById (R.id.longitude_admin);
        txtOtherinfo = findViewById (R.id.otherinfo_admin);

        update = findViewById (R.id.btn_update);
        delete = findViewById (R.id.btn_delete);

        // Intent to detail with id data
        editTextId = findViewById (R.id.editTextId);
        editTextId.setText (id_data);

        update.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                uploadUpdate ();
            }
        });
        delete.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                confirmDelete ();
            }
        });

        // Intent to detail with id data
        Intent iGet = getIntent ();
        id_data = iGet.getStringExtra ("ID");

        changePhoto = findViewById (R.id.change_photo);
        imgPreview = (ImageView) findViewById (R.id.imgPreview);
        changePhoto.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                showPictureDialog ();
            }
        });

        // Intent to detail with id data
        DetailAPI = ADMIN_PANEL_URL + "/bd_detail_list.php" + "?accesskey=" + AccessKey + "&ID=" + id_data;
        new getDataTask ().execute ();


    }

    //Get Current Location and Pick Marker Location
    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission (this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        Task<Location> task = client.getLastLocation ();
        task.addOnSuccessListener (new OnSuccessListener<Location> () {
            @Override
            public void onSuccess(final Location location) {
                if (location !=null){
                    supportMapFragment.getMapAsync (new OnMapReadyCallback () {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            LatLng latLng = new LatLng (location.getLatitude (),location.getLongitude ());
                            MarkerOptions options = new MarkerOptions ().position (latLng).title ("Lokasi Anda");
                            googleMap.animateCamera (CameraUpdateFactory.newLatLngZoom (latLng, 10));
                            googleMap.addMarker (options);

                            gMaps = googleMap;

                            gMaps.setOnMapClickListener (new GoogleMap.OnMapClickListener () {
                                @Override

                                public void onMapClick(LatLng latLng) {

                                    //Creating Marker
                                    MarkerOptions markerOptions = new MarkerOptions ();
                                    //Set Marker Position
                                    markerOptions.position (latLng);
                                    //Set Latitude and Longitude On Marker
                                    markerOptions.title ("Lat : "+latLng.latitude+ " , " + "Lng : " + latLng.longitude);
                                    //Clear the previously Click Position
                                    gMaps.clear ();
                                    //Zoom the Marker
                                    gMaps.animateCamera (CameraUpdateFactory.newLatLngZoom (latLng, 10));
                                    //Add Marker On Map
                                    gMaps.addMarker (markerOptions);

                                    //Set Latitude dan Longitude to Edit Text
                                    txtLatitude.setText("" + latLng.latitude);
                                    txtLongitude.setText("" + latLng.longitude);

                                }
                            });
                        }
                    });
                }else{
                    supportMapFragment.getMapAsync (new OnMapReadyCallback () {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {

                            gMaps = googleMap;

                            gMaps.setOnMapClickListener (new GoogleMap.OnMapClickListener () {
                                @Override

                                public void onMapClick(LatLng latLng) {

                                    //Creating Marker
                                    MarkerOptions markerOptions = new MarkerOptions ();
                                    //Set Marker Position
                                    markerOptions.position (latLng);
                                    //Set Latitude and Longitude On Marker
                                    markerOptions.title ("Lat : "+latLng.latitude+ " , " + "Lng : " + latLng.longitude);
                                    //Clear the previously Click Position
                                    gMaps.clear ();
                                    //Zoom the Marker
                                    gMaps.animateCamera (CameraUpdateFactory.newLatLngZoom (latLng, 10));
                                    //Add Marker On Map
                                    gMaps.addMarker (markerOptions);

                                    //Set Latitude dan Longitude to Edit Text
                                    txtLatitude.setText("" + latLng.latitude);
                                    txtLongitude.setText("" + latLng.longitude);

                                }
                            });
                        }
                    });
                }
            }
        });
    }






    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Image");
        String[] pictureDialogItems = {
                "From gallery",
                "From camera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallery();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    public String getStringImage(Bitmap bmp) {

        if (bmp != null)
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream ();
            bmp.compress (Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray ();
            String encodedImage = Base64.encodeToString (imageBytes, Base64.DEFAULT);
            return encodedImage;
        }else {
            bmp = ((BitmapDrawable) imgPreview.getDrawable ()).getBitmap ();
            ByteArrayOutputStream baos = new ByteArrayOutputStream ();
            bmp.compress (Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray ();
            String encodedImage = Base64.encodeToString (imageBytes, Base64.DEFAULT);
            return encodedImage;
        }
    }

    private void setToImageView(Bitmap bmp) {
        //compress image

        if (bmp != null)
        {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream ();
            bmp.compress (Bitmap.CompressFormat.JPEG, 100, bytes);
            decoded = BitmapFactory.decodeStream (new ByteArrayInputStream (bytes.toByteArray ()));
            imgPreview.setImageBitmap (decoded);
        }else{
            bmp = ((BitmapDrawable) imgPreview.getDrawable ()).getBitmap ();
            ByteArrayOutputStream bytes = new ByteArrayOutputStream ();
            bmp.compress (Bitmap.CompressFormat.JPEG, 100, bytes);
            decoded = BitmapFactory.decodeStream (new ByteArrayInputStream (bytes.toByteArray ()));
            imgPreview.setImageBitmap (decoded);
        }


    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();
        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {

                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                setToImageView(getResizedBitmap(bitmap, 512));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == CAMERA && resultCode == RESULT_OK) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            File destination = new File(Environment.getExternalStorageDirectory(),
                    System.currentTimeMillis() + ".jpg");
            FileOutputStream fo;
            try {
                destination.createNewFile();
                fo = new FileOutputStream(destination);
                fo.write(bytes.toByteArray());
                fo.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));
            imgPreview.setImageBitmap(decoded);
            //Bitmap bitmap = (Bitmap)data.getExtras().get("data");
            //setToImageView(getResizedBitmap(bitmap,1080));
            // imgView.setImageURI(image_uri);
        }

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
                id_admin = detail.getString ("id_admin");
                foto = detail.getString ("foto");

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

        // Create the ParseFile

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
        final String foto = getStringImage(decoded);


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
                    Toast.makeText(DetailInformationActivity.this, "Updating Data Successfully", Toast.LENGTH_LONG).show();
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
                hashMap.put(KEY_FOTO, foto);
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
        alertDialogBuilder.setMessage("Yakin Ingin Menghapus Data?");
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



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 44){
            if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getCurrentLocation ();
            }
        }
    }






}