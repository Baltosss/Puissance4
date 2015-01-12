package com.puissance4.controller.sensor_controllers;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import com.puissance4.configuration.SensorConfiguration;

public class ShakeDetector implements SensorEventListener {
    // Arrays to store gravity and linear acceleration values
	private float[] mGravity = { 0.0f, 0.0f, 0.0f };
	private float[] mLinearAcceleration = { 0.0f, 0.0f, 0.0f };

	// OnShakeListener that will be notified when the shake is detected
	private ShakeListener mShakeListener;
	
	// Start time for the shake detection
	long startTime = 0;
	
	// Counter for shake movements
	int moveCount = 0;

    public ShakeDetector(ShakeListener shakeListener) {
    	mShakeListener = shakeListener;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        setCurrentAcceleration(event);
        double currentLinearAcceleration = getCurrentLinearAcceleration();
        if (currentLinearAcceleration > SensorConfiguration.MIN_SHAKE_ACCELERATION) {
            long now = System.currentTimeMillis();
            if (startTime == 0) {
                startTime = now;
            }
            long elapsedTime = now - startTime;
            if (elapsedTime > SensorConfiguration.MAX_SHAKE_DURATION) {
                resetShakeDetection();
            } else {
                moveCount++;
                if (moveCount > SensorConfiguration.MIN_MOVEMENTS) {
                    mShakeListener.onShake();
                    resetShakeDetection();
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
  	    // Intentionally blank
    }
    
    private void setCurrentAcceleration(SensorEvent event) {
    	// alpha is calculated as t / (t + dT)
        // with t, the low-pass filter's time-constant
        // and dT, the event delivery rate
        final float alpha = 0.8f;
        mGravity[0] = alpha * mGravity[0] + (1 - alpha) * event.values[0];
        mGravity[1] = alpha * mGravity[1] + (1 - alpha) * event.values[1];
        mGravity[2] = alpha * mGravity[2] + (1 - alpha) * event.values[2];
        mLinearAcceleration[0] = event.values[0] - mGravity[0];
        mLinearAcceleration[1] = event.values[1] - mGravity[1];
        mLinearAcceleration[2] = event.values[2] - mGravity[2];
    }
    
    private double getCurrentLinearAcceleration() {
    	return Math.sqrt(((double) (
                                mLinearAcceleration[0]*mLinearAcceleration[0]+
                                mLinearAcceleration[1]*mLinearAcceleration[1]+
                                mLinearAcceleration[2]*mLinearAcceleration[2]
                        )));
    }
    
    private void resetShakeDetection() {
    	startTime = 0;
    	moveCount = 0;
    }
}