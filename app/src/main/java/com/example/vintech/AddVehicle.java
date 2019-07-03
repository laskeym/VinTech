package com.example.vintech;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

public class AddVehicle extends AppCompatActivity implements GetVehicleInfoListener {
    private static final String TAG = "AddVehicle";
    private VehicleInfoList vehicleInfoList;
    private VehicleInfo vehicleInfo;
    private VinValidator validator;

    private TextView confirmError;
    private ProgressDialog pdialog;
    private TextInputLayout vehicleVINLayout;
    private EditText vehicleVIN, vehicleYear, vehicleMake, vehicleModel;
    private TextWatcher textWatcher;
    private Button confirmBtn;
    private View.OnClickListener confirmBtnListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle);

        confirmError = findViewById(R.id.confirmError);
        vehicleInfoList = new VehicleInfoList(this);
        validator = new VinValidator(this);

        vehicleVINLayout = findViewById(R.id.vehicleVINLayout);
        vehicleVIN = findViewById(R.id.vehicleVIN);
        vehicleYear = findViewById(R.id.vehicleYear);
        vehicleMake = findViewById(R.id.vehicleMake);
        vehicleModel = findViewById(R.id.vehicleModel);
        confirmBtn = findViewById(R.id.confirmBtn);

        vehicleVIN.addTextChangedListener(setTextWatcher());
        confirmBtn.setOnClickListener(setConfirmBtnListener());
    }

    private TextWatcher setTextWatcher() {
        textWatcher = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                String vin = vehicleVIN.getText().toString().toUpperCase();
                if(vin.length() < 17 || vin.length() > 17) {
                    clearVehicleInfo();
                } else {
                    if(validator.validateVIN(vin)) {
                        if(!validator.doesVINExist(vehicleInfoList)) {
                            vehicleVINLayout.setError(null);
                            getVehicleInfo(vin);
                        } else {
                            vehicleVINLayout.setError("VIN has already been added!");
                        }
                    } else {
                        vehicleVINLayout.setError("Invalid VIN number!");
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        };

        return textWatcher;
    }

    private void clearVehicleInfo() {
        vehicleYear.setText(null);
        vehicleMake.setText(null);
        vehicleModel.setText(null);
    }

    private View.OnClickListener setConfirmBtnListener() {
        confirmBtnListener = new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(vehicleVINLayout.getError() == null && vehicleVIN.length() == 17) {
                    vehicleInfoList.addVehicleInfo(vehicleInfo);
                    ListActivity.fa.finish();
                    Intent intent = new Intent(AddVehicle.this, ListActivity.class);
                    startActivity(intent);
                    AddVehicle.this.finish();
                } else {
                    confirmError.setText("Check VIN Number!");
                }
            }
        };

        return confirmBtnListener;
    }

    @Override
    public void onEventCompleted(VehicleInfo vehicleInfo) {
        if(vehicleVIN.length() == 17) {
            Log.d(TAG, "onEventCompleted: " + vehicleInfo.getVin());
            this.vehicleInfo = vehicleInfo;
            vehicleYear.setText(vehicleInfo.getModelYear());
            vehicleMake.setText(vehicleInfo.getMake());
            vehicleModel.setText(vehicleInfo.getModel());
        }
        pdialog.dismiss();
        confirmBtn.setEnabled(true);
    }

    @Override
    public void onEventFailed() {}

    private void getVehicleInfo(String vin) {
        pdialog = ProgressDialog.show(this, "", "Grabbing vehicle details...", true);
        confirmBtn.setEnabled(false);
        new GetVehicleInfoTask(vin, this).execute();
    }
}
