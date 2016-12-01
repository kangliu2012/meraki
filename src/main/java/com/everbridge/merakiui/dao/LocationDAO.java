package com.everbridge.merakiui.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.everbridge.meraki.domain.domain.Location;
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

/**
 * Created by kangliu on 10/13/16.
 */
@Component
public class LocationDAO {
    @Autowired
    private Configuration configuration = null;

    public String addLocation(Location location) {
        String url = configuration.getBaseurl() + "location";
        RestTemplate restTemplate = HALUtils.getRestTemplate();
        ParameterizedTypeReference<Resources<Location>> pt = new ParameterizedTypeReference<Resources<Location>>(){};
        HttpEntity<Location> requestEntity = new HttpEntity<Location>(location);
        ResponseEntity<Resources<Location>> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, pt);
        if( response.getStatusCode() == HttpStatus.CREATED ){
            String selfURL = response.getBody().getLink("self").getHref();
            return selfURL.substring(selfURL.lastIndexOf('/') + 1);
        }
        return null;
    }

    public HttpStatus updateLocation(Location location, String ID) {
        String url = configuration.getBaseurl() + "location/" + ID;
        RestTemplate restTemplate = HALUtils.getRestTemplate();
        ParameterizedTypeReference<Resources<Location>> pt = new ParameterizedTypeReference<Resources<Location>>(){};
        HttpEntity<Location> requestEntity = new HttpEntity<Location>(location);
        ResponseEntity<Resources<Location>> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, pt);
        return response.getStatusCode();
    }

    public Resources<Resource<Location>> getAllLocationsByURL(String url) {
        RestTemplate restTemplate = HALUtils.getRestTemplate();
        ParameterizedTypeReference<Resources<Resource<Location>>> pt = new ParameterizedTypeReference<Resources<Resource<Location>>>(){};
        ResponseEntity<Resources<Resource<Location>>> response = restTemplate.exchange(url, HttpMethod.GET, null, pt);
        Resources<Resource<Location>> resourceResources = response.getBody();
        return resourceResources;
    }

    public Resource<Resource<Location>> getOneLocationByURL(String url) {
        RestTemplate restTemplate = HALUtils.getRestTemplate();
        ParameterizedTypeReference<Resource<Resource<Location>>> pt = new ParameterizedTypeReference<Resource<Resource<Location>>>(){};
        ResponseEntity<Resource<Resource<Location>>> response = restTemplate.exchange(url, HttpMethod.GET, null, pt);
        Resource<Resource<Location>> resourceResources = response.getBody();
        return resourceResources;
    }

    public Location getLocationByID(String Id) {
        String url = configuration.getBaseurl() + "location/" + Id;
        RestTemplate restTemplate = HALUtils.getRestTemplate();
        ParameterizedTypeReference<Resource<Location>> pt = new ParameterizedTypeReference<Resource<Location>>(){};
        ResponseEntity<Resource<Location>> response = restTemplate.exchange(url, HttpMethod.GET, null, pt);
        Resource<Location> resource = response.getBody();
        return resource.getContent();
    }

    public Resource<Resource<Location>> getOneLocationByAccesspointID(String accesspointID) {
        String url = configuration.getBaseurl() + "accesspoint/" + accesspointID + "/location";
        return getOneLocationByURL(url);
    }

    public Resources<Resource<Location>> getAllLocationsByOrgID(String orgID) {
        String url = configuration.getBaseurl() + "location/search/findByOrganization_Key";
        url += ("?key=" + orgID );
        return getAllLocationsByURL(url);
    }
}
