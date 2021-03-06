package com.example.vintech;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;

public class ListActivity extends AppCompatActivity implements RecyclerItemTouchHelperListener, ClearListDialog.ClearListDialogListener {
    private static final String TAG = "LIST ACTIVITY";

    public static ListActivity fa;
    private Context context;
    private Toolbar toolbar;

    private RelativeLayout rootLayout;

    private TextView emptyList;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private FloatingActionButton listMenuBtn, addVINBtn, sendEmailBtn, clearAllBtn;
    private int menuIcon = R.drawable.ic_menu_black_24dp;
    private int closeIcon = R.drawable.ic_close_black_24dp;
    private Float translationY = 100f;
    private Boolean isMenuOpen = false;

    private OvershootInterpolator interpolator = new OvershootInterpolator();

    private VehicleInfoList vehicleInfoList;
    private ArrayList<VehicleInfoItem> vehicleInfoItemList; //  RecyclerView list

    // JExcel / Java Mail
    private File fileLocation;
    private SaveWorkbook saveWorkbook;
    private SendEmail email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        fa = this;
        context = ListActivity.this;
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        rootLayout = findViewById(R.id.rootLayout);
        vehicleInfoList = new VehicleInfoList(context);
        convertVehicleArrayList();

        initRecyclerView();

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback =
                new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecyclerView);

        initFabMenu();

        fileLocation = new File(context.getFilesDir(), "VinTechVehicleReport.xls");
        saveWorkbook = new SaveWorkbook(context, fileLocation);
        email = new SendEmail(ListActivity.this, fileLocation);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if(viewHolder instanceof VehicleInfoAdapter.VehicleInfoViewHolder) {
            String vin = vehicleInfoItemList.get(viewHolder.getAdapterPosition()).getVehicleVIN();

            final VehicleInfoItem deleteItem = vehicleInfoItemList.get(viewHolder.getAdapterPosition());
            final VehicleInfo deleteInfo = vehicleInfoList.getVehicleInfoList().get(viewHolder.getAdapterPosition());
            final int deleteIndex = viewHolder.getAdapterPosition();

            ((VehicleInfoAdapter) mAdapter).removeItem(deleteIndex);
            vehicleInfoList.removeVehicleInfo(deleteIndex);

            Snackbar snackbar = Snackbar.make(rootLayout, "Vehicle removed from list!", Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("UNDO", new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    ((VehicleInfoAdapter) mAdapter).restoreItem(deleteItem, deleteIndex);
                    vehicleInfoList.restoreVehicleInfo(deleteInfo, deleteIndex);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }

    private void convertVehicleArrayList() {
        vehicleInfoItemList = new ArrayList<>();
        for(VehicleInfo vehicleInfo: vehicleInfoList.getVehicleInfoList()) {
            vehicleInfoItemList.add(vehicleInfoItemList.size(),
                    new VehicleInfoItem(
                            R.drawable.ic_directions_car_black_24dp,
                            vehicleInfo.getDescription(),
                            vehicleInfo.getVin()
                    ));
        }
    }

    private void initRecyclerView() {
        emptyList = findViewById(R.id.emptyList);
        emptyList.setVisibility(View.GONE);
        emptyList.setText("Nothing to show...");

        mRecyclerView = findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new VehicleInfoAdapter(vehicleInfoItemList);

        if(mAdapter.getItemCount() == 0) {
            emptyList.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);
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
                            Intent intent = new Intent(context, AddVehicle.class);
                            startActivity(intent);
                            break;
                        case R.id.sendEmailBtn:
                            saveWorkbook.saveWorkbook(vehicleInfoList.getVehicleInfoList());
                            email.sendEmail();
                            break;
                        case R.id.clearAllBtn:
                            openClearListDialog();
                            break;
                    }
                }
            };

    public void openClearListDialog() {
        ClearListDialog clearListDialog = new ClearListDialog();
        clearListDialog.show(getSupportFragmentManager(), "Clear List Dialog");
    }

    @Override
    public void clearVehicleList() {
        vehicleInfoList.clearList();
        vehicleInfoItemList.clear();
        initRecyclerView();
    }
}
