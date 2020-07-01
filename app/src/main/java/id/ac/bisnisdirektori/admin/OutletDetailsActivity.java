package id.ac.bisnisdirektori.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import id.ac.bisnisdirektori.R;

public class OutletDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outlet_details);

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
    }
}