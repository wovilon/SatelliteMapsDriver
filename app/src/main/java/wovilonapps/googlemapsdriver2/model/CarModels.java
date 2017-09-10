package wovilonapps.googlemapsdriver2.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;

import wovilonapps.googlemapsdriver2.R;


public class CarModels {
    Context context;

    public CarModels(Context context){
        this.context = context;
    }

    public ArrayList<Car> getAllCars(){
        ArrayList<Car> cars = new ArrayList<>();
        cars.add(getCityCar());
        cars.add(getSportCar());
        cars.add(getWeakTruck());
        cars.add(getPowerfulTruck());
        return cars;
    }

    public Car getCityCar(){
        String model = "City car";
        double m1 = 500;
        double m2 = 700;
        double m3 = 0;
        double Fp_max = 5000;
        double Fb_max = 7500;
        double l = 3;
        double lt = 0;
        double alpha_wheels_max = 45;
        double wheel_speed = 30;
        double wheel_speed_release = wheel_speed * 1.3;
        Bitmap carBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.city_car);
        Bitmap trailerBitmap = null;

        return new Car(context,model, m1, m2, m3, Fp_max, Fb_max, l, lt, alpha_wheels_max,
                wheel_speed,wheel_speed_release, carBitmap, trailerBitmap);
    }


    public Car getSportCar(){
        String model = "Sport car";
        double m1 = 300;
        double m2 = 400;
        double m3 = 0;
        double Fp_max = 7000;
        double Fb_max = 14000;
        double l = 3;
        double lt = 0;
        double alpha_wheels_max = 45;
        double wheel_speed = 50;
        double wheel_speed_release = wheel_speed * 1.2;
        Bitmap carBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.sport_car);
        Bitmap trailerBitmap = null;

        return new Car(context,model, m1, m2, m3, Fp_max, Fb_max, l, lt, alpha_wheels_max,
                wheel_speed,wheel_speed_release, carBitmap, trailerBitmap);
    }


    public Car getWeakTruck(){
        String model = "Weak truck";
        double m1 = 5000;
        double m2 = 2000;
        double m3 = 7000 + 15000;
        double Fp_max = 15000;
        double Fb_max = 80000;
        double l = 3;
        double lt = 7;
        double alpha_wheels_max = 45;
        double wheel_speed = 25;
        double wheel_speed_release = wheel_speed * 0.9;
        Bitmap carBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.tractor_weak);
        Bitmap trailerBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.trailer);

        return new Car(context,model, m1, m2, m3, Fp_max, Fb_max, l, lt, alpha_wheels_max,
                wheel_speed,wheel_speed_release, carBitmap, trailerBitmap);
    }


    public Car getPowerfulTruck(){
        String model = "Powerful truck";
        double m1 = 5000;
        double m2 = 2000;
        double m3 = 7000 + 15000;
        double Fp_max = 30000;
        double Fb_max = 100000;
        double l = 3;
        double lt = 7;
        double alpha_wheels_max = 45;
        double wheel_speed = 25;
        double wheel_speed_release = wheel_speed * 0.9;
        Bitmap carBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.tractor_powerful);
        Bitmap trailerBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.trailer);

        return new Car(context,model, m1, m2, m3, Fp_max, Fb_max, l, lt, alpha_wheels_max,
                wheel_speed,wheel_speed_release, carBitmap, trailerBitmap);
    }
}

