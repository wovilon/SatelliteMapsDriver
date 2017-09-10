package wovilonapps.googlemapsdriver2;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import wovilonapps.googlemapsdriver2.model.CarModels;
import wovilonapps.googlemapsdriver2.model.TwoPointsCarMotion;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, View.OnTouchListener {

    private GoogleMap mMap;
    private TwoPointsCarMotion carMotion;
    private Button buttonAccelerate, buttonBrake, buttonLeft, buttonRight;
    private ImageView car;
    private ImageView trailer;
    double scaleCoeff;      // shows scale of car0 image, = car0 img width, pixels / view width, dpi


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        car = (ImageView)findViewById(R.id.car);
        trailer = (ImageView) findViewById(R.id.trailer);
        /*ViewGroup.LayoutParams params = car0.getLayoutParams();
        car0.setLayoutParams(params);*/



        buttonAccelerate = (Button)findViewById(R.id.btAccelerate);
        buttonBrake = (Button)findViewById(R.id.btBrake);
        buttonLeft = (Button)findViewById(R.id.btLeft);
        buttonRight = (Button)findViewById(R.id.btRight);

        buttonAccelerate.setOnTouchListener(this);
        buttonBrake.setOnTouchListener(this);
        buttonLeft.setOnTouchListener(this);
        buttonRight.setOnTouchListener(this);

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
        LatLng carLocation = new LatLng(40.653834, -73.865783);
        /*mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(carLocation.latitude, carLocation.longitude), 20.0f));*/
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(carLocation.latitude, carLocation.longitude), 20.1f));

        //Bitmap carBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.car0);
        //carBitmap = Bitmap.createScaledBitmap(carBitmap, 100, 100, false);

        //BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(carBitmap);
        //MarkerOptions carMarker= (new MarkerOptions().position(carLocation));
        //carMarker.icon(icon);
        //mMap.addMarker(carMarker);

        carMotion = new TwoPointsCarMotion(this, mMap, carLocation, car, trailer,
                new CarModels(this).getPowerfulTruck());
        carMotion.execute();

    }


    @Override
    public boolean onTouch(View view, MotionEvent e) {
        if (view.getId()==buttonAccelerate.getId()) {
            if (e.getAction() == MotionEvent.ACTION_DOWN)
                carMotion.updateAcceleration(true);
            else if (e.getAction() == MotionEvent.ACTION_UP)
                carMotion.updateAcceleration(false);
        }
        else if (view.getId()==buttonBrake.getId()) {
            if (e.getAction() == MotionEvent.ACTION_DOWN)
                carMotion.updateBrakes(true);
            else if (e.getAction() == MotionEvent.ACTION_UP)
                carMotion.updateBrakes(false);
        }
        else if (view.getId()==buttonLeft.getId()) {
            if (e.getAction() == MotionEvent.ACTION_DOWN)
                carMotion.updateLeft(true);
            else if (e.getAction() == MotionEvent.ACTION_UP)
                carMotion.updateLeft(false);
        }
        else if (view.getId()==buttonRight.getId()) {
            if (e.getAction() == MotionEvent.ACTION_DOWN)
                carMotion.updateRight(true);
            else if (e.getAction() == MotionEvent.ACTION_UP)
                carMotion.updateRight(false);
        }

        return false;
    }

}
