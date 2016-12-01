package com.everbridge.merakiui.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.everbridge.merakiui.configuration.Configuration;
import com.google.common.collect.ImmutableMap;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Vector;

/**
 * Created by kangliu on 10/13/16.
 */
@Component
public class LinkAPIsDAO {
    @Autowired
    private Configuration configuration = null;

    public HttpStatus LinkTwoObjects(String parent, String parentID, String child, String childID) {
        String parentLink = configuration.getBaseurl() + parent + "/" + parentID + "/" + child;
        List<String[]> vecPlus = new Vector<>();
        vecPlus.add(new String[]{"account", "organization"});
        for(int i = 0; i < vecPlus.size(); i++) {
            String [] plus = vecPlus.get(i);
            if( parent.equals(plus[0]) && child.equals(plus[1])) {
                parentLink += "s";
                break;
            }
        }
        String childLink = configuration.getBaseurl() + child + "/" + childID;
        RestTemplate template = new RestTemplate();
        HttpHeaders reqHeaders = new HttpHeaders();
        reqHeaders.add(HttpHeaders.CONTENT_TYPE, new MediaType("text", "uri-list").toString());
        HttpEntity<String> reqEntity = new HttpEntity<String>(childLink, reqHeaders);
        ResponseEntity<String> stringResponseEntity = template.exchange(parentLink, HttpMethod.PUT, reqEntity, String.class,
                ImmutableMap.of());
        return stringResponseEntity.getStatusCode();
    }

    public HttpStatus UnlinkTwoObjects(String parent, String parentID, String child, String childID) {
        String parentLink = configuration.getBaseurl() + parent + "/" + parentID + "/" + child;
        List<String[]> vecPlus = new Vector<>();
        vecPlus.add(new String[]{"account", "organization"});
        for(int i = 0; i < vecPlus.size(); i++) {
            String [] plus = vecPlus.get(i);
            if( parent.equals(plus[0]) && child.equals(plus[1])) {
                parentLink += "s";
                break;
            }
        }
        String childLink = configuration.getBaseurl() + child + "/" + childID;
        RestTemplate template = new RestTemplate();
        HttpHeaders reqHeaders = new HttpHeaders();
        reqHeaders.add(HttpHeaders.CONTENT_TYPE, new MediaType("text", "uri-list").toString());
        HttpEntity<String> reqEntity = new HttpEntity<String>(childLink, reqHeaders);
        ResponseEntity<String> stringResponseEntity = template.exchange(parentLink, HttpMethod.DELETE, reqEntity, String.class,
                ImmutableMap.of());
        return stringResponseEntity.getStatusCode();
    }
}
