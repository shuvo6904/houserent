package com.example.houserentproject;

import java.io.Serializable;

public class HomePageData implements Serializable {
    private String image;
    private String rentAmount;
    private String location;
    private String buildingName;
    private String floorNumber;
    private String detailsAboutHostel;
    private String valueOfGender;
    private String valueOfRentType;
    private String datePick;
    private String adUserId;
    private String id;
    private String postStatus;
    private double hostelLat;
    private double hostelLon;
    private String electricityBill;
    private String gasBill;
    private String wifiBill;
    private String othersBill;
    private String generator;
    private String elevator;


    public HomePageData() {
    }

    public HomePageData(String image, String rentAmount, String location, String buildingName, String floorNumber, String detailsAboutHostel, String valueOfGender, String valueOfRentType, String datePick, String adUserId, String id, String postStatus, double hostelLat, double hostelLon, String electricityBill, String gasBill, String wifiBill, String othersBill, String generator, String elevator) {
        this.image = image;
        this.rentAmount = rentAmount;
        this.location = location;
        this.buildingName = buildingName;
        this.floorNumber = floorNumber;
        this.detailsAboutHostel = detailsAboutHostel;
        this.valueOfGender = valueOfGender;
        this.valueOfRentType = valueOfRentType;
        this.datePick = datePick;
        this.adUserId = adUserId;
        this.id = id;
        this.postStatus = postStatus;
        this.hostelLat = hostelLat;
        this.hostelLon = hostelLon;
        this.electricityBill = electricityBill;
        this.gasBill = gasBill;
        this.wifiBill = wifiBill;
        this.othersBill = othersBill;
        this.generator = generator;
        this.elevator = elevator;

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

    public String getDetailsAboutHostel() {
        return detailsAboutHostel;
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

    public String getElectricityBill() {
        return electricityBill;
    }

    public String getGasBill() {
        return gasBill;
    }

    public String getWifiBill() {
        return wifiBill;
    }

    public String getOthersBill() {
        return othersBill;
    }

    public String getGenerator() {
        return generator;
    }

    public String getElevator() {
        return elevator;
    }
}
