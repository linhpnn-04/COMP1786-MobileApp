package com.example.lab5contactdatabase;

import java.io.Serializable;

public class Hike implements Serializable{
    private int id;
    private String name;
    private String location;
    private String dateandtime;

    private int parkingAvailable;


    private double lengthofhike;
    private int hike_lv;
    private String scenicPoints;
    private String description;
    private String hikeType;
    private static final long serialVersionUID = 1L;
    public Hike(int id, String name, String location, String dateandtime, int parkingAvailable, double lengthofhike, int hike_lv, String scenicPoints, String description, String hikeType) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.dateandtime = dateandtime;
        this.parkingAvailable = parkingAvailable;
        this.lengthofhike = lengthofhike;
        this.hike_lv = hike_lv;
        this.scenicPoints = scenicPoints;
        this.description = description;
        this.hikeType = hikeType;

    }
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getDateandtime() {
        return dateandtime;
    }

    public int getParkingAvailable() {
        return parkingAvailable;

    }

    public double getLengthofhike() {
        return lengthofhike;
    }

    public int getHike_lv() {
        return hike_lv;
    }

    public String getScenicPoints() {
        return scenicPoints;
    }
    public String getDescription() {
        return description;
    }
    public String getHikeType() {
        return hikeType;}

    public void setName(String name) {
        this.name = name;
    }

}