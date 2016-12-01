package com.everbridge.merakiui.soa;

import com.everbridge.meraki.domain.domain.Account;
import com.everbridge.meraki.domain.domain.Organization;
import com.everbridge.merakiui.dao.AccountDAO;
import com.everbridge.merakiui.dao.OrganizationDAO;
import com.everbridge.merakiui.model.AccountUI;
import com.everbridge.merakiui.model.OrganizationUI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by kangliu on 10/14/16.
 */
@Component
public class OrganizationSOA {
    @Autowired
    private AccountDAO accountDAO = null;

    @Autowired
    private AccountSOA accountSOA = null;

    @Autowired
    private OrganizationDAO organizationDAO = null;

    public OrganizationUI TransferJsonObjToUIObj(Organization organization, String id) {
        OrganizationUI organizationUI = new OrganizationUI();
        organizationUI.setId(id);
        organizationUI.setName(organization.name());
        organizationUI.setEbOrganizationId(organization.ebOrganizationId());
        organizationUI.setValidator(organization.validator());
        organizationUI.setSecret(organization.secret());
        organizationUI.setDynamicLocationUpdateInterval(organization.dynamicLocationUpdateInterval());
        organizationUI.setEnvironment(organization.environment());
        organizationUI.setCreatedAt(organization.createdAt());
        organizationUI.setUpdateAt(organization.updatedAt());
        organizationUI.setImportActive(organization.importActive());
        organizationUI.setImportFinishedAt(organization.importFinishedAt());
        organizationUI.setApiUsername(organization.apiUsername());
        organizationUI.setApiPassword(organization.apiPassword());
        organizationUI.setGeolocateBy(organization.geolocateBy());
        return organizationUI;
    }

    public List<AccountUI> getAccontUIList(Resource<Organization> organizationResource) {
        List<AccountUI> accountUIList = new Vector<AccountUI>();
        String accountsURL = organizationResource.getLink("accounts").getHref();
        Collection<Resource<Account>> resourceCollectionAccount = accountDAO.getAllAccountsByURL(accountsURL).getContent();
        Iterator<Resource<Account>> resourceIteratorAccount = resourceCollectionAccount.iterator();
        while (resourceIteratorAccount.hasNext()) {
            Resource<Account> accountResource = resourceIteratorAccount.next();
            Account account = accountResource.getContent();
            String selfURL = accountResource.getLink("self").getHref();
            String ID = selfURL.substring(selfURL.lastIndexOf('/') + 1);
            AccountUI accountUI = accountSOA.TransferJsonObjToUIObj(account, ID);
            accountUIList.add(accountUI);
        }
        return accountUIList;
    }

    public Organization TransferUIObjToJsonObj(OrganizationUI organizationUI) {
        Organization organization = new Organization();
        organization.name_$eq(organizationUI.getName());
        organization.ebOrganizationId_$eq(organizationUI.getEbOrganizationId());
        organization.validator_$eq(organizationUI.getValidator());
        organization.secret_$eq(organizationUI.getSecret());
        organization.dynamicLocationUpdateInterval_$eq(organizationUI.getDynamicLocationUpdateInterval());
        organization.environment_$eq(organizationUI.getEnvironment());
        organization.createdAt_$eq(organizationUI.getCreatedAt());
        organization.updatedAt_$eq(organizationUI.getUpdateAt());
        organization.importActive_$eq(organizationUI.isImportActive());
        organization.importFinishedAt_$eq(organizationUI.getImportFinishedAt());
        organization.apiUsername_$eq(organizationUI.getApiUsername());
        organization.apiPassword_$eq(organizationUI.getApiPassword());
        organization.geolocateBy_$eq(organizationUI.getGeolocateBy());
        return organization;
    }

    public List<OrganizationUI> getAllOrganizations() {
        Collection<Resource<Organization>> resourceCollectionOrg = organizationDAO.getAllOrganizations();
        Iterator<Resource<Organization>> resourceIterator = resourceCollectionOrg.iterator();
        List<OrganizationUI> organizationList = new Vector<OrganizationUI>();
        while (resourceIterator.hasNext()) {
            Resource<Organization> organizationResource = resourceIterator.next();
            OrganizationUI organizationUI = getOrganizationUI(organizationResource);
            List<AccountUI> accountUIList = getAccontUIList(organizationResource);
            organizationUI.setAccountList(accountUIList);
            organizationList.add(organizationUI);
        }
        Comparator<OrganizationUI> comp = new Comparator<OrganizationUI>() {
            @Override
            public int compare(OrganizationUI o1, OrganizationUI o2) {
                return Integer.parseInt(o1.getId()) - Integer.parseInt(o2.getId());
            }
        };
        Collections.sort(organizationList, comp);
        return organizationList;
    }

    public void UpdateOrganization(Organization organizationForUpdate, OrganizationUI organizationUI) {
        organizationUI.setImportFinishedAt(organizationForUpdate.importFinishedAt());
        organizationUI.setImportActive(organizationForUpdate.importActive());
        organizationUI.setCreatedAt(organizationForUpdate.createdAt());
        organizationUI.setUpdateAt(new Date());
        organizationUI.setImportFinishedAt(organizationForUpdate.importFinishedAt());

        Organization organization = TransferUIObjToJsonObj(organizationUI);
        organizationDAO.updateOrganization(organization, organizationUI.getId());
    }

    public OrganizationUI getOrganizationByAccountID(String accoutID) {
        Resources<Resource<Organization>> organizationResource = organizationDAO.getOrganizationByAccountID(accoutID);
        Collection<Resource<Organization>> resourceCollection = organizationResource.getContent();
        Resource<Organization> resource = resourceCollection.iterator().next();
        String selfURL = resource.getLink("self").getHref();
        String orgID = selfURL.substring(selfURL.lastIndexOf('/') + 1);
        Organization organization = resource.getContent();
        return TransferJsonObjToUIObj(organization, orgID);
    }

    public String getOrganizationIDByAccountID(String accountID) {
        Resources<Resource<Organization>> resourceResources = organizationDAO.getAllOrganizationsByAccountID(accountID);
        Collection<Resource<Organization>> resourceCollection = resourceResources.getContent();
        Iterator<Resource<Organization>> iterator = resourceCollection.iterator();
        Resource<Organization> resouce = iterator.next();
        String orgURL = resouce.getLink("self").getHref();
        return orgURL.substring(orgURL.lastIndexOf('/') + 1);
    }

    public boolean isOrgIDExist(String orgID) {
        return organizationDAO.isOrgIDExist(orgID);
    }

    public String addOrganization(OrganizationUI organizationUI) {
        Organization organization = TransferUIObjToJsonObj(organizationUI);
        return organizationDAO.addOrganization(organization);
    }

    public Organization getOrganizationByID(String ID) {
        return organizationDAO.getOrganizationByID(ID);
    }

    public void deleteOrganizationByID(String ID) {
        organizationDAO.deleteOrganizationByID(ID);
    }

    private OrganizationUI getOrganizationUI(Resource<Organization> organizationResource) {
        Organization organization = organizationResource.getContent();
        String selfURL = organizationResource.getLink("self").getHref();
        String ID = selfURL.substring(selfURL.lastIndexOf('/') + 1);
        return TransferJsonObjToUIObj(organization, ID);
    }
}