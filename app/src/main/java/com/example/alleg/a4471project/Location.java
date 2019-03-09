package com.example.alleg.a4471project;

public class Location {

    public String longitude;
    public String latitude;

    public Location(){
        // Default constructor required for calls to DataSnapshot.getValue(Location.class)
    }

    public Location(String longitude, String latitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
