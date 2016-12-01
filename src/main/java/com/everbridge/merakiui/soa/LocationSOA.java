package com.everbridge.merakiui.soa;

import com.everbridge.meraki.domain.domain.Location;
import com.everbridge.merakiui.dao.LocationDAO;
import com.everbridge.merakiui.model.LocationMainUI;
import com.everbridge.merakiui.model.LocationUI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.stereotype.Component;
import scala.math.BigDecimal;

import java.util.*;

/**
 * Created by kangliu on 10/24/16.
 */
@Component
public class LocationSOA {
    @Autowired
    private LocationDAO locationDAO = null;

    @Autowired
    private ClientSOA clientSOA = null;

    public List<LocationUI> getAllLocationsByOrgID(String orgID) {
        Resources<Resource<Location>> resourceCollection = locationDAO.getAllLocationsByOrgID(orgID);
        Iterator<Resource<Location>> resourceIterator = resourceCollection.iterator();
        List<LocationUI> locationUIList = new Vector<LocationUI>();
        while (resourceIterator.hasNext()) {
            Resource<Location> locationResource = resourceIterator.next();
            LocationUI locationUI = getLocationUI(locationResource);
            locationUIList.add(locationUI);
        }
        Comparator<LocationUI> comp = new Comparator<LocationUI>() {
            @Override
            public int compare(LocationUI o1, LocationUI o2) {
                return Integer.parseInt(o1.getId()) - Integer.parseInt(o2.getId());
            }
        };
        Collections.sort(locationUIList, comp);
        return locationUIList;
    }

    public LocationUI TransferJsonObjToUIObj(Location location, String id) {
        LocationUI locationUI = new LocationUI();
        locationUI.setId(id);
        locationUI.setEbId(location.ebId());
        locationUI.setName(location.name());
        locationUI.setStreet(location.street());
        locationUI.setAptSuite(location.aptSuite());
        locationUI.setCity(location.city());
        locationUI.setState(location.state());
        locationUI.setZipCode(location.zipCode());
        if( location.lat() != null ) {
            locationUI.setLat(location.lat().toString());
        } else {
            locationUI.setLat("");
        }

        if( location.lng() != null ) {
            locationUI.setLng(location.lng().toString());
        } else {
            locationUI.setLng("");
        }
        locationUI.setCountry(location.country());
        locationUI.setFloorNumber(location.floorNumber());
        locationUI.setRoomNumber(location.roomNumber());
        locationUI.setLocationIcon(location.locationIcon());
        return locationUI;
    }

    private LocationUI getLocationUI(Resource<Location> locationResource) {
        Location location = locationResource.getContent();
        String selfURL = locationResource.getLink("self").getHref();
        String ID = selfURL.substring(selfURL.lastIndexOf('/') + 1);
        return TransferJsonObjToUIObj(location, ID);
    }

    public List<LocationMainUI> getLocationMainUIListByOrgID(String orgID) {
        List<LocationMainUI> locationMainUIList = new Vector<LocationMainUI>();
        List<LocationUI> locationUIs = getAllLocationsByOrgID(orgID);
        for(int i = 0; i < locationUIs.size(); i++) {
            LocationUI locationUI = locationUIs.get(i);
            LocationMainUI locationMainUI = new LocationMainUI();
            locationMainUI.setId(locationUI.getId());
            locationMainUI.setName(locationUI.getName());
            Long count = clientSOA.getKnownClientCountByOrgIDAndLocationID(orgID, locationUI.getId());
            locationMainUI.setKnownCount(count.toString());
            locationMainUIList.add(locationMainUI);
        }
        return locationMainUIList;
    }

    public String getLocationNameByID(String id) {
        Location location = locationDAO.getLocationByID(id);
        return location.name();
    }

