package wovilonapps.googlemapsdriver2.model;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import wovilonapps.googlemapsdriver2.google_libraries.SphericalUtil;


public class TwoPointsCarMotion extends AsyncTask {
    private GoogleMap mMap;
    private boolean running = true;
    Context context;
    ImageView car;
    ImageView trailer;

    private double m1 = 500;    // mass of front axis, kg
    private double m2 = 700;    // mass of back axis, kg
    private double m3 = 2000;   // mass of trailer, kg
    private double Fp_max = 5000;    // force of car power, N   //TODO to high power, 800 is normal
    private double Fb_max = Fp_max * 1.5;

    private double Fp = 0;        // current force of car power, N
    private double Fr = 0;    // current force of resistance, N
    private double Fb = 0;     //current force of steering (brakes), N


    private double l = 3;       // car length (between axis), m
    private double lt = 7;      // trailer length, m
    private double a1 = 0;      // acceleration of m1
    private double alfa_a1 = 0; // angle betewwn acceleration of m1 and x axis
    private double v1 = 0;      // v1 of point m1
    private double alpha_v1 = 0; // angle between v1 of m1 and x axis

    private LatLng carLocation;
    private DoublePoint trailerPosition = new DoublePoint(-lt/2, 0); // position of trailer relative to car(tractor)
    private double alpha_car = 0;   // angle of car position, degrees
    private double alpha_trailer = 45; // angle of trailer position, degrees
    private double dS_car = 0;      // car motion per time interval (middle of the car)
    private double dS_trailer = 0; // trailer motion per time interval (middle of the car)
    private double dx_m3 = 0; //trailer dx (point m3 = center)
    private double dy_m3 = 0; //trailer dy (point m3 = center)
    private double v_car = 0; //velocity of car (center of the car)
    private double alpha_wheels = 0; //angle of wheels rotation (relative to car)
    private double alpha_wheels_max = 45; //maximum angle of wheels rotation
    private double wheel_speed = 40; // speed of wheel rotation, degrees/s
    private double wheel_speed_release = 60; // speed of wheel release, degrees/s


    private int iterationTime = 20; //time iteration interval, ms
    private double dt = ((double)iterationTime)/1000; //time interval, s

    private double rotVelocity = 0;
    int acceleratePressedTime, brakePressedTime, rightPressedTime, leftPressedTime;
    int accelerateUnpressedTime, brakeUnpressedTime, rightUnpressedTime, leftUnpressedTime;
    private boolean acceleratePressed = false, brakesPressed = false, rightPressed = false, leftPressed = false;

