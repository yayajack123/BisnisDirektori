package id.ac.bisnisdirektori;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Objects;

import id.ac.bisnisdirektori.admin.LoginAdminActivity;
import id.ac.bisnisdirektori.admin.RegisterAdminActivity;

public class LoginActivity extends AppCompatActivity {
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button login = (Button) findViewById(R.id.btn_login);
        login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        Button register = (Button) findViewById(R.id.btn_register);
        register.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getBaseContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
        Button admin = (Button) findViewById(R.id.btn_loginadmin);
        admin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getBaseContext(), LoginAdminActivity.class);
                startActivity(intent);
            }
        });

        Button register_admin = (Button) findViewById(R.id.btn_registeradmin);
        register_admin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getBaseContext(), RegisterAdminActivity.class);
                startActivity(intent);
            }
        });
    }
}