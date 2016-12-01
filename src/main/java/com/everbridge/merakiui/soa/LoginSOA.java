package com.everbridge.merakiui.soa;

import com.everbridge.merakiui.dao.LoginDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by kangliu on 11/2/16.
 */
@Component
public class LoginSOA {
    @Autowired
    private LoginDAO loginDAO = null;

    public boolean login(String email, String password) {
        return loginDAO.login(email, password);
    }
}
