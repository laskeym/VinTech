package com.example.vintech;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VehicleInfo {
    @SerializedName("VIN")
    @Expose
    private String vin;

    @SerializedName("Make")
    @Expose
    private String make;

    @SerializedName("ModelYear")
    @Expose
    private String modelYear;

    @SerializedName("Model")
    @Expose
    private String Model;

    @SerializedName("BodyClass")
    @Expose
    private String bodyClass;

    public String getVin() {
        return vin;
    }

    public String getMake() {
        return make;
    }

    public String getModelYear() {
        return modelYear;
    }

    public String getModel() {
        return Model;
    }

    public String getBodyClass() {
        return bodyClass;
    }

    public String getDescription() {
        StringBuilder vehicleDesc = new StringBuilder();
        vehicleDesc.append(this.getModelYear());
        vehicleDesc.append(" ");
        vehicleDesc.append(this.getMake());
        vehicleDesc.append(" ");
        vehicleDesc.append(this.getModel());

        return vehicleDesc.toString();
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public void setModelYear(String modelYear) {
        this.modelYear = modelYear;
    }

    public void setModel(String model) {
        Model = model;
    }

    public void setBodyClass(String bodyClass) {
        this.bodyClass = bodyClass;
    }
}
