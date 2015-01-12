package com.puissance4.controller.sensor_controllers;

import android.widget.Toast;
import com.puissance4.view.activities.GameActivity;

/**
 * Created by fred on 12/01/15.
 */
public class ShakeListener {
    // Minimum time (in milliseconds) between 2 skakes
    private static final int MIN_DURATION_BETWEEN_SHAKES = 2000;

    private GameActivity context;
    private long startShakingTime;

    public ShakeListener(GameActivity context) {
        this.context = context;
        startShakingTime = 0;
    }

    public void onShake(){
        if(startShakingTime == 0) {
            startShakingTime = System.currentTimeMillis();
            Toast.makeText(context, "SHUFFLE !", Toast.LENGTH_SHORT).show();
        }
        else {
            if(System.currentTimeMillis() - startShakingTime > MIN_DURATION_BETWEEN_SHAKES) {
                startShakingTime = 0;
                onShake();
            }
        }
    }
}
