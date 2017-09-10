package wovilonapps.googlemapsdriver2.model;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;


public class GameLocation {
    public String caption;
    public String description;
    public LatLng latLng;

    public GameLocation() {
        //default constructor
    }

    public GameLocation(String caption, LatLng latLng) {
        this.caption = caption;
        this.latLng = latLng;
    }
}
