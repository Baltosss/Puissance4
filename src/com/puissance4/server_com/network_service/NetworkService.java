package com.puissance4.server_com.network_service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
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
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;

import com.example.puissance4.R;
import com.puissance4.server_com.network_handlers.NetworkComm;
import com.puissance4.view.activities.MainActivity;

public class NetworkService extends Service {
    private final IBinder binder = new PingBinder();
    private double latitude;
    private double longitude;
    private Handler pingHandler;
    private Handler notificationExpirationHandler;
    private LocationManager locationManager;
    private NotificationManager notificationManager;
    private PowerManager powerManager;
    private PowerManager.WakeLock wakeLock;
    private Criteria criteria;
    private static boolean cancelTimeout;

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
            //locationManager.requestSingleUpdate(criteria, new pingLocationListener(), null);
            NetworkComm.getInstance().sendPing(latitude, longitude);
            pingHandler.postDelayed(this, 5000);
        }
    };

    public class PingBinder extends Binder {
        public NetworkService getService() {
            return NetworkService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        setCoordinates(0, 0);
        cancelTimeout = false;
        NetworkComm.getInstance().setService(this);

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.slot_red_star)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.app_name) + " " + getString(R.string.isRunning));

        Intent openIntent = new Intent(getApplicationContext(), MainActivity.class);
        openIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent openPendingIntent = PendingIntent.getActivity(this, 1, openIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.addAction(new NotificationCompat.Action(R.drawable.icon_green_dot, getString(R.string.open), openPendingIntent));

        Intent closeIntent = new Intent(getApplicationContext(), MainActivity.class);
        closeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        closeIntent.putExtra("CLOSEAPP", true);
        PendingIntent closePendingIntent = PendingIntent.getActivity(this, 2, closeIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.addAction(new NotificationCompat.Action(R.drawable.icon_red_dot, getString(R.string.quit), closePendingIntent));

        startForeground(6, builder.build());

        powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Tag");
        wakeLock.acquire();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_MEDIUM);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);

        Location lastKnownLocation = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, true));
        if (lastKnownLocation != null) {
            latitude = lastKnownLocation.getLatitude();
            longitude = lastKnownLocation.getLongitude();
        }
        locationManager.requestLocationUpdates(30000, 0, criteria, new pingLocationListener(), null);

        pingHandler = new Handler();
        notificationExpirationHandler = new Handler();
        pingHandler.post(pingRunnable);
    }

    public void proposalReceived(String advname, int x, int y, int n) {
        //DIALOGUE ET APPELER answerProposal();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.slot_red_star)
                .setContentTitle(advname + " " + getString(R.string.requestsAMatch))
                .setTicker(advname + " " + getString(R.string.requestsAMatch))
                .setContentText(getString(R.string.touchToAccept))
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setCategory(Notification.CATEGORY_SOCIAL)
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE);

        Intent acceptGameIntent = new Intent(this, MainActivity.class);
        acceptGameIntent.putExtra("ADVNAME", advname);
        acceptGameIntent.putExtra("X", x);
        acceptGameIntent.putExtra("Y", y);
        acceptGameIntent.putExtra("N", n); //nb shuffles

        PendingIntent acceptPendingIntent = PendingIntent.getActivity(this, 0, acceptGameIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(acceptPendingIntent);

        Intent refuseGameIntent = new Intent(this, RefuseGameReceiver.class);
        PendingIntent refusePendingIntent = PendingIntent.getBroadcast(this, 0, refuseGameIntent, 0);
        builder.setDeleteIntent(refusePendingIntent);

        notificationManager.notify(5, builder.build());

        notificationExpirationHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!cancelTimeout) {
                    notificationManager.cancel(5);
                    NetworkComm.getInstance().answerProposal(false);
                }

                cancelTimeout = false;
            }
        }, 60000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        wakeLock.release();
        NetworkComm.getInstance().disconnect();
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

    public static void cancelTimeout() {
        cancelTimeout = true;
    }
}
