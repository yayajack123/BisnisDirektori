package id.ac.bisnisdirektori.admin;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import id.ac.bisnisdirektori.R;

public class InformationActivity extends AppCompatActivity{

    //Shared Preferences from Login Admin
    public final static String TAG_ID = "id";
    private String id;
    SharedPreferences sharedpreferences;

    //Initialize Variable
    GoogleMap gMaps;

    //Current Location
    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient client;

    EditText edtNama, edtNotelp, edtEmail, edtWebsite, edtOpentime, edtPrice, edtKategori, edtAlamat, edtLatitude, edtLongitude,edtOtherinfo,edtAdmin;
    Button submit, viewList, changePhoto;
    ImageView imgView;
    private int GALLERY = 1, CAMERA = 2;
    Bitmap bitmap, decoded;
    private String UPLOAD_URL = "https://www.pantaucovid19.net/bd_upload.php";
    int success;
    private static final String TAG = InformationActivity.class.getSimpleName();
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private String KEY_NAMA = "nama_bisnis";
    private String KEY_NOTELP = "no_telp";
    private String KEY_EMAIL = "email";
    private String KEY_WEBSITE = "website";
    private String KEY_OPENTIME = "opentime";
    private String KEY_PRICE = "price";
    private String KEY_KATEGORI = "kategori";
    private String KEY_ALAMAT = "alamat";
    private String KEY_LATITUDE = "latitude";
    private String KEY_LONGITUDE = "longitude";
    private String KEY_FOTO = "foto";
    private String KEY_OTHERINFO = "otherinfo";
    private String KEY_ADMIN = "id_admin";
    private static final String KEY_EMPTY = "";
    String tag_json_obj = "json_obj_req";
    String nama_bisnis, no_telp,email, website, opentime, price, kategori, alamat, latitude, longitude, foto, otherinfo, id_admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //Obtain the SupportMapFragment
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager ().findFragmentById (R.id.google_map);
        client = LocationServices.getFusedLocationProviderClient (this);

        if (ActivityCompat.checkSelfPermission (InformationActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            getCurrentLocation ();
        } else {
            ActivityCompat.requestPermissions (InformationActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }

        //sharedpreferences
        sharedpreferences = getSharedPreferences(LoginAdminActivity.my_shared_preferences, Context.MODE_PRIVATE);
        //get data
        id = getIntent().getStringExtra(TAG_ID);
        //set data if data null
        id = sharedpreferences.getString(TAG_ID, null);

        edtNama = findViewById(R.id.businessname_admin);
        edtNotelp = findViewById(R.id.phonenumber_admin);
        edtEmail = findViewById(R.id.email_admin);
        edtWebsite = findViewById(R.id.web_admin);
        edtOpentime = findViewById(R.id.opentime_admin);
        edtPrice = findViewById(R.id.price_admin);
        edtKategori = findViewById(R.id.kategori_admin);
        edtAlamat= findViewById(R.id.alamat_admin);
        edtLatitude = findViewById(R.id.latitude_admin);
        edtLongitude = findViewById(R.id.longitude_admin);
        edtOtherinfo = findViewById(R.id.otherinfo_admin);
        edtAdmin = findViewById(R.id.id_admin);

        edtAdmin.setText(id);

        changePhoto = findViewById (R.id.change_photo);
        imgView = findViewById(R.id.imgView);
        submit = findViewById(R.id.btn_submit);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Kategori, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        changePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();
            }
        });





        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nama_bisnis = edtNama.getText().toString();
                no_telp = edtNotelp.getText().toString();
                email = edtEmail.getText().toString();
                website = edtWebsite.getText().toString();
                opentime = edtOpentime.getText().toString();
                price = edtPrice.getText().toString();
                kategori = edtKategori.getText().toString();
                alamat = edtAlamat.getText().toString();
                latitude = edtLatitude.getText().toString();
                longitude = edtLongitude.getText().toString();
                otherinfo = edtOtherinfo.getText ().toString ();
                id_admin = edtAdmin.getText().toString();

                uploadCRUD();

//                startActivity(new Intent (InformationActivity.this, ListInformationActivity.class));
//                finish ();

            }
        });
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
                                    edtLatitude.setText("" + latLng.latitude);
                                    edtLongitude.setText("" + latLng.longitude);

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
                                    edtLatitude.setText("" + latLng.latitude);
                                    edtLongitude.setText("" + latLng.longitude);

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
            bmp = ((BitmapDrawable) imgView.getDrawable ()).getBitmap ();
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
            imgView.setImageBitmap (decoded);
        }else{
            bmp = ((BitmapDrawable) imgView.getDrawable ()).getBitmap ();
            ByteArrayOutputStream bytes = new ByteArrayOutputStream ();
            bmp.compress (Bitmap.CompressFormat.JPEG, 100, bytes);
            decoded = BitmapFactory.decodeStream (new ByteArrayInputStream (bytes.toByteArray ()));
            imgView.setImageBitmap (decoded);
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
            imgView.setImageBitmap(decoded);
            //Bitmap bitmap = (Bitmap)data.getExtras().get("data");
            //setToImageView(getResizedBitmap(bitmap,1080));
            // imgView.setImageURI(image_uri);
        }
    }



    private void uploadCRUD() {
        //menampilkan progress dialog
        final ProgressDialog loading = ProgressDialog.show(this, "Uploading...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "Response: " + response.toString());
                        try {
                            JSONObject jObj = new JSONObject(response);
                            success = jObj.getInt(TAG_SUCCESS);
                            if (success == 1) {
                                Log.e("v Add", jObj.toString());
                                Toast.makeText(InformationActivity.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                                finish();
                                startActivity(getIntent());
                            } else {
                                Toast.makeText(InformationActivity.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //menghilangkan progress dialog
                        loading.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //menghilangkan progress dialog
                        loading.dismiss();
                        //menampilkan toast
                        Toast.makeText(InformationActivity.this, "Please Complete Form & Image".toString(), Toast.LENGTH_LONG).show();
                        Log.e(TAG, error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                //membuat parameters
                Map<String, String> params = new HashMap<String, String> ();
                //menambah parameter yang di kirim ke web servis
                params.put(KEY_NAMA, edtNama.getText().toString().trim());
                params.put(KEY_NOTELP, edtNotelp.getText().toString().trim());
                params.put(KEY_EMAIL, edtEmail.getText().toString().trim());
                params.put(KEY_WEBSITE, edtWebsite.getText().toString().trim());
                params.put(KEY_OPENTIME, edtOpentime.getText().toString().trim());
                params.put(KEY_PRICE, edtPrice.getText().toString().trim());
                params.put(KEY_KATEGORI, edtKategori.getText().toString().trim());
                params.put(KEY_ALAMAT, edtAlamat.getText().toString().trim());
                params.put(KEY_LATITUDE, edtLatitude.getText().toString().trim());
                params.put(KEY_LONGITUDE, edtLongitude.getText().toString().trim());
                params.put(KEY_OTHERINFO, edtOtherinfo.getText().toString().trim());
                params.put(KEY_ADMIN, edtAdmin.getText().toString().trim());
                params.put(KEY_FOTO, getStringImage(decoded));
                //kembali ke parameters
                Log.e(TAG, "" + params);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest, tag_json_obj);
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