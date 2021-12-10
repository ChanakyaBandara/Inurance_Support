package com.example.inurance_new;

public class Accident {
    private String txtdate,NIC,des,lat,lan,WID,states;

    public Accident(String txtdate, String NIC, String des, String lat, String lan, String WID, String imageUrl, String states) {
        this.txtdate = txtdate;
        this.NIC = NIC;
        this.des = des;
        this.lat = lat;
        this.lan = lan;
        this.WID = WID;
        this.states = states;
    }

    public Accident() {
        this.txtdate = "txtdate";
        this.NIC = "NIC";
        this.des = "des";
        this.lat = "lat";
        this.lan = "lan";
        this.WID = "WID";
        this.states = "Pending";
    }

    public String getStates() {
        return states;
    }

    public void setStates(String states) {
        this.states = states;
    }

    public String getTxtdate() {
        return txtdate;
    }

    public void setTxtdate(String txtdate) {
        this.txtdate = txtdate;
    }

    public String getNIC() {
        return NIC;
    }

    public void setNIC(String NIC) {
        this.NIC = NIC;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLan() {
        return lan;
    }

    public void setLan(String lan) {
        this.lan = lan;
    }

    public String getWID() {
        return WID;
    }

    public void setWID(String WID) {
        this.WID = WID;
    }

}
