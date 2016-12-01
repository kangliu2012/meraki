package com.everbridge.merakiui.soa;

import com.everbridge.meraki.domain.domain.Client;
import com.everbridge.meraki.domain.domain.Contact;
import com.everbridge.meraki.domain.domain.Organization;
import com.everbridge.merakiui.dao.ClientDAO;
import com.everbridge.merakiui.dao.ContactDAO;
import com.everbridge.merakiui.dao.OrganizationDAO;
import com.everbridge.merakiui.model.ClientUI;
import com.everbridge.merakiui.model.ContactUI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by kangliu on 10/24/16.
 */
@Component
public class ContactSOA {
    @Autowired
    private ContactDAO contactDAO = null;

    @Autowired
    private ClientDAO clientDAO = null;

    @Autowired
    private ClientSOA clientSOA = null;

    @Autowired
    private OrganizationDAO organizationDAO = null;

    @Autowired
    private LinkAPIsSOA linkAPIsSOA = null;

    public List<String> getClientIDList(Resource<Contact> contactResource) {
        List<String> list = new Vector<>();
        String clientsURL = contactResource.getLink("clients").getHref();

        Collection<Resource<Client>> resourceCollection = clientDAO.getAllClientsByURL(clientsURL).getContent();

        Iterator<Resource<Client>> resourceIterator = resourceCollection.iterator();
        if( false == resourceIterator.hasNext() ) return null;
        while (resourceIterator.hasNext()) {
            Resource<Client> clientResource = resourceIterator.next();
            Client client = clientResource.getContent();
            String selfURL = clientResource.getLink("self").getHref();

            String ID = selfURL.substring(selfURL.lastIndexOf('/') + 1);
            list.add(ID);
        }
        return list;
    }

    public List<ClientUI> getClientUIList(Resource<Contact> contactResource) {
        List<ClientUI> contactUIList = new Vector<ClientUI>();
        String clientsURL = contactResource.getLink("clients").getHref();

        Collection<Resource<Client>> resourceCollection = clientDAO.getAllClientsByURL(clientsURL).getContent();

        Iterator<Resource<Client>> resourceIterator = resourceCollection.iterator();
        if( false == resourceIterator.hasNext() ) return null;

        while (resourceIterator.hasNext()) {
            Resource<Client> clientResource = resourceIterator.next();
            Client client = clientResource.getContent();
            String selfURL = clientResource.getLink("self").getHref();

            String ID = selfURL.substring(selfURL.lastIndexOf('/') + 1);
            ClientUI clientUI = clientSOA.TransferJsonObjToUIObj(client, ID);
            contactUIList.add(clientUI);
       }
       return contactUIList;
    }

    private ContactUI getContactUI(Resource<Contact> contactResource) {
        Contact contact = contactResource.getContent();
        String selfURL = contactResource.getLink("self").getHref();
        String ID = selfURL.substring(selfURL.lastIndexOf('/') + 1);
        return TransferJsonObjToUIObj(contact, ID);
    }

    public ContactUI TransferJsonObjToUIObj(Contact contact, String id) {
        ContactUI contactUI = new ContactUI();
        contactUI.setId(id);
        contactUI.setEbContactId(contact.ebContactId());
        contactUI.setExternalId(contact.externalId());
        contactUI.setFirstName(contact.firstName());
        contactUI.setLastName(contact.lastName());
        return contactUI;
    }

    public String getOrgIDByContactID(String contactID) {
        Resources<Resource<Organization>> resourceResources = organizationDAO.getAllOrganizationsByContactID(contactID);
        String orgURL = resourceResources.getLink("self").getHref();
        return orgURL.substring(orgURL.lastIndexOf('/') + 1);
    }

    public int getContactUnpairedCountByOrgID(String orgID) {
        Collection<Resource<Contact>> resourceCollectionContact = contactDAO.getContactUnpairedCountByOrgID(orgID);
        return resourceCollectionContact.size();
    }

    public int  getContactPairedCountByOrgID(String orgID) {
        Collection<Resource<Contact>> resourceCollectionContact = contactDAO.getContactPairedCountByOrgID(orgID);
        return resourceCollectionContact.size();
    }

