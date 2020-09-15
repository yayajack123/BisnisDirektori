package id.ac.bisnisdirektori.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
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
import java.util.ArrayList;
import java.util.HashMap;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.palette.graphics.Palette;
import id.ac.bisnisdirektori.R;

public class OutletDetailsActivity extends AppCompatActivity {

    public final static String TAG_ID = "id";
    String id;
    SharedPreferences sharedpreferences;

    private int GALLERY = 1, CAMERA = 2;
    Bitmap bitmap, decoded;
    ImageView imgPreview;
    TextView txt_name_admin, txt_address_admin, txt_email_admin;
    int IOConnect = 0;
    String fullname, address, foto, email;
    String DetailAPI;
    private String id_data;
    public static String ADMIN_PANEL_URL = "https://www.pantaucovid19.net/";
    public static String AccessKey = "12345";
    public static final String KEY_ID= "id";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outlet_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Button profile = (Button) findViewById(R.id.edit_profile);
        profile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getBaseContext(), AdminProfileActivity.class);
                startActivity(intent);
            }
        });
        Button databisnis = (Button) findViewById(R.id.information);
        databisnis.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getBaseContext(), InformationActivity.class);
                startActivity(intent);
            }
        });

        Button listbisnis = (Button) findViewById(R.id.listdatabisnis);
        listbisnis.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getBaseContext(), ListInformationActivity.class);
                startActivity(intent);
            }
        });

        Button editpassword = (Button) findViewById(R.id.edit_password);
        editpassword.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getBaseContext(), AdminPasswordActivity.class);
                startActivity(intent);
            }
        });

        Button test = (Button) findViewById(R.id.managephoto);
        test.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getBaseContext(), ListManagePhotoActivity.class);
                startActivity(intent);
            }
        });







        //sharedpreferences
        sharedpreferences = getSharedPreferences (LoginAdminActivity.my_shared_preferences, Context.MODE_PRIVATE);
        //get data
        id = getIntent ().getStringExtra (KEY_ID);
        //set data if data null
        id = sharedpreferences.getString (KEY_ID, null);
        //convert id to string id1
        String id1 = id;


        txt_name_admin = (TextView) findViewById (R.id.admin_fullname);
        txt_address_admin = (TextView)findViewById (R.id.admin_address);
        txt_email_admin = (TextView)findViewById (R.id.admin_email);

        imgPreview = (ImageView) findViewById (R.id.imgPreview);


        DetailAPI = ADMIN_PANEL_URL + "/bd_outlet_profile_admin.php?id=" + id1;
        new getDataTask ().execute ();



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_home) {

            // Do something
            Intent intent = new Intent(getBaseContext(), HomeAdminActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




    //Display Profile Admin
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
                txt_name_admin.setText(fullname);
                txt_address_admin.setText(address);
                txt_email_admin.setText (email);

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
                address = detail.getString("address");
                foto = detail.getString ("foto");
                email = detail.getString ("email");


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


}