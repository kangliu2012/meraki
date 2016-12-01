package com.everbridge.merakiui.dao;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import com.everbridge.meraki.domain.domain.AccessPoint;
import com.everbridge.meraki.domain.utils.HALUtils;
import com.everbridge.merakiui.configuration.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;

/**
 * Created by kangliu on 10/24/16.
 */
@Component
public class AccessPointDAO {
    @Autowired
    private Configuration configuration = null;

    public Collection<Resource<AccessPoint>> getAllAccessPointsByOrgID(String orgID) {
        String url = configuration.getBaseurl() + "accesspoint/search/findByOrganization_Key";
        url += ("?key=" + orgID);
        RestTemplate restTemplate = HALUtils.getRestTemplate();
        ParameterizedTypeReference<Resources<Resource<AccessPoint>>> pt = new ParameterizedTypeReference<Resources<Resource<AccessPoint>>>(){};
        ResponseEntity<Resources<Resource<AccessPoint>>> response = restTemplate.exchange(url, HttpMethod.GET, null, pt);
        Resources<Resource<AccessPoint>> resourceResources = response.getBody();
        return resourceResources.getContent();
    }

    public Collection<Resource<AccessPoint>> getAllAccessPointsMappedByOrgID(String orgID) {
        String url = configuration.getBaseurl() + "accesspoint/search/findDistinctByOrganization_KeyAndLocationNotNull";
        url += ("?key=" + orgID);
        RestTemplate restTemplate = HALUtils.getRestTemplate();
        ParameterizedTypeReference<Resources<Resource<AccessPoint>>> pt = new ParameterizedTypeReference<Resources<Resource<AccessPoint>>>(){};
        ResponseEntity<Resources<Resource<AccessPoint>>> response = restTemplate.exchange(url, HttpMethod.GET, null, pt);
        Resources<Resource<AccessPoint>> resourceResources = response.getBody();
        return resourceResources.getContent();
    }

    public Collection<Resource<AccessPoint>> getAllAccessPointsUnmappedByOrgID(String orgID) {
        String url = configuration.getBaseurl() + "accesspoint/search/findDistinctByOrganization_KeyAndLocationNull";
        url += ("?key=" + orgID);
        RestTemplate restTemplate = HALUtils.getRestTemplate();
        ParameterizedTypeReference<Resources<Resource<AccessPoint>>> pt = new ParameterizedTypeReference<Resources<Resource<AccessPoint>>>(){};
        ResponseEntity<Resources<Resource<AccessPoint>>> response = restTemplate.exchange(url, HttpMethod.GET, null, pt);
        Resources<Resource<AccessPoint>> resourceResources = response.getBody();
        return resourceResources.getContent();
    }

    public HttpStatus updateAccessPoint(AccessPoint accessPoint, String ID) {
        String url = configuration.getBaseurl() + "accesspoint/" + ID;
        RestTemplate restTemplate = HALUtils.getRestTemplate();
        ParameterizedTypeReference<Resources<AccessPoint>> pt = new ParameterizedTypeReference<Resources<AccessPoint>>(){};
        HttpEntity<AccessPoint> requestEntity = new HttpEntity<>(accessPoint);
        ResponseEntity<Resources<AccessPoint>> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, pt);
        return response.getStatusCode();
    }

    public AccessPoint getAccessPointByID(String ID) {
        String url = configuration.getBaseurl() + "accesspoint/" + ID;
        RestTemplate restTemplate = HALUtils.getRestTemplate();
        ParameterizedTypeReference<Resource<AccessPoint>> pt = new ParameterizedTypeReference<Resource<AccessPoint>>(){};
        ResponseEntity<Resource<AccessPoint>> response = restTemplate.exchange(url, HttpMethod.GET, null, pt);
        Resource<AccessPoint> resource = response.getBody();
        return resource.getContent();
    }

    public Long getAccessPointUnmappedCountByOrgID(String orgID) {
        String url = configuration.getBaseurl() + "accesspoint/search/countDistinctByOrganization_KeyAndLocationNull";
        url += ("?key=" + orgID );
        RestTemplate restTemplate = HALUtils.getRestTemplate();
        ParameterizedTypeReference<Long> pt = new ParameterizedTypeReference<Long>(){};
        ResponseEntity<Long> response = restTemplate.exchange(url, HttpMethod.GET, null, pt);
        Long resource = response.getBody();
        return resource;
    }

    public Long getAccessPointMappedCountByOrgID(String orgID) {
        String url = configuration.getBaseurl() + "accesspoint/search/countDistinctByOrganization_KeyAndLocationNotNull";
        url += ("?key=" + orgID );
        RestTemplate restTemplate = HALUtils.getRestTemplate();
        ParameterizedTypeReference<Long> pt = new ParameterizedTypeReference<Long>(){};
        ResponseEntity<Long> response = restTemplate.exchange(url, HttpMethod.GET, null, pt);
        Long resource = response.getBody();
        return resource;
    }
}
