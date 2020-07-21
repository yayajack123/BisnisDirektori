package id.ac.bisnisdirektori.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import id.ac.bisnisdirektori.R;

public class AdminProfileActivity extends AppCompatActivity {

    TextView txt_id, txt_email,txt_fullname, txt_phonenumber, txt_password;
    String id,email, fullname, phonenumber, password;

    String tag_json_obj = "json_obj_req";
    SharedPreferences sharedpreferences;
    Boolean session = false;

    public static final String my_shared_preferences = "my_shared_preferences";
    public static final String session_status = "session_status";

    public static final String TAG_ID = "id";
    public static final String TAG_EMAIL = "email";
    public static final String TAG_FULLNAME = "fullname";
    public static final String TAG_PHONENUMBER = "phonenumber";
    public static final String TAG_PASSWORD = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile);

        txt_id = (TextView) findViewById(R.id.id_admin);
        txt_email = (TextView) findViewById(R.id.email_admin);
        txt_fullname = (TextView) findViewById(R.id.fullname_admin);
        txt_phonenumber = (TextView) findViewById(R.id.phonenumber_admin);
        txt_password = (TextView) findViewById(R.id.password_admin);

        sharedpreferences = getSharedPreferences(LoginAdminActivity.my_shared_preferences, Context.MODE_PRIVATE);

        id = getIntent().getStringExtra(TAG_ID);
        email = getIntent().getStringExtra(TAG_EMAIL);
        fullname = getIntent().getStringExtra(TAG_FULLNAME);
        phonenumber = getIntent().getStringExtra(TAG_PHONENUMBER);
        password = getIntent().getStringExtra(TAG_PASSWORD);

        id = sharedpreferences.getString(TAG_ID, null);
        email = sharedpreferences.getString(TAG_EMAIL, null);
        fullname = sharedpreferences.getString(TAG_FULLNAME, null);
        phonenumber = sharedpreferences.getString(TAG_PHONENUMBER, null);
        password = sharedpreferences.getString(TAG_PASSWORD, null);

        txt_id.setText("" + id);
        txt_email.setText("" + email);
        txt_fullname.setText("" + fullname);
        txt_phonenumber.setText("" + phonenumber);
        txt_password.setText("" + password);
    }
}