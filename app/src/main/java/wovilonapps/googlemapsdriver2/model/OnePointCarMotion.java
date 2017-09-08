package wovilonapps.googlemapsdriver2.model;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import wovilonapps.googlemapsdriver2.google_libraries.SphericalUtil;


public class OnePointCarMotion extends AsyncTask {
    private GoogleMap mMap;
    private boolean running = true;
    Context context;
    Button car;


    private double acceleration = 0;
    private double velocity = 0;
    private LatLng carLocation;
    private double rotation = 90;
    private double rotVelocity = 0;

    private int dt = 20; //time iteration time, ms
    int acceleratePressedTime, brakePressedTime, rightPressedTime, leftPressedTime;
    int accelerateUnpressedTime, brakeUnpressedTime, rightUnpressedTime, leftUnpressedTime;
    private boolean acceleratePressed = false, brakesPressed = false, rightPressed = false, leftPressed = false;

    public OnePointCarMotion(Context context, GoogleMap googleMap, LatLng position, Button carButton) {
        mMap = googleMap;
        carLocation = position;
        this.context = context;
        car = carButton;
    }


    @Override
    protected Object doInBackground(Object[] objects) {
        while (running) {

            calculateVelocity();
            calculateRotVelocity();
            rotation += rotVelocity;
            /*carLocation = new LatLng(carLocation.latitude + v1 * Math.sin(Math.toRadians(rotation)),
                    carLocation.longitude + v1 * Math.cos(Math.toRadians(rotation)));*/
            carLocation = SphericalUtil.computeOffset(carLocation, velocity, rotation);

            publishProgress();
            try {
                Thread.sleep(dt);
            } catch (InterruptedException ie) {
                Log.d("MyLOG", "Interrupted exception in OnePointCarMotion");
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Object[] values) {
        super.onProgressUpdate(values);
        car.setRotation((float)rotation);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(carLocation));
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
        if (acceleratePressed) velocity += 2e-3;     // 0.0000001
        else if (brakesPressed) {if(velocity>0) velocity -= 6e-3;}
        else {if(velocity>0) velocity -= 2e-4; else if (velocity>0) velocity += 2e-4;}

        if (Math.abs(velocity)<1e-4) velocity=0;
    }

    private void calculateRotVelocity(){
        if ((leftPressed & rightPressed) | (!leftPressed & !rightPressed)) {
            if (rotVelocity>0) rotVelocity -= 0.3 * velocity; else rotVelocity += 0.3 * velocity; //release of rotation
            if (Math.abs(rotVelocity) < 0.25 * velocity) rotVelocity = 0; }


        else if (leftPressed) rotVelocity -= 0.2 * velocity;
        else rotVelocity += 0.2 * velocity;

        if (Math.abs(rotVelocity/velocity)>20) //min "radius" of rotation (in order that carView not rotate too fast at low speeds)
            rotVelocity=velocity * 20 * Math.sin(rotVelocity);
        if (rotVelocity>0.9) rotVelocity = 0.9; // max rotation speed
        else if (rotVelocity<-0.9) rotVelocity = -0.9;  // max rotation speed
        if (Math.abs(velocity)<1e-4) rotVelocity=0; //to avoid rotating of carView at null v1
    }




}