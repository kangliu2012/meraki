package com.everbridge.merakiui.soa;

import com.everbridge.meraki.domain.domain.Account;
import com.everbridge.meraki.domain.domain.Organization;
import com.everbridge.merakiui.dao.AccountDAO;
import com.everbridge.merakiui.dao.OrganizationDAO;
import com.everbridge.merakiui.model.AccountUI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by kangliu on 10/14/16.
 */
@Component
public class AccountSOA {
    @Autowired
    private AccountDAO accountDAO = null;

    @Autowired
    private OrganizationDAO organizationDAO = null;

    public AccountUI TransferJsonObjToUIObj(Account account, String ID) {
        AccountUI accountUI = new AccountUI();
        accountUI.setId(ID);
        accountUI.setEmail(account.email());
        accountUI.setPassword(account.password());
        accountUI.setAdmin(account.admin());
        return accountUI;
    }

    public List<AccountUI> getAllAccountsByOrgID(String orgID) {
        List<AccountUI> accountList = new Vector<>();
        Resources<Resource<Account>> resourceResources = accountDAO.getAllAccountsByOrgID(orgID);
        Collection<Resource<Account>> resourceCollection = resourceResources.getContent();
        Iterator<Resource<Account>> resourceIterator = resourceCollection.iterator();
        while (resourceIterator.hasNext()) {
            Resource<Account> accountResource = resourceIterator.next();
            String selfURL = accountResource.getLink("self").getHref();
            String ID = selfURL.substring(selfURL.lastIndexOf('/') + 1);
            AccountUI accountUI = TransferJsonObjToUIObj(accountResource.getContent(), ID);
            accountList.add(accountUI);
        }
        return accountList;
    }

    public List<AccountUI> getAllAccounts() {
        Collection<Resource<Account>> resourceCollection = accountDAO.getAllAccount();
        Iterator<Resource<Account>> resourceIterator = resourceCollection.iterator();

        List<AccountUI> accountUIList = new Vector<AccountUI>();
        while (resourceIterator.hasNext()) {
            Resource<Account> accountResource = resourceIterator.next();
            Account account = accountResource.getContent();
            String selfURL = accountResource.getLink("self").getHref();
            String ID = selfURL.substring(selfURL.lastIndexOf('/') + 1);
            AccountUI accountUI = TransferJsonObjToUIObj(account, ID);

            String orgURL = accountResource.getLink("organizations").getHref();
            Collection<Resource<Organization>> resources = organizationDAO.getAllOrganizationsByURL(orgURL).getContent();
            Iterator<Resource<Organization>> resourceIterator1 = resources.iterator();
            Resource<Organization> organizationResource = resourceIterator1.next();
            Organization organization = organizationResource.getContent();
            accountUI.setOrganization(organization.name());
            accountUIList.add(accountUI);
        }
        Comparator<AccountUI> comp = new Comparator<AccountUI>() {
            @Override
            public int compare(AccountUI o1, AccountUI o2) {
                return Integer.parseInt(o1.getId()) - Integer.parseInt(o2.getId());
            }
        };
        Collections.sort(accountUIList, comp);
        return accountUIList;
    }

    public AccountUI getAccountUIByID(String ID) {
        Account account = accountDAO.getAccountByID(ID);
        return TransferJsonObjToUIObj(account, ID);
    }

    public String getOrganizationIDByAccountID(String ID) {
        Resources<Resource<Organization>> resourceResources = organizationDAO.getAllOrganizationsByAccountID(ID);
        Collection<Resource<Organization>> resourceCollection1 = resourceResources.getContent();
        Resource<Organization> resource = resourceCollection1.iterator().next();
        String orgURL = resource.getLink("self").getHref();
        return orgURL.substring(orgURL.lastIndexOf('/') + 1);
    }

    public List<AccountUI> getAllAccountsInSameOrganizationByOneAccountID(String accountID) {
        Resources<Resource<Organization>> resourceResources = organizationDAO.getAllOrganizationsByAccountID(accountID);
        Iterator<Resource<Organization>> resourceIterator = resourceResources.iterator();

        Resource<Organization> resource = resourceIterator.next();

        String accountsURL = resource.getLink("accounts").getHref();
        Resources<Resource<Account>> resources = accountDAO.getAllAccountsByURL(accountsURL);
        Collection<Resource<Account>> resourceCollection = resources.getContent();

        Iterator<Resource<Account>> resourceIterator1 = resourceCollection.iterator();
        List<AccountUI> accountUIList = new Vector<AccountUI>();
        while (resourceIterator1.hasNext()) {
            Resource<Account> accountResource = resourceIterator1.next();
            Account account = accountResource.getContent();
            String selfURL = accountResource.getLink("self").getHref();
            accountID = selfURL.substring(selfURL.lastIndexOf('/') + 1);
            AccountUI accountUI = TransferJsonObjToUIObj(account, accountID);
            accountUIList.add(accountUI);
        }
        Comparator<AccountUI> comp = new Comparator<AccountUI>() {
            @Override
            public int compare(AccountUI o1, AccountUI o2) {
                return Integer.parseInt(o1.getId()) - Integer.parseInt(o2.getId());
            }
        };
        Collections.sort(accountUIList, comp);
        return accountUIList;
    }

    public void updateAccount(Account accountForUpdate, AccountUI accountUI) {
        accountForUpdate.password_$eq(accountUI.getPassword());
        accountDAO.updateAccount(accountForUpdate, accountUI.getId());
    }

    public String getAccountIDByEmail(String email) {
        return accountDAO.getAccountIDByEmail(email);
    }

    public boolean isEmailExist(String email) {
        return accountDAO.isEmailExist(email);
    }

    public String addAccount(AccountUI accountUI) {
        Account account = TransferUIObjToJsonObj(accountUI);
        return accountDAO.addAccount(account);
    }

    public Account getAccountByID(String ID) {
        return accountDAO.getAccountByID(ID);
    }

    public HttpStatus updateAccount(Account account, String ID) {
        return accountDAO.updateAccount(account, ID);
    }

    public void deleteAccountByID(String ID) {
        accountDAO.deleteAccountByID(ID);
    }

    private Account TransferUIObjToJsonObj(AccountUI accountUI) {
        Account account = new Account();
        account.email_$eq(accountUI.getEmail());
        account.password_$eq(accountUI.getPassword());
        account.admin_$eq(accountUI.isAdmin());
        return account;
    }

    public boolean isAdmin(String email) {
        return accountDAO.isAdmin(email);
    }
}