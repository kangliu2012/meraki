package com.everbridge.merakiui.model;

import java.util.List;

/**
 * Created by kangliu on 10/24/16.
 */
public class ContactUI {
    private String id;
    private String externalId;
    private String firstName;
    private String lastName;
    private String ebContactId;
    private List<ClientUI> clientUIList;

    public void setId(String id) { this.id = id; }

    public String getId() { return id; }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setEbContactId(String ebContactId) {
        this.ebContactId = ebContactId;
    }

    public String getEbContactId() {
        return ebContactId;
    }

    public void setClientUIList(List<ClientUI> clientUIList) {
        this.clientUIList = clientUIList;
    }

    public List<ClientUI> getClientUIList() {
        return clientUIList;
    }
}
