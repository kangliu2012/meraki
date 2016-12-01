package com.everbridge.merakiui.controller;

import com.everbridge.merakiui.model.ClientMainUI;
import com.everbridge.merakiui.model.LocationMainUI;
import com.everbridge.merakiui.soa.ClientSOA;
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
 * Created by kangliu on 11/3/16.
 */
@Controller
public class ClientController {
    @Autowired
    private ClientSOA clientSOA = null;

    @Autowired
    private LocationSOA locationSOA = null;

    @Autowired
    private CookieAccess cookieAccess = null;

    @Autowired
    private ErrorPage errorPage = null;

    @RequestMapping(value = "/client/{locID}", method = RequestMethod.GET)
    public ModelAndView displayClient(@PathVariable String locID, HttpServletRequest httpServletRequest) {
        LoginStatus loginStatus = cookieAccess.getLoginStatus(httpServletRequest);
        switch (loginStatus) {
            case ENDUSER:
                ModelAndView model = new ModelAndView();
                model.setViewName("enduser/client");
                String orgID = cookieAccess.getOrganizationID(httpServletRequest);
                List<LocationMainUI> locationMainUIList = locationSOA.getLocationMainUIListByOrgID(orgID);
                model.addObject("locationMainUIList", locationMainUIList);
                List<ClientMainUI> clientMainUIList = clientSOA.getMappedClientListByOrgIDAndLocationID(orgID, locID);
                Long count = clientSOA.getKnownClientCountByOrgIDAndLocationID(orgID, locID);
                Long totalPages = count / 20 + 1;
                model.addObject("curPage", "1");
                model.addObject("totalPages", totalPages);
                String locationName = locationSOA.getLocationNameByID(locID);
                model.addObject("locid", locID);
                model.addObject("location", locationName);
                model.addObject("clientList", clientMainUIList);
                return model;
            default:
                return errorPage.errorPage();
        }
    }

    @RequestMapping(value = "/client/{locID}/page/{page}", method = RequestMethod.GET)
    public ModelAndView displayClientByPage(@PathVariable String locID, @PathVariable String page, HttpServletRequest httpServletRequest) {
        LoginStatus loginStatus = cookieAccess.getLoginStatus(httpServletRequest);
        switch (loginStatus) {
            case ENDUSER:
                ModelAndView model = new ModelAndView();
                model.setViewName("enduser/client");
                String orgID = cookieAccess.getOrganizationID(httpServletRequest);
                List<LocationMainUI> locationMainUIList = locationSOA.getLocationMainUIListByOrgID(orgID);
                model.addObject("locationMainUIList", locationMainUIList);
                String sPage = String.valueOf(Integer.valueOf(page) - 1);
                List<ClientMainUI> clientMainUIList = clientSOA.getMappedClientListByOrgIDAndLocationIDAndPage("1", locID, sPage);
                Long count = clientSOA.getKnownClientCountByOrgIDAndLocationID("1", locID);
                Long totalPages = count / 20 + 1;
                model.addObject("curPage", page);
                model.addObject("totalPages", totalPages);
                String locationName = locationSOA.getLocationNameByID(locID);
                model.addObject("locid", locID);
                model.addObject("location", locationName);
                model.addObject("clientList", clientMainUIList);
                return model;
            default:
                return errorPage.errorPage();
        }
    }
}
