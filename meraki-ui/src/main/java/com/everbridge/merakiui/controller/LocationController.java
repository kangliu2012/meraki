package com.everbridge.merakiui.controller;

import com.everbridge.meraki.domain.domain.Location;
import com.everbridge.merakiui.model.LocationMainUI;
import com.everbridge.merakiui.model.LocationUI;
import com.everbridge.merakiui.soa.LocationSOA;
import com.everbridge.merakiui.util.CookieAccess;
import com.everbridge.merakiui.util.ErrorPage;
import com.everbridge.merakiui.util.LoginStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.List;

/**
 * Created by kangliu on 10/24/16.
 */
@Controller
public class LocationController {
    @Autowired
    private LocationSOA locationSOA = null;

    @Autowired
    private CookieAccess cookieAccess = null;

    @Autowired
    private ErrorPage errorPage = null;

    @RequestMapping(value = "/locations", method = RequestMethod.GET)
    public ModelAndView displayLocations(HttpServletRequest httpServletRequest) {
        LoginStatus loginStatus = cookieAccess.getLoginStatus(httpServletRequest);
        switch (loginStatus) {
            case ENDUSER:
                String orgID = cookieAccess.getOrganizationID(httpServletRequest);
                ModelAndView model = new ModelAndView();
                model.setViewName("enduser/locations");
                List<LocationMainUI> locationMainUIList = locationSOA.getLocationMainUIListByOrgID(orgID);
                model.addObject("locationMainUIList", locationMainUIList);
                List<LocationUI> locationUIList = locationSOA.getAllLocationsByOrgID(orgID);
                model.addObject("locationList", locationUIList);
                model.addObject("csvList", null);
                return model;
            default:
                return errorPage.errorPage();
        }
    }

    @RequestMapping(value = "/locationsuploads", method = RequestMethod.POST)
    public String contactsuploads(@RequestParam("uploadingFiles") MultipartFile[] uploadingFiles, HttpServletRequest httpServletRequest) throws IOException {
        for (MultipartFile uploadedFile : uploadingFiles) {
            byte[] bytes = uploadedFile.getBytes();
            String fileString = new String(bytes, "UTF-8");
            List<Location> locationList = locationSOA.transferFileStringToLocationList(fileString);
            String orgID = cookieAccess.getOrganizationID(httpServletRequest);
            locationSOA.saveOrUpdateLocations(locationList, orgID);
        }
        return "redirect:/locations";
    }
}
