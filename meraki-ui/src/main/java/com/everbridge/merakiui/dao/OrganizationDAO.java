package com.everbridge.merakiui.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.everbridge.meraki.domain.domain.Organization;
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
import java.util.Iterator;

/**
 * Created by kangliu on 10/12/16.
 */
@Component
public class OrganizationDAO {
    @Autowired
    private Configuration configuration = null;

    public String addOrganization(Organization organization) {
        String url = configuration.getBaseurl() + "organization";
        RestTemplate restTemplate = HALUtils.getRestTemplate();
        ParameterizedTypeReference<Resources<Organization>> pt = new ParameterizedTypeReference<Resources<Organization>>(){};
        HttpEntity<Organization> requestEntity = new HttpEntity<Organization>(organization);
        ResponseEntity<Resources<Organization>> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, pt);
        if( response.getStatusCode() == HttpStatus.CREATED ){
            String selfURL = response.getBody().getLink("self").getHref();
            return selfURL.substring(selfURL.lastIndexOf('/') + 1);
        }
        return null;
    }

    public void deleteOrganizationByID(String ID) {
        String url = configuration.getBaseurl() + "organization/" + ID;
        RestTemplate restTemplate = HALUtils.getRestTemplate();
        restTemplate.delete(url);
    }

    public HttpStatus updateOrganization(Organization organization, String ID) {
        String url = configuration.getBaseurl() + "organization/" + ID;
        RestTemplate restTemplate = HALUtils.getRestTemplate();
        ParameterizedTypeReference<Resources<Organization>> pt = new ParameterizedTypeReference<Resources<Organization>>(){};
        HttpEntity<Organization> requestEntity = new HttpEntity<Organization>(organization);
        ResponseEntity<Resources<Organization>> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, pt);
        return response.getStatusCode();
    }

    public Collection<Resource<Organization>> getAllOrganizations() {
        String url = configuration.getBaseurl() + "organization";
        RestTemplate restTemplate = HALUtils.getRestTemplate();
        ParameterizedTypeReference<Resources<Resource<Organization>>> pt = new ParameterizedTypeReference<Resources<Resource<Organization>>>(){};
        ResponseEntity<Resources<Resource<Organization>>> response = restTemplate.exchange(url, HttpMethod.GET, null, pt);
        Resources<Resource<Organization>> resourceResources = response.getBody();
        return resourceResources.getContent();
    }

    public Resources<Resource<Organization>> getAllOrganizationsByAccountID(String accountID) {
        String url = configuration.getBaseurl() + "account/" + accountID + "/organizations";
        return getAllOrganizationsByURL(url);
    }

    public Resources<Resource<Organization>> getAllOrganizationsByContactID(String contactID) {
        String url = configuration.getBaseurl() + "contact/" + contactID + "/organization";
        return getAllOrganizationsByURL(url);
    }

    public Resources<Resource<Organization>> getAllOrganizationsByURL(String url) {
        RestTemplate restTemplate = HALUtils.getRestTemplate();
        ParameterizedTypeReference<Resources<Resource<Organization>>> pt = new ParameterizedTypeReference<Resources<Resource<Organization>>>(){};
        ResponseEntity<Resources<Resource<Organization>>> response = restTemplate.exchange(url, HttpMethod.GET, null, pt);
        Resources<Resource<Organization>> resourceResources = response.getBody();
        return resourceResources;
    }

    public Organization getOrganizationByID(String ID) {
        String url = configuration.getBaseurl() + "organization/" + ID;
        RestTemplate restTemplate = HALUtils.getRestTemplate();
        ParameterizedTypeReference<Resource<Organization>> pt = new ParameterizedTypeReference<Resource<Organization>>(){};
        ResponseEntity<Resource<Organization>> response = restTemplate.exchange(url, HttpMethod.GET, null, pt);
        Resource<Organization> resource = response.getBody();
        return resource.getContent();
    }

    public boolean isOrgIDExist(String orgID) {
        Collection<Resource<Organization>> resourceCollection = getAllOrganizations();
        Iterator<Resource<Organization>> resourceIterator = resourceCollection.iterator();
        while(resourceIterator.hasNext()) {
            Resource<Organization> accountResource = resourceIterator.next();
            Organization organization = accountResource.getContent();
            if( organization.ebOrganizationId().equals(orgID) ) {
                return true;
            }
        }
        return false;
    }

    public Resources<Resource<Organization>> getOrganizationByAccountID(String accountID) {
        String url = configuration.getBaseurl() + "account/" + accountID + "/organizations";
        return getAllOrganizationsByURL(url);
    }
}
