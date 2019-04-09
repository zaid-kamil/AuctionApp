package com.example.majorproject;

public class CreateModel {

    public String title;
    public String discription;
    public String minibid;
    public String time;
    public String url;

    public CreateModel() {
    }


    public CreateModel(String ettitle, String discription, String minibid, String time, String url) {

        this.title = ettitle;
        this.discription = discription;
        this.minibid = minibid;
        this.time = time;
        this.url = url;
    }
}
