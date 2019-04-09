package com.example.majorproject;

public class UserModel {

    String name;
    String addr;
    String email;
    String phone;
    String etstate;
    String etcity;
    String pincode;

        public UserModel(){}

    public UserModel(String name, String addr, String email, String phone, String etstate, String etcity, String pincode)
    {
            this.name=name;
            this.addr=addr;
            this.email=email;
            this.phone=phone;
            this.etcity=etcity;
            this.etstate=etstate;
            this.pincode=pincode;


    }
}
