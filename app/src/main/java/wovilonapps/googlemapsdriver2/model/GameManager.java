package wovilonapps.googlemapsdriver2.model;

import com.google.android.gms.maps.model.LatLng;


public class GameManager {
    private Car car;
    private LatLng location;




    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public LatLng getLocation() {
        return location;
    }
}
