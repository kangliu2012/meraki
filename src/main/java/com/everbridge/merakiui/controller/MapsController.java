package com.everbridge.merakiui.controller;

import com.everbridge.meraki.domain.domain.Location;
import com.everbridge.merakiui.model.ContactUI;
import com.everbridge.merakiui.model.LocationMainUI;
import com.everbridge.merakiui.soa.ContactSOA;
import com.everbridge.merakiui.soa.LocationSOA;
import com.everbridge.merakiui.util.CookieAccess;
import com.everbridge.merakiui.util.ErrorPage;
import com.everbridge.merakiui.util.LoginStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by kangliu on 11/23/16.
 */
@Controller
public class MapsController {
    @Autowired
    CookieAccess cookieAccess = null;

    @Autowired
    ErrorPage errorPage = null;

    @Autowired
    LocationSOA locationSOA = null;

    @Autowired
    ContactSOA contactSOA = null;

    @RequestMapping(value = "/maps/location/{locID}", method = RequestMethod.GET)
    public ModelAndView displayMaps(@PathVariable String locID, HttpServletRequest httpServletRequest) {
        LoginStatus loginStatus = cookieAccess.getLoginStatus(httpServletRequest);
        String orgID = cookieAccess.getOrganizationID(httpServletRequest);
        if( orgID == null ) return errorPage.errorPage();
        switch (loginStatus) {
            case ENDUSER:
                ModelAndView model = new ModelAndView();
                model.setViewName("enduser/maps");
                List<LocationMainUI> locationMainUIList = locationSOA.getLocationMainUIListByOrgID(orgID);
                model.addObject("locationMainUIList", locationMainUIList);
                Location location = locationSOA.getLocationByID(locID);
                model.addObject("lat", location.lat());
                model.addObject("lng", location.lng());
                List<ContactUI> contactUIList = contactSOA.getAllContactsPairedByOrgID(orgID);
                model.addObject("contactUIList", contactUIList);
                model.addObject("locID", locID);
                return model;
            default:
                return errorPage.errorPage();
        }
    }
}
