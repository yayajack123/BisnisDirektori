package id.ac.bisnisdirektori.ui.notifications;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import id.ac.bisnisdirektori.DetailCategory;
import id.ac.bisnisdirektori.EditProfileUserActivity;
import id.ac.bisnisdirektori.LoginActivity;
import id.ac.bisnisdirektori.MainActivity;
import id.ac.bisnisdirektori.R;
import id.ac.bisnisdirektori.RegisterActivity;
import id.ac.bisnisdirektori.UserPasswordActivity;
import id.ac.bisnisdirektori.admin.Server;

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

    String id, fullname, email, foto;
    private TextView nama, mail;
    private ImageView photo;

    ArrayList<HashMap<String, String>> list_data;

    Boolean session = false;

    public static final String TAG_ID = "id";
    public static final String TAG_EMAIL = "email";
    public static final String TAG_FULLNAME = "fullname";
    public static final String TAG_PHONENUMBER = "phonenumber";
    public static final String TAG_ADDRESS = "address";
    public static final String TAG_FOTO = "foto";


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
//        session = sharedpreferences.getBoolean(session_status, false);
        sharedpreferences = getContext().getSharedPreferences(LoginActivity.my_shared_preferences, Context.MODE_PRIVATE);
        id = sharedpreferences.getString("id", "null");

        String url = Server.URL+"profileuser.php?id="+id;

        nama = (TextView) root.findViewById(R.id.name_user);
        mail = (TextView) root.findViewById(R.id.email_user);
        photo = (ImageView) root.findViewById(R.id.photo_user);

        requestQueue = Volley.newRequestQueue(getActivity());

        list_data = new ArrayList<HashMap<String, String>>();



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

        Button btn_change = (Button) root.findViewById(R.id.change_password);
        btn_change.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getActivity(), UserPasswordActivity.class);
                startActivity(intent);
            }
        });

        Button btn_login = (Button) root.findViewById(R.id.login);
        btn_login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        Button btn_register = (Button) root.findViewById(R.id.register);
        btn_register.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getActivity(), RegisterActivity.class);
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
//                editor.putString(TAG_EMAIL, null);
//                editor.putString(TAG_FULLNAME, null);
//                editor.putString(TAG_PHONENUMBER, null);
//                editor.putString(TAG_ADDRESS, null);
//                editor.putString(TAG_FOTO, null);
                editor.commit();

                Intent intent = new Intent(getActivity(), LoginActivity.class);
//                finish();
                startActivity(intent);
            }
        });

        if(id=="null"){
            btn_logout.setVisibility(View.GONE);
            btn_edit.setVisibility(View.GONE);
            btn_change.setVisibility(View.GONE);
        }else{
            btn_login.setVisibility(View.GONE);
            btn_register.setVisibility(View.GONE);
            stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("user");
                        for (int a = 0; a < jsonArray.length(); a ++){
                            JSONObject json = jsonArray.getJSONObject(a);
                            HashMap<String, String> map  = new HashMap<String, String>();
                            map.put("fullname", json.getString("fullname"));
                            map.put("email", json.getString("email"));
                            map.put("foto", json.getString("foto"));
                            list_data.add(map);
                        }
                        nama.setText(list_data.get(0).get("fullname"));
                        mail.setText(list_data.get(0).get("email"));
                        Glide.with(getActivity())
                                .load(Server.URL+list_data.get(0).get("foto"))
                                .placeholder(R.mipmap.ic_launcher)
                                .into(photo);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            requestQueue.add(stringRequest);
        }

        return root;
    }
}
