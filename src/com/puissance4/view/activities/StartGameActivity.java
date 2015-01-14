package com.puissance4.view.activities;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.puissance4.R;
import com.puissance4.configuration.GameConfiguration;
import com.puissance4.model.Party;
import com.puissance4.server_com.network_handlers.NetworkComm;

/**
 * Created by cyrille on 13/01/15.
 */
public class StartGameActivity extends Activity {
    private boolean quitOnResume;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        quitOnResume = false;

        setContentView(R.layout.loading);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(5);

        Intent gameIntent = new Intent(this, GameActivity.class);

        String[] players = new String[2];
        players[0] = GameConfiguration.USERNAME;
        players[1] = getIntent().getStringExtra("ADVNAME");

        GameConfiguration.GRID_HEIGHT = getIntent().getIntExtra("X", 0);
        GameConfiguration.GRID_WIDTH = getIntent().getIntExtra("Y", 0);

        switch (NetworkComm.getInstance().answerProposal(true)) {
            case 0:
                //CREATE PARTY WITH SELF AS FIRST PLAYER
                gameIntent.putExtra("party", new Party(players, GameConfiguration.GRID_HEIGHT, GameConfiguration.GRID_WIDTH));
                startActivity(gameIntent);
                break;
            case 1:
                //CREATE PARTY WITH SELF AS SECOND PLAYER
                gameIntent.putExtra("party", new Party(players, GameConfiguration.GRID_HEIGHT, GameConfiguration.GRID_WIDTH));
                startActivity(gameIntent);
                break;
            default:
                //ERREUR
                finish();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(quitOnResume) {
            finish();
        } else {
            quitOnResume = true;
        }
    }
}
