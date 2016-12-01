package com.everbridge.merakiui.soa;

import com.everbridge.meraki.domain.domain.AccessPoint;
import com.everbridge.meraki.domain.domain.Location;
import com.everbridge.merakiui.dao.AccessPointDAO;
import com.everbridge.merakiui.dao.LocationDAO;
import com.everbridge.merakiui.model.AccessPointUI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by kangliu on 10/24/16.
 */
@Component
public class AccessPointSOA {
    @Autowired
    private AccessPointDAO accessPointDAO = null;

    @Autowired
    private LocationDAO locationDAO = null;

    public List<AccessPointUI> getAllAccessPointsByOrgID(String orgID) {
        Collection<Resource<AccessPoint>> resourceCollection = accessPointDAO.getAllAccessPointsByOrgID(orgID);
        Iterator<Resource<AccessPoint>> resourceIterator = resourceCollection.iterator();
        List<AccessPointUI> accessPointUIList = new Vector<AccessPointUI>();
        while (resourceIterator.hasNext()) {
            Resource<AccessPoint> accessPointResource = resourceIterator.next();
            AccessPointUI accessPointUI = getAccountUI(accessPointResource);
            String locID = getLocationIDByAccesspointID(accessPointUI.getId());
            accessPointUI.setLocationId(locID);
            accessPointUIList.add(accessPointUI);
        }
        Comparator<AccessPointUI> comp = new Comparator<AccessPointUI>() {
            @Override
            public int compare(AccessPointUI o1, AccessPointUI o2) {
                return Integer.parseInt(o1.getId()) - Integer.parseInt(o2.getId());
            }
        };
        Collections.sort(accessPointUIList, comp);
        return accessPointUIList;
    }

    public List<AccessPointUI> getAllAccessPointsMappedByOrgID(String orgID) {
        Collection<Resource<AccessPoint>> resourceCollection = accessPointDAO.getAllAccessPointsMappedByOrgID(orgID);
        Iterator<Resource<AccessPoint>> resourceIterator = resourceCollection.iterator();
        List<AccessPointUI> accessPointUIList = new Vector<AccessPointUI>();
        while (resourceIterator.hasNext()) {
            Resource<AccessPoint> accessPointResource = resourceIterator.next();
            AccessPointUI accessPointUI = getAccountUI(accessPointResource);
            String locID = getLocationIDByAccesspointID(accessPointUI.getId());
            accessPointUI.setLocationId(locID);
            accessPointUIList.add(accessPointUI);
        }
        Comparator<AccessPointUI> comp = new Comparator<AccessPointUI>() {
            @Override
            public int compare(AccessPointUI o1, AccessPointUI o2) {
                return Integer.parseInt(o1.getId()) - Integer.parseInt(o2.getId());
            }
        };
        Collections.sort(accessPointUIList, comp);
        return accessPointUIList;
    }

    public List<AccessPointUI> getAllAccessPointsUnmappedByOrgID(String orgID) {
        Collection<Resource<AccessPoint>> resourceCollection = accessPointDAO.getAllAccessPointsUnmappedByOrgID(orgID);
        Iterator<Resource<AccessPoint>> resourceIterator = resourceCollection.iterator();
        List<AccessPointUI> accessPointUIList = new Vector<AccessPointUI>();
        while (resourceIterator.hasNext()) {
            Resource<AccessPoint> accessPointResource = resourceIterator.next();
            AccessPointUI accessPointUI = getAccountUI(accessPointResource);
            accessPointUI.setLocationId("-1");
            accessPointUIList.add(accessPointUI);
        }
        Comparator<AccessPointUI> comp = new Comparator<AccessPointUI>() {
            @Override
            public int compare(AccessPointUI o1, AccessPointUI o2) {
                return Integer.parseInt(o1.getId()) - Integer.parseInt(o2.getId());
            }
        };
        Collections.sort(accessPointUIList, comp);
        return accessPointUIList;
    }

    public AccessPointUI TransferJsonObjToUIObj(AccessPoint accessPoint, String id) {
        AccessPointUI accessPointUI = new AccessPointUI();
        accessPointUI.setId(id);
        accessPointUI.setMac(accessPoint.mac());
        accessPointUI.setName(accessPoint.name());
        accessPointUI.setCustomerName(accessPoint.customerName());
        return accessPointUI;
    }

    public String getLocationIDByAccesspointID(String ID) {
        Resource<Resource<Location>> resource = locationDAO.getOneLocationByAccesspointID(ID);
        String locationURL = resource.getLink("self").getHref();
        return locationURL.substring(locationURL.lastIndexOf('/') + 1);
    }

    public boolean updateAccessPointBycustomerName(String ID, String customerName) {
        AccessPoint accessPoint = accessPointDAO.getAccessPointByID(ID);
        accessPoint.customerName_$eq(customerName);

        HttpStatus httpStatus = accessPointDAO.updateAccessPoint(accessPoint, ID);
        return httpStatus.equals(HttpStatus.OK);
    }

    private AccessPointUI getAccountUI(Resource<AccessPoint> accessPointResource) {
        AccessPoint accessPoint = accessPointResource.getContent();
        String selfURL = accessPointResource.getLink("self").getHref();
        String ID = selfURL.substring(selfURL.lastIndexOf('/') + 1);
        return TransferJsonObjToUIObj(accessPoint, ID);
    }

    public String getAccessPointUnmappedCountByOrgID(String orgID) {
        Long count = accessPointDAO.getAccessPointUnmappedCountByOrgID(orgID);
        return count.toString();
    }

    public String getAccessPointMappedCountByOrgID(String orgID) {
        Long count = accessPointDAO.getAccessPointMappedCountByOrgID(orgID);
        return count.toString();
    }
}
