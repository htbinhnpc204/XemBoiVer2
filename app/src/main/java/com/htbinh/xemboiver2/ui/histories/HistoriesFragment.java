package com.htbinh.xemboiver2.ui.histories;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.htbinh.xemboiver2.CustomDivider.MyDividerItemDecoration;
import com.htbinh.xemboiver2.LoadingDialog;
import com.htbinh.xemboiver2.R;
import com.htbinh.xemboiver2.Session;
import com.htbinh.xemboiver2.data.model.Adapter.HistoryAdapter;
import com.htbinh.xemboiver2.data.model.History;
import com.htbinh.xemboiver2.databinding.FragmentHistoriesBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class HistoriesFragment extends Fragment implements HistoryAdapter.HistoryAdapterListener {

    private FragmentHistoriesBinding binding;
    private RecyclerView recyclerView;
    private List<History> histories;
    public static HistoryAdapter mAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHistoriesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = binding.listHistories;
        histories = new ArrayList<>();
        mAdapter = new HistoryAdapter(histories, this);
        recyclerView.addItemDecoration(new MyDividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL, 36));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        fetchData();

        return root;
    }

    private void fetchData(){
        RequestQueue queue = Volley.newRequestQueue(getContext());
        List<History> list = new ArrayList<>();
        JsonArrayRequest listHistory = new JsonArrayRequest(Request.Method.GET, "https://xemboi-backend.herokuapp.com/history/user/" + new Session(getContext()).getUserName(),
                null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                histories.clear();
                for(int i = 0; i < response.length(); i++){
                    try {
                        JSONObject obj = response.getJSONObject(i);
                        histories.add(new History(Integer.parseInt(obj.getString("id")), obj.getString("tennguoiay"), obj.getString("dobnguoiay"), obj.getString("chitiet")));
                        mAdapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Get list histories", error.toString());
            }
        });
        queue.add(listHistory);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onContactSelected(History history) {
        Bundle sendBundle = new Bundle();
        sendBundle.putString("html", history.getChitiet());
        Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main).navigate(R.id.nav_result, sendBundle);
    }

    @Override
    public void onDeletClick(History history) {
        LoadingDialog loadingDialog = new LoadingDialog(getActivity());
        loadingDialog.startLoading();
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest deleteRequest = new StringRequest(Request.Method.POST, "https://xemboi-backend.herokuapp.com/history/delete/" + history.getId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loadingDialog.dismissLoading();
                Toast.makeText(getContext(), "" + response, Toast.LENGTH_SHORT).show();
                fetchData();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingDialog.dismissLoading();

            }
        });
        queue.add(deleteRequest);
    }

    public static void doUpdate(String query) {
        mAdapter.getFilter().filter(query);
    }
}