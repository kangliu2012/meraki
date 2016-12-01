package com.everbridge.merakiui.controller;

import com.everbridge.merakiui.model.AccessPointUI;
import com.everbridge.merakiui.model.LocationMainUI;
import com.everbridge.merakiui.model.LocationUI;
import com.everbridge.merakiui.soa.*;
import com.everbridge.merakiui.util.CookieAccess;
import com.everbridge.merakiui.util.ErrorPage;
import com.everbridge.merakiui.util.LoginStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Vector;

/**
 * Created by kangliu on 10/20/16.
 */
@Controller
public class AccessPointController {
    @Autowired
    private AccessPointSOA accessPointSOA = null;

    @Autowired
    private LocationSOA locationSOA = null;

    @Autowired
    private CookieAccess cookieAccess = null;

    @Autowired
    private ErrorPage errorPage = null;

    @RequestMapping(value = "/accesspoints", method = RequestMethod.GET)
    public ModelAndView displayAccesspoints(HttpServletRequest httpServletRequest) {
        LoginStatus loginStatus = cookieAccess.getLoginStatus(httpServletRequest);
        String orgID = cookieAccess.getOrganizationID(httpServletRequest);
        if( orgID == null ) return errorPage.errorPage();
        switch (loginStatus) {
            case ENDUSER:
                ModelAndView model = new ModelAndView();
                model.setViewName("enduser/accesspoints");
                List<LocationMainUI> locationMainUIList = locationSOA.getLocationMainUIListByOrgID(orgID);
                model.addObject("locationMainUIList", locationMainUIList);

                List<AccessPointUI> accessPointUIListMapped = accessPointSOA.getAllAccessPointsMappedByOrgID(orgID);
                List<AccessPointUI> accessPointUIListUnmapped = accessPointSOA.getAllAccessPointsUnmappedByOrgID(orgID);
                List<AccessPointUI> accessPointUIList = new Vector<>();
                for(int i = 0; i < accessPointUIListMapped.size(); i++) {
                    accessPointUIList.add(accessPointUIListMapped.get(i));
                }
                for(int i = 0; i < accessPointUIListUnmapped.size(); i++) {
                    accessPointUIList.add(accessPointUIListUnmapped.get(i));
                }
                model.addObject("accesspointList", accessPointUIList);
                List<LocationUI> locationUIList = locationSOA.getAllLocationsByOrgID(orgID);

                model.addObject("locationList", locationUIList);
                model.addObject("selecttext", "Select One");
                return model;
            default:
                return errorPage.errorPage();
        }
    }

    @RequestMapping(value = "/accesspoints/mapped", method = RequestMethod.GET)
    public ModelAndView displayAccesspointsMapped(HttpServletRequest httpServletRequest) {
        LoginStatus loginStatus = cookieAccess.getLoginStatus(httpServletRequest);
        String orgID = cookieAccess.getOrganizationID(httpServletRequest);
        if( orgID == null ) return errorPage.errorPage();
        switch (loginStatus) {
            case ENDUSER:
                ModelAndView model = new ModelAndView();
                model.setViewName("enduser/accesspoints");
                List<LocationMainUI> locationMainUIList = locationSOA.getLocationMainUIListByOrgID(orgID);
                model.addObject("locationMainUIList", locationMainUIList);
                List<AccessPointUI> accessPointUIList = accessPointSOA.getAllAccessPointsMappedByOrgID(orgID);
                model.addObject("accesspointList", accessPointUIList);
                List<LocationUI> locationUIList = locationSOA.getAllLocationsByOrgID(orgID);
                model.addObject("locationList", locationUIList);
                model.addObject("selecttext", "Select One");
                return model;
            default:
                return errorPage.errorPage();
        }
    }

    @RequestMapping(value = "/accesspoints/unmapped", method = RequestMethod.GET)
    public ModelAndView displayAccesspointsUnmapped(HttpServletRequest httpServletRequest) {
        LoginStatus loginStatus = cookieAccess.getLoginStatus(httpServletRequest);
        String orgID = cookieAccess.getOrganizationID(httpServletRequest);
        if( orgID == null ) return errorPage.errorPage();
        switch (loginStatus) {
            case ENDUSER:
                ModelAndView model = new ModelAndView();
                model.setViewName("enduser/accesspoints");
                List<LocationMainUI> locationMainUIList = locationSOA.getLocationMainUIListByOrgID(orgID);
                model.addObject("locationMainUIList", locationMainUIList);
                List<AccessPointUI> accessPointUIList = accessPointSOA.getAllAccessPointsUnmappedByOrgID(orgID);
                model.addObject("accesspointList", accessPointUIList);
                List<LocationUI> locationUIList = locationSOA.getAllLocationsByOrgID(orgID);
                model.addObject("locationList", locationUIList);
                model.addObject("selecttext", "Select One");
                return model;
            default:
                return errorPage.errorPage();
        }
    }
}

