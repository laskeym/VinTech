package com.example.vintech;

public interface GetVehicleInfoListener {
    public void onEventCompleted(VehicleInfo vehicleInfo);
    public void onEventFailed();
}