    public List<ContactUI> getAllContactsUnpairedByOrgID(String orgID) {
        Collection<Resource<Contact>> resourceCollectionContact = contactDAO.getAllContactsUnpairedByOrgID(orgID);
        Iterator<Resource<Contact>> resourceIterator = resourceCollectionContact.iterator();
        List<ContactUI> contactUIList = new Vector<ContactUI>();
        while (resourceIterator.hasNext()) {
            Resource<Contact> contactResource = resourceIterator.next();
            ContactUI contactUI = getContactUI(contactResource);
            contactUIList.add(contactUI);
        }
        sortContactUIList(contactUIList);
        return contactUIList;
    }

    public List<ContactUI> getAllContactsPairedByOrgID(String orgID) {
        Collection<Resource<Contact>> resourceCollectionContact = contactDAO.getAllContactsPairedByOrgID(orgID);
        Iterator<Resource<Contact>> resourceIterator = resourceCollectionContact.iterator();
        List<ContactUI> contactUIList = new Vector<ContactUI>();
        while (resourceIterator.hasNext()) {
            Resource<Contact> contactResource = resourceIterator.next();
            ContactUI contactUI = getContactUI(contactResource);
            List<ClientUI> clientUIList = getClientUIList(contactResource);
            if( clientUIList != null ) {
                contactUI.setClientUIList(clientUIList);
            }
            contactUIList.add(contactUI);
        }
        sortContactUIList(contactUIList);
        return contactUIList;
    }

    public List<ContactUI> searchContactsByOrgID(String orgID, String keyWord) {
        Collection<Resource<Contact>> resourceCollectionContact = contactDAO.getAllContactsByOrgID(orgID);
        Iterator<Resource<Contact>> resourceIterator = resourceCollectionContact.iterator();
        List<ContactUI> contactUIList = new Vector<ContactUI>();
        while (resourceIterator.hasNext()) {
            Resource<Contact> contactResource = resourceIterator.next();
            Contact contact = contactResource.getContent();
            String firstname = contact.firstName().toUpperCase();
            String lastname = contact.lastName().toUpperCase();
            String externalID = contact.externalId().toUpperCase();
            int idx = externalID.indexOf('@');
            if( idx > 0 ) {
                externalID = externalID.substring(0, idx);
            }
            if( firstname.contains(keyWord) || lastname.contains(keyWord) || externalID.contains(keyWord)) {
                String selfURL = contactResource.getLink("self").getHref();
                String ID = selfURL.substring(selfURL.lastIndexOf('/') + 1);
                ContactUI contactUI = TransferJsonObjToUIObj(contact, ID);
                List<ClientUI> clientUIList = getClientUIList(contactResource);
                if( clientUIList != null ) {
                    contactUI.setClientUIList(clientUIList);
                }
                contactUIList.add(contactUI);
            }
        }
        return contactUIList;
    }

    public List<ContactUI> getAllContactsByOrgID(String orgID) {
        Collection<Resource<Contact>> resourceCollectionContact = contactDAO.getAllContactsByOrgID(orgID);
        Iterator<Resource<Contact>> resourceIterator = resourceCollectionContact.iterator();
        List<ContactUI> contactUIList = new Vector<ContactUI>();
        int ct = 0;
        while (resourceIterator.hasNext()) {
            ct++;
            Resource<Contact> contactResource = resourceIterator.next();
            ContactUI contactUI = getContactUI(contactResource);
            List<ClientUI> clientUIList = getClientUIList(contactResource);
            if( clientUIList != null ) {
                contactUI.setClientUIList(clientUIList);
            }
            contactUIList.add(contactUI);
        }
        sortContactUIList(contactUIList);
        return contactUIList;
    }

    public List<ContactUI> getAllContactsByOrgIDAndPage(String orgID, String page) {
        Collection<Resource<Contact>> resourceCollectionContact = contactDAO.getAllContactsByOrgIDAndPage(orgID, page);
        Iterator<Resource<Contact>> resourceIterator = resourceCollectionContact.iterator();
        List<ContactUI> contactUIList = new Vector<ContactUI>();
        while (resourceIterator.hasNext()) {
            Resource<Contact> contactResource = resourceIterator.next();
            ContactUI contactUI = getContactUI(contactResource);
            List<ClientUI> clientUIList = getClientUIList(contactResource);
            if( clientUIList != null ) {
                contactUI.setClientUIList(clientUIList);
            }
            contactUIList.add(contactUI);
        }
        sortContactUIList(contactUIList);
        return contactUIList;
    }

    public Long getCountByOrgID(String orgID) {
        return contactDAO.getCountByOrgID(orgID);
    }

