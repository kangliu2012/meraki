package com.everbridge.merakiui.model;

import java.util.Date;
import java.util.List;

/**
 * Created by kangliu on 10/13/16.
 */
public class OrganizationUI {
    private String id;
    private String name;
    private String ebOrganizationId;
    private String validator;
    private String secret;
    private int dynamicLocationUpdateInterval;
    private String environment;
    private Date createdAt;
    private Date updateAt;
    private boolean importActive;
    private Date importFinishedAt;
    private String apiUsername;
    private String apiPassword;
    private String geolocateBy;
    private List<AccountUI> accountList;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setEbOrganizationId(String ebOrganizationId) {
        this.ebOrganizationId = ebOrganizationId;
    }

    public String getEbOrganizationId() {
        return ebOrganizationId;
    }

    public void setValidator(String validator) {
        this.validator = validator;
    }

    public String getValidator() {
        return validator;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getSecret() {
        return secret;
    }

    public void setDynamicLocationUpdateInterval(int dynamicLocationUpdateInterval) {
        this.dynamicLocationUpdateInterval = dynamicLocationUpdateInterval;
    }

    public int getDynamicLocationUpdateInterval() {
        return dynamicLocationUpdateInterval;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setImportActive(boolean importActive) {
        this.importActive = importActive;
    }

    public boolean isImportActive() {
        return importActive;
    }

    public void setImportFinishedAt(Date importFinishedAt) {
        this.importFinishedAt = importFinishedAt;
    }

    public Date getImportFinishedAt() {
        return importFinishedAt;
    }

    public void setApiUsername(String apiUsername) {
        this.apiUsername = apiUsername;
    }

    public String getApiUsername() {
        return apiUsername;
    }

    public void setApiPassword(String apiPassword) {
        this.apiPassword = apiPassword;
    }

    public String getApiPassword() {
        return apiPassword;
    }

    public void setGeolocateBy(String geolocateBy) {
        this.geolocateBy = geolocateBy;
    }

    public String getGeolocateBy() {
        return geolocateBy;
    }

    public void setAccountList(List<AccountUI> accountList) {
        this.accountList = accountList;
    }

    public List<AccountUI> getAccountList() {
        return accountList;
    }

}
