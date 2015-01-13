package com.puissance4.server_com.ping_service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.puissance4.configuration.GameConfiguration;
import com.puissance4.server_com.network_handlers.NetworkComm;

public class PingService extends Service {
    private final IBinder binder = new PingBinder();
    private double latitude;
    private double longitude;
    private Handler pingHandler;
    private LocationManager locationManager;
    private Criteria criteria;

    private class pingLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }
    }

    private Runnable pingRunnable = new Runnable() {
        @Override
        public void run() {
            locationManager.requestSingleUpdate(criteria, new pingLocationListener(), null);
            NetworkComm.getInstance().sendPing(latitude, longitude);
            pingHandler.postDelayed(this, 5000);
        }
    };

    public class PingBinder extends Binder {
        public PingService getService() {
            return PingService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        setCoordinates(0, 0);
        NetworkComm.getInstance().setService(this);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_MEDIUM);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);

        pingHandler = new Handler();
        pingHandler.post(pingRunnable);
    }

    public void proposalReceived(String advname, int x, int y) {
        //A NE FAIRE QUE EN CAS DE REPONSE POSITIVE
        GameConfiguration.GRID_HEIGHT = x;
        GameConfiguration.GRID_WIDTH = y;

        //DIALOGUE ET APPELER answerProposal();
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setContentTitle("Match")
                .setContentText(advname + " vous propose de jouer !");

        switch (NetworkComm.getInstance().answerProposal(true)) {
            case 0:
                startGame(true);
                break;
            case 1:
                startGame(false);
                break;
            default:
                //ERREUR
                break;
        }
    }

    public void startGame(boolean firstPlayer) {
        //lancer l'activty de jeu
    }

    public synchronized void setCoordinates(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public synchronized double[] getCoordinates() {
        double[] ret = new double[2];
        ret[0] = latitude;
        ret[1] = longitude;
        return ret;
    }
}
