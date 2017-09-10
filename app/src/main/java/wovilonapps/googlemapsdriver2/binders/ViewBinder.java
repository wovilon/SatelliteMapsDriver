package wovilonapps.googlemapsdriver2.binders;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

/**
 * Created by Администратор on 10.03.2017.
 */ //class to operate with bitmaps in SimpleAdapter
public class ViewBinder implements SimpleAdapter.ViewBinder {
    @Override
    public boolean setViewValue(View view, Object data,
                                String textRepresentation) {
        if ((view instanceof ImageView) & (data instanceof Bitmap)) {
            ImageView iv = (ImageView) view;
            Bitmap bm = (Bitmap) data;
            iv.setImageBitmap(bm);
            return true;
        }
        return false;
    }
}
