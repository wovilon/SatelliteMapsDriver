package wovilonapps.googlemapsdriver2.model;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import wovilonapps.googlemapsdriver2.google_libraries.SphericalUtil;


public class TwoPointsCarMotion extends AsyncTask {
    private GoogleMap mMap;
    private boolean running = true;
    Context context;
    Button car;

    private double m1 = 500;    // mass of front axis, kg
    private double m2 = 700;    // mass of back axis, kg
    private double Fp_max = 8000;    // force of car power, N   //TODO to high power, 800 is normal
    private double Fb_max = Fp_max * 1.5;

    private double Fp = 0;        // current force of car power, N
    private double Fr = 0;    // current force of resistance, N
    private double Fb = 0;     //current force of steering (brakes), N

    private double l = 3;       // car length (between axis), m
    private double a1 = 0;      // acceleration of m1
    private double alfa_a1 = 0; // angle betewwn acceleration of m1 and x axis
    private double v1 = 0;      // v1 of point m1
    private double alpha_v1 = 0; // angle between v1 of m1 and x axis

    private LatLng carLocation;
    private double alpha_car = 90;   // angle of car position, degrees
    private double dS = 0;      // car motion per time interval
    private double v_car = 0; //velocity of car (center of the car)
    private double alpha_wheels = 0; //angle of wheels rotation (relative to car)




    private int iterationTime = 20; //time iteration interval, ms
    private double dt = ((double)iterationTime)/1000; //time interval, s

    private double rotVelocity = 0;
    int acceleratePressedTime, brakePressedTime, rightPressedTime, leftPressedTime;
    int accelerateUnpressedTime, brakeUnpressedTime, rightUnpressedTime, leftUnpressedTime;
    private boolean acceleratePressed = false, brakesPressed = false, rightPressed = false, leftPressed = false;

    public TwoPointsCarMotion(Context context, GoogleMap googleMap, LatLng location, Button carButton) {
        mMap = googleMap;
        carLocation = location;
        this.context = context;
        car = carButton;
    }


    @Override
    protected Object doInBackground(Object[] objects) {
        while (running) {

            calculateAlphaWheels();
            calculateAcceleration();
            calculateVelocity();
            calculateDeltaPosition();

            carLocation = SphericalUtil.computeOffset(carLocation, dS, alpha_car);

            publishProgress();
            try {
                Thread.sleep(iterationTime);
            } catch (InterruptedException ie) {
                Log.d("MyLOG", "Interrupted exception in OnePointCarMotion");
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Object[] values) {
        super.onProgressUpdate(values);
        car.setRotation((float) alpha_car);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(carLocation));
        //carMarker.position(carLocation);
        //mMap.clear();
        //mMap.addMarker(carMarker);
    }

    public void updateAcceleration(boolean on) {
        if (on) {acceleratePressed = true; Fp = Fp_max;}
        else {acceleratePressed = false; Fp = 0;}
    }

    public void updateBrakes(boolean on) {
        if (on) {brakesPressed = true; Fb = Fb_max;}
        else {brakesPressed = false; Fb = 0;}
    }

    public void updateRight(boolean on) {
        if (on) rightPressed = true;
        else rightPressed = false;
    }

    public void updateLeft(boolean on) {
        if (on) leftPressed = true;
        else leftPressed = false;
    }


    private void calculateAlphaWheels(){
        if (rightPressed) alpha_wheels+=0.5;
        if (leftPressed) alpha_wheels-=0.5;
    }

    private void calculateAcceleration() {
        Fr = v1 * 1000;

        a1 = ( Fp - Fr - Fb ) / (m1+m2); //absolute value - m1*a1 - m2*a1*Math.cos(Math.toRadians(alpha_wheels))

        alfa_a1 = alpha_car + alpha_wheels;
    }

    private void calculateVelocity() {
        v1 += a1 * dt;
        alpha_v1 = alpha_car + alpha_wheels;
    }

    private void calculateDeltaPosition(){   // not location!
        //by third scheme (see adds)
        double x1_old = l/2 * Math.cos(Math.toRadians(alpha_car));
        double y1_old = l/2 * Math.sin(Math.toRadians(alpha_car));
        double x2_old = l/2 * Math.cos(Math.toRadians(alpha_car + 180));
        double y2_old = l/2 * Math.sin(Math.toRadians(alpha_car + 180));

        double x1_new = x1_old + v1*Math.cos(Math.toRadians(alpha_v1)) * dt;
        double y1_new = y1_old + v1*Math.sin(Math.toRadians(alpha_v1)) * dt;

        alpha_car = Math.toRadians(Math.atan(
                Math.abs(x1_new - x2_old / y1_new - y2_old)
        ));

        double xcar_new = x1_new - (l/2) * Math.cos(Math.toRadians(alpha_car)); // hypotenuse divite opposite catet
        double ycar_new = y1_new - (l/2) * Math.sin(Math.toRadians(alpha_car));

        dS = Math.sqrt(Math.pow(xcar_new, 2) + Math.pow(ycar_new, 2));


    }











    private void calculateRotVelocity(){
        if ((leftPressed & rightPressed) | (!leftPressed & !rightPressed)) {
            if (rotVelocity>0) rotVelocity -= 0.3 * v1; else rotVelocity += 0.3 * v1; //release of alpha_car
            if (Math.abs(rotVelocity) < 0.25 * v1) rotVelocity = 0; }


        else if (leftPressed) rotVelocity -= 0.2 * v1;
        else rotVelocity += 0.2 * v1;

        if (Math.abs(rotVelocity/ v1)>20) //min "radius" of alpha_car (in order that car not rotate too fast at low speeds)
            rotVelocity= v1 * 20 * Math.sin(rotVelocity);
        if (rotVelocity>0.9) rotVelocity = 0.9; // max alpha_car speed
        else if (rotVelocity<-0.9) rotVelocity = -0.9;  // max alpha_car speed
        if (Math.abs(v1)<1e-4) rotVelocity=0; //to avoid rotating of car at null v1
    }




}