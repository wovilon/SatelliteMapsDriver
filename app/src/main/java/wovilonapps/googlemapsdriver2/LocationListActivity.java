package wovilonapps.googlemapsdriver2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import wovilonapps.googlemapsdriver2.binders.ViewBinder;
import wovilonapps.googlemapsdriver2.model.CarModels;
import wovilonapps.googlemapsdriver2.model.LocationsBase;

public class LocationListActivity extends AppCompatActivity {
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_list);

        listView = (ListView)findViewById(R.id.LocationListView);

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
                returnResult(i);
            }
        });
    }

    void returnResult(int i){
        Intent resultIntent=new Intent();
        resultIntent.putExtra("type", "location");
        resultIntent.putExtra("locationNumber", i);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
