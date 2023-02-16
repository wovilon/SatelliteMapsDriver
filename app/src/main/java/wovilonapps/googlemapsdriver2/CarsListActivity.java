package wovilonapps.googlemapsdriver2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import wovilonapps.googlemapsdriver2.binders.ViewBinder;
import wovilonapps.googlemapsdriver2.model.Car;
import wovilonapps.googlemapsdriver2.model.CarModels;

public class CarsListActivity extends AppCompatActivity {
ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cars_list);

        listView = (ListView)findViewById(R.id.CarsListView);

        String caption = "Caption";
        String image = "Image";
        ArrayList<Map<String,Object>> data = new ArrayList<>();
        Map<String, Object> m;
        CarModels carModels = new CarModels(this);

        for (int i=0; i<carModels.getAllCars().size(); i++){
            m = new HashMap<>();
            m.put(caption, carModels.getAllCars().get(i).model);
            m.put(image, carModels.getAllCars().get(i).carBitmap);
            data.add(m);
        }

        String[] from = {caption, image};
        int[] to = {R.id.itemCaption, R.id.itemImage};

        final SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.cars_list_item, from, to);
        adapter.setViewBinder(new ViewBinder());
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
        resultIntent.putExtra("type", "car");
        resultIntent.putExtra("carNumber",i);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
