package com.example.majorproject;

import java.util.Map;

class CompleteAuctionModel {

    public String uname;
    public String address;
    public String email;
    public String phone;
    public String state;
    public String city;
    public String pincode;
    public String uid;

    public CompleteAuctionModel(String uname, String address, String email,
                                String phone, String state, String city, String pincode, String uid) {
        this.uname = uname;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.state = state;
        this.city = city;
        this.pincode = pincode;
        this.uid = uid;
    }
}
