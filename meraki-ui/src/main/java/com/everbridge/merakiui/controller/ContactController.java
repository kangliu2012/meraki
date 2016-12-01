package com.everbridge.merakiui.controller;

import com.everbridge.meraki.domain.domain.Organization;
import com.everbridge.meraki.domain.utils.HALUtils;
import com.everbridge.merakiui.configuration.Configuration;
import com.everbridge.merakiui.model.AccountUI;
import com.everbridge.merakiui.model.ContactUI;
import com.everbridge.merakiui.model.LocationMainUI;
import com.everbridge.merakiui.soa.ContactSOA;
import com.everbridge.merakiui.soa.LocationSOA;
import com.everbridge.merakiui.soa.OrganizationSOA;
import com.everbridge.merakiui.util.CookieAccess;
import com.everbridge.merakiui.util.ErrorPage;
import com.everbridge.merakiui.util.LoginStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * Created by kangliu on 10/24/16.
 */
@Controller
public class ContactController {
    @Autowired
    private ContactSOA contactSOA = null;

    @Autowired
    private CookieAccess cookieAccess = null;

    @Autowired
    private ErrorPage errorPage = null;

    @Autowired
    private Configuration configuration = null;

    @Autowired
    private LocationSOA locationSOA = null;

    @Autowired
    private OrganizationSOA organizationSOA = null;

    @RequestMapping(value = "/useruploads", method = RequestMethod.GET)
    public ModelAndView displayMacs(HttpServletRequest httpServletRequest) {
        LoginStatus loginStatus = cookieAccess.getLoginStatus(httpServletRequest);
        switch (loginStatus) {
            case ENDUSER:
                ModelAndView model = new ModelAndView();
                model.setViewName("enduser/useruploads");
                String orgID = cookieAccess.getOrganizationID(httpServletRequest);
                List<LocationMainUI> locationMainUIList = locationSOA.getLocationMainUIListByOrgID(orgID);
                model.addObject("locationMainUIList", locationMainUIList);
                //readFromImportTimeFile(model);
                return model;
            default:
                return errorPage.errorPage();
        }
    }

    private void readFromImportTimeFile(ModelAndView model) {
        String importTime = "";
        try {
            String filepath = System.getProperty("user.dir");
            filepath += "/public/text/importTime.txt";
            Scanner in = new Scanner(new FileReader(filepath));
            while( in.hasNext() ) {
                importTime += " ";
                importTime += in.next();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if( !importTime.equals("")) {
            model.addObject("importTime",  "Last imported at " + importTime);
        }
    }

    @RequestMapping(value = "/contactsuploads", method = RequestMethod.POST)
    public String contactsuploads(@RequestParam("uploadingFiles") MultipartFile[] uploadingFiles, HttpServletRequest httpServletRequest) throws IOException {
        LoginStatus loginStatus = cookieAccess.getLoginStatus(httpServletRequest);
        switch (loginStatus) {
            case ENDUSER:
                for(MultipartFile uploadedFile : uploadingFiles) {
                    String fileString = new String(uploadedFile.getBytes(),"UTF-8");
                    String orgID = cookieAccess.getOrganizationID(httpServletRequest);
                    Set<String> stringSet = contactSOA.getContactIDList(orgID);
                    List<ContactUI> contactList = contactSOA.transferFileStringToContactList(fileString, orgID, stringSet);
                    contactSOA.saveOrUpdateLocations(contactList, orgID, stringSet);
                }
                return "redirect:/useruploads";
            default:
                return "redirect:/login";
        }
    }

    private HttpStatus verifyUsernamePassword(String orgID) {
        Organization organization = organizationSOA.getOrganizationByID(orgID);
        String url = configuration.getEverbridgeurl() + "contacts/" + organization.ebOrganizationId();
        RestTemplate restTemplate = HALUtils.getNonHALRestTemplate(organization.apiUsername(),organization.apiPassword());
        ParameterizedTypeReference<String> pt = new ParameterizedTypeReference<String>(){};
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, pt);
            return response.getStatusCode();
        } catch (HttpStatusCodeException exception) {
            return exception.getStatusCode();
        }
    }

    @RequestMapping(value = "/useruploads", method = RequestMethod.POST)
    public ModelAndView importEverbridgeContacts(HttpServletRequest httpServletRequest) {
        LoginStatus loginStatus = cookieAccess.getLoginStatus(httpServletRequest);
        ModelAndView model = new ModelAndView();
        switch (loginStatus) {
            case ENDUSER:
                String orgID = cookieAccess.getOrganizationID(httpServletRequest);
                HttpStatus httpstatus = verifyUsernamePassword(orgID);
                model.setViewName("enduser/useruploads");
                List<LocationMainUI> locationMainUIList = locationSOA.getLocationMainUIListByOrgID(orgID);
                model.addObject("locationMainUIList", locationMainUIList);
                if( httpstatus == HttpStatus.UNAUTHORIZED) {
                    model.addObject("importStatus", "false");
                    model.addObject("importTime",  "Username or Password is not correct!");
                    return model;
                }
                if( httpstatus == HttpStatus.NOT_FOUND ) {
                    model.addObject("importStatus", "false");
                    model.addObject("importTime",  "Organization url for importing contact is not correct!");
                    return model;
                }
                String url = configuration.getBaseurl() + "geteverbridgecontacts/" + orgID;
                RestTemplate restTemplate = HALUtils.getNonHALRestTemplate();
                ParameterizedTypeReference<String> pt = new ParameterizedTypeReference<String>(){};
                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, pt);
                model.setViewName("enduser/useruploads");
                model.addObject("importStatus", "true");
//                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
//                LocalDateTime now = LocalDateTime.now();
//                String curTime = dtf.format(now);
//                try {
//                    String filepath = System.getProperty("user.dir");
//                    model.addObject("filepath", filepath);
//                    //filepath += "/src/main/resources/public/text/importTime.txt";
//                    filepath += "/public/text/importTime.txt";
//                    File output = new File(filepath);
//                    PrintWriter printer = new PrintWriter(output);
//                    printer.write(curTime);
//                    printer.flush();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                model.addObject("importTime",  "Last imported at " + curTime);
                return model;
            default:
                model.setViewName("login");
                model.addObject("account", new AccountUI());
                return model;
        }
    }
}
