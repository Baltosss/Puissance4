package com.puissance4.view.activities;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.puissance4.R;
import com.puissance4.configuration.GameConfiguration;
import com.puissance4.controller.button_controllers.FriendListButtonListener;
import com.puissance4.controller.button_controllers.FriendPlayerButtonListener;
import com.puissance4.controller.button_controllers.NearPlayerButtonListener;
import com.puissance4.controller.button_controllers.SettingsButtonListener;
import com.puissance4.model.Party;
import com.puissance4.server_com.network_handlers.NetworkComm;
import com.puissance4.server_com.network_service.NetworkService;

/**
 * Created by fred on 08/01/15.
 */
public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startService(new Intent(this, NetworkService.class));
        setContentView(R.layout.main);
        Button nearPlayerButton = (Button) findViewById(R.id.buttonPlayNearPlayer);
        Button friendPlayerButton = (Button) findViewById(R.id.buttonPlayFriend);
        Button friendListButton = (Button) findViewById(R.id.buttonFriendList);
        Button settingsButton = (Button) findViewById(R.id.buttonSettings);
        nearPlayerButton.setOnClickListener(new NearPlayerButtonListener(this));
        friendPlayerButton.setOnClickListener(new FriendPlayerButtonListener(this));
        friendListButton.setOnClickListener(new FriendListButtonListener(this));
        settingsButton.setOnClickListener(new SettingsButtonListener(this));

        if (getIntent().getBooleanExtra("CLOSEAPP", false)) {
            stopService(new Intent(this, NetworkService.class));
            finish();
            android.os.Process.killProcess(android.os.Process.myPid());
        } else if (getIntent().hasExtra("ADVNAME")) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(5);
            NetworkService.cancelTimeout();

            Intent gameIntent = new Intent(this, GameActivity.class);

            String[] players = new String[2];
            players[0] = GameConfiguration.USERNAME;
            players[1] = getIntent().getStringExtra("ADVNAME");

            GameConfiguration.GRID_HEIGHT = getIntent().getIntExtra("X", 0);
            GameConfiguration.GRID_WIDTH = getIntent().getIntExtra("Y", 0);

            int firstPlayer = NetworkComm.getInstance().answerProposal(true);
            switch (firstPlayer) {
                case 0:
                    players[0] = GameConfiguration.USERNAME;
                    players[1] = getIntent().getStringExtra("ADVNAME");
                    gameIntent.putExtra("party", new Party(players, 0, GameConfiguration.GRID_HEIGHT, GameConfiguration.GRID_WIDTH));
                    startActivity(gameIntent);
                    break;
                case 1:
                    players[0] = getIntent().getStringExtra("ADVNAME");
                    players[1] = GameConfiguration.USERNAME;
                    gameIntent.putExtra("party", new Party(players, 1, GameConfiguration.GRID_HEIGHT, GameConfiguration.GRID_WIDTH));
                    startActivity(gameIntent);
                    break;
                default:
                    //ERREUR
                    finish();
                    break;
            }
        }
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
    }
}