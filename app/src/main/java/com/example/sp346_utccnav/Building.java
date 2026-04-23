package com.example.sp346_utccnav;

public class Building {
    private String name;
    private double latitude;
    private double longitude;
    private String description;
    private Integer imageResourceId; 
    private Integer startPixel; // New field for start pixel

    public Building(String name, double latitude, double longitude, String description, Integer imageResourceId, Integer startPixel) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.imageResourceId = imageResourceId;
        this.startPixel = startPixel;
    }

    public String getName() { return name; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public String getDescription() { return description; }
    public Integer getImageResourceId() { return imageResourceId; }
    public Integer getStartPixel() { return startPixel; }

}