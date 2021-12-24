package com.htbinh.xemboiver2.ui.info;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.htbinh.xemboiver2.Session;
import com.htbinh.xemboiver2.Validation;
import com.htbinh.xemboiver2.databinding.FragmentInfoBinding;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class InfoFragment extends Fragment {

    private FragmentInfoBinding binding;

    EditText nameEditText, dobEditText;
    Button btnUpdate;
    Session session;
    RadioButton rdNam, rdNu;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentInfoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        session = new Session(getContext());
        nameEditText = binding.name;
        dobEditText = binding.dob;
        btnUpdate = binding.update;
        rdNam = binding.rdNam;
        rdNu = binding.rdNu;

        nameEditText.setText(session.getName());
        dobEditText.setText(session.getDob());

        if (session.getGioitinh().equals("Nữ")) {
            rdNu.setChecked(true);
        } else {
            rdNam.setChecked(true);
        }

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameEditText.getText().toString().trim();
                String dob = dobEditText.getText().toString().trim();
                if(Validation.validateData(name, dob)){
                    doUpdate(name, dob);
                }else{
                    Toast.makeText(getContext(), "Hãy nhập hết các trường dữ liệu!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return root;
    }

    private void doUpdate(String name, String dob){
        JSONObject loginJSON = new JSONObject();
        JSONObject account = new JSONObject();
        try {
            account.put("user", session.getUserName());
            account.put("password", "");
            loginJSON.put("name", name);
            loginJSON.put("dob", dob);
            loginJSON.put("gioitinh", rdNam.isChecked() ? rdNam.getText().toString() : rdNu.getText().toString());
            loginJSON.put("user", account);
        } catch (Exception e) {
        }
        String loginString = loginJSON.toString();

        RequestQueue queue = Volley.newRequestQueue(getContext());

        StringRequest updateRequest = new StringRequest(Request.Method.POST, "https://xemboi-backend.herokuapp.com/account/updateinfo", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                session.setName(name);
                session.setDob(dob);
                session.setGioitinh(rdNam.isChecked() ? rdNam.getText().toString() : rdNu.getText().toString());
                Toast.makeText(getContext(), response , Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Lỗi nhé! " + error.toString(), Toast.LENGTH_LONG).show();
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
        queue.add(updateRequest);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}