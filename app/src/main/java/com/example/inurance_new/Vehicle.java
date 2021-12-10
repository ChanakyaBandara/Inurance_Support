package com.example.inurance_new;

public class Vehicle {
    private String Customer_Name ;
    private String ImgURL;
    private String NIC;
    private String Vehicle_chassis_No ;
    private String Vehicle_engine_No;
    private String Vehicle_make;
    private String Vehicle_model;
    private String Vehicle_no;

    public Vehicle(String customer_Name, String imgURL, String NIC, String vehicle_chassis_No, String vehicle_engine_No, String vehicle_make, String vehicle_model, String vehicle_no) {
        Customer_Name = customer_Name;
        ImgURL = imgURL;
        this.NIC = NIC;
        Vehicle_chassis_No = vehicle_chassis_No;
        Vehicle_engine_No = vehicle_engine_No;
        Vehicle_make = vehicle_make;
        Vehicle_model = vehicle_model;
        Vehicle_no = vehicle_no;
    }
    public Vehicle() {
    }

    public String getCustomer_Name() {
        return Customer_Name;
    }

    public void setCustomer_Name(String customer_Name) {
        Customer_Name = customer_Name;
    }

    public String getImgURL() {
        return ImgURL;
    }

    public void setImgURL(String imgURL) {
        ImgURL = imgURL;
    }

    public String getNIC() {
        return NIC;
    }

    public void setNIC(String NIC) {
        this.NIC = NIC;
    }

    public String getVehicle_chassis_No() {
        return Vehicle_chassis_No;
    }

    public void setVehicle_chassis_No(String vehicle_chassis_No) {
        Vehicle_chassis_No = vehicle_chassis_No;
    }

    public String getVehicle_engine_No() {
        return Vehicle_engine_No;
    }

    public void setVehicle_engine_No(String vehicle_engine_No) {
        Vehicle_engine_No = vehicle_engine_No;
    }

    public String getVehicle_make() {
        return Vehicle_make;
    }

    public void setVehicle_make(String vehicle_make) {
        Vehicle_make = vehicle_make;
    }

    public String getVehicle_model() {
        return Vehicle_model;
    }

    public void setVehicle_model(String vehicle_model) {
        Vehicle_model = vehicle_model;
    }

    public String getVehicle_no() {
        return Vehicle_no;
    }

    public void setVehicle_no(String vehicle_no) {
        Vehicle_no = vehicle_no;
    }
}
