package com.everbridge.merakiui.model;

/**
 * Created by kangliu on 10/26/16.
 */
public class ClientUI {
    private String id;
    private String mac;
    private String lat;
    private String lng;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getMac() {
        return mac;
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
}
