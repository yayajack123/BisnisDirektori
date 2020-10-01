package id.ac.bisnisdirektori.review;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import id.ac.bisnisdirektori.R;

public class ImageReviewActivity extends AppCompatActivity {
    public static String ADMIN_PANEL_URL = "https://www.pantaucovid19.net/";
    String foto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_review);
        Intent iGet = getIntent();
        foto = iGet.getStringExtra ("foto");
        ImageView imageView = (ImageView) findViewById(R.id.full_image_view);
        Glide.with(this)
                .load(ADMIN_PANEL_URL+foto)
                .placeholder(R.mipmap.ic_launcher)
                .into(imageView);
    }
}