package wovilonapps.googlemapsdriver2.model;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;


public class GameManager implements Serializable {
    private int carNumber;
    private int locationNumber;



    public int getCarNumber() {
        return carNumber;
    }

    public int getLocationNumber() {
        return locationNumber;
    }

    public void setCarNumber(int carNumber) {
        this.carNumber = carNumber;
    }

    public void setLocationNumber(int locationNumber) {
        this.locationNumber = locationNumber;
    }
}
