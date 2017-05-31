package com.jhzy.nursinghandover.common;

import android.app.Application;
import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by nakisaRen
 * on 17/2/20.
 */

public class App extends Application {

    @Override public void onCreate() {
        Fresco.initialize(getApplicationContext());
    }
}
