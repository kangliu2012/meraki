package com.everbridge.merakiui.controller;

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
 * Created by kangliu on 10/24/16.
 */
@Controller
public class UserMacController {
    @Autowired
    private ContactSOA contactSOA = null;

    @Autowired
    private CookieAccess cookieAccess = null;

    @Autowired
    private ErrorPage errorPage = null;

    @Autowired
    private LocationSOA locationSOA = null;

    @RequestMapping(value = "/usermacs", method = RequestMethod.GET)
    public ModelAndView displayMacs(HttpServletRequest httpServletRequest) {
        LoginStatus loginStatus = cookieAccess.getLoginStatus(httpServletRequest);
        switch (loginStatus) {
            case ENDUSER:
                ModelAndView model = new ModelAndView();
                model.setViewName("enduser/usermacs");
                String orgID = cookieAccess.getOrganizationID(httpServletRequest);
                List<LocationMainUI> locationMainUIList = locationSOA.getLocationMainUIListByOrgID(orgID);
                model.addObject("locationMainUIList", locationMainUIList);
                List<ContactUI> contactUIList = contactSOA.getAllContactsByOrgIDAndPage(orgID, "0");
                Long count = contactSOA.getCountByOrgID(orgID);
                Long totalPages = count / 20 + 1;
                model.addObject("curPage", 1);
                model.addObject("totalPages", totalPages);
                model.addObject("contactList", contactUIList);
                return model;
            default:
                return errorPage.errorPage();
        }
    }

    @RequestMapping(value = "/usermacs/page/{page}", method = RequestMethod.GET)
    public ModelAndView displayMacsByPage(@PathVariable("page") String page, HttpServletRequest httpServletRequest) {
        LoginStatus loginStatus = cookieAccess.getLoginStatus(httpServletRequest);
        switch (loginStatus) {
            case ENDUSER:
                ModelAndView model = new ModelAndView();
                String sPage = String.valueOf(Integer.valueOf(page) - 1);
                model.setViewName("enduser/usermacs");
                String orgID = cookieAccess.getOrganizationID(httpServletRequest);
                List<LocationMainUI> locationMainUIList = locationSOA.getLocationMainUIListByOrgID(orgID);
                model.addObject("locationMainUIList", locationMainUIList);
                List<ContactUI> contactUIList = contactSOA.getAllContactsByOrgIDAndPage(orgID, sPage);
                Long count = contactSOA.getCountByOrgID(orgID);
                Long totalPages = count / 20 + 1;
                model.addObject("curPage", page);
                model.addObject("totalPages", totalPages);
                model.addObject("contactList", contactUIList);
                return model;
            default:
                return errorPage.errorPage();
        }
    }

    @RequestMapping(value = "/usermacs/{search}", method = RequestMethod.GET)
    public ModelAndView displayMacsSearch(@PathVariable("search") String search, HttpServletRequest httpServletRequest) {
        LoginStatus loginStatus = cookieAccess.getLoginStatus(httpServletRequest);
        switch (loginStatus) {
            case ENDUSER:
                search = search.toUpperCase();
                ModelAndView model = new ModelAndView();
                model.setViewName("enduser/usermacs");
                String orgID = cookieAccess.getOrganizationID(httpServletRequest);
                List<LocationMainUI> locationMainUIList = locationSOA.getLocationMainUIListByOrgID(orgID);
                model.addObject("locationMainUIList", locationMainUIList);

                List<ContactUI> contactUIList = contactSOA.searchContactsByOrgID(orgID, search);
                model.addObject("curPage", 1);
                model.addObject("totalPages", 1);
                model.addObject("contactList", contactUIList);
                return model;
            default:
                return errorPage.errorPage();
        }
    }

