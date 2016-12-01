package com.everbridge.merakiui.controller;

import com.everbridge.meraki.domain.domain.Location;
import com.everbridge.merakiui.model.AccessPointUI;
import com.everbridge.merakiui.model.ClientUI;
import com.everbridge.merakiui.model.ContactUI;
import com.everbridge.merakiui.model.LocationMainUI;
import com.everbridge.merakiui.soa.*;
import com.everbridge.merakiui.util.CookieAccess;
import com.everbridge.merakiui.util.ErrorPage;
import com.everbridge.merakiui.util.LoginStatus;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * Created by kangliu on 10/25/16.
 */
@RestController
public class RestfulController {
    @Autowired
    private AccessPointSOA accessPointSOA = null;

    @Autowired
    private LinkAPIsSOA linkAPIsSOA = null;

    @Autowired
    private ContactSOA contactSOA = null;

    @Autowired
    private ClientSOA clientSOA = null;

    @Autowired
    private CookieAccess cookieAccess = null;

    @Autowired
    private LocationSOA locationSOA = null;

    @Autowired
    ErrorPage errorPage = null;

    @RequestMapping(value = "/accesspointCustomerNameSave", method = RequestMethod.POST)
    public Map<String, Object> accesspointCustomerNameSave(@RequestBody AccessPointUI accessPointUI, HttpServletRequest httpServletRequest) {
        LoginStatus loginStatus = cookieAccess.getLoginStatus(httpServletRequest);
        Map<String, Object> hmap = new HashMap<String, Object>();
        switch (loginStatus) {
            case ENDUSER:
                boolean result = accessPointSOA.updateAccessPointBycustomerName(accessPointUI.getId(), accessPointUI.getCustomerName());
                hmap.put("save", result);
                return hmap;
            default:
                hmap.put("save", "error");
                return hmap;
        }
    }

    @RequestMapping(value = "/accesspointLocationSave", method = RequestMethod.POST)
    public Map<String, Object> accesspointLocationSave(@RequestBody AccessPointUI accessPointUI, HttpServletRequest httpServletRequest) {
        LoginStatus loginStatus = cookieAccess.getLoginStatus(httpServletRequest);
        Map<String, Object> hmap = new HashMap<String, Object>();
        switch (loginStatus) {
            case ENDUSER:
                if( accessPointUI.getLocationId().equals("-1") ) {
                    String locID = accessPointSOA.getLocationIDByAccesspointID(accessPointUI.getId());
                    HttpStatus httpStatus = linkAPIsSOA.UnlinkTwoObjects("accesspoint", accessPointUI.getId(), "location", locID);
                    boolean result = httpStatus.equals(HttpStatus.OK);
                    hmap.put("result", result);
                } else {
                    HttpStatus httpStatus = linkAPIsSOA.LinkTwoObjects("accesspoint", accessPointUI.getId(), "location", accessPointUI.getLocationId());
                    boolean result = httpStatus.equals(HttpStatus.OK);
                    hmap.put("result", result);
                }
                return hmap;
            default:
                hmap.put("result", "error");
                return hmap;
        }
    }

    @RequestMapping(value = "/downloadLocations", method = RequestMethod.POST)
    public Map<String, Object> downloadLocations(HttpServletRequest httpServletRequest) {
        LoginStatus loginStatus = cookieAccess.getLoginStatus(httpServletRequest);
        Map<String, Object> hmap = new HashMap<String, Object>();
        switch (loginStatus) {
            case ENDUSER:
                String orgID = cookieAccess.getOrganizationID(httpServletRequest);
                List<String[]> csvList = locationSOA.getLocationMapDataByOrg(orgID);
                hmap.put("csvList", csvList);
                return hmap;
            default:
                hmap.put("result", "error");
                return hmap;
        }
    }

