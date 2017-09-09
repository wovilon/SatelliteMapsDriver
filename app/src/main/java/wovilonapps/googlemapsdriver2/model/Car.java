package wovilonapps.googlemapsdriver2.model;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import wovilonapps.googlemapsdriver2.R;

public class Car {
    Context context;
    public final String model;
    public final double m1;    // mass of front axis, kg
    public final double m2;    // mass of back axis, kg
    public double m3;   // mass of trailerView, kg
    public double Fp_max;    // force of carView power, N   //TODO to high power, 800 is normal
    public final double Fb_max;

    public final double l;       // carView length (between axis), m
    public final double lt;      // trailerView length, m

    public final double alpha_wheels_max; //maximum angle of wheels rotation
    public final double wheel_speed; // speed of wheel rotation, degrees/s
    public final double wheel_speed_release; // speed of wheel release, degrees/s
    public final Bitmap carBitmap;
    public final Bitmap trailerBitmap;

    public Car(Context context,String model, double m1, double m2, double m3,
               double Fp_max, double Fb_max, double l, double lt,
               double alpha_wheels_max, double wheel_speed, double wheel_speed_release, Bitmap carBitmap, Bitmap trailerBitmap){
        this.context = context;
        this.model = model;

        this.m1 = m1;
        this.m2 = m2;
        this.m3 = m3;
        this.Fp_max = Fp_max;
        this.Fb_max = Fb_max;
        this.l = l;
        this.lt = lt;
        this.alpha_wheels_max = alpha_wheels_max;
        this.wheel_speed = wheel_speed;
        this.wheel_speed_release = wheel_speed_release;
        this.carBitmap = carBitmap;
        this.trailerBitmap = trailerBitmap;

    }
}
