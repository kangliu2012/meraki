package com.everbridge.merakiui.controller;

import com.everbridge.merakiui.model.LocationMainUI;
import com.everbridge.merakiui.model.MainPageInfo;
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

/**
 * Created by kangliu on 10/19/16.
 */
@Controller
public class MainController {
    @Autowired
    private ContactSOA contactSOA = null;

    @Autowired
    private AccessPointSOA accessPointSOA = null;

    @Autowired
    private LocationSOA locationSOA = null;

    @Autowired
    private CookieAccess cookieAccess = null;

    @Autowired
    private ErrorPage errorPage = null;

    @RequestMapping(value = "/main", method = RequestMethod.GET)
    public ModelAndView main(HttpServletRequest httpServletRequest) {
        LoginStatus loginStatus = cookieAccess.getLoginStatus(httpServletRequest);
        switch (loginStatus) {
            case ENDUSER:
                return enduserMain(httpServletRequest);
            default:
                return errorPage.errorPage();
        }
    }

    private ModelAndView enduserMain(HttpServletRequest httpServletRequest) {
        ModelAndView model = new ModelAndView();
        model.setViewName("enduser/main");

        MainPageInfo mainPageInfo = new MainPageInfo();
        String orgID = cookieAccess.getOrganizationID(httpServletRequest);

        int contacUnpairedCount = contactSOA.getContactUnpairedCountByOrgID(orgID);
        int contacPairedCount = contactSOA.getContactPairedCountByOrgID(orgID);
        mainPageInfo.setContactUnpaired(String.valueOf(contacUnpairedCount));
        mainPageInfo.setContactPaired(String.valueOf(contacPairedCount));

        String accesspointUnmappedCount = accessPointSOA.getAccessPointUnmappedCountByOrgID(orgID);
        String accesspointMappedCount = accessPointSOA.getAccessPointMappedCountByOrgID(orgID);

        mainPageInfo.setAccessPointUnmapped(accesspointUnmappedCount);
        mainPageInfo.setAccessPointMapped(accesspointMappedCount);

        List<LocationMainUI> locationMainUIList = locationSOA.getLocationMainUIListByOrgID(orgID);
        model.addObject("locationMainUIList", locationMainUIList);
        model.addObject("mainPageInfo", mainPageInfo);
        return model;
    }
}
