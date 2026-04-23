package com.example.sp346_utccnav;

public class HistoryEntry {
    private Long id;
    private String destination;
    private String startingPoint;
    private Double latitude;
    private Double longitude;
    private String timestamp;

    public HistoryEntry() {}

    // Constructor to help MainActivity save data
    public HistoryEntry(String destination, String startingPoint, Double latitude, Double longitude) {
        this.destination = destination;
        this.startingPoint = startingPoint;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getDestination() { return destination; }
    public String getStartingPoint() { return startingPoint; }
    public Double getLatitude() { return latitude; }
    public Double getLongitude() { return longitude; }
    public String getTimestamp() { return timestamp; }
}