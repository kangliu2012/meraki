package com.everbridge.merakiui.controller;

import com.everbridge.merakiui.model.AccountUI;
import com.everbridge.merakiui.soa.*;
import com.everbridge.merakiui.util.CookieAccess;
import com.everbridge.merakiui.util.LoginStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by kangliu on 10/19/16.
 */
@Controller
public class LoginController {
    @Autowired
    private LoginSOA loginSOA = null;

    @Autowired
    private AccountSOA accountSOA = null;

    @Autowired
    private OrganizationSOA organizationSOA = null;

    @Autowired
    private CookieAccess cookieAccess = null;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView login(HttpServletRequest httpServletRequest) {
        LoginStatus loginStatus = cookieAccess.getLoginStatus(httpServletRequest);
        switch (loginStatus) {
            case ADMIN:
                return directAdminLogin();
            case ENDUSER:
                return directEnduserLogin();
            default:
                return displayLogin();
        }
    }

    private ModelAndView displayLogin() {
        ModelAndView model = new ModelAndView();
        model.setViewName("login");
        model.addObject("account", new AccountUI());
        return model;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ModelAndView login(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        return toLogin(httpServletRequest, httpServletResponse);
    }

    private ModelAndView directAdminLogin() {
        ModelAndView model = new ModelAndView();
        model.setViewName("login");
        model.addObject("login", "admin");
        return model;
    }

    private ModelAndView directEnduserLogin() {
        ModelAndView model = new ModelAndView();
        model.setViewName("login");
        model.addObject("login", "enduser");
        return model;
    }

    private ModelAndView toLogin(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        ModelAndView model = new ModelAndView();
        String email = httpServletRequest.getParameter("email");
        String password = httpServletRequest.getParameter("password");
        model.setViewName("login");
        boolean login = loginSOA.login(email, password);
        if (login) {
            boolean isAdmin = accountSOA.isAdmin(email);
            if( isAdmin ) {
                cookieAccess.setCookie(httpServletResponse, "admin", "true");
                model.addObject("login", "admin");
            } else {
                cookieAccess.setCookie(httpServletResponse, "admin", "false");
                String accountID = accountSOA.getAccountIDByEmail(email);
                String orgID = organizationSOA.getOrganizationIDByAccountID(accountID);
                cookieAccess.setCookie(httpServletResponse, "accountID", accountID);
                cookieAccess.setCookie(httpServletResponse, "orgID", orgID);
                model.addObject("login", "enduser");
            }
        } else {
            model.addObject("invalid", "true");
            model.addObject("email", email);
            model.addObject("password", password);
        }
        return model;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpServletResponse httpServletResponse) {
        cookieAccess.removeCookies(httpServletResponse);
        ModelAndView model = new ModelAndView();
        model.setViewName("login");
        return "redirect:/";
    }
}
