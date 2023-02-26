package wovilonapps.googlemapsdriver2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import wovilonapps.googlemapsdriver2.model.Car;
import wovilonapps.googlemapsdriver2.model.CarModels;
import wovilonapps.googlemapsdriver2.model.GameLocation;
import wovilonapps.googlemapsdriver2.model.GameManager;
import wovilonapps.googlemapsdriver2.model.LocationsBase;

public class FreeGameSettingsActivity extends AppCompatActivity {
    ImageView imageViewCar;
    ImageView imageViewTrailer;
    TextView location;
    GameManager gameManager;
    LocationsBase locationsBase = new LocationsBase();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_game_settings);

        imageViewCar = (ImageView)findViewById(R.id.imageViewCar);
        imageViewTrailer = (ImageView)findViewById(R.id.imageViewTrailer);
        location = (TextView)findViewById(R.id.textLocation);

        gameManager = new GameManager();
        gameManager.setCarNumber(3);
        gameManager.setLat(locationsBase.locations.get(0).latLng.latitude);
        gameManager.setLng(locationsBase.locations.get(0).latLng.longitude);

    }

    public void onBtSelectCarClick(View view) {
        Intent intent = new Intent(this, CarsListActivity.class);
        startActivityForResult(intent, 0);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            //if it is result from CarsListActivity
            if (data.getStringExtra("type").equals("car")) {
                int carNumber = data.getIntExtra("carNumber", 0);
                Car car = new CarModels(this).getAllCars().get(carNumber);
                gameManager.setCarNumber(carNumber);
                imageViewCar.setImageBitmap(car.carBitmap);
                if (car.trailerBitmap != null) {
                    imageViewTrailer.setImageBitmap(car.trailerBitmap);
                    imageViewTrailer.setVisibility(View.VISIBLE);
                } else imageViewTrailer.setVisibility(View.INVISIBLE);
            }

            //if it is result from LocationsListActivity
            else if (data.getStringExtra("type").equals("location")) {
                int locationNumber = data.getIntExtra("locationNumber", -2);
                if (locationNumber == -1){
                    LocationsBase base = new LocationsBase();
                    gameManager.setLat(data.getDoubleExtra("lat", 0));
                    gameManager.setLng(data.getDoubleExtra("lng", 0));
                    location.setText(R.string.Custom);
                }
                else if (locationNumber >= 0) {
                    GameLocation gameLocation = new LocationsBase().locations.get(locationNumber);
                    LocationsBase base = new LocationsBase();
                    gameManager.setLat(base.locations.get(locationNumber).latLng.latitude);
                    gameManager.setLng(base.locations.get(locationNumber).latLng.longitude);
                    location.setText(gameLocation.caption);
                }
            }
        }
    }

    public void onButtonSelectLocationClick(View view) {
        Intent intent = new Intent(this, LocationListActivity.class);
        startActivityForResult(intent, 0);
    }

    public void onButtonPlayFreeGameClick(View view) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("gameManagerArray", gameManager);

        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);

    }
}
