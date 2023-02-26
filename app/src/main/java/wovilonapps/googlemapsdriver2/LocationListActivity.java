package wovilonapps.googlemapsdriver2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import wovilonapps.googlemapsdriver2.binders.ViewBinder;
import wovilonapps.googlemapsdriver2.model.CarModels;
import wovilonapps.googlemapsdriver2.model.GameLocation;
import wovilonapps.googlemapsdriver2.model.LocationsBase;

public class LocationListActivity extends AppCompatActivity {
    ListView listView;
    EditText etCustomLocation;
    Button btOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_list);

        listView = (ListView)findViewById(R.id.LocationListView);
        etCustomLocation = (EditText)findViewById(R.id.etCustomLocation);
        btOk = (Button) findViewById(R.id.btSetLocation);

        String caption = "Caption";
        //String latlng = "LatLng";
        ArrayList<Map<String,Object>> data = new ArrayList<>();
        Map<String, Object> m;
        LocationsBase locationsBase = new LocationsBase();

        for (int i=0; i<locationsBase.locations.size(); i++){
            m = new HashMap<>();
            m.put(caption, locationsBase.locations.get(i).caption);
            //m.put(latlng, locationsBase.locations.get(i).latLng);
            data.add(m);
        }

        String[] from = {caption};
        int[] to = {R.id.itemLocationCaption};

        final SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.locations_list_item, from, to);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                returnResult(i, 0,0);
            }
        });

        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etCustomLocation.getText().length() > 0){
                    String text = etCustomLocation.getText().toString();
                    int divider = 0;
                    if (text.contains(", ")){
                        divider = text.indexOf(", ");
                    }
                    else if (text.contains(". ")){
                        divider = text.indexOf(". ");
                    }

                    if (divider != 0){
                        double lat = Double.parseDouble(text.substring(0, divider-1).replaceAll(",","."));
                        double lng = Double.parseDouble(text.substring(divider+2, text.length()-1).replaceAll(",","."));
                        returnResult(-1, lat, lng);
                    }

                }
            }
        });

    }

    void returnResult(int i, double lat, double lng){
        Intent resultIntent=new Intent();
        resultIntent.putExtra("type", "location");
        resultIntent.putExtra("locationNumber", i);
        resultIntent.putExtra("lat", lat);
        resultIntent.putExtra("lng", lng);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

}
