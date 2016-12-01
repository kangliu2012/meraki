package com.everbridge.merakiui.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.everbridge.meraki.domain.domain.Contact;
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
public class ContactDAO {
    @Autowired
    private Configuration configuration = null;

    public Collection<Resource<Contact>> getAllContactsByOrgIDAndPage(String orgID, String page) {
        String url = configuration.getBaseurl() + "contact/search/findByOrganization_Key";
        url += ("?key=" + orgID);
        url += ("&page=" + page);
        RestTemplate restTemplate = HALUtils.getRestTemplate();
        ParameterizedTypeReference<Resources<Resource<Contact>>> pt = new ParameterizedTypeReference<Resources<Resource<Contact>>>(){};
        ResponseEntity<Resources<Resource<Contact>>> response = restTemplate.exchange(url, HttpMethod.GET, null, pt);
        Resources<Resource<Contact>> resourceResources = response.getBody();
        return resourceResources.getContent();
    }

    public Collection<Resource<Contact>> getContactUnpairedCountByOrgID(String orgID) {
        String url = configuration.getBaseurl() + "contact/search/findDistinctByOrganization_KeyAndClientsNull";
        url += ("?key=" + orgID + "&size=1000000");
        RestTemplate restTemplate = HALUtils.getRestTemplate();
        ParameterizedTypeReference<Resources<Resource<Contact>>> pt = new ParameterizedTypeReference<Resources<Resource<Contact>>>(){};
        ResponseEntity<Resources<Resource<Contact>>> response = restTemplate.exchange(url, HttpMethod.GET, null, pt);
        Resources<Resource<Contact>> resourceResources = response.getBody();
        return resourceResources.getContent();
    }

    public Collection<Resource<Contact>> getAllContactsByOrgID(String orgID) {
        String url = configuration.getBaseurl() + "contact/search/findByOrganization_Key";
        url += ("?key=" + orgID);
        url += ("&size=100000");
        RestTemplate restTemplate = HALUtils.getRestTemplate();
        ParameterizedTypeReference<Resources<Resource<Contact>>> pt = new ParameterizedTypeReference<Resources<Resource<Contact>>>(){};
        ResponseEntity<Resources<Resource<Contact>>> response = restTemplate.exchange(url, HttpMethod.GET, null, pt);
        Resources<Resource<Contact>> resourceResources = response.getBody();
        return resourceResources.getContent();
    }

    public Collection<Resource<Contact>> getContactPairedCountByOrgID(String orgID) {
        String url = configuration.getBaseurl() + "contact/search/findDistinctByOrganization_KeyAndClientsNotNull";
        url += ("?key=" + orgID + "&size=1000000");
        RestTemplate restTemplate = HALUtils.getRestTemplate();
        ParameterizedTypeReference<Resources<Resource<Contact>>> pt = new ParameterizedTypeReference<Resources<Resource<Contact>>>(){};
        ResponseEntity<Resources<Resource<Contact>>> response = restTemplate.exchange(url, HttpMethod.GET, null, pt);
        Resources<Resource<Contact>> resourceResources = response.getBody();
        return resourceResources.getContent();
    }

    public Collection<Resource<Contact>> getAllContactsUnpairedByOrgID(String orgID) {
        String url = configuration.getBaseurl() + "contact/search/findDistinctByOrganization_KeyAndClientsNull";
        url += ("?key=" + orgID + "&size=1000000");
        RestTemplate restTemplate = HALUtils.getRestTemplate();
        ParameterizedTypeReference<Resources<Resource<Contact>>> pt = new ParameterizedTypeReference<Resources<Resource<Contact>>>(){};
        ResponseEntity<Resources<Resource<Contact>>> response = restTemplate.exchange(url, HttpMethod.GET, null, pt);
        Resources<Resource<Contact>> resourceResources = response.getBody();
        return resourceResources.getContent();
    }

    public Collection<Resource<Contact>> getAllContactsPairedByOrgID(String orgID) {
        String url = configuration.getBaseurl() + "contact/search/findDistinctByOrganization_KeyAndClientsNotNull";
        url += ("?key=" + orgID + "&size=1000000");
        RestTemplate restTemplate = HALUtils.getRestTemplate();
        ParameterizedTypeReference<Resources<Resource<Contact>>> pt = new ParameterizedTypeReference<Resources<Resource<Contact>>>(){};
        ResponseEntity<Resources<Resource<Contact>>> response = restTemplate.exchange(url, HttpMethod.GET, null, pt);
        Resources<Resource<Contact>> resourceResources = response.getBody();
        return resourceResources.getContent();
    }

