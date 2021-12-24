package com.htbinh.xemboiver2.data.model.Adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.htbinh.xemboiver2.R;
import com.htbinh.xemboiver2.data.model.History;

import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> implements Filterable {

    private List<History> histories;
    private List<History> historyFilters;
    private HistoryAdapterListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView id, name, dob, stt;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            id = view.findViewById(R.id.itemId);
            stt = view.findViewById(R.id.itemStt);
            name = view.findViewById(R.id.itemName);
            dob = view.findViewById(R.id.itemDob);
            thumbnail = view.findViewById(R.id.itemDel);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(historyFilters.get(getAdapterPosition()));
                }
            });

            thumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onDeletClick(historyFilters.get(getAdapterPosition()));
                }
            });
        }
    }

    public HistoryAdapter(List<History> histories, HistoryAdapterListener listener) {
        this.histories = histories;
        this.listener = listener;
        this.historyFilters = histories;
    }

    @Override
    public MyViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final History history = historyFilters.get(position);
        holder.stt.setText("STT: " + position);
        holder.name.setText("Người ấy: " + history.getTenNguoiAy());
        holder.dob.setText("Ngày sinh: " + history.getDobNguoiAy());
    }



    @Override
    public int getItemCount() {
        return historyFilters.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    historyFilters = histories;
                } else {
                    List<History> filteredList = new ArrayList<>();
                    for (History row : histories) {
                        if (row.getTenNguoiAy().toLowerCase().contains(charString.toLowerCase()) || row.getDobNguoiAy().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }
                    historyFilters = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = historyFilters;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                historyFilters = (ArrayList<History>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface HistoryAdapterListener {
        void onContactSelected(History history);
        void onDeletClick(History history);
    }
}
