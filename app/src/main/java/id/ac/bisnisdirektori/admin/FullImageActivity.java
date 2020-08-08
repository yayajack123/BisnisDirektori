package id.ac.bisnisdirektori.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import id.ac.bisnisdirektori.R;

public class FullImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);
        Intent i = getIntent();
        // Get Selected Image Id
        int position = i.getExtras().getInt("id");
        adapterListDetailPhoto imageAdapter = new adapterListDetailPhoto(this);
        ImageView imageView = (ImageView) findViewById(R.id.full_image_view);
        imageView.setImageResource((Integer) imageAdapter.getItem(position));
    }
}