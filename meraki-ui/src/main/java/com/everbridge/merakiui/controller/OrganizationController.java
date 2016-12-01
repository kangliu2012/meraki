package com.everbridge.merakiui.controller;

import com.everbridge.meraki.domain.domain.Organization;
import com.everbridge.merakiui.model.AccountUI;
import com.everbridge.merakiui.model.LocationMainUI;
import com.everbridge.merakiui.model.OrganizationUI;
import com.everbridge.merakiui.soa.AccountSOA;
import com.everbridge.merakiui.soa.LinkAPIsSOA;
import com.everbridge.merakiui.soa.LocationSOA;
import com.everbridge.merakiui.soa.OrganizationSOA;
import com.everbridge.merakiui.util.CookieAccess;
import com.everbridge.merakiui.util.ErrorPage;
import com.everbridge.merakiui.util.LoginStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by kangliu on 10/11/16.
 */
@Controller
public class OrganizationController {

    @Autowired
    private AccountSOA accountSOA = null;

    @Autowired
    private OrganizationSOA organizationSOA = null;

    @Autowired
    private LinkAPIsSOA linkAPIsSOA = null;

    @Autowired
    private CookieAccess cookieAccess = null;

    @Autowired
    private ErrorPage errorPage = null;

    @Autowired
    private LocationSOA locationSOA = null;

    @RequestMapping(value = "/organizations", method = RequestMethod.GET)
    public ModelAndView displayOrganization(HttpServletRequest httpServletRequest) {
        LoginStatus loginStatus = cookieAccess.getLoginStatus(httpServletRequest);
        switch (loginStatus) {
            case ADMIN:
                return adminDisplayOrganization();
            case ENDUSER:
                return enduserDisplayOrganization(httpServletRequest);
            default:
                return errorPage.errorPage();
        }
    }

    private ModelAndView adminDisplayOrganization() {
        ModelAndView model = new ModelAndView();
        model.setViewName("admin/organizations");
        List<OrganizationUI> organizationList = organizationSOA.getAllOrganizations();
        model.addObject("organizationList", organizationList);
        return model;
    }

    private ModelAndView enduserDisplayOrganization(HttpServletRequest httpServletRequest) {
        ModelAndView model = new ModelAndView();
        model.setViewName("enduser/organizations");
        String orgID = cookieAccess.getOrganizationID(httpServletRequest);
        List<LocationMainUI> locationMainUIList = locationSOA.getLocationMainUIListByOrgID(orgID);
        model.addObject("locationMainUIList", locationMainUIList);
        Organization organization = organizationSOA.getOrganizationByID(orgID);
        OrganizationUI organizationUI = organizationSOA.TransferJsonObjToUIObj(organization, orgID);
        model.addObject("organization", organizationUI);
        return model;
    }

    @RequestMapping(value = "/organizations", method = RequestMethod.POST)
    public ModelAndView displayOrganizationAndSave(@ModelAttribute OrganizationUI organizationUI, HttpServletRequest httpServletRequest) {
        LoginStatus loginStatus = cookieAccess.getLoginStatus(httpServletRequest);
        switch (loginStatus) {
            case ADMIN:
                return adminDisplayOrganizationAndSave(organizationUI, httpServletRequest);
            case ENDUSER:
                return enduserDisplayOrganizationAndSave(organizationUI);
            default:
                return errorPage.errorPage();
        }
    }

