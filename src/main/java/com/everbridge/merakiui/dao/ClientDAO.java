package com.everbridge.merakiui.dao;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import com.everbridge.meraki.domain.domain.Client;
import com.everbridge.meraki.domain.utils.HALUtils;
import com.everbridge.merakiui.configuration.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * Created by kangliu on 10/26/16.
 */
@Component
public class ClientDAO {
    @Autowired
    private Configuration configuration = null;

    public Resources<Resource<Client>> getAllClientsByURL(String url) {
        RestTemplate restTemplate = HALUtils.getRestTemplate();
        ParameterizedTypeReference<Resources<Resource<Client>>> pt = new ParameterizedTypeReference<Resources<Resource<Client>>>(){};
        ResponseEntity<Resources<Resource<Client>>> response = restTemplate.exchange(url, HttpMethod.GET, null, pt);
        Resources<Resource<Client>> resourceResources = response.getBody();
        return resourceResources;
    }

    public Resources<Resource<Client>> getClientListByMacAndOrgID(String mac, String orgID) {
        String url = configuration.getBaseurl() + "client/search/findByMacStartingWithAndOrganization_KeyAndContactNull";
        url += ("?mac=" + mac + "&key=" + orgID );
        return getAllClientsByURL(url);
    }

    public Resources<Resource<Client>> getAllClientListByMacAndOrgID(String mac, String orgID) {
        String url = configuration.getBaseurl() + "client/search/findByMacStartingWithAndOrganization_KeyAndContactNull";
        url += ("?mac=" + mac + "&key=" + orgID + "&page=0&size=100000");
        return getAllClientsByURL(url);
    }

    public Resources<Resource<Client>> getMappedClientListByOrgIDAndLocationID(String orgID, String locID) {
        String url = configuration.getBaseurl() + "client/search/findDistinctByOrganization_KeyAndAccesspoint_Location_Key";
        url += ("?orgKey=" + orgID + "&locKey=" + locID );
        return getAllClientsByURL(url);
    }

    public Resources<Resource<Client>> getMappedClientListByOrgIDAndLocationIDAndPage(String orgID, String locID, String page) {
        String url = configuration.getBaseurl() + "client/search/findDistinctByOrganization_KeyAndAccesspoint_Location_Key";
        url += ("?orgKey=" + orgID + "&locKey=" + locID + "&page=" + page);
        return getAllClientsByURL(url);
    }


    public Long getKnownClientCountByLocationID(String orgID, String locID) {
        String url = configuration.getBaseurl() + "client/search/countDistinctByOrganization_KeyAndAccesspoint_Location_Key";
        url += ("?orgKey=" + orgID );
        url += ("&locKey=" + locID);
        RestTemplate restTemplate = HALUtils.getRestTemplate();
        ParameterizedTypeReference<Long> pt = new ParameterizedTypeReference<Long>(){};
        ResponseEntity<Long> response = restTemplate.exchange(url, HttpMethod.GET, null, pt);
        Long resource = response.getBody();
        return resource;
    }

    public Resource<Client> getClientByMacAndOrgID(String mac, String orgID) {
        String url = configuration.getBaseurl() + "client/search/findByMacAndOrganization_Key";
        url += ("?mac=" + mac + "&key=" + orgID );
        RestTemplate restTemplate = HALUtils.getRestTemplate();
        ParameterizedTypeReference<Resource<Client>> pt = new ParameterizedTypeReference<Resource<Client>>(){};
        ResponseEntity<Resource<Client>> response = restTemplate.exchange(url, HttpMethod.GET, null, pt);
        return response.getBody();
    }
 }
