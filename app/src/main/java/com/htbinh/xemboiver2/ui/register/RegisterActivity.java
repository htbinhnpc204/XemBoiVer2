package com.htbinh.xemboiver2.ui.register;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.htbinh.xemboiver2.MainActivity;
import com.htbinh.xemboiver2.R;
import com.htbinh.xemboiver2.Session;
import com.htbinh.xemboiver2.databinding.ActivityRegisterBinding;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    Session session;
    private ActivityRegisterBinding binding;
    ProgressBar loadingProgressBar;
    EditText passwordEditText;
    EditText rePasswordEditText;
    EditText usernameEditText;
    Button registerButton;
    CheckBox showPasswordButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        usernameEditText = binding.registerU;
        passwordEditText = binding.registerP;
        rePasswordEditText = binding.rePassword;
        registerButton = binding.register;
        loadingProgressBar = binding.loading;
        showPasswordButton = binding.showPassword;



        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                String user = usernameEditText.getText().toString().trim().toLowerCase(Locale.ROOT);
                String pass = passwordEditText.getText().toString().trim().toLowerCase(Locale.ROOT);
                String rePass = rePasswordEditText.getText().toString().trim().toLowerCase(Locale.ROOT);
                if(isValidRegister() == 0){
                    if(!pass.equals(rePass)){
                        Toast.makeText(getApplicationContext(), "M???t kh???u x??c nh???n v?? m???t kh???u ph???i tr??ng nhau", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        if(user.equals("") || pass.equals("") || rePass.equals("")){
                            Toast.makeText(getApplicationContext(), "Vui l??ng ??i???n ?????y ????? c??c tr?????ng th??ng tin", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            register(user, pass);
                        }
                    }
                }
                else if(isValidRegister() == 1){
                    Toast.makeText(getApplicationContext(), "Vui l??ng ??i???n ?????y ????? th??ng tin ????ng nh???p", Toast.LENGTH_SHORT).show();
                }
                else if(isValidRegister() == 2){
                    Toast.makeText(getApplicationContext(), "Vui l??ng ??i???n t??i kho???n", Toast.LENGTH_SHORT).show();
                }
                else if(isValidRegister() == 3){
                    Toast.makeText(getApplicationContext(), "Vui l??ng ??i???n m???t kh???u", Toast.LENGTH_SHORT).show();
                }
                else if(isValidRegister() == 4){
                    Toast.makeText(getApplicationContext(), "Vui l??ng nh???p l???i m???t kh???u", Toast.LENGTH_SHORT).show();
                }
                else if(isValidRegister() == 5){
                    Toast.makeText(getApplicationContext(), "T??i kho???n v?? m???t kh???u ph???i l???n h??n 5 k?? t???", Toast.LENGTH_SHORT).show();
                }
            }
        });

        showPasswordButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    rePasswordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

                } else {
                    passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    rePasswordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
    }

    private int isValidRegister(){
        if(usernameEditText.getText().toString().trim().equals("") && passwordEditText.getText().toString().trim().equals("") && rePasswordEditText.getText().toString().trim().equals("")){
            return 1;
        }
        if(usernameEditText.getText().toString().trim().equals("")){
            return 2;
        }
        if(passwordEditText.getText().toString().trim().equals("")){
            return 3;
        }
        if(rePasswordEditText.getText().toString().trim().equals("")){
            return 4;
        }
        if(usernameEditText.getText().toString().trim().length() < 5 || passwordEditText.getText().toString().trim().length() < 5 || rePasswordEditText.getText().toString().trim().length() < 5) {
            return 5;
        }
        return 0;
    }

    private void register(String user, String password){
        JSONObject loginJSON = new JSONObject();
        try {
            loginJSON.put("user", user);
            loginJSON.put("password", password);
        } catch (Exception e) {
        }
        String loginString = loginJSON.toString();

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest registerRequest = new StringRequest(Request.Method.POST, "https://xemboi-backend.herokuapp.com/register", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loadingProgressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                if (response.contains("th??nh c??ng")) {
                    String welcome = getString(R.string.welcome) + user;
                    Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
                    session = new Session(getApplicationContext());
                    session.setUserName(user);
                    Intent i = new Intent(RegisterActivity.this, MainActivity.class);
                    i.putExtra("isRegister", "true");
                    startActivity(i);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingProgressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "L???i ????ng k??, vui l??ng th??? l???i sau! " + error.toString(), Toast.LENGTH_LONG).show();
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
        queue.add(registerRequest);
    }
}