    private ModelAndView adminDisplayOrganizationAndSave(@ModelAttribute OrganizationUI organizationUI, HttpServletRequest httpServletRequest) {
        ModelAndView model = new ModelAndView();
        if( organizationUI.getId() == null ) {
            if(organizationSOA.isOrgIDExist(organizationUI.getEbOrganizationId())) {
                model.setViewName("admin/addorganization");
                List<AccountUI> accountList = accountSOA.getAllAccounts();
                organizationUI.setAccountList(accountList);
                model.addObject("duplicateOrgID", "true");
                model.addObject("organization", organizationUI);
                model.addObject("accountlist", accountList);
                return model;
            }
            if( organizationUI.getCreatedAt() == null ) {
                organizationUI.setCreatedAt(new Date());
            }
            if( organizationUI.getUpdateAt() == null ) {
                organizationUI.setUpdateAt(new Date());
            }
            if( organizationUI.getImportFinishedAt() == null ) {
                organizationUI.setImportFinishedAt(new Date());
            }
            String orgID = organizationSOA.addOrganization(organizationUI);
            if( orgID != null ) {
                String[] accountList = httpServletRequest.getParameterMap().get("accountlist");
                if( accountList != null ) {
                    for (String accountID: accountList ) {
                        linkAPIsSOA.LinkTwoObjects("account",accountID,"organization",orgID);
                    }
                }
            }
            return displayOrganization(httpServletRequest);
        }
        else {
            Organization organizationForUpdate = organizationSOA.getOrganizationByID(organizationUI.getId());
            if( !organizationForUpdate.ebOrganizationId().equals(organizationUI.getEbOrganizationId() )) {
                if(organizationSOA.isOrgIDExist(organizationUI.getEbOrganizationId())) {
                    model.setViewName("admin/editorganization");
                    List<AccountUI> accountList = accountSOA.getAllAccounts();
                    organizationUI.setAccountList(accountList);
                    model.addObject("duplicateOrgID", "true");
                    model.addObject("organization", organizationUI);
                    model.addObject("accountlist", accountList);
                    return model;
                }
            }
            organizationSOA.UpdateOrganization(organizationForUpdate, organizationUI);
            return displayOrganization(httpServletRequest);
        }
    }

    private ModelAndView enduserDisplayOrganizationAndSave(@ModelAttribute OrganizationUI organizationUI) {
        ModelAndView model = new ModelAndView();
        Organization organizationForUpdate = organizationSOA.getOrganizationByID(organizationUI.getId());
        organizationSOA.UpdateOrganization(organizationForUpdate, organizationUI);
        model.setViewName("enduser/organizations");
        List<LocationMainUI> locationMainUIList = locationSOA.getLocationMainUIListByOrgID(organizationUI.getId());
        model.addObject("locationMainUIList", locationMainUIList);
        model.addObject("organization", organizationUI);
        return model;
    }

    @RequestMapping(value = "/organizations/new")
    public ModelAndView addNewOrganization(HttpServletRequest httpServletRequest) {
        LoginStatus loginStatus = cookieAccess.getLoginStatus(httpServletRequest);
        switch (loginStatus) {
            case ADMIN:
                ModelAndView model = new ModelAndView();
                model.setViewName("admin/addorganization");
                OrganizationUI organizationUI = new OrganizationUI();
                List<AccountUI> accountList = accountSOA.getAllAccounts();
                organizationUI.setAccountList(accountList);
                model.addObject("organization", organizationUI);
                model.addObject("accountlist", accountList);
                return model;
            default:
                return errorPage.errorPage();
        }
    }

    @RequestMapping(value = "/organizations/edit/{orgID}")
    public ModelAndView editOrganizationByID(@PathVariable("orgID") String orgID, HttpServletRequest httpServletRequest) {
        LoginStatus loginStatus = cookieAccess.getLoginStatus(httpServletRequest);
        switch (loginStatus) {
            case ADMIN:
                ModelAndView model = new ModelAndView();
                model.setViewName("admin/editorganization");
                Organization organization = organizationSOA.getOrganizationByID(orgID);
                OrganizationUI organizationUI = organizationSOA.TransferJsonObjToUIObj(organization, orgID);
                List<AccountUI> accountList = accountSOA.getAllAccountsByOrgID(orgID);
                model.addObject("organization", organizationUI);
                model.addObject("accountlist", accountList);
                return model;
            default:
                return errorPage.errorPage();
        }
    }

    @RequestMapping(value = "/organizations/delete/{ID}", method = RequestMethod.GET)
    public String deleteAccountByID(@PathVariable("ID") String ID, HttpServletRequest httpServletRequest) {
        LoginStatus loginStatus = cookieAccess.getLoginStatus(httpServletRequest);
        switch (loginStatus) {
            case ADMIN:
                organizationSOA.deleteOrganizationByID(ID);
        }
        return "redirect:/organizations";
    }
}
