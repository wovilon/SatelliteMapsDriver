package wovilonapps.googlemapsdriver2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import wovilonapps.googlemapsdriver2.Model.CarMotion;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        LatLng carLocation = new LatLng(48.463801, 35.047894);
        /*mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(carLocation.latitude, carLocation.longitude), 20.0f));*/
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(carLocation.latitude, carLocation.longitude), 20.0f));

        //Bitmap carBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.car);
        //carBitmap = Bitmap.createScaledBitmap(carBitmap, 100, 100, false);

        //BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(carBitmap);
        //MarkerOptions carMarker= (new MarkerOptions().position(carLocation));
        //carMarker.icon(icon);
        //mMap.addMarker(carMarker);

        //CarMotion carMotion = new CarMotion(mMap, carLocation, carMarker);
        //carMotion.execute();
    }

}
