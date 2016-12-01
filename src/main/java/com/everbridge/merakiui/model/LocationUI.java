package com.everbridge.merakiui.model;

import java.math.BigDecimal;

/**
 * Created by kangliu on 10/14/16.
 */
public class LocationUI {
    private String id;
    private String ebId;
    private String name;
    private String street;
    private String aptSuite;
    private String city;
    private String state;
    private String zipCode;
    private String country;
    private String floorNumber;
    private String roomNumber;
    private String lat;
    private String lng;
    private String locationIcon;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setEbId(String ebId) {
        this.ebId = ebId;
    }

    public String getEbId() {
        return ebId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreet() {
        return street;
    }

    public void setAptSuite(String aptSuite) {
        this.aptSuite = aptSuite;
    }

    public String getAptSuite() {
        return aptSuite;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry() {
        return country;
    }

    public void setFloorNumber(String floorNumber) {
        this.floorNumber = floorNumber;
    }

    public String getFloorNumber() {
        return floorNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setLocationIcon(String locationIcon) {
        this.locationIcon = locationIcon;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLat() {
        return lat;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLng() {
        return lng;
    }

    public String getLocationIcon() {
        return locationIcon;
    }

}
