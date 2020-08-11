package id.ac.bisnisdirektori.admin;
import id.ac.bisnisdirektori.LoginActivity;
import id.ac.bisnisdirektori.admin.Server;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.navigation.ui.AppBarConfiguration;
import id.ac.bisnisdirektori.R;

public class HomeAdminActivity extends AppCompatActivity {

    SharedPreferences sharedpreferences;

    public static final String TAG_ID = "id";
    public static final String TAG_EMAIL = "email";
    public static final String TAG_FULLNAME = "fullname";
    public static final String TAG_PHONENUMBER = "phonenumber";
    public static final String TAG_ADDRESS = "address";
    public static final String TAG_PASSWORD = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_admin);


        //sharedpreferences
        sharedpreferences = getSharedPreferences(LoginAdminActivity.my_shared_preferences, Context.MODE_PRIVATE);




        CardView profile = (CardView) findViewById(R.id.card_profile);
        profile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getBaseContext(), OutletDetailsActivity.class);
                startActivity(intent);
            }
        });

        CardView promosi = (CardView) findViewById(R.id.card_promosi);
        promosi.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getBaseContext(), ListManagePromosiActivity.class);
                startActivity(intent);
            }
        });

        CardView ulasan = (CardView) findViewById(R.id.card_ulasan);
        ulasan.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getBaseContext(), ListManageUlasanActivity.class);
                startActivity(intent);
            }
        });

        CardView call = (CardView) findViewById(R.id.card_call);
        call.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getBaseContext(), ListManageCallActivity.class);
                startActivity(intent);
            }
        });






        Button logout_admin = (Button) findViewById(R.id.logout_admin);
        logout_admin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getBaseContext(), LoginAdminActivity.class);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean(LoginAdminActivity.session_status, false);
                editor.putString(TAG_ID, null);
                editor.putString(TAG_EMAIL, null);
                editor.putString(TAG_FULLNAME, null);
                editor.putString(TAG_PHONENUMBER, null);
                editor.putString(TAG_ADDRESS, null);
                editor.putString(TAG_PASSWORD, null);
                editor.clear();
                editor.apply();
                editor.commit();
                finish ();
                startActivity(intent);
            }
        });



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_notif) {

            // Do something
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}