    public Collection<Resource<Contact>> getAllContactsPairedByOrgIDAndPage(String orgID, String page) {
        String url = configuration.getBaseurl() + "contact/search/findDistinctByOrganization_KeyAndClientsNotNull";
        url += ("?key=" + orgID);
        url += ("&page=" + page);
        RestTemplate restTemplate = HALUtils.getRestTemplate();
        ParameterizedTypeReference<Resources<Resource<Contact>>> pt = new ParameterizedTypeReference<Resources<Resource<Contact>>>(){};
        ResponseEntity<Resources<Resource<Contact>>> response = restTemplate.exchange(url, HttpMethod.GET, null, pt);
        Resources<Resource<Contact>> resourceResources = response.getBody();
        return resourceResources.getContent();
    }

    public Collection<Resource<Contact>> getAllContactsUnpairedByOrgIDAndPage(String orgID, String page) {
        String url = configuration.getBaseurl() + "contact/search/findDistinctByOrganization_KeyAndClientsNull";
        url += ("?key=" + orgID);
        url += ("&page=" + page);
        RestTemplate restTemplate = HALUtils.getRestTemplate();
        ParameterizedTypeReference<Resources<Resource<Contact>>> pt = new ParameterizedTypeReference<Resources<Resource<Contact>>>(){};
        ResponseEntity<Resources<Resource<Contact>>> response = restTemplate.exchange(url, HttpMethod.GET, null, pt);
        Resources<Resource<Contact>> resourceResources = response.getBody();
        return resourceResources.getContent();
    }

    public Long getCountByOrgID(String orgID) {
        String url = configuration.getBaseurl() + "contact/search/countByOrganization_Key";
        url += ("?key=" + orgID );
        RestTemplate restTemplate = HALUtils.getRestTemplate();
        ParameterizedTypeReference<Long> pt = new ParameterizedTypeReference<Long>(){};
        ResponseEntity<Long> response = restTemplate.exchange(url, HttpMethod.GET, null, pt);
        Long resource = response.getBody();
        return resource;
    }

    public Resource<Contact> getContactResourceByID(String ID) {
        String url = configuration.getBaseurl() + "contact/" + ID;
        RestTemplate restTemplate = HALUtils.getRestTemplate();
        ParameterizedTypeReference<Resource<Contact>> pt = new ParameterizedTypeReference<Resource<Contact>>(){};
        ResponseEntity<Resource<Contact>> response = restTemplate.exchange(url, HttpMethod.GET, null, pt);
        Resource<Contact> resource = response.getBody();
        return resource;
    }

    public Contact getContactByID(String ID) {
        String url = configuration.getBaseurl() + "contact/" + ID;
        RestTemplate restTemplate = HALUtils.getRestTemplate();
        ParameterizedTypeReference<Resource<Contact>> pt = new ParameterizedTypeReference<Resource<Contact>>(){};
        ResponseEntity<Resource<Contact>> response = restTemplate.exchange(url, HttpMethod.GET, null, pt);
        Resource<Contact> resource = response.getBody();
        return resource.getContent();
    }

    public HttpStatus updateContact(Contact contact, String ID) {
        String url = configuration.getBaseurl() + "contact/" + ID;
        RestTemplate restTemplate = HALUtils.getRestTemplate();
        ParameterizedTypeReference<Resources<Contact>> pt = new ParameterizedTypeReference<Resources<Contact>>(){};
        HttpEntity<Contact> requestEntity = new HttpEntity<Contact>(contact);
        ResponseEntity<Resources<Contact>> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, pt);
        return response.getStatusCode();
    }

    public String addContact(Contact contact) {
        String url = configuration.getBaseurl() + "contact";
        RestTemplate restTemplate = HALUtils.getRestTemplate();
        ParameterizedTypeReference<Resources<Contact>> pt = new ParameterizedTypeReference<Resources<Contact>>(){};
        HttpEntity<Contact> requestEntity = new HttpEntity<Contact>(contact);
        ResponseEntity<Resources<Contact>> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, pt);
        if( response.getStatusCode() == HttpStatus.CREATED ){
            String selfURL = response.getBody().getLink("self").getHref();
            return selfURL.substring(selfURL.lastIndexOf('/') + 1);
        }
        return null;
    }
}
