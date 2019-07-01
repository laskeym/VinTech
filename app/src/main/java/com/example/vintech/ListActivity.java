package com.example.vintech;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {
    private static final String TAG = "ListAct";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    // When deleting, delete from both arrays, then update SharedPrefs
    private ArrayList<VehicleInfo> vehicleInfoList; // Retrieve from SharedPrefs
    private ArrayList<VehicleInfoItem> vehicleInfoItemList; //  RecyclerView list

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        loadVehicleInfoList();
        convertVehicleArrayList();

        mRecyclerView = findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new VehicleInfoAdapter(vehicleInfoItemList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void loadVehicleInfoList() {
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("vehicleInfoList", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString("vehicleInfoList", null);
        Type type = new TypeToken<ArrayList<VehicleInfo>>() {}.getType();

        vehicleInfoList = gson.fromJson(json, type);
        if(vehicleInfoList == null) {
            vehicleInfoList = new ArrayList<>();
        }
    }

    private void convertVehicleArrayList() {
        vehicleInfoItemList = new ArrayList<>();
        for(VehicleInfo vehicleInfo: vehicleInfoList) {
            vehicleInfoItemList.add(vehicleInfoItemList.size(),
                    new VehicleInfoItem(
                            R.drawable.ic_directions_car_black_24dp,
                            vehicleInfo.getDescription(),
                            vehicleInfo.getVin()
                    ));
        }
    }
}
