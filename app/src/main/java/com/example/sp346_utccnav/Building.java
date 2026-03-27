package com.example.sp346_utccnav;

public class Building {
    private String name;
    private double latitude;
    private double longitude;
    private String description;
    private Integer imageResourceId; // Using Integer instead of int to allow null

    public Building(String name, double latitude, double longitude, String description, Integer imageResourceId) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.imageResourceId = imageResourceId;
    }

    public String getName() { return name; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public String getDescription() { return description; }
    public Integer getImageResourceId() { return imageResourceId; }
}