    public List<ContactUI> getAllContactsUnpairedByOrgIDAndPage (String orgID, String page) {
        Collection<Resource<Contact>> resourceCollectionContact = contactDAO.getAllContactsUnpairedByOrgIDAndPage(orgID, page);
        Iterator<Resource<Contact>> resourceIterator = resourceCollectionContact.iterator();
        List<ContactUI> contactUIList = new Vector<ContactUI>();
        while (resourceIterator.hasNext()) {
            Resource<Contact> contactResource = resourceIterator.next();
            ContactUI contactUI = getContactUI(contactResource);
            List<ClientUI> clientUIList = getClientUIList(contactResource);
            if (clientUIList != null) {
                contactUI.setClientUIList(clientUIList);
            }
            contactUIList.add(contactUI);
        }
        sortContactUIList(contactUIList);
        return contactUIList;
    }

    public List<ContactUI> getAllContactsPairedByOrgIDAndPage (String orgID, String page) {
        Collection<Resource<Contact>> resourceCollectionContact = contactDAO.getAllContactsPairedByOrgIDAndPage(orgID, page);
        Iterator<Resource<Contact>> resourceIterator = resourceCollectionContact.iterator();
        List<ContactUI> contactUIList = new Vector<ContactUI>();
        while (resourceIterator.hasNext()) {
            Resource<Contact> contactResource = resourceIterator.next();
            ContactUI contactUI = getContactUI(contactResource);
            List<ClientUI> clientUIList = getClientUIList(contactResource);
            if (clientUIList != null) {
                contactUI.setClientUIList(clientUIList);
            }
            contactUIList.add(contactUI);
        }
        sortContactUIList(contactUIList);
        return contactUIList;
    }

    private void sortContactUIList(List<ContactUI> contactUIList) {
        Comparator<ContactUI> comp = new Comparator<ContactUI>() {
            @Override
            public int compare(ContactUI o1, ContactUI o2) {
                return Integer.parseInt(o1.getId()) - Integer.parseInt(o2.getId());
            }
        };
        Collections.sort(contactUIList, comp);
    }

    public List<ContactUI> transferFileStringToContactList(String fileString, String orgID, Set<String> stringSet) {
        List<ContactUI> contactUIList = new Vector<ContactUI>();
        String[] lines = fileString.split("\n");
        for(int i = 1; i < lines.length; i++) {
            String[] data = lines[i].split(",");
            ContactUI contactUI = new ContactUI();
            contactUI.setId(getRealString(data[0]));
            contactUI.setExternalId(getRealString(data[1]));
            contactUI.setFirstName(getRealString(data[2]));
            contactUI.setLastName(getRealString(data[3]));
            contactUI.setEbContactId(getRealString(data[4]));
            if( stringSet.contains(contactUI.getId() )) {
                Resource<Contact> contactResource = contactDAO.getContactResourceByID(contactUI.getId());
                List<String> clientIDList = getClientIDList(contactResource);
                if( clientIDList != null ) {
                    for(int k = 0; k < clientIDList.size(); k++) {
                        linkAPIsSOA.UnlinkTwoObjects("client", clientIDList.get(k), "contact", contactUI.getId());
                    }
                }
            }

            if( data.length <= 5 || data[5] == null || data[5].length() == 0 || data[5].length() == 1 ) {
                contactUI.setClientUIList(null);
            } else {
                List<ClientUI> clientUIList = new Vector<>();
                if( data[5].charAt(0) == '"' ) {
                    if( data[5].length() > 2 ) {
                        String s = data[5].substring(1, data[5].length() - 1);
                        String[] macData = s.split(" ");
                        for(int j = 0; j < 1; j++) {
                            ClientUI clientUI  = clientSOA.getClientByMacAndOrgID(macData[j], orgID);
                            if( clientUI != null ) {
                                clientUIList.add(clientUI);
                            }
                        }
                    }
                } else {
                    data[5] = data[5].substring(0, data[5].length() - 1);
                    String[] macData = data[5].split(" ");
                    for(int j = 0; j < macData.length; j++) {
                        ClientUI clientUI  = clientSOA.getClientByMacAndOrgID(macData[j], orgID);
                        if( clientUI != null ) {
                            clientUIList.add(clientUI);
                        }
                    }
                }
                contactUI.setClientUIList(clientUIList);
            }
            contactUIList.add(contactUI);
        }
        return contactUIList;
    }

