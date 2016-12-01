package com.everbridge.merakiui.model;

/**
 * Created by kangliu on 10/26/16.
 */
public class ClientMainUI {
    private String id;
    private String ipv4;
    private String lat;
    private String lng;
    private String mac;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setIpv4(String ipv4) {
        this.ipv4 = ipv4;
    }

    public String getIpv4() {
        return ipv4;
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

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getMac() {
        return mac;
    }
}
