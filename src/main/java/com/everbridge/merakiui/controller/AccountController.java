package com.everbridge.merakiui.controller;

import com.everbridge.meraki.domain.domain.Account;
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
 * Created by kangliu on 10/18/16.
 */
@Controller
public class AccountController {
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

    @RequestMapping(value = "/accounts", method = RequestMethod.GET)
    public ModelAndView displayAccounts(HttpServletRequest httpServletRequest) {
        LoginStatus loginStatus = cookieAccess.getLoginStatus(httpServletRequest);
        switch (loginStatus) {
            case ADMIN:
                return adminDisplayAccounts();
            case ENDUSER:
                return enduserDisplayAccounts(httpServletRequest);
            default:
                return errorPage.errorPage();
        }
    }

    private ModelAndView adminDisplayAccounts() {
        ModelAndView model = new ModelAndView();
        model.setViewName("admin/accounts");
        List<AccountUI> accountUIList = accountSOA.getAllAccounts();
        model.addObject("accountList", accountUIList);
        return model;
    }

    private ModelAndView enduserDisplayAccounts(HttpServletRequest httpServletRequest) {
        ModelAndView model = new ModelAndView();
        model.setViewName("enduser/accounts");
        String accountID = cookieAccess.getAccountID(httpServletRequest);
        String orgID = cookieAccess.getOrganizationID(httpServletRequest);
        List<LocationMainUI> locationMainUIList = locationSOA.getLocationMainUIListByOrgID(orgID);
        model.addObject("locationMainUIList", locationMainUIList);
        List<AccountUI> accountUIList = accountSOA.getAllAccountsInSameOrganizationByOneAccountID(accountID);
        model.addObject("accountList", accountUIList);
        return model;
    }

    @RequestMapping(value = "/accounts/new")
    public ModelAndView addNewAccount(HttpServletRequest httpServletRequest) {
        LoginStatus loginStatus = cookieAccess.getLoginStatus(httpServletRequest);
        switch (loginStatus) {
            case ADMIN:
                return adminAddNewAccount();
            case ENDUSER:
                return enduserAddNewAccount(httpServletRequest);
            default:
                return errorPage.errorPage();
        }
    }

    private ModelAndView adminAddNewAccount() {
        ModelAndView model = new ModelAndView();
        model.setViewName("admin/addaccount");
        model.addObject("account", new AccountUI());
        List<OrganizationUI> organizationUIList = organizationSOA.getAllOrganizations();
        model.addObject("organizationlist", organizationUIList);
        return model;
    }

    private ModelAndView enduserAddNewAccount(HttpServletRequest httpServletRequest) {
        ModelAndView model = new ModelAndView();
        model.setViewName("enduser/addaccount");
        String orgID = cookieAccess.getOrganizationID(httpServletRequest);
        List<LocationMainUI> locationMainUIList = locationSOA.getLocationMainUIListByOrgID(orgID);
        model.addObject("locationMainUIList", locationMainUIList);
        model.addObject("account", new AccountUI());
        model.addObject("orgID", orgID);
        return model;
    }

    @RequestMapping(value = "/accounts", method = RequestMethod.POST)
    public ModelAndView displayAccountsAndSave(@ModelAttribute AccountUI accountUI, HttpServletRequest httpServletRequest) {
        LoginStatus loginStatus = cookieAccess.getLoginStatus(httpServletRequest);
        switch (loginStatus) {
            case ADMIN:
                return adminDisplayAccountsAndSave(accountUI, httpServletRequest);
            case ENDUSER:
                return enduserDisplayAccountsAndSave(accountUI, httpServletRequest);
            default:
                return errorPage.errorPage();
        }
    }

    private ModelAndView adminDisplayAccountsAndSave(AccountUI accountUI, HttpServletRequest httpServletRequest) {
        if( accountUI.getId() == null ) {
            if( accountSOA.isEmailExist(accountUI.getEmail())) {
                ModelAndView model = new ModelAndView();
                model.setViewName("admin/addaccount");
                List<OrganizationUI> organizationUIList = organizationSOA.getAllOrganizations();
                model.addObject("duplicateAccountEmail", "true");
                model.addObject("account", accountUI);
                model.addObject("organizationlist", organizationUIList);
                return model;
            }
            String[] organizationlists = httpServletRequest.getParameterMap().get("organizationlist");
            String ID = accountSOA.addAccount(accountUI);
            if( ID != null && organizationlists != null ) {
                linkAPIsSOA.LinkTwoObjects("account", ID, "organization", organizationlists[0]);
            }
        }
        // update
        else {
            String orgID = httpServletRequest.getParameter("organizationlist");
            Account account = accountSOA.getAccountByID(accountUI.getId());
            account.password_$eq(accountUI.getPassword());
            accountSOA.updateAccount(account, accountUI.getId());
            linkAPIsSOA.LinkTwoObjects("account", accountUI.getId(), "organization", orgID);
        }
        return displayAccounts(httpServletRequest);
    }

