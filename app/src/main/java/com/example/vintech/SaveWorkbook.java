package com.example.vintech;

import android.content.Context;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import jxl.Workbook;
import jxl.demo.Write;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class SaveWorkbook {
    private Context context;
    private WritableWorkbook workbook;
    private File fileLocation;
    private WritableSheet workSheet;


    public SaveWorkbook(Context c, String fileName) {
        context = c;
        fileLocation = new File(c.getFilesDir(), fileName);
    }

    public void saveWorkbook(ArrayList<VehicleInfo> vehicleInfoList) {
        initWorkbook();
        appendHeaders();
        addVehicleInfo(vehicleInfoList);

        try {
            workbook.write();
            Toast.makeText(context, "Workbook saved at: " + fileLocation, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (WriteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void initWorkbook() {
        try {
            workbook = Workbook.createWorkbook(fileLocation);
            workSheet = workbook.createSheet("Vehicle List", 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void appendHeaders() {
        WritableFont cellFont = new WritableFont(WritableFont.TIMES, 14);
        WritableCellFormat cellFormat;

        try {
            cellFont.setBoldStyle(WritableFont.BOLD);
            cellFormat = new WritableCellFormat(cellFont);

            Label label = new Label(0, 0, "Year", cellFormat);
            workSheet.addCell(label);

            label = new Label(1, 0, "Make", cellFormat);
            workSheet.addCell(label);

            label = new Label(2, 0, "Model", cellFormat);
            workSheet.addCell(label);

            label = new Label(3, 0, "VIN", cellFormat);
            workSheet.addCell(label);

            label = new Label(4, 0, "Body Type", cellFormat);
            workSheet.addCell(label);
        } catch (WriteException e) {
            e.printStackTrace();
        }
    }

    private void addVehicleInfo(ArrayList<VehicleInfo> vehicleInfoList) {
        int i = 0;
        Label label;

        for (VehicleInfo vehicleInfo: vehicleInfoList) {
            try {
                label = new Label(0, i+1, vehicleInfo.getModelYear());
                workSheet.addCell(label);

                label = new Label(1, i+1, vehicleInfo.getMake());
                workSheet.addCell(label);

                label = new Label(2, i+1, vehicleInfo.getModel());
                workSheet.addCell(label);

                label = new Label(3, i+1, vehicleInfo.getVin());
                workSheet.addCell(label);

                label = new Label(4, i+1, vehicleInfo.getBodyClass());
                workSheet.addCell(label);
            } catch (WriteException e) {
                e.printStackTrace();
            }
        }
    }
}
