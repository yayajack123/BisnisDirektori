package id.ac.bisnisdirektori.ui.notifications;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;

import id.ac.bisnisdirektori.DetailCategory;
import id.ac.bisnisdirektori.EditProfileUserActivity;
import id.ac.bisnisdirektori.LoginActivity;
import id.ac.bisnisdirektori.MainActivity;
import id.ac.bisnisdirektori.R;

import static id.ac.bisnisdirektori.LoginActivity.TAG_ADDRESS;
import static id.ac.bisnisdirektori.LoginActivity.TAG_EMAIL;
import static id.ac.bisnisdirektori.LoginActivity.TAG_FULLNAME;
import static id.ac.bisnisdirektori.LoginActivity.TAG_ID;
import static id.ac.bisnisdirektori.LoginActivity.TAG_PHONENUMBER;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    SharedPreferences sharedpreferences;
    public static final String my_shared_preferences = "my_shared_preferences";
    public static final String session_status = "session_status";

    private RequestQueue requestQueue;
    private StringRequest stringRequest;

    String id, username;

    Boolean session = false;

    public static final String TAG_ID = "id";
    public static final String TAG_EMAIL = "email";
    public static final String TAG_FULLNAME = "fullname";
    public static final String TAG_PHONENUMBER = "phonenumber";
    public static final String TAG_ADDRESS = "address";


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
//        session = sharedpreferences.getBoolean(session_status, false);
        sharedpreferences = getContext().getSharedPreferences(LoginActivity.my_shared_preferences, Context.MODE_PRIVATE);
        id = sharedpreferences.getString("id", "null");

        Button btn_edit = (Button) root.findViewById(R.id.top_user);
        btn_edit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getActivity(), EditProfileUserActivity.class);
                startActivity(intent);
            }
        });

        Button btn_logout = (Button) root.findViewById(R.id.log_out);
        btn_logout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean(LoginActivity.session_status, false);
                editor.putString(TAG_ID, null);
                editor.putString(TAG_EMAIL, null);
                editor.putString(TAG_FULLNAME, null);
                editor.putString(TAG_PHONENUMBER, null);
                editor.putString(TAG_ADDRESS, null);
                editor.commit();

                Intent intent = new Intent(getActivity(), LoginActivity.class);
//                finish();
                startActivity(intent);
            }
        });

        return root;
    }
}
