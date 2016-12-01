package com.everbridge.merakiui.dao;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import com.everbridge.meraki.domain.domain.Account;
import com.everbridge.meraki.domain.utils.HALUtils;
import com.everbridge.merakiui.configuration.Configuration;
import org.springframework.hateoas.Resource;
import org.springframework.http.*;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resources;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * Created by kangliu on 10/12/16.
 */
@Component
public class AccountDAO {
    @Autowired
    private Configuration configuration = null;

    public String addAccount(Account account) {
        String url = configuration.getBaseurl() + "account";
        RestTemplate restTemplate = HALUtils.getRestTemplate();
        ParameterizedTypeReference<Resources<Account>> pt = new ParameterizedTypeReference<Resources<Account>>(){};
        HttpEntity<Account> requestEntity = new HttpEntity<>(account);
        ResponseEntity<Resources<Account>> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, pt);
        if( response.getStatusCode() == HttpStatus.CREATED ){
            String selfURL = response.getBody().getLink("self").getHref();
            return selfURL.substring(selfURL.lastIndexOf('/') + 1);
        }
        return null;
    }

    public void deleteAccountByID(String ID) {
        String url = configuration.getBaseurl() + "account/" + ID;
        RestTemplate restTemplate = HALUtils.getRestTemplate();
        restTemplate.delete(url);
    }

    public HttpStatus updateAccount(Account account, String index) {
        String url = configuration.getBaseurl() + "account/" + index;
        RestTemplate restTemplate = HALUtils.getRestTemplate();
        ParameterizedTypeReference<Resources<Account>> pt = new ParameterizedTypeReference<Resources<Account>>(){};
        HttpEntity<Account> requestEntity = new HttpEntity<>(account);
        ResponseEntity<Resources<Account>> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, pt);
        return response.getStatusCode();
    }

    public Resources<Resource<Account>> getAllAccountsByOrgID(String orgID) {
        String url = configuration.getBaseurl() + "organization/" + orgID + "/accounts";
        return getAllAccountsByURL(url);
    }

    public Resources<Resource<Account>> getAllAccountsByURL(String url) {
        RestTemplate restTemplate = HALUtils.getRestTemplate();
        ParameterizedTypeReference<Resources<Resource<Account>>> pt = new ParameterizedTypeReference<Resources<Resource<Account>>>(){};
        ResponseEntity<Resources<Resource<Account>>> response = restTemplate.exchange(url, HttpMethod.GET, null, pt);
        Resources<Resource<Account>> resourceResources = response.getBody();
        return resourceResources;
    }

    public Collection<Resource<Account>> getAllAccount() {
        String url = configuration.getBaseurl() + "account";
        RestTemplate restTemplate = HALUtils.getRestTemplate();
        ParameterizedTypeReference<Resources<Resource<Account>>> pt = new ParameterizedTypeReference<Resources<Resource<Account>>>(){};
        ResponseEntity<Resources<Resource<Account>>> response = restTemplate.exchange(url, HttpMethod.GET, null, pt);
        Resources<Resource<Account>> resourceResources = response.getBody();
        return resourceResources.getContent();
    }

    public Account getAccountByID(String ID) {
        String url = configuration.getBaseurl() + "account/" + ID;
        RestTemplate restTemplate = HALUtils.getRestTemplate();
        ParameterizedTypeReference<Resource<Account>> pt = new ParameterizedTypeReference<Resource<Account>>(){};
        ResponseEntity<Resource<Account>> response = restTemplate.exchange(url, HttpMethod.GET, null, pt);
        Resource<Account> resource = response.getBody();
        return resource.getContent();
    }

    public boolean isEmailExist(String email) {
        Collection<Resource<Account>> resourceCollection = getAllAccount();
        Iterator<Resource<Account>> resourceIterator = resourceCollection.iterator();
        while(resourceIterator.hasNext()) {
            Resource<Account> accountResource = resourceIterator.next();
            Account account = accountResource.getContent();
            if( account.email().equals(email) ) {
                return true;
            }
        }
        return false;
    }

     public boolean isAdmin(String email) {
        Collection<Resource<Account>> resourceCollection = getAllAccount();
        Iterator<Resource<Account>> resourceIterator = resourceCollection.iterator();
        while(resourceIterator.hasNext()) {
            Resource<Account> accountResource = resourceIterator.next();
            Account account = accountResource.getContent();
            if( account.email().equals(email) ) {
                return account.admin();
            }
        }
        return false;
    }

    public String getAccountIDByEmail(String email) {
        Collection<Resource<Account>> accountCollection = getAllAccount();
        Iterator<Resource<Account>> iterator = accountCollection.iterator();
        while(iterator.hasNext()) {
            Resource<Account> accountResource = iterator.next();
            Account account = accountResource.getContent();
            if( account.email().equals(email)) {
                String selfURL = accountResource.getLink("self").getHref();
                return selfURL.substring(selfURL.lastIndexOf('/') + 1);
            }
        }
        return null;
    }
}
