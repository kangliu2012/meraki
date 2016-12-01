package com.everbridge.merakiui.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.everbridge.meraki.domain.domain.Account;
import org.springframework.hateoas.Resource;

import java.util.Collection;
import java.util.Iterator;

/**
 * Created by kangliu on 10/19/16.
 */
@Component
public class LoginDAO {
    @Autowired
    private AccountDAO accountDAO = null;

    public boolean login(String email, String password) {
        Collection<Resource<Account>> accountCollection = accountDAO.getAllAccount();
        Iterator<Resource<Account>> iterator = accountCollection.iterator();
        while( iterator.hasNext() ) {
            Resource<Account> resource = iterator.next();
            Account account = resource.getContent();
            if( account.email().equals(email)) {
                return account.password().equals(password);
            }
        }
        return false;
    }
}
