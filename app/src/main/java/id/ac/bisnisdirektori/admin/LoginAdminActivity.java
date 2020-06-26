package id.ac.bisnisdirektori.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import id.ac.bisnisdirektori.LoginActivity;
import id.ac.bisnisdirektori.R;

public class LoginAdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_admin);

        Button admin = (Button) findViewById(R.id.login_admin);
        admin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getBaseContext(), HomeAdminActivity.class);
                startActivity(intent);
            }
        });
        Button login_user = (Button) findViewById(R.id.login_user);
        login_user.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}