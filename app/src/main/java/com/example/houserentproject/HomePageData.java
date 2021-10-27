package com.example.houserentproject;

import java.io.Serializable;

public class HomePageData implements Serializable {
    private String image;
    private String rentAmount;
    private String location;
    private String buildingName;
    private String floorNumber;
    private String detailsAddress;
    private String valueOfGender;
    private String valueOfRentType;
    private String datePick;
    private String adUserId;
    private String id;
    private String postStatus;
    private double hostelLat;
    private double hostelLon;


    public HomePageData() {
    }

    public HomePageData(String image, String rentAmount, String location, String buildingName, String floorNumber, String detailsAddress, String valueOfGender, String valueOfRentType, String datePick, String adUserId, String id, String postStatus, double hostelLat, double hostelLon) {
        this.image = image;
        this.rentAmount = rentAmount;
        this.location = location;
        this.buildingName = buildingName;
        this.floorNumber = floorNumber;
        this.detailsAddress = detailsAddress;
        this.valueOfGender = valueOfGender;
        this.valueOfRentType = valueOfRentType;
        this.datePick = datePick;
        this.adUserId = adUserId;
        this.id = id;
        this.postStatus = postStatus;
        this.hostelLat = hostelLat;
        this.hostelLon = hostelLon;
    }

    public String getImage() {
        return image;
    }

    public String getRentAmount() {
        return rentAmount;
    }

    public String getLocation() {
        return location;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public String getFloorNumber() {
        return floorNumber;
    }

    public String getDetailsAddress() {
        return detailsAddress;
    }

    public String getValueOfGender() {
        return valueOfGender;
    }

    public String getValueOfRentType() {
        return valueOfRentType;
    }

    public String getDatePick() {
        return datePick;
    }

    public String getAdUserId() {
        return adUserId;
    }

    public String getId() {
        return id;
    }

    public String getPostStatus() {
        return postStatus;
    }

    public double getHostelLat() {
        return hostelLat;
    }

    public double getHostelLon() {
        return hostelLon;
    }
}
