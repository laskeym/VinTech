package com.example.vintech;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GetVehicleInfoTask extends AsyncTask<Void, Void, Void> {
    private final static String BASE_URL = "https://vpic.nhtsa.dot.gov/";

    private String vinNum;
    private GetVehicleInfoListener callback;

    private NHTSAVehicleApi nhtsaVehicleApi;
    private VehicleInfo vehicleInfo;

    public GetVehicleInfoTask(String vin, GetVehicleInfoListener cb) {
        vinNum = vin;
        callback = cb;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        initApi();
        getVehicleInfo();

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
    }

    private void initApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        nhtsaVehicleApi = retrofit.create(NHTSAVehicleApi.class);
    }

    private void getVehicleInfo() {
        Call<VehicleResult> call = nhtsaVehicleApi.getVehicleInfo(vinNum);

        call.enqueue(new Callback<VehicleResult>() {
            @Override
            public void onResponse(Call<VehicleResult> call, Response<VehicleResult> response) {
                if (!response.isSuccessful()) {
                    // Need some error handling here
                    return;
                }

                VehicleResult vehicleResult = response.body();
                vehicleInfo = vehicleResult.getResults().get(0);
                callback.onEventCompleted(vehicleInfo);
            }

            @Override
            public void onFailure(Call<VehicleResult> call, Throwable t) {
                // Need some error handling here
                return;
            }
        });
    }
}
