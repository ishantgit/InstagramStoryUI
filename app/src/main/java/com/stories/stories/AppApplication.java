package com.stories.stories;

import android.app.Application;
import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

public class AppApplication extends Application {

    private static int screen_height,screen_width;

    @Override
    public void onCreate() {
        super.onCreate();
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screen_height = size.y;
        screen_width = size.x;
    }

    public static int getScreen_width() {
        return screen_width;
    }

    public static int getScreen_height() {
        return screen_height;
    }
}