    public TwoPointsCarMotion(Context context, GoogleMap googleMap, LatLng location, ImageView carImg, ImageView trailerImg) {
        mMap = googleMap;
        carLocation = location;
        this.context = context;
        car = carImg;
        trailer = trailerImg;
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
        car.setRotation((float) alpha_car);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(carLocation));
        trailer.setRotation((float)alpha_trailer);

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
        double max = Math.abs(alpha_wheels_max) - (1.2 * v1); //TODO fictive growth steering radius with speed up
        if (Math.abs(alpha_wheels) <= alpha_wheels_max) {
            if (rightPressed & alpha_wheels<max) alpha_wheels += wheel_speed * dt;
            if (leftPressed & alpha_wheels>-max) alpha_wheels -= wheel_speed * dt;
        }
        if (!rightPressed & !leftPressed)
            alpha_wheels += Math.signum(alpha_wheels) * (-1) * wheel_speed_release * dt;
    }

    private void calculateAcceleration() {
        Fr = v1 * 150;
        if (v1 < 0.1) Fb = 0;

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

        alpha_car = Math.toDegrees(Math.atan2( (y1_new - y2_old), (x1_new - x2_old) ));

        double xcar_new = x1_new - (l/2) * Math.cos(Math.toRadians(alpha_car)); // hypotenuse divite opposite catet
        double ycar_new = y1_new - (l/2) * Math.sin(Math.toRadians(alpha_car));

        dS_car = Math.sqrt(Math.pow(xcar_new, 2) + Math.pow(ycar_new, 2));

        double x2_new = x1_new - (l) * Math.cos(Math.toRadians(alpha_car)); // like xcar_new equation
        double y2_new = y1_new - (l) * Math.sin(Math.toRadians(alpha_car));
        double dx_m2 = x2_new - x2_old;
        double dy_m2 = y2_new - y2_old;


        if (lt != 0){ //if trailer exists
            // new system of coordinates (trailer)
            x2_old = lt/2 * Math.cos(Math.toRadians(alpha_trailer));
            y2_old = lt/2 * Math.sin(Math.toRadians(alpha_trailer));
            double x3_old = lt/2 * Math.cos(Math.toRadians(alpha_trailer + 180));
            double y3_old = lt/2 * Math.sin(Math.toRadians(alpha_trailer + 180));

            x2_new = x2_old + dx_m2;    //to new coordinates: coordinate + dx
            y2_new = y2_old + dy_m2;

            alpha_trailer = Math.toDegrees(Math.atan2( (y2_new - y3_old), (x2_new - x3_old) ));

            double x3_new = x2_new - lt * Math.cos(Math.toRadians(alpha_trailer)); // hypotenuse divite opposite catet
            double y3_new = y2_new - lt * Math.sin(Math.toRadians(alpha_trailer));

            dS_trailer = Math.sqrt(Math.pow(x3_new, 2) + Math.pow(y3_new, 2));

            dx_m3 = x3_new - x3_old;
            dy_m3 = y3_new - y3_old;

    }


    }












        // try to add shfting, drifting
       /* private void calculateAlphaWheels(){
            //double max = Math.abs(alpha_wheels_max) - (1.2 * v1); //TODO fictive growth steering radius with speed up
            if (Math.abs(alpha_wheels) <= alpha_wheels_max) {
                if (rightPressed ) alpha_wheels += wheel_speed * dt;
                if (leftPressed ) alpha_wheels -= wheel_speed * dt;
            }
            if (!rightPressed & !leftPressed) {
                alpha_wheels += Math.signum(alpha_wheels) * (-1) * wheel_speed_release * dt;
                if (Math.abs(alpha_wheels) < 1) alpha_wheels = 0;
            }
        }

        private void calculateAcceleration() {
            Fr = v1 * 500;
            double a_side = dS_car*Math.sin(Math.toRadians(alpha_wheels))/Math.pow(dt,2);

            if (Math.abs((m1) * a_side) > Ffr1_limit) Fside = m1 * a_side - Ffr1_limit; // if over friction limit
            else Fside = 0;

            a1_x1 = ( Fp - Fr - Fb ) / (m1+m2);  // acceleration projection on axis x1
            a1_y1 = Fside / m1;      // acceleration projection on axis y1
            a1 = Math.sqrt(Math.pow(a1_x1,2)
                    + Math.pow(a1_y1, 2)); //absolute value

            alfa_a1 = alpha_car +  Math.toDegrees(Math.atan2(a1_y1, a1_x1));
        }

        private void calculateVelocity() {
        *//*double v1_old = v1;
        v1 = Math.sqrt(v1*v1 + (a1 * dt)*(a1 * dt) - 2*v1*(a1 * dt)
                * Math.cos(Math.toRadians(alpha_v1) + Math.PI - Math.toRadians(alfa_a1))); //theorem of cosinuses
        //alpha_v1 = (alpha_car + alpha_wheels) - a1_y1 * dt;
        alpha_v1 = Math.toDegrees(Math.asin(Math.sin(Math.toRadians(alpha_v1)
                + Math.PI - Math.toRadians(alfa_a1)) * (a1 * dt) / v1_old)) - alpha_v1;
        alfa_a1 = alfa_a1;*//*

            double v1x = v1*Math.cos(Math.toRadians(alpha_v1)) + (a1*dt)*Math.cos(Math.toRadians(alfa_a1));
            double v1y = v1*Math.sin(Math.toRadians(alpha_v1)) + (a1*dt)*Math.sin(Math.toRadians(alfa_a1));
            v1 = Math.sqrt(v1x*v1x + v1y*v1y);
            alpha_v1 = Math.toDegrees(Math.atan2(v1y, v1x));
        }
    *//*    z^2 = (x^2) + (y^2) - 2*x*y*cos(a+pi-b);  -- длина (по теореме косинусов)
    c = arcsin( sin(a+pi-b) * y/z ) - a;  -- угол (по теореме синусов)    *//*

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


        }*/






















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