    public void saveOrUpdateLocations(List<Location> locationList, String orgID) {
        List<LocationUI> locationUIList = getAllLocationsByOrgID(orgID);
        for(int i = 0; i < locationList.size(); i++) {
            Location location = locationList.get(i);
            boolean found = false;
            for(int j = 0; j < locationUIList.size(); j++) {
                LocationUI locationUI = locationUIList.get(j);
                if( location.ebId().equals(locationUI.getEbId())) {
                    locationDAO.updateLocation(location, locationUI.getId());
                    found = true;
                }
            }
            if( !found ) {
                locationDAO.addLocation(location);
            }
        }
    }

    public List<Location> transferFileStringToLocationList(String fileString) {
        List<Location> locationList = new Vector<Location>();
        String[] lines = fileString.split("\n");
        for(int i = 1; i < lines.length; i++) {
            String[] data = lines[i].split(",");
            Location location = new Location();
            location.ebId_$eq(getRealString(data[0]));
            location.name_$eq(getRealString(data[1]));
            location.street_$eq(getRealString(data[2]));
            location.aptSuite_$eq(getRealString(data[3]));
            location.city_$eq(getRealString(data[4]));
            location.state_$eq(getRealString(data[5]));
            location.zipCode_$eq(getRealString(data[6]));
            location.country_$eq(getRealString(data[7]));
            location.floorNumber_$eq(getRealString(data[8]));
            location.roomNumber_$eq(getRealString(data[9]));
            location.lat_$eq(getBigDecimal(data[10]));
            location.lng_$eq(getBigDecimal(data[11]));
            location.locationIcon_$eq(getRealString(data[12]));
            locationList.add(location);
        }
        return locationList;
    }

    public List<String[]> getLocationMapDataByOrg(String orgID) {
        List<LocationUI> locationUIList = getAllLocationsByOrgID(orgID);
        List<String[]> list = new Vector<>();
        String [] head = {"Everbridge Id", "Name", "Street", "Apt/Suite", "City", "State", "Zip Code", "Country", "Floor Number", "Room Number", "Lat", "Lng", "Icon"};
        list.add(head);
        for(int i = 0; i < locationUIList.size(); i++) {
            String[] strings = new String[head.length];
            LocationUI locationUI = locationUIList.get(i);
            strings[0] = (locationUI.getEbId() != null) ?  locationUI.getEbId() : "";
            strings[1] = (locationUI.getName() != null) ?  locationUI.getName() : "";
            strings[2] = (locationUI.getStreet() != null) ?  locationUI.getStreet() : "";
            strings[3] = (locationUI.getAptSuite() != null) ?  locationUI.getAptSuite() : "";
            strings[4] = (locationUI.getCity() != null) ?  locationUI.getCity() : "";
            strings[5] = (locationUI.getState() != null) ?  locationUI.getState() : "";
            strings[6] = (locationUI.getZipCode() != null) ?  locationUI.getZipCode() : "";
            strings[7] = (locationUI.getCountry() != null) ?  locationUI.getCountry() : "";
            strings[8] = (locationUI.getFloorNumber() != null) ?  locationUI.getFloorNumber() : "";
            strings[9] = (locationUI.getRoomNumber() != null) ?  locationUI.getRoomNumber() : "";
            strings[10] = (locationUI.getLat() != null) ?  locationUI.getLat() : "";
            strings[11] = (locationUI.getLng() != null) ?  locationUI.getLng() : "";
            strings[12] = (locationUI.getLocationIcon() != null) ?  locationUI.getLocationIcon() : "";
            list.add(strings);
        }
        return list;
    }

    public Location getLocationByID(String id){
        return locationDAO.getLocationByID(id);
    }

    private BigDecimal getBigDecimal(String s) {
        if( s.length() == 0) {
            return null;
        }
        if( s.charAt(0) == '"' ) {
            if( s.length() > 2 ) {
                return BigDecimal.javaBigDecimal2bigDecimal(new java.math.BigDecimal(s.substring(1, s.length() - 1)));
            } else {
                return null;
            }
        }
        return BigDecimal.javaBigDecimal2bigDecimal(new java.math.BigDecimal(s));
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
}
