package com.htbinh.xemboiver2.ui.home;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.htbinh.xemboiver2.R;
import com.htbinh.xemboiver2.Session;
import com.htbinh.xemboiver2.databinding.FragmentHomeBinding;

import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    Button Submit;
    TextView DoB;
    EditText Name;
    ImageView DobPicker;

    int selectedYear = 2000;
    int selectedMonth = 0;
    int selectedDayOfMonth = 1;

    boolean isSaved = false;

    DatePickerDialog.OnDateSetListener dateSetListener;
    Map<String, String> data;
    Session session;
    Connection.Response response;

    String html = "";
    View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        root = binding.getRoot();
        session = new Session(getContext());
        Submit = binding.btnSubmit;
        DoB = binding.DoB;
        Name = binding.edtName;
        DobPicker = binding.btnSelectDoB;

        dateSetListener  = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {
                String Day = "" + ((dayOfMonth + 1) < 10 ? "0" + (dayOfMonth + 1) : (dayOfMonth + 1));
                String Month = (monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : (monthOfYear + 1) + "";
                String Year = "" + year;
                String date = Day + "/" + Month + "/" + Year;

                DoB.setText(date);
            }
        };

        DobPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = createDatePickerDialog(dateSetListener);
                datePickerDialog.show();
            }
        });



        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Name.getText().toString().equals("") || DoB.getText().toString().equals("Chọn ngày sinh")){
                    Toast.makeText(getContext(), "Vui lòng điền đầy đủ tên của 2 bên thông gia ☺", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(session.getName().equals("") || session.getDob().equals("")){
                    Toast.makeText(getContext(), "Vui lòng cập nhật thông tin cá nhân ☺", Toast.LENGTH_SHORT).show();
                    return;
                }

                String[] ngaySinhNam = session.getDob().split("/");
                String[] ngaySinhNu = DoB.getText().toString().split("/");
                data = new HashMap<>();
                data.put("action", "vnk_boitinhyeu");
                data.put("namenam", session.getName());
                data.put("ngaynam", ngaySinhNam[0]);
                data.put("thangnam", ngaySinhNam[1]);
                data.put("namnam", ngaySinhNam[2]);
                data.put("namenu", Name.getText().toString().trim());
                data.put("ngaynu", ngaySinhNu[0]);
                data.put("thangnu", ngaySinhNu[1]);
                data.put("namnu", ngaySinhNu[2]);
                new getData().execute();
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void doSaveHistory(View v){
        JSONObject historyJson = new JSONObject();
        JSONObject account = new JSONObject();
        try {
            account.put("user", session.getUserName());
            account.put("password", "");
            historyJson.put("tennguoiay", Name.getText());
            historyJson.put("dobnguoiay", DoB.getText());
            historyJson.put("chitiet", html);
            historyJson.put("user", account);
        } catch (Exception e) {
        }
        String history = historyJson.toString();
        RequestQueue queue = Volley.newRequestQueue(getContext());

        StringRequest loginRequest = new StringRequest(Request.Method.POST, "https://xemboi-backend.herokuapp.com/history/add", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("true")) {
                    Bundle sendBundle = new Bundle();
                    sendBundle.putString("html", html);
                    sendBundle.putString("action", "result");
                    Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main).navigate(R.id.nav_result, sendBundle);
                    Toast.makeText(getContext(), "Đã lưu lịch sử", Toast.LENGTH_SHORT).show();
                } else {
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Lỗi lưu lịch sử! " + error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                return history.getBytes(StandardCharsets.UTF_8);
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

    private DatePickerDialog createDatePickerDialog(DatePickerDialog.OnDateSetListener listener){
        return new DatePickerDialog(getContext(), listener, selectedYear, selectedMonth, selectedDayOfMonth);
    }

    private class getData extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getContext(), "Đang tính toán vui lòng chờ ♥", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... strings) {
            try  {
                response = Jsoup.connect(getResources().getString(R.string.src_link))
                        .data(data).cookie("cookie","cookie")
                        .method(Connection.Method.POST)
                        .execute();
            } catch (Exception e) {
                Log.i("Error", e.toString());
            }
            return response.body();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            html = s;
            doSaveHistory(root);
        }
    }
}