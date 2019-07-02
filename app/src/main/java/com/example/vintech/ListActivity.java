package com.example.vintech;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {
    private static final String TAG = "LIST ACTIVITY";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private FloatingActionButton listMenuBtn, addVINBtn, sendEmailBtn, clearAllBtn;
    private int menuIcon = R.drawable.ic_menu_black_24dp;
    private int closeIcon = R.drawable.ic_close_black_24dp;
    private Float translationY = 100f;
    private Boolean isMenuOpen = false;

    private OvershootInterpolator interpolator = new OvershootInterpolator();

    // When deleting, delete from both arrays, then update SharedPrefs
    private ArrayList<VehicleInfo> vehicleInfoList; // Retrieve from SharedPrefs
    private ArrayList<VehicleInfoItem> vehicleInfoItemList; //  RecyclerView list

    // JExcel / Java Mail
    private Context context;

    private File fileLocation;
    private SaveWorkbook saveWorkbook;
    private SendEmail email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        loadVehicleInfoList();
        convertVehicleArrayList();

        initRecyclerView();
        initFabMenu();

        context = ListActivity.this;
        fileLocation = new File(context.getFilesDir(), "VinTechVehicleReport.xls");
        saveWorkbook = new SaveWorkbook(context, fileLocation);
        email = new SendEmail(ListActivity.this, fileLocation);
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

    private void initFabMenu() {
        listMenuBtn = findViewById(R.id.listMenuBtn);
        addVINBtn = findViewById(R.id.addVINBtn);
        sendEmailBtn = findViewById(R.id.sendEmailBtn);
        clearAllBtn = findViewById(R.id.clearAllBtn);

        listMenuBtn.setImageResource(R.drawable.ic_menu_black_24dp);

        addVINBtn.setAlpha(0f);
        sendEmailBtn.setAlpha(0f);
        clearAllBtn.setAlpha(0f);

        addVINBtn.setTranslationY(translationY);
        sendEmailBtn.setTranslationY(translationY);
        clearAllBtn.setTranslationY(translationY);

        listMenuBtn.setOnClickListener(fabMenuClick);
        addVINBtn.setOnClickListener(fabMenuClick);
        sendEmailBtn.setOnClickListener(fabMenuClick);
        clearAllBtn.setOnClickListener(fabMenuClick);
    }

    private void initRecyclerView() {
        mRecyclerView = findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new VehicleInfoAdapter(vehicleInfoItemList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void openMenu() {
        isMenuOpen = !isMenuOpen;

        listMenuBtn.animate().setInterpolator(interpolator).rotation(45f).setDuration(300).start();
        listMenuBtn.setImageResource(closeIcon);
        listMenuBtn.animate().setInterpolator(interpolator).rotation(90f).setDuration(300).start();

        addVINBtn.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        sendEmailBtn.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        clearAllBtn.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
    }

    private void closeMenu() {
        isMenuOpen = !isMenuOpen;

        listMenuBtn.animate().setInterpolator(interpolator).rotation(45f).setDuration(300).start();
        listMenuBtn.setImageResource(menuIcon);
        listMenuBtn.animate().setInterpolator(interpolator).rotation(0f).setDuration(300).start();

        addVINBtn.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        sendEmailBtn.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        clearAllBtn.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
    }

    private View.OnClickListener fabMenuClick =
            new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()) {
                        case R.id.listMenuBtn:
                            if (isMenuOpen) {
                                closeMenu();
                            } else {
                                openMenu();
                            }
                            break;
                        case R.id.addVINBtn:
                            Log.i(TAG, "onClick: addVINBtn");
                            break;
                        case R.id.sendEmailBtn:
                            Log.i(TAG, "onClick: sendEmailBtn");
                            saveWorkbook.saveWorkbook(vehicleInfoList);
                            email.sendEmail();
                            break;
                        case R.id.clearAllBtn:
                            Log.i(TAG, "onClick: clearAllBtn");
                            break;
                    }
                }
            };

}
