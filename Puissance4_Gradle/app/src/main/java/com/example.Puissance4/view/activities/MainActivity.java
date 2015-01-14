package com.example.Puissance4.view.activities;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Button;
import com.example.Puissance4.R;
import com.example.Puissance4.controller.button_controllers.FriendListButtonListener;
import com.example.Puissance4.controller.button_controllers.FriendPlayerButtonListener;
import com.example.Puissance4.controller.button_controllers.NearPlayerButtonListener;
import com.example.Puissance4.controller.button_controllers.SettingsButtonListener;
import com.example.Puissance4.server_com.network_handlers.NetworkComm;
import com.example.Puissance4.server_com.ping_service.PingService;

/**
 * Created by fred on 08/01/15.
 */
public class MainActivity extends Activity {
    private PingService pingService;
    private boolean pingServiceBound = false;

    private ServiceConnection pingServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            PingService.PingBinder binder = (PingService.PingBinder) service;
            pingService = binder.getService();
            pingServiceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            pingServiceBound = false;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //CONNECTION TO THE SERVER IF NEEDED
        new Thread(new Runnable() {
            @Override
            public void run() {
                NetworkComm.getInstance();
            }
        }).start();

        //PING SERVICE INIT
        bindService(new Intent(this, PingService.class), pingServiceConnection, Context.BIND_AUTO_CREATE);

        setContentView(R.layout.main);
        Button nearPlayerButton = (Button) findViewById(R.id.buttonPlayNearPlayer);
        Button friendPlayerButton = (Button) findViewById(R.id.buttonPlayFriend);
        Button friendListButton = (Button) findViewById(R.id.buttonFriendList);
        Button settingsButton = (Button) findViewById(R.id.buttonSettings);
        nearPlayerButton.setOnClickListener(new NearPlayerButtonListener(this));
        friendPlayerButton.setOnClickListener(new FriendPlayerButtonListener(this));
        friendListButton.setOnClickListener(new FriendListButtonListener(this));
        settingsButton.setOnClickListener(new SettingsButtonListener(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (pingServiceBound) {
            unbindService(pingServiceConnection);
            pingServiceBound = false;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                NetworkComm.getInstance().disconnect();
            }
        }).start();
    }
}