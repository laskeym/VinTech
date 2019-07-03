package com.example.vintech;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class VehicleInfoList {
    private Context context;

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private Gson gson;
    private ArrayList<VehicleInfo> vehicleInfoList;

    public VehicleInfoList(Context context) {
        this.context = context;
        prefs = context.getSharedPreferences("vehicleInfoList", Context.MODE_PRIVATE);
        editor = prefs.edit();
        gson = new Gson();

        loadVehicleInfoList();
    }

    public ArrayList<VehicleInfo> getVehicleInfoList() {
        return vehicleInfoList;
    }

    public VehicleInfo getVehicleInfo(String vin) {
        for(VehicleInfo vehicleInfo: vehicleInfoList) {
            if(vehicleInfo.getVin().equals(vin)) {
                return vehicleInfo;
            }
        }
        return null;
    }

    private void loadVehicleInfoList() {
        String json = prefs.getString("vehicleInfoList", null);
        Type type = new TypeToken<ArrayList<VehicleInfo>>() {}.getType();

        vehicleInfoList = gson.fromJson(json, type);
        if(vehicleInfoList == null) {
            vehicleInfoList = new ArrayList<>();
        }
    }

    public void addVehicleInfo(VehicleInfo vehicleInfo) {
        vehicleInfoList.add(0, vehicleInfo);
        saveVehicleInfoList();
    }

    public void removeVehicleInfo(int position) {
        vehicleInfoList.remove(position);
        saveVehicleInfoList();
    }

    public void restoreVehicleInfo(VehicleInfo vehicleInfo, int position) {
        vehicleInfoList.add(position, vehicleInfo);
        saveVehicleInfoList();
    }

    private void saveVehicleInfoList() {
        String json = gson.toJson(vehicleInfoList);
        editor.putString("vehicleInfoList", json);
        editor.apply();
    }
}
