package wovilonapps.googlemapsdriver2.model;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import wovilonapps.googlemapsdriver2.google_libraries.SphericalUtil;


public class TwoPointsCarMotion extends AsyncTask {
    private GoogleMap mMap;
    private boolean running = true;
    Context context;
    ImageView carView;
    ImageView trailerView;
    Car car;

    // default values
    private double m1 = 500;    // mass of front axis, kg
    private double m2 = 700;    // mass of back axis, kg
    private double m3 = 2000;   // mass of trailerView, kg
    private double Fp_max = 5000;    // force of carView power, N   //TODO to high power, 800 is normal
    private double Fb_max = Fp_max * 1.5;

    private double l = 3;       // carView length (between axis), m
    private double lt = 7;      // trailerView length, m

    private double alpha_wheels_max = 45; //maximum angle of wheels rotation
    private double wheel_speed = 30; // speed of wheel rotation, degrees/s
    private double wheel_speed_release = 40; // speed of wheel release, degrees/s


    private double Fp = 0;        // current force of carView power, N
    private double Fr = 0;    // current force of resistance, N
    private double Fb = 0;     //current force of steering (brakes), N

    private double a1 = 0;      // acceleration of m1
    private double alfa_a1 = 0; // angle betewwn acceleration of m1 and x axis
    private double v1 = 0;      // v1 of point m1
    private double alpha_v1 = 0; // angle between v1 of m1 and x axis

    private LatLng carLocation;
    private double alpha_car = 0;   // angle of carView position, degrees
    private double alpha_trailer = 45; // angle of trailerView position, degrees
    private double dS_car = 0;      // carView motion per time interval (middle of the carView)
    private double alpha_wheels = 0; //angle of wheels rotation (relative to carView)

    private int iterationTime = 20; //time iteration interval, ms
    private double dt = ((double)iterationTime)/1000; //time interval, s

    private boolean acceleratePressed = false, brakesPressed = false, rightPressed = false, leftPressed = false;

    public TwoPointsCarMotion(Context context, GoogleMap googleMap, LatLng location, ImageView carView,
                              ImageView trailerView, Car car) {
        mMap = googleMap;
        carLocation = location;
        this.context = context;
        this.carView = carView;
        this.trailerView = trailerView;
        if (lt == 0) trailerView.setVisibility(View.INVISIBLE);

        this.car = car;
        this.m1 = car.m1;
        this.m2 = car.m2;
        this.m3 = car.m3;
        this.Fp_max = car.Fp_max;
        this.Fb_max = car.Fb_max;
        this.l = car.l;
        this.lt = car.lt;
        this.alpha_wheels_max = car.alpha_wheels_max;
        this.wheel_speed = car.wheel_speed;
        this.wheel_speed_release = car.wheel_speed_release;

        this.carView.setImageBitmap(car.carBitmap);
        this.trailerView.setImageBitmap(car.trailerBitmap);
    }


    @Override
    protected Object doInBackground(Object[] objects) {
        while (running) {

            calculateAlphaWheels();
            calculateAcceleration();
            calculateVelocity();
            calculateDeltaPosition();

            carLocation = SphericalUtil.computeOffset(carLocation, dS_car, alpha_car);

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
        carView.setRotation((float) alpha_car);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(carLocation));
        trailerView.setRotation((float)alpha_trailer);

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
        double max = Math.abs(alpha_wheels_max) - (0.9 * v1); //TODO fictive growth steering radius with speed up
        if (Math.abs(alpha_wheels) >= max) alpha_wheels = max * Math.signum(alpha_wheels);

        if (rightPressed) alpha_wheels += wheel_speed * dt;
        if (leftPressed) alpha_wheels -= wheel_speed * dt;

        if (!rightPressed & !leftPressed)
            alpha_wheels -= Math.signum(alpha_wheels) * wheel_speed_release * dt;
    }

    private void calculateAcceleration() {
        Fr = v1 * 150;
        if (v1 < 0.1) Fb = 0;

        a1 = ( Fp - Fr - Fb ) / (m1+m2+m3); //absolute value - m1*a1 - m2*a1*Math.cos(Math.toRadians(alpha_wheels))

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

        alpha_car = Math.toDegrees(Math.atan2( (y1_new - y2_old), (x1_new - x2_old) ));

        double xcar_new = x1_new - (l/2) * Math.cos(Math.toRadians(alpha_car)); // hypotenuse divite opposite catet
        double ycar_new = y1_new - (l/2) * Math.sin(Math.toRadians(alpha_car));

        dS_car = Math.sqrt(Math.pow(xcar_new, 2) + Math.pow(ycar_new, 2));

        double x2_new = x1_new - (l) * Math.cos(Math.toRadians(alpha_car)); // like xcar_new equation
        double y2_new = y1_new - (l) * Math.sin(Math.toRadians(alpha_car));
        double dx_m2 = x2_new - x2_old;
        double dy_m2 = y2_new - y2_old;


        if (lt != 0){ //if trailerView exists
            // new system of coordinates (trailerView)
            x2_old = lt/2 * Math.cos(Math.toRadians(alpha_trailer));
            y2_old = lt/2 * Math.sin(Math.toRadians(alpha_trailer));
            double x3_old = lt/2 * Math.cos(Math.toRadians(alpha_trailer + 180));
            double y3_old = lt/2 * Math.sin(Math.toRadians(alpha_trailer + 180));

            x2_new = x2_old + dx_m2;    //to new coordinates: coordinate + dx
            y2_new = y2_old + dy_m2;

            alpha_trailer = Math.toDegrees(Math.atan2( (y2_new - y3_old), (x2_new - x3_old) ));

            double x3_new = x2_new - lt * Math.cos(Math.toRadians(alpha_trailer)); // hypotenuse divite opposite catet
            double y3_new = y2_new - lt * Math.sin(Math.toRadians(alpha_trailer));


        }


    }



}