    private ModelAndView enduserDisplayAccountsAndSave(AccountUI accountUI, HttpServletRequest httpServletRequest) {
        if( accountUI.getId() == null ) {
            if( accountSOA.isEmailExist(accountUI.getEmail())) {
                ModelAndView model = new ModelAndView();
                model.setViewName("enduser/addaccount");
                String orgID = cookieAccess.getOrganizationID(httpServletRequest);
                List<LocationMainUI> locationMainUIList = locationSOA.getLocationMainUIListByOrgID(orgID);
                model.addObject("locationMainUIList", locationMainUIList);
                model.addObject("account", accountUI);
                model.addObject("orgID", orgID);
                model.addObject("duplicateAccountEmail", "true");
                return model;
            }
            String[] orgID = httpServletRequest.getParameterMap().get("orgID");
            String ID = accountSOA.addAccount(accountUI);
            if( ID != null ) {
                linkAPIsSOA.LinkTwoObjects("account", ID, "organization", orgID[0]);
            }
        }
        // update
        else {
            String orgID = httpServletRequest.getParameter("orgID");
            Account accountForUpdate = accountSOA.getAccountByID(accountUI.getId());
            accountSOA.updateAccount(accountForUpdate, accountUI);
            linkAPIsSOA.LinkTwoObjects("account", accountUI.getId(), "organization", orgID);
        }
        return displayAccounts(httpServletRequest);
    }

    @RequestMapping(value = "/accounts/edit/{ID}", method = RequestMethod.GET)
    public ModelAndView editAccount(@PathVariable("ID") String ID, HttpServletRequest httpServletRequest) {
        LoginStatus loginStatus = cookieAccess.getLoginStatus(httpServletRequest);
        switch (loginStatus) {
            case ADMIN:
                return adminEditAccount(ID);
            case ENDUSER:
                return enduserEditAccount(ID, httpServletRequest);
            default:
                return errorPage.errorPage();
        }
    }

    private ModelAndView adminEditAccount(String accountID) {
        ModelAndView model = new ModelAndView();
        model.setViewName("admin/editaccount");
        AccountUI accountUI = accountSOA.getAccountUIByID(accountID);
        List<OrganizationUI> organizationUIList = organizationSOA.getAllOrganizations();
        String orgID = accountSOA.getOrganizationIDByAccountID(accountID);
        model.addObject("orgID", orgID);
        model.addObject("account", accountUI);
        model.addObject("organizationlist", organizationUIList);
        return model;
    }

    private ModelAndView enduserEditAccount(String accountID, HttpServletRequest httpServletRequest) {
        ModelAndView model = new ModelAndView();
        model.setViewName("enduser/editaccount");
        String orgID = cookieAccess.getOrganizationID(httpServletRequest);
        List<LocationMainUI> locationMainUIList = locationSOA.getLocationMainUIListByOrgID(orgID);
        model.addObject("locationMainUIList", locationMainUIList);
        Account account = accountSOA.getAccountByID(accountID);
        AccountUI accountUI = accountSOA.TransferJsonObjToUIObj(account, accountID);
        model.addObject("account", accountUI);
        model.addObject("orgID", orgID);
        return model;
    }

    @RequestMapping(value = "/accounts/delete/{ID}", method = RequestMethod.GET)
    public String DeleteAccount(@PathVariable("ID") String ID, HttpServletRequest httpServletRequest) {
        LoginStatus loginStatus = cookieAccess.getLoginStatus(httpServletRequest);
        switch (loginStatus) {
            case ADMIN:
                accountSOA.deleteAccountByID(ID);
            case ENDUSER:
                if ( cookieAccess.getAccountID(httpServletRequest).equals(ID) ) {
                    accountSOA.deleteAccountByID(ID);
                    return "redirect:/logout";
                } else {
                    accountSOA.deleteAccountByID(ID);
                }
        }
        return "redirect:/accounts";
    }
}