    @RequestMapping(value = "/usermacsunpaired", method = RequestMethod.GET)
    public ModelAndView filterUnpaired(HttpServletRequest httpServletRequest) {
        LoginStatus loginStatus = cookieAccess.getLoginStatus(httpServletRequest);
        switch (loginStatus){
            case ENDUSER:
                ModelAndView model = new ModelAndView();
                model.setViewName("enduser/usermacs");
                String orgID = cookieAccess.getOrganizationID(httpServletRequest);
                List<LocationMainUI> locationMainUIList = locationSOA.getLocationMainUIListByOrgID(orgID);
                model.addObject("locationMainUIList", locationMainUIList);

                List<ContactUI> contactUIList = contactSOA.getAllContactsUnpairedByOrgIDAndPage(orgID, "0");
                int count = contactSOA.getContactUnpairedCountByOrgID(orgID);
                int totalPages = count / 20 + 1;
                model.addObject("curPage", 1);
                model.addObject("totalPages", totalPages);
                model.addObject("contactList", contactUIList);
                return model;
            default:
                return errorPage.errorPage();
        }
    }

    @RequestMapping(value = "/usermacspaired", method = RequestMethod.GET)
    public ModelAndView filterPaired(HttpServletRequest httpServletRequest) {
        LoginStatus loginStatus = cookieAccess.getLoginStatus(httpServletRequest);
        switch (loginStatus){
            case ENDUSER:
                ModelAndView model = new ModelAndView();
                model.setViewName("enduser/usermacs");
                String orgID = cookieAccess.getOrganizationID(httpServletRequest);
                List<LocationMainUI> locationMainUIList = locationSOA.getLocationMainUIListByOrgID(orgID);
                model.addObject("locationMainUIList", locationMainUIList);

                List<ContactUI> contactUIList = contactSOA.getAllContactsPairedByOrgIDAndPage(orgID, "0");
                int count = contactSOA.getContactPairedCountByOrgID(orgID);
                int totalPages = count / 20 + 1;
                model.addObject("curPage", 1);
                model.addObject("totalPages", totalPages);
                model.addObject("contactList", contactUIList);
                return model;
            default:
                return errorPage.errorPage();
        }
    }

    @RequestMapping(value = "/usermacsunpaired/page/{page}", method = RequestMethod.GET)
    public ModelAndView filterUnpairedByPage(@PathVariable("page") String page, HttpServletRequest httpServletRequest) {
        LoginStatus loginStatus = cookieAccess.getLoginStatus(httpServletRequest);
        switch (loginStatus){
            case ENDUSER:
                ModelAndView model = new ModelAndView();
                model.setViewName("enduser/usermacs");
                String sPage = String.valueOf(Integer.valueOf(page) - 1);
                String orgID = cookieAccess.getOrganizationID(httpServletRequest);
                List<ContactUI> contactUIList = contactSOA.getAllContactsUnpairedByOrgIDAndPage(orgID, sPage);
                List<LocationMainUI> locationMainUIList = locationSOA.getLocationMainUIListByOrgID(orgID);
                model.addObject("locationMainUIList", locationMainUIList);

                int count = contactSOA.getContactUnpairedCountByOrgID(orgID);
                int totalPages = count / 20 + 1;
                model.addObject("curPage", page);
                model.addObject("totalPages", totalPages);
                model.addObject("contactList", contactUIList);
                return model;
            default:
                return errorPage.errorPage();
        }
    }

    @RequestMapping(value = "/usermacspaired/page/{page}", method = RequestMethod.GET)
    public ModelAndView filterPairedByPage(@PathVariable("page") String page, HttpServletRequest httpServletRequest) {
        LoginStatus loginStatus = cookieAccess.getLoginStatus(httpServletRequest);
        switch (loginStatus){
            case ENDUSER:
                ModelAndView model = new ModelAndView();
                model.setViewName("enduser/usermacs");
                String sPage = String.valueOf(Integer.valueOf(page) - 1);
                String orgID = cookieAccess.getOrganizationID(httpServletRequest);
                List<LocationMainUI> locationMainUIList = locationSOA.getLocationMainUIListByOrgID(orgID);
                model.addObject("locationMainUIList", locationMainUIList);

                List<ContactUI> contactUIList = contactSOA.getAllContactsPairedByOrgIDAndPage(orgID, sPage);
                int count = contactSOA.getContactPairedCountByOrgID(orgID);
                int totalPages = count / 20 + 1;
                model.addObject("curPage", page);
                model.addObject("totalPages", totalPages);
                model.addObject("contactList", contactUIList);
                return model;
            default:
                return errorPage.errorPage();
        }
    }
}
