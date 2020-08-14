package id.ac.bisnisdirektori.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.palette.graphics.Palette;
import id.ac.bisnisdirektori.R;

public class AdminProfileActivity extends AppCompatActivity {

    public final static String TAG_ID = "id";
    String id;
    SharedPreferences sharedpreferences;

    private int GALLERY = 1, CAMERA = 2;
    Bitmap bitmap, decoded;
    ImageView imgPreview, imgChange;
    EditText txtFullname, txtPhonenumber, txtEmail, txtAddress,txtFoto,editTextId;
    TextView txt_top_fullname, txt_top_email, changePhoto;
    Button update, delete;
    CoordinatorLayout coordinatorLayout;
    int IOConnect = 0;
    String fullname, phonenumber, email, address, foto;
    String DetailAPI;
    private String id_data;
    public static String ADMIN_PANEL_URL = "https://www.pantaucovid19.net/";
    public static String AccessKey = "12345";
    public static final String KEY_ID= "id";
    public static final String KEY_FULLNAME = "fullname";
    public static final String KEY_PHONENUMBER = "phonenumber";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_FOTO = "foto";
    public static final String URL_UPDATE = "https://www.pantaucovid19.net/bd_update_profile_admin.php";
    public static final String EMP_ID = "emp_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_admin_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //sharedpreferences
        sharedpreferences = getSharedPreferences (LoginAdminActivity.my_shared_preferences, Context.MODE_PRIVATE);
        //get data
        id = getIntent ().getStringExtra (KEY_ID);
        //set data if data null
        id = sharedpreferences.getString (KEY_ID, null);
        //convert id to string id1
        String id1 = id;

        txtFullname = findViewById (R.id.fullname_admin);
        txtPhonenumber = findViewById (R.id.phonenumber_admin);
        txtEmail = findViewById (R.id.email_admin);
        txtAddress = findViewById (R.id.address_admin);
        txt_top_fullname = (TextView) findViewById (R.id.top_name_admin);
        txt_top_email = (TextView)findViewById (R.id.top_email_admin);


        update = findViewById (R.id.btn_update);
        update.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                uploadUpdate ();
            }
        });


        changePhoto = findViewById (R.id.change_photo_admin);
        imgPreview = (ImageView) findViewById (R.id.imgPreview);
        changePhoto.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                showPictureDialog ();
            }
        });

        DetailAPI = ADMIN_PANEL_URL + "/bd_detail_profile_admin.php?id=" + id1;
        new getDataTask ().execute ();


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
            ByteArrayOutputStream baso = new ByteArrayOutputStream ();
            bmp.compress (Bitmap.CompressFormat.JPEG, 100, baso);
            byte[] imageBytes = baso.toByteArray ();
            String encodedImage = Base64.encodeToString (imageBytes, Base64.DEFAULT);
            return encodedImage;
        }else {
            bmp = ((BitmapDrawable) imgPreview.getDrawable ()).getBitmap ();
            ByteArrayOutputStream baso = new ByteArrayOutputStream ();
            bmp.compress (Bitmap.CompressFormat.JPEG, 100, baso);
            byte[] imageBytes = baso.toByteArray ();
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
            if ((fullname != null) && IOConnect == 0) {
//                coordinatorLayout.setVisibility(View.VISIBLE);
                Picasso.with(getApplicationContext()).load(ADMIN_PANEL_URL + "/" + foto).placeholder(R.drawable.userthumb).into(imgPreview, new Callback () {
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
                txtFullname.setText(fullname);
                txtPhonenumber.setText(phonenumber);
                txtEmail.setText(email);
                txtAddress.setText(address);
                txt_top_fullname.setText (fullname);
                txt_top_email.setText (email);



            }
        }
    }

    // method to parse json data from server
    public void parseJSONData() {
        //sharedpreferences
        sharedpreferences = getSharedPreferences (LoginAdminActivity.my_shared_preferences, Context.MODE_PRIVATE);
        //get data
        id = getIntent ().getStringExtra (TAG_ID);
        //set data if data null
        id = sharedpreferences.getString (TAG_ID, null);
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
                fullname = detail.getString("fullname");
                phonenumber = detail.getString("phonenumber");
                email = detail.getString("email");
                address = detail.getString("address");
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

    //Update Data Profile Admin
    private void uploadUpdate() {

        // Create the ParseFile
        final String fullname = txtFullname.getText().toString().trim();
        final String phonenumber = txtPhonenumber.getText().toString().trim();
        final String email = txtEmail.getText().toString().trim();
        final String address = txtAddress.getText().toString().trim();
        final String foto = getStringImage(decoded);


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
                    startActivity(new Intent(AdminProfileActivity.this, OutletDetailsActivity.class));

                } else {
                    Toast.makeText(AdminProfileActivity.this, "Error", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(KEY_ID, id);
                hashMap.put(KEY_FULLNAME, fullname);
                hashMap.put(KEY_PHONENUMBER, phonenumber);
                hashMap.put(KEY_EMAIL, email);
                hashMap.put(KEY_ADDRESS, address);
                hashMap.put(KEY_FOTO, foto);
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(URL_UPDATE, hashMap);
                return s;
            }
        }
        uploadWorkKnowledge ue = new uploadWorkKnowledge();
        ue.execute();

    }

}