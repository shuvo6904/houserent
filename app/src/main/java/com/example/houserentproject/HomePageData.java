package com.example.houserentproject;

import java.io.Serializable;

public class HomePageData implements Serializable {

    private String rentAmount;
    private String location;
    private String image;
    private String buildingName;
    private String floorNumber;
    private String detailsAddress;
    private String valueOfGender;
    private String valueOfRentType;
    private String datePick;
    private String nameOfUser;
    private String phnNumOfUser;
    private String id;
    private String postStatus;


    public HomePageData() {
    }

    public HomePageData(String rentAmount, String location, String image, String buildingName, String floorNumber, String detailsAddress, String valueOfGender, String valueOfRentType, String datePick, String nameOfUser, String phnNumOfUser, String id, String postStatus) {
        this.rentAmount = rentAmount;
        this.location = location;
        this.image = image;
        this.buildingName = buildingName;
        this.floorNumber = floorNumber;
        this.detailsAddress = detailsAddress;
        this.valueOfGender = valueOfGender;
        this.valueOfRentType = valueOfRentType;
        this.datePick = datePick;
        this.nameOfUser = nameOfUser;
        this.phnNumOfUser = phnNumOfUser;
        this.id = id;
        this.postStatus = postStatus;
    }



    public String getId() {
        return id;
    }

    public String getRentAmount() {
        return rentAmount;
    }

    public String getLocation() {
        return location;
    }

    public String getImage() {
        return image;
    }

    public String getBuildingName(){return buildingName;}

    public String getFloorNumber() {
        return floorNumber;
    }

    public String getDetailsAddress() {
        return detailsAddress;
    }

    public String getValueOfGender(){ return valueOfGender;}

    public String getValueOfRentType() {
        return valueOfRentType;
    }

    public String getDatePick() {
        return datePick;
    }

    public String getNameOfUser() {
        return nameOfUser;
    }

    public String getPhnNumOfUser() {
        return phnNumOfUser;
    }

    public String getPostStatus(){return postStatus;}

}
