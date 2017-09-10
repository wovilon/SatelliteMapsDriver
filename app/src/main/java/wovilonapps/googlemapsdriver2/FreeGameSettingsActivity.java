package wovilonapps.googlemapsdriver2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import wovilonapps.googlemapsdriver2.model.CarModels;
import wovilonapps.googlemapsdriver2.model.GameManager;

public class FreeGameSettingsActivity extends AppCompatActivity {
    ImageView imageViewCar;
    ImageView imageViewTrailer;
    GameManager gameManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_game_settings);

        imageViewCar = (ImageView)findViewById(R.id.imageViewCar);
        imageViewTrailer = (ImageView)findViewById(R.id.imageViewTrailer);
        gameManager = new GameManager();
    }

    public void onBtSelectCarClick(View view) {
        Intent intent = new Intent(this, CarsListActivity.class);
        startActivityForResult(intent, 0);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int carNumber = data.getIntExtra("carNumber", 0);
        gameManager.setCar(new CarModels(this).getAllCars().get(carNumber));
        imageViewCar.setImageBitmap(gameManager.getCar().carBitmap);
        if (gameManager.getCar().trailerBitmap != null) {
            imageViewTrailer.setImageBitmap(gameManager.getCar().trailerBitmap);
            imageViewTrailer.setVisibility(View.VISIBLE);
        }
        else imageViewTrailer.setVisibility(View.INVISIBLE);
    }
}
