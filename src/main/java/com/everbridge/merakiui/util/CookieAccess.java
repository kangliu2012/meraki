package com.everbridge.merakiui.util;

import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by kangliu on 11/5/16.
 */
@Component
public class CookieAccess {
    public void setCookie(HttpServletResponse httpServletResponse, String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        httpServletResponse.addCookie(cookie);
    }

    public LoginStatus getLoginStatus(HttpServletRequest httpServletRequest) {
        Cookie[] cookies = httpServletRequest.getCookies();
        if( cookies != null ) {
            for(int i = 0; i < cookies.length; i++) {
                if( cookies[i].getName().equals("admin") ) {
                    if( cookies[i].getValue().equals("true")) {
                        return LoginStatus.ADMIN;
                    }
                    if( cookies[i].getValue().equals("false")) {
                        return LoginStatus.ENDUSER;
                    }
                }
            }
        }
        return LoginStatus.ERROR;
    }

    public String getOrganizationID(HttpServletRequest httpServletRequest) {
        Cookie[] cookies = httpServletRequest.getCookies();
        if( cookies != null ) {
            for(int i = 0; i < cookies.length; i++) {
                if( cookies[i].getName().equals("orgID") ) {
                    return cookies[i].getValue();
                }
            }
        }
        return null;
    }

    public String getAccountID(HttpServletRequest httpServletRequest) {
        Cookie[] cookies = httpServletRequest.getCookies();
        if( cookies != null ) {
            for(int i = 0; i < cookies.length; i++) {
                if( cookies[i].getName().equals("accountID") ) {
                    return cookies[i].getValue();
                }
            }
        }
        return null;
    }

    public void removeCookies(HttpServletResponse httpServletResponse) {
        setCookie(httpServletResponse, "admin", null);
        setCookie(httpServletResponse, "orgID", null);
        setCookie(httpServletResponse, "accountID", null);
    }
}
