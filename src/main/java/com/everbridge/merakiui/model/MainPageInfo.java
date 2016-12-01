package com.everbridge.merakiui.model;

/**
 * Created by kangliu on 11/2/16.
 */
public class MainPageInfo {
    private String contactUnpaired;
    private String contactPaired;
    private String accessPointUnmapped;
    private String accessPointMapped;

    public void setContactUnpaired(String contactUnpaired) {
        this.contactUnpaired = contactUnpaired;
    }

    public String getContactUnpaired() {
        return contactUnpaired;
    }

    public void setContactPaired(String contactPaired) {
        this.contactPaired = contactPaired;
    }

    public String getContactPaired() {
        return contactPaired;
    }

    public void setAccessPointUnmapped(String accessPointUnmapped) {
        this.accessPointUnmapped = accessPointUnmapped;
    }

    public String getAccessPointUnmapped() {
        return accessPointUnmapped;
    }

    public void setAccessPointMapped(String accessPointMapped) {
        this.accessPointMapped = accessPointMapped;
    }

    public String getAccessPointMapped() {
        return accessPointMapped;
    }
}
