package com.htbinh.xemboiver2.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.htbinh.xemboiver2.LoadingDialog;
import com.htbinh.xemboiver2.MainActivity;
import com.htbinh.xemboiver2.R;
import com.htbinh.xemboiver2.Session;
import com.htbinh.xemboiver2.data.model.History;
import com.htbinh.xemboiver2.databinding.ActivityLoginBinding;
import com.htbinh.xemboiver2.ui.register.RegisterActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    Session session;
    private ActivityLoginBinding binding;

    ProgressBar loadingProgressBar;
    EditText passwordEditText;
    EditText usernameEditText;
    Button loginButton;
    Button registerButton;
    CheckBox saveCheckBox;

    LoadingDialog loadingDialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        usernameEditText = binding.username;
        passwordEditText = binding.password;
        loginButton = binding.login;
        registerButton = binding.register;
        loadingProgressBar = binding.loading;
        saveCheckBox = binding.autoLogin;

        loadingDialog = new LoadingDialog(this);

        session = new Session(getApplicationContext());

        if(!session.getUserName().equals("")){
            usernameEditText.setText(session.getUserName());
        }

        if(session.getSaveState().equals("remember")){
            passwordEditText.setText(session.getPassword());
            saveCheckBox.setChecked(true);
            doLogin(session.getUserName(), session.getPassword());
        }

        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String user = usernameEditText.getText().toString().trim().toLowerCase(Locale.ROOT);
                String pass = passwordEditText.getText().toString().trim().toLowerCase(Locale.ROOT);
                if(isValidLogin() == 0){
                    doLogin(user, pass);
                }
                else if(isValidLogin() == 1){
                    Toast.makeText(getApplicationContext(), "Vui l??ng ??i???n ?????y ????? th??ng tin ????ng nh???p", Toast.LENGTH_SHORT).show();
                }
                else if(isValidLogin() == 2){
                    Toast.makeText(getApplicationContext(), "Vui l??ng ??i???n t??i kho???n", Toast.LENGTH_SHORT).show();
                }
                else if(isValidLogin() == 3){
                    Toast.makeText(getApplicationContext(), "Vui l??ng ??i???n m???t kh???u", Toast.LENGTH_SHORT).show();
                }
                else if(isValidLogin() == 4){
                    Toast.makeText(getApplicationContext(), "T??i kho???n v?? m???t kh???u ph???i l???n h??n 5 k?? t???", Toast.LENGTH_SHORT).show();
                }
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    private int isValidLogin(){
        if(usernameEditText.getText().toString().trim().equals("") && passwordEditText.getText().toString().trim().equals("")){
            return 1;
        }
        if(usernameEditText.getText().toString().trim().equals("")){
            return 2;
        }
        if(passwordEditText.getText().toString().trim().equals("")){
            return 3;
        }
        if(usernameEditText.getText().toString().trim().length() < 5 || passwordEditText.getText().toString().trim().length() < 5){
            return 4;
        }
        return 0;
    }

    private void doLogin(String user, String password){
        loadingDialog.startLoading();
        JSONObject loginJSON = new JSONObject();
        try {
            loginJSON.put("user", user);
            loginJSON.put("password", password);
        } catch (Exception e) {
        }
        String loginString = loginJSON.toString();

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest personRequest = new JsonObjectRequest(Request.Method.GET, "https://xemboi-backend.herokuapp.com/account/" + user, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    if(response != null){
                        session.setName(response.getString("name"));
                        session.setDob(response.getString("dob"));
                        session.setGioitinh(response.getString("gioitinh"));
                    }
                    loadingDialog.dismissLoading();
                    String welcome = getString(R.string.welcome) + (session.getName().equals("") ? session.getUserName() : session.getName());
                    Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    i.putExtra("isRegister", "false");
                    startActivity(i);
                }catch (Exception e){
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String welcome = getString(R.string.welcome) + user;
                Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                i.putExtra("isRegister", "true");
                startActivity(i);
            }
        });

        StringRequest loginRequest = new StringRequest(Request.Method.POST, "https://xemboi-backend.herokuapp.com/login", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("true")){
                    if(saveCheckBox.isChecked()){
                        session.setSaveState("remember");
                        session.setPassword(password);
                    }
                    else{
                        session.setSaveState("none");
                    }
                    session.freshData();
                    session.setUserName(user);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            queue.add(personRequest);
                        }
                    },500);
                } else {
                    loadingDialog.dismissLoading();
                    Toast.makeText(getApplicationContext(), "T??i kho???n ho???c m???t kh???u kh??ng ch??nh x??c!"
                            , Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingProgressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "L???i ????ng nh???p, vui l??ng th??? l???i sau! " + error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                return loginString.getBytes(StandardCharsets.UTF_8);
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                return params;
            }

        };
        queue.add(loginRequest);
    }
}