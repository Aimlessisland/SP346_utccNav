package com.example.sp346_utccnav;

import java.util.List;

public class calculateNear {

    private double latitude;
    private double longitude;

    // Constructor to receive location from MainActivity
    public calculateNear(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getNearbuilding() {
        List<Building> buildings = BuildingRepository.getBuildings();
        if (buildings == null || buildings.isEmpty()) {
            return "ไม่พบข้อมูลอาคาร";
        }

        Building closestBuilding = null;
        double minDistance = Double.MAX_VALUE;

        for (Building building : buildings) {
            double dist = calculateDistance(this.latitude, this.longitude, 
                                           building.getLatitude(), building.getLongitude());
            if (dist < minDistance) {
                minDistance = dist;
                closestBuilding = building;
            }
        }

        return (closestBuilding != null) ? closestBuilding.getName() : "ไม่พบอาคารใกล้เคียง";
    }

    // Haversine formula for distance calculation in meters
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2))
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.cos(Math.toRadians(theta));
        dist = Math.acos(dist);
        dist = Math.toDegrees(dist);
        dist = dist * 60 * 1.1515 * 1.609344 * 1000;
        return (dist);
    }

    public String getBuildingName() {
        return getNearbuilding();
    }
}