    @RequestMapping(value = "/downloadContacts", method = RequestMethod.POST)
    public Map<String, Object> downloadContacts(HttpServletRequest httpServletRequest) {
        LoginStatus loginStatus = cookieAccess.getLoginStatus(httpServletRequest);
        Map<String, Object> hmap = new HashMap<String, Object>();
        switch (loginStatus) {
            case ENDUSER:
                String orgID = cookieAccess.getOrganizationID(httpServletRequest);
                List<String[]> csvList = contactSOA.getContactListDataByOrg(orgID);
                hmap.put("csvList", csvList);
                return hmap;
            default:
                hmap.put("result", "error");
                return hmap;
        }
    }

    @RequestMapping(value = "/searchMacByOrgID", method = RequestMethod.POST)
    public Map<String, Object> searchMAcByOrgID(@RequestBody ObjectNode json, HttpServletRequest httpServletRequest) {
        LoginStatus loginStatus = cookieAccess.getLoginStatus(httpServletRequest);
        Map<String, Object> hmap = new HashMap<String, Object>();
        switch (loginStatus) {
            case ENDUSER:
                String mac = json.get("mac").asText();
                String contactId = json.get("contactId").asText();
                String orgID = contactSOA.getOrgIDByContactID(contactId);
                List<ClientUI> clientUIList = clientSOA.getClientListByMacAndOrgID(mac, orgID);
                List<ClientUI> limitNumber = new Vector<>();
                int sz = 10;
                if( clientUIList.size() < 10 ) sz = clientUIList.size();
                for(int i = 0; i < sz; i++) {
                    limitNumber.add(clientUIList.get(i));
                }
                hmap.put("clientList", limitNumber);
                return hmap;
            default:
                hmap.put("result", "error");
                return hmap;
        }
    }

    @RequestMapping(value = "/addClientToContact", method = RequestMethod.POST)
    public Map<String, Object>  addClientToContact(@RequestBody ObjectNode json, HttpServletRequest httpServletRequest) {
        LoginStatus loginStatus = cookieAccess.getLoginStatus(httpServletRequest);
        Map<String, Object> hmap = new HashMap<String, Object>();
        switch (loginStatus) {
            case ENDUSER:
                String clientID = json.get("clientId").asText();
                String contactId = json.get("contactId").asText();
                linkAPIsSOA.LinkTwoObjects("client", clientID, "contact", contactId);
                return hmap;
            default:
                hmap.put("result", "error");
                return hmap;
        }
    }

    @RequestMapping(value = "/removeClientFromContact", method = RequestMethod.POST)
    public Map<String, Object>  removeClientFromContact(@RequestBody ObjectNode json, HttpServletRequest httpServletRequest) {
        LoginStatus loginStatus = cookieAccess.getLoginStatus(httpServletRequest);
        Map<String, Object> hmap = new HashMap<String, Object>();
        switch (loginStatus) {
            case ENDUSER:
                String clientID = json.get("clientId").asText();
                String contactId = json.get("contactId").asText();
                linkAPIsSOA.UnlinkTwoObjects("client", clientID, "contact", contactId);
                hmap.put("result", "true");
                return hmap;
            default:
                hmap.put("result", "error");
                return hmap;
        }
    }

    @RequestMapping(value = "/resetMap", method = RequestMethod.POST)
    public Map<String, Object>  resetMap(@RequestBody ObjectNode json, HttpServletRequest httpServletRequest) {
        LoginStatus loginStatus = cookieAccess.getLoginStatus(httpServletRequest);
        String orgID = cookieAccess.getOrganizationID(httpServletRequest);
        Map<String, Object> hmap = new HashMap<String, Object>();
        switch (loginStatus) {
            case ENDUSER:
                String locID = json.get("locID").asText();
                Location location = locationSOA.getLocationByID(locID);
                hmap.put("lat", location.lat());
                hmap.put("lng", location.lng());
                List<ContactUI> contactUIList = contactSOA.getAllContactsPairedByOrgID(orgID);
                hmap.put("contactUIList", contactUIList);
                hmap.put("result", "true");
                return hmap;
            default:
                hmap.put("result", "error");
                return hmap;
        }
    }

}
