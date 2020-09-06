package id.ac.bisnisdirektori.review;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;

import id.ac.bisnisdirektori.LoginActivity;
import id.ac.bisnisdirektori.R;

public class PostReviewActivity extends AppCompatActivity {

    SharedPreferences sharedpreferences;

    public static final String TAG_ID = "id";
    String id;
    int IOConnect = 0;
    private String id_data, nama_bisnis;

    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    private android.widget.RatingBar RatingBar;
    private TextView rate;

    EditText rating, foto, review;
    Button submit, viewList;
    String idd, idu, rat, fot, rev;
    public static final String Image = "image";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_review);

        sharedpreferences = getSharedPreferences(LoginActivity.my_shared_preferences, Context.MODE_PRIVATE);

        id = sharedpreferences.getString("id_user", "null");

        Intent iGet = getIntent();
        id_data = iGet.getStringExtra("id_data");
        nama_bisnis = iGet.getStringExtra("nama_bisnis");

        TextView title = (TextView) findViewById(R.id.title_product);
        title.setText(nama_bisnis);

        rate = findViewById(R.id.txt_rate);
        RatingBar = findViewById(R.id.rating_product);
        RatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float nilai, boolean b) {
                rate.setText(String.valueOf(nilai));
            }
        });



//        submit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                rat = rate.getText().toString();
//                fot = foto.getText().toString();
//                rev = review.getText().toString();
//
//                final DataBuku data = new DataBuku(
////                        jud, gen, sin, Integer.parseInt(sto), Integer.parseInt(hrg), Integer.parseInt(pen)
//                        id_data, id, sin, Integer.parseInt(hrg), Integer.parseInt(pen)
//                );
//                AppExecutors.getInstance().diskIO().execute(new Runnable() {
//                    @Override
//                    public void run() {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                mDb.dataBukuDao().insertBuku(data);
//                            }
//                        });
//                    }
//                });
//                uploadCRUD();
//                startActivity(new Intent(AdminTambahBukuActivity.this, AdminBukuActivity.class));
//            }
//        });
//        ivCamera.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SelectImageFromGallery();
//            }
//        });
    }
}