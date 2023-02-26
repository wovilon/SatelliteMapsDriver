package wovilonapps.googlemapsdriver2.model;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;


public class GameManager implements Serializable {
    private int carNumber;
    private double lat;
    private double lng;



    public int getCarNumber() {
        return carNumber;
    }

    public LatLng getLatLng() {
        return new LatLng(lat, lng);
    }

    public void setCarNumber(int carNumber) {
        this.carNumber = carNumber;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }
    public void setLng(double lng) {
        this.lng = lng;
    }
}
