package com.example.vintech;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface NHTSAVehicleApi {
    // National Highway Traffic Safety Administration(NHTSA) Vehicle API
    @GET("api/vehicles/decodevinvalues/{vin}?format=json")
    Call<VehicleResult> getVehicleInfo(@Path("vin")String vinNum);
}
