package com.puissance4.controller.sensor_controllers;

import android.widget.Toast;

import com.example.puissance4.R;
import com.puissance4.configuration.SensorConfiguration;
import com.puissance4.view.activities.GameActivity;

/**
 * Created by fred on 12/01/15.
 */
public class ShakeListener {
    private GameActivity context;
    private long startShakingTime;

    public ShakeListener(GameActivity context) {
        this.context = context;
        startShakingTime = 0;
    }

    public void onShake() {
        if (startShakingTime == 0) {
            startShakingTime = System.currentTimeMillis();
            Toast.makeText(context, R.string.shuffle, Toast.LENGTH_SHORT).show();
        } else {
            if (System.currentTimeMillis() - startShakingTime > SensorConfiguration.MIN_DURATION_BETWEEN_SHAKES) {
                startShakingTime = 0;
                onShake();
            }
        }
    }
}
