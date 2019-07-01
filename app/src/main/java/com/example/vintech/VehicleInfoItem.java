package com.example.vintech;

public class VehicleInfoItem {
    private int vehicleImageResource;
    private String vehicleDescription;
    private String vehicleVIN;

    public VehicleInfoItem(int vehicleImgResource, String vehicleDescText, String vehicleVINText) {
        vehicleImageResource = vehicleImgResource;
        vehicleDescription = vehicleDescText;
        vehicleVIN = vehicleVINText;
    }

    public int getVehicleImageResource() {
        return vehicleImageResource;
    }

    public String getVehicleDescription() {
        return vehicleDescription;
    }

    public String getVehicleVIN() {
        return vehicleVIN;
    }
}
