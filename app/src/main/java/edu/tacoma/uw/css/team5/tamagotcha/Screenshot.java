package edu.tacoma.uw.css.team5.tamagotcha;

import android.graphics.Bitmap;
import android.view.View;

public class Screenshot {

    public static Bitmap takescreenshot(View v){
        v.setDrawingCacheEnabled(true);
        v.buildDrawingCache();

        Bitmap b = Bitmap.createBitmap(v.getDrawingCache());
        v.setDrawingCacheEnabled(false);
        return b;

    }

    public static Bitmap takeScreenshotOfView(View v )
    {
        return takescreenshot(v.getRootView());
    }
}