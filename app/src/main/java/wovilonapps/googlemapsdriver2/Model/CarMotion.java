package wovilonapps.googlemapsdriver2.Model;


import android.content.Context;
import android.graphics.Point;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import wovilonapps.googlemapsdriver2.R;


public class CarMotion extends AsyncTask {
    private GoogleMap mMap;
    private boolean running = true;
    Context context;
    Button car;

    private double acceleration = 0;
    private double velocity = 0;
    private LatLng carLocation;
    private double rotation = 90;

    int iterationTime = 20;
    int acceleratePressedTime, brakePressedTime, rightPressedTime, leftPressedTime;
    int accelerateUnpressedTime, brakeUnpressedTime, rightUnpressedTime, leftUnpressedTime;
    boolean acceleratePressed = false, brakesPressed = false, rightPressed = false, leftPressed = false;

    public CarMotion(Context context, GoogleMap googleMap, LatLng position, Button carButton) {
        mMap = googleMap;
        carLocation = position;
        this.context = context;
        car = carButton;
    }


    @Override
    protected Object doInBackground(Object[] objects) {
        while (running) {

            calculateVelocity();
            calculateRotation();

            carLocation = new LatLng(carLocation.latitude + velocity * Math.sin(Math.toRadians(rotation)),
                    carLocation.longitude + velocity * Math.cos(Math.toRadians(rotation)));


            publishProgress();
            try {
                Thread.sleep(iterationTime);
            } catch (InterruptedException ie) {
                Log.d("MyLOG", "Interrupted exception in CarMotion");
            }
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

    public void updateAcceleration(boolean on) {
        if (on) acceleratePressed = true;
        else acceleratePressed = false;
    }

    public void updateBrakes(boolean on) {
        if (on) brakesPressed = true;
        else brakesPressed = false;
    }

    public void updateRight(boolean on) {
        if (on) rightPressed = true;
        else rightPressed = false;
    }

    public void updateLeft(boolean on) {
        if (on) leftPressed = true;
        else leftPressed = false;
    }


    private void calculateVelocity() {
        if (acceleratePressed) velocity += 1e-7;     // 0.0000001
        else if (brakesPressed) {if(velocity>0) velocity -= 1e-6;}
        else {if(velocity>0) velocity -= 2e-8;}

    }

    private void calculateRotation(){
        if ((leftPressed & rightPressed) | (!leftPressed & !rightPressed)) rotation+=0;
        else if (leftPressed) rotation += 1;
        else rotation -= 1;
    }




}