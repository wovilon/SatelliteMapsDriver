package wovilonapps.googlemapsdriver2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.model.MarkerOptions;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    public void onBtNewGameClick(View view) {
        Toast toast = Toast.makeText(getApplicationContext(),
                "CAREER IS NOT AVAILABLE IN ALPHA VERSION. TRY FREE GAME!!", Toast.LENGTH_LONG);
        toast.show();
    }

    public void onBtFreeGameClick(View view) {
        Intent intent = new Intent(this, FreeGameSettingsActivity.class);
        startActivity(intent);
    }
}
