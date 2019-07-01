package com.example.vintech;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class VehicleResult {
    @SerializedName("Results")
    @Expose
    private List<VehicleInfo> results = new ArrayList<>();

    public List<VehicleInfo> getResults() {
        return results;
    }
}

