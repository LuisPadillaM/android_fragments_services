package com.example.luispadilla.handlerservices.ui.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.luispadilla.handlerservices.R;

import java.util.List;

public class NetworkListAdapter extends  RecyclerView.Adapter<NetworkListAdapter.ViewHolder> {

    List<String> networks;

    public NetworkListAdapter(List<String> networks) {
        this.networks = networks;
    }

    public void updateNetworks(List<String> networks) {
        this.networks = networks;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from( parent.getContext());
        View view = inflater.inflate(R.layout.list_row, parent, false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.assignData(this.networks.get(position));
    }

    @Override
    public int getItemCount() {
        return this.networks.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView networkName;
        public ViewHolder(View itemView) {
            super(itemView);
            networkName  = itemView.findViewById(R.id.row_text);
        }
        public void assignData(String name) {
            networkName.setText(name);
        }
    }
}
