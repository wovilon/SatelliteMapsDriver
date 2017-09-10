package wovilonapps.googlemapsdriver2.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class LocationsBase {
public ArrayList<GameLocation> locations;

    public LocationsBase(){
        locations = new ArrayList<>();

        locations.add(new GameLocation("Default location1", new LatLng(40.653834, -73.865783)));
        locations.add(new GameLocation("Default location2", new LatLng(41.653834, -74.865783)));
        locations.add(new GameLocation("Default location3", new LatLng(42.653834, -75.865783)));
    }
}
