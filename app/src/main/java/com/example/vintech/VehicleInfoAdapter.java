package com.example.vintech;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class VehicleInfoAdapter extends RecyclerView.Adapter<VehicleInfoAdapter.VehicleInfoViewHolder> {
    private static final String TAG = "VehicleAdapter";
    private ArrayList<VehicleInfoItem> vehicleInfoList;

    public static class VehicleInfoViewHolder extends RecyclerView.ViewHolder {
        public ImageView vehicleImageView;
        public TextView vehicleDescView;
        public TextView vehicleVINView;

        public VehicleInfoViewHolder(@NonNull View itemView) {
            super(itemView);

            vehicleImageView = itemView.findViewById(R.id.vehicleImage);
            vehicleDescView = itemView.findViewById(R.id.vehicleDescription);
            vehicleVINView = itemView.findViewById(R.id.vehicleVIN);
        }
    }

    public VehicleInfoAdapter(ArrayList<VehicleInfoItem> vInfoList) {
        vehicleInfoList = vInfoList;
    }

    @NonNull
    @Override
    public VehicleInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_list_listitem, parent, false);
        VehicleInfoViewHolder vivh = new VehicleInfoViewHolder(v);

        return vivh;
    }

    @Override
    public void onBindViewHolder(@NonNull VehicleInfoViewHolder holder, int position) {
        VehicleInfoItem vehicleInfoItem = vehicleInfoList.get(position);

        holder.vehicleImageView.setImageResource(vehicleInfoItem.getVehicleImageResource());
        holder.vehicleDescView.setText(vehicleInfoItem.getVehicleDescription());
        holder.vehicleVINView.setText(vehicleInfoItem.getVehicleVIN());
    }

    @Override
    public int getItemCount() {
        return vehicleInfoList.size();
    }
}
