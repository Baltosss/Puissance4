package com.puissance4.server_com.network_service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.puissance4.view.activities.GameActivity;

/**
 * Created by cyrille on 14/01/15.
 */
public class AdversaryMessagesReceiver extends BroadcastReceiver {
    GameActivity view;

    public void setView(GameActivity view) {
        this.view = view;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (view != null) {
            switch (intent.getStringExtra("ACTION")) {
                case "MOVE":
                    int param = intent.getIntExtra("COLUMN", -1);
                    if (param >= 0) {
                        view.opponentMove(param);
                    }
                    break;
                case "RAND":
                    Integer[][] grid = (Integer[][]) intent.getSerializableExtra("GRID");
                    if (grid != null) {
                        view.opponentRandom(grid);
                    }
                    break;
                case "WIN":
                    int result = intent.getIntExtra("WINCODE", 3);
                    if (result != 3) {
                        view.opponentWin(result);
                    }
                    break;
                case "DISCONNECT":
                    view.adversaryDisconnected();
                    break;
                default:
                    break;
            }
        }
    }
}
