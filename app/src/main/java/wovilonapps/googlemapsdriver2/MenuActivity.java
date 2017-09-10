package wovilonapps.googlemapsdriver2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.model.MarkerOptions;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    public void onBtNewGameCkick(View view) {

    }

    public void onBtFreeGameClick(View view) {
        Intent intent = new Intent(this, FreeGameSettingsActivity.class);
        startActivity(intent);
    }
}
