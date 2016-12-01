package com.everbridge.merakiui.soa;

import com.everbridge.merakiui.dao.LinkAPIsDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * Created by kangliu on 10/23/16.
 */
@Component
public class LinkAPIsSOA {
    @Autowired
    private LinkAPIsDAO linkAPIsDAO = null;

    public HttpStatus LinkTwoObjects(String parent, String parentID, String child, String childID) {
        return linkAPIsDAO.LinkTwoObjects(parent, parentID, child, childID);
    }

    public HttpStatus UnlinkTwoObjects(String parent, String parentID, String child, String childID) {
        return linkAPIsDAO.UnlinkTwoObjects(parent, parentID, child, childID);
    }
}
