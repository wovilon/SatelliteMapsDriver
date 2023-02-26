package wovilonapps.googlemapsdriver2.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class LocationsBase {
public ArrayList<GameLocation> locations;

    public LocationsBase(){
        locations = new ArrayList<>();

        locations.add(new GameLocation("New York, USA, city", new LatLng(40.653834, -73.865783)));
        //locations.add(new GameLocation("New York, USA, city center", new LatLng(40.768004, -73.970275)));
        locations.add(new GameLocation("New York, USA, outskirts", new LatLng(40.865973, -73.593799)));
        locations.add(new GameLocation("Las Vegas, USA, city center", new LatLng(36.166683, -115.152833)));
        locations.add(new GameLocation("Paris, France, city center", new LatLng(48.858962, 2.293221)));
        locations.add(new GameLocation("Charles de Gaulle Airport, France, airport", new LatLng(49.016229, 2.545484)));
        locations.add(new GameLocation("Cape Town, South Africa, coast", new LatLng(49.016229, 2.545484)));
        locations.add(new GameLocation("Barcelona, Spain, coast", new LatLng(41.387758, 2.196608)));
        locations.add(new GameLocation("Laguna Seca, USA, racing track", new LatLng(36.586338, -121.756759)));
        locations.add(new GameLocation("Shuld, Germany, little town", new LatLng(50.446247, 6.886687)));
        locations.add(new GameLocation("Mtorrech, Tunis, sandy town", new LatLng(33.871934, 10.126494)));
        locations.add(new GameLocation("Alice-Springs, Australia, sandy town", new LatLng(-23.701631, 133.871973)));
        locations.add(new GameLocation("Francfurt, Germany, autobahn", new LatLng(50.201012, 8.645383)));

        locations.add(new GameLocation("Aircraft cemetery, USA", new LatLng(32.150260, -110.834107)));
    }
}
