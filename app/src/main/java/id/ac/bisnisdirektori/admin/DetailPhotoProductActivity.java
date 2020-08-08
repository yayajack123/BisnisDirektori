package id.ac.bisnisdirektori.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class DetailPhotoProductActivity extends AppCompatActivity {

    private String id_data1;

    //    ImageView imgView;
    private int GALLERY = 1, CAMERA = 2;
    Bitmap bitmap, decoded;
    ImageView imgPreview, imgChange;
    EditText txtFoto,editTextId, editIdData;
    Button update, delete, changePhoto;
    CoordinatorLayout coordinatorLayout;
    int IOConnect = 0;
    String foto, id_data;
    String DetailAPI;
    private String id_foto;
    public static String ADMIN_PANEL_URL = "https://www.pantaucovid19.net/";
    public static String AccessKey = "12345";
    public static final String KEY_IDFOTO = "id_foto";
    public static final String KEY_FOTO = "foto";
    public static final String KEY_IDDATA = "id_data";
    public static final String URL_UPDATE = "https://www.pantaucovid19.net/bd_update_photo_product.php";
    public static final String URL_DELETE = "https://www.pantaucovid19.net/bd_delete_photo_product.php?id_foto=";
    public static final String EMP_ID = "emp_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_detail_photo_product);

//        // Intent to detail with id data
//        Intent iGet = getIntent ();
//        id_data1 = iGet.getStringExtra ("id_data");
//        String idData = id_data1;


        coordinatorLayout = (CoordinatorLayout) findViewById (R.id.main_content);
        imgPreview = findViewById (R.id.imgPreview);


//        update.setOnClickListener (new View.OnClickListener () {
//            @Override
//            public void onClick(View v) {
//                uploadUpdate ();
//            }
//        });
//        delete.setOnClickListener (new View.OnClickListener () {
//            @Override
//            public void onClick(View v) {
//                confirmDelete ();
//            }
//        });

        // Intent to detail with id data
        Intent iGet = getIntent ();
        id_foto = iGet.getStringExtra ("id_foto");
        id_data = iGet.getStringExtra ("id_data");

        changePhoto = findViewById (R.id.change_photo);
        imgPreview = (ImageView) findViewById (R.id.imgPreview);
        changePhoto.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                showPictureDialog ();
            }
        });

        update = findViewById (R.id.btn_update);
        delete = findViewById (R.id.btn_delete);

        // Intent to detail with id data
        editTextId = findViewById (R.id.editTextId);
        editTextId.setText (id_foto);

        editIdData = findViewById (R.id.id_data);
        editIdData.setText (id_data);

        // Intent to detail with id data
        DetailAPI = ADMIN_PANEL_URL + "/bd_detail_list_photo.php?id_foto="+ id_foto;
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
            if ((foto != null) && IOConnect == 0) {
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
                id_data = detail.getString ("id_data");
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
        final String foto = getStringImage(decoded);


        class uploadWorkKnowledge extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(DetailPhotoProductActivity.this, "Updating...", "Wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                if (s != null) {
                    Toast.makeText(DetailPhotoProductActivity.this, "Success", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(DetailPhotoProductActivity.this, ListPhotoActivity.class));

                } else {
                    Toast.makeText(DetailPhotoProductActivity.this, "Error", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(KEY_IDFOTO, id_foto);
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
                loading = ProgressDialog.show(DetailPhotoProductActivity.this, "Updating...", "Wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(DetailPhotoProductActivity.this, s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(URL_DELETE, id_foto);
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
                        startActivity(new Intent(DetailPhotoProductActivity.this, ListPhotoActivity.class));
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