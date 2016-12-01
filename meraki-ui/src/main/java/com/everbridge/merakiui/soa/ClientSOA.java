package com.everbridge.merakiui.soa;

import com.everbridge.meraki.domain.domain.Client;
import com.everbridge.merakiui.dao.ClientDAO;
import com.everbridge.merakiui.model.ClientMainUI;
import com.everbridge.merakiui.model.ClientUI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/**
 * Created by kangliu on 10/26/16.
 */
@Component
public class ClientSOA {
    @Autowired
    private ClientDAO clientDAO = null;

    public ClientUI TransferJsonObjToUIObj(Client client, String ID) {
        ClientUI clientUI = new ClientUI();
        clientUI.setId(ID);
        clientUI.setMac(client.mac());
        clientUI.setLng(client.lng().toString());
        clientUI.setLat(client.lat().toString());
        return clientUI;
    }

    public ClientMainUI TransferJsonObjToMainUIObj(Client client, String ID) {
        ClientMainUI clientMainUI = new ClientMainUI();
        clientMainUI.setId(ID);
        clientMainUI.setIpv4(client.ipv4());
        clientMainUI.setMac(client.mac());
        clientMainUI.setLng(client.lng().toString());
        clientMainUI.setLat(client.lat().toString());
        return clientMainUI;
    }

    public ClientUI getClientByMacAndOrgID(String mac, String orgID) {
        Resource<Client> resourceResources = clientDAO.getClientByMacAndOrgID(mac, orgID);
        Client client = resourceResources.getContent();
        String selfURL = resourceResources.getLink("self").getHref();
        String ID = selfURL.substring(selfURL.lastIndexOf('/') + 1);
        ClientUI clientUI = TransferJsonObjToUIObj(client, ID);
        return clientUI;
    }

    public List<ClientUI> getClientListByMacAndOrgID(String mac, String orgID) {
        List<ClientUI> clientUIList = new Vector<ClientUI>();
        Resources<Resource<Client>> resourceResources = clientDAO.getClientListByMacAndOrgID(mac,orgID);
        Collection<Resource<Client>> collection = resourceResources.getContent();
        Iterator<Resource<Client>> iterator = collection.iterator();
        while (iterator.hasNext()) {
            Resource<Client> clientResource = iterator.next();
            Client client = clientResource.getContent();
            String selfURL = clientResource.getLink("self").getHref();
            String ID = selfURL.substring(selfURL.lastIndexOf('/') + 1);
            ClientUI clientUI = TransferJsonObjToUIObj(client, ID);
            clientUIList.add(clientUI);
        }
        return clientUIList;
    }

    public List<ClientUI> getAllClientListByMacAndOrgID(String mac, String orgID) {
        List<ClientUI> clientUIList = new Vector<ClientUI>();
        Resources<Resource<Client>> resourceResources = clientDAO.getAllClientListByMacAndOrgID(mac,orgID);
        Collection<Resource<Client>> collection = resourceResources.getContent();
        Iterator<Resource<Client>> iterator = collection.iterator();
        while (iterator.hasNext()) {
            Resource<Client> clientResource = iterator.next();
            Client client = clientResource.getContent();
            String selfURL = clientResource.getLink("self").getHref();
            String ID = selfURL.substring(selfURL.lastIndexOf('/') + 1);
            ClientUI clientUI = TransferJsonObjToUIObj(client, ID);
            clientUIList.add(clientUI);
        }
        return clientUIList;
    }


    public Long getKnownClientCountByOrgIDAndLocationID(String orgID, String locID) {
        return clientDAO.getKnownClientCountByLocationID(orgID, locID);
    }

    public List<ClientMainUI> getMappedClientListByOrgIDAndLocationID(String orgID, String locID) {
        List<ClientMainUI> clientUIList = new Vector<>();
        Resources<Resource<Client>> resourceResources = clientDAO.getMappedClientListByOrgIDAndLocationID(orgID,locID);
        Collection<Resource<Client>> collection = resourceResources.getContent();
        Iterator<Resource<Client>> iterator = collection.iterator();
        while (iterator.hasNext()) {
            Resource<Client> clientResource = iterator.next();
            Client client = clientResource.getContent();
            String selfURL = clientResource.getLink("self").getHref();
            String ID = selfURL.substring(selfURL.lastIndexOf('/') + 1);
            ClientMainUI clientMainUI = TransferJsonObjToMainUIObj(client, ID);
            clientUIList.add(clientMainUI);
        }
        return clientUIList;
    }

    public List<ClientMainUI> getMappedClientListByOrgIDAndLocationIDAndPage(String orgID, String locID, String page) {
        List<ClientMainUI> clientUIList = new Vector<>();
        Resources<Resource<Client>> resourceResources = clientDAO.getMappedClientListByOrgIDAndLocationIDAndPage(orgID,locID, page);
        Collection<Resource<Client>> collection = resourceResources.getContent();
        Iterator<Resource<Client>> iterator = collection.iterator();
        while (iterator.hasNext()) {
            Resource<Client> clientResource = iterator.next();
            Client client = clientResource.getContent();
            String selfURL = clientResource.getLink("self").getHref();
            String ID = selfURL.substring(selfURL.lastIndexOf('/') + 1);
            ClientMainUI clientMainUI = TransferJsonObjToMainUIObj(client, ID);
            clientUIList.add(clientMainUI);
        }
        return clientUIList;
    }
}