    public List<String[]> getContactListDataByOrg(String orgID) {
        Collection<Resource<Contact>> resourceCollectionContact = contactDAO.getAllContactsByOrgID(orgID);
        Iterator<Resource<Contact>> resourceIterator = resourceCollectionContact.iterator();
        List<String[]> list = new Vector<>();
        String [] head = {"id", "external_id", "first_name", "last_name", "eb_contact_id", "client_mac"};
        list.add(head);
        while (resourceIterator.hasNext()) {
            Resource<Contact> contactResource = resourceIterator.next();
            Contact contact = contactResource.getContent();
            String selfURL = contactResource.getLink("self").getHref();
            String contactID = selfURL.substring(selfURL.lastIndexOf('/') + 1);
            String[] strings = new String[head.length];
            strings[0] = (contactID != null) ?  contactID : "";
            strings[1] = (contact.externalId() != null) ?  contact.externalId() : "";
            strings[2] = (contact.firstName() != null) ?  contact.firstName() : "";
            strings[3] = (contact.lastName() != null) ?  contact.lastName() : "";
            strings[4] = (contact.ebContactId() != null) ?  contact.ebContactId() : "";

            String clientsURL = contactResource.getLink("clients").getHref();
            Collection<Resource<Client>> resourceCollection = clientDAO.getAllClientsByURL(clientsURL).getContent();
            Iterator<Resource<Client>> resourceIterator1 = resourceCollection.iterator();
            strings[5] = "";

            while (resourceIterator1.hasNext()) {
                Resource<Client> clientResource = resourceIterator1.next();
                Client client = clientResource.getContent();
                if( strings[5].length() > 0 ) {
                    strings[5] += " ";
                }
                strings[5] += client.mac();
            }
            list.add(strings);
        }
        return list;
    }

    private String getRealString(String s) {
        if( s.length() == 0) {
            return null;
        }
        if( s.charAt(0) == '"' ) {
            if( s.length() > 2 ) {
                return s.substring(1, s.length() - 1);
            } else {
                return null;
            }
        }
        if( s.charAt(s.length() - 1) == '\r' ) {
            return s.substring(0, s.length() - 1);
        }
        return s;
    }

    public Set<String> getContactIDList(String orgID) {
        Collection<Resource<Contact>> resourceCollectionContact = contactDAO.getAllContactsByOrgID(orgID);
        Iterator<Resource<Contact>> resourceIterator = resourceCollectionContact.iterator();
        Set<String> stringSet = new HashSet<>();
        while (resourceIterator.hasNext()) {
            Resource<Contact> contactResource = resourceIterator.next();
            String selfURL = contactResource.getLink("self").getHref();
            String ID = selfURL.substring(selfURL.lastIndexOf('/') + 1);
            stringSet.add(ID);
        }
        return stringSet;
    }

    public void saveOrUpdateLocations(List<ContactUI> contactUIList, String orgID, Set<String> stringSet) {
        for (int i = 0; i < contactUIList.size(); i++) {
            ContactUI contactUI = contactUIList.get(i);
            boolean ebIDExist = stringSet.contains(contactUI.getId());
            if (ebIDExist) {
                Contact contact = contactDAO.getContactByID(contactUI.getId());
                if (contact != null) {
                    contact.firstName_$eq(contactUI.getFirstName());
                    contact.lastName_$eq(contactUI.getLastName());
                    contact.ebContactId_$eq(contactUI.getEbContactId());
                    contact.externalId_$eq(contactUI.getExternalId());
                    contactDAO.updateContact(contact, contactUI.getId());

                    List<ClientUI> clientUIList = contactUI.getClientUIList();
                    if (clientUIList != null && clientUIList.size() > 0) {
                        for (int j = 0; j < clientUIList.size(); j++) {
                            linkAPIsSOA.LinkTwoObjects("client", clientUIList.get(j).getId(), "contact", contactUI.getId());
                        }
                    }
                }
            } else {
                Contact contact = new Contact();
                contact.firstName_$eq(contactUI.getFirstName());
                contact.lastName_$eq(contactUI.getLastName());
                contact.ebContactId_$eq(contactUI.getEbContactId());
                contact.externalId_$eq(contactUI.getExternalId());
                String newID = contactDAO.addContact(contact);
                linkAPIsSOA.LinkTwoObjects("contact", newID, "organization", orgID);
                List<ClientUI> clientUIList = contactUI.getClientUIList();
                if (clientUIList != null && clientUIList.size() > 0) {
                    for (int j = 0; j < clientUIList.size(); j++) {
                        linkAPIsSOA.LinkTwoObjects("client", clientUIList.get(j).getId(), "contact", contactUI.getId());
                    }
                }
            }
        }
    }
}
