package wovilonapps.googlemapsdriver2.Model;


import android.graphics.Point;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class CarMotion extends AsyncTask {
    private GoogleMap mMap;
    private boolean running = true;
    private LatLng carLocation;
    private DoublePoint velocity = new DoublePoint(0.000001, 0.000001);

    public CarMotion(GoogleMap googleMap, LatLng position) {
        mMap = googleMap;
        carLocation = position;
    }



    @Override
    protected Object doInBackground(Object[] objects) {
        while (running){

            carLocation = new LatLng(carLocation.latitude + velocity.x, carLocation.longitude + velocity.y);



            publishProgress();
            try {
                Thread.sleep(20);
            }catch (InterruptedException ie){Log.d("MyLOG", "Interrupted exception in CarMotion");}
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Object[] values) {
        super.onProgressUpdate(values);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(
                carLocation));
        //carMarker.position(carLocation);
        //mMap.clear();
        //mMap.addMarker(carMarker);
    }

    public void setVelocity(){
        new DoublePoint(0.000001, 0.000001);
    }
}
