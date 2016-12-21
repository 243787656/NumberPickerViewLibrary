package com.okunev.numberpickerview;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

/**
 * Project stolotoapp. Created by gwa on 9/15/16.
 */

class Helpers {

    static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    static boolean isPointInsideView(float x, float y, View view) {
        float viewX = view.getX();
        float viewY = view.getY();
        //point is inside view bounds
        return (x > viewX && x < (viewX + view.getWidth())) &&
                (y > viewY && y < (viewY + view.getHeight()));
    }

    static int getWidthOfTextInPx(Context context, String text) {
        Paint paint = new Paint();
        paint.setTextSize(16);
        paint.setTypeface(Typeface.DEFAULT);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        Rect result = new Rect();
        paint.getTextBounds(text, 0, text.length(), result);
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        float logicalDensity = metrics.density;
        return (int) Math.ceil(result.width() * logicalDensity);
    }

}
