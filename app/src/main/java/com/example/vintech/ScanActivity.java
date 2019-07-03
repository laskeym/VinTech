package com.example.vintech;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class ScanActivity extends AppCompatActivity implements GetVehicleInfoListener {
    private static final String TAG = "ScanAct";
    private Context context;

    private SurfaceView svBarcode;
    private TextView tvBarcode;

    private BarcodeDetector detector;
    private CameraSource cameraSource;

    private VehicleInfoList vehicleInfoList;
    private VinValidator vinValidator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        context = getApplicationContext();
        svBarcode = findViewById(R.id.sv_barcode);
        tvBarcode = findViewById(R.id.tv_barcode);

        vehicleInfoList = new VehicleInfoList(context);
        vinValidator = new VinValidator(context);

        initBarcodeScanner();
    }

    @Override
    public void onEventCompleted(VehicleInfo vehicleInfo) {
        vehicleInfoList.addVehicleInfo(vehicleInfo);
        tvBarcode.setText("VIN added!");
    }

    @Override
    public void onEventFailed() {
        tvBarcode.setText("Failed to retrieve vehicle info!");
    }

    private void initBarcodeScanner() {
        detector = new BarcodeDetector.Builder(context)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();
        detector.setProcessor(barcodeProcessor);

        cameraSource = new CameraSource.Builder(context, detector)
                .setRequestedPreviewSize(1024, 768)
                .setRequestedFps(30f)
                .setAutoFocusEnabled(true)
                .build();
        svBarcode.getHolder().addCallback(shCallback());
    }

    private Detector.Processor<Barcode> barcodeProcessor = new Detector.Processor<Barcode>() {
        @Override
        public void release() {}

        @Override
        public void receiveDetections(Detector.Detections<Barcode> detections) {
            final SparseArray<Barcode> barcodes = detections.getDetectedItems();

            if(barcodes != null && barcodes.size() > 0) {
                final String vin = barcodes.valueAt(0).displayValue;

                if(vinValidator.validateVIN(vin)) {
                    if(vinValidator.doesVINExist(vehicleInfoList)) {
                        tvBarcode.setText("VIN already has been scanned!");
                    } else {
                        tvBarcode.setText("Grabbing vehicle information...");
                        getVehicleInfo(vin);
                    }
                } else {
                    tvBarcode.setText("Invalid VIN!");
                }
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private SurfaceHolder.Callback2 shCallback() {
        final SurfaceHolder.Callback2 callback = new SurfaceHolder.Callback2() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                if(ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    try {
                        cameraSource.start(svBarcode.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    String[] permissionArray = new String[]{Manifest.permission.CAMERA};
                    ActivityCompat.requestPermissions(ScanActivity.this, permissionArray, 123);
                }
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                cameraSource.stop();
            }

            @Override
            public void surfaceRedrawNeeded(SurfaceHolder surfaceHolder) {}

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {}
        };

        return callback;
    }

    public void getVehicleInfo(String vin) {
        new GetVehicleInfoTask(vin, this).execute();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 123) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                try {
                    cameraSource.start(svBarcode.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(context, "Scanner won't work without camera permission!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        detector.release();
        cameraSource.stop();
        cameraSource.release();
    }
}
