package wovilonapps.googlemapsdriver2.model;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import wovilonapps.googlemapsdriver2.R;

public class Car {
    Context context;
    public final double m1 = 500;    // mass of front axis, kg
    public final double m2 = 700;    // mass of back axis, kg
    public double m3 = 5000;   // mass of trailerView, kg
    public double Fp_max = 5000;    // force of carView power, N   //TODO to high power, 800 is normal
    public final double Fb_max = Fp_max * 1.5;

    public final double l = 3;       // carView length (between axis), m
    public final double lt = 7;      // trailerView length, m

    public final double alpha_wheels_max = 45; //maximum angle of wheels rotation
    public final double wheel_speed = 30; // speed of wheel rotation, degrees/s
    public final double wheel_speed_release = 40; // speed of wheel release, degrees/s
    public final Bitmap carBitmap;
    public final Bitmap trailerBitmap;

    public Car(Context context){
        this.context = context;
        carBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.tractor);
        trailerBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.trailer);
    }
}
