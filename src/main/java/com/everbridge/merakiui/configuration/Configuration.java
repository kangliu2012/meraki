package com.everbridge.merakiui.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by kangliu on 10/21/16.
 */
@Component
public class Configuration {
    @Value("${baseurl}")
    private String baseurl;

    @Value("${everbridgeurl}")
    private String everbridgeurl;

    public String getBaseurl() {
        return baseurl;
    }

    public String getEverbridgeurl() {
        return everbridgeurl;
    }
}