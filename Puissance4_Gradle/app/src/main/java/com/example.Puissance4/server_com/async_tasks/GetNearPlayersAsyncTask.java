package com.example.Puissance4.server_com.async_tasks;

import com.example.Puissance4.server_com.network_handlers.NetworkComm;
import com.example.Puissance4.server_com.network_handlers.NetworkPlayer;
import com.example.Puissance4.view.activities.NearPlayerPickerActivity;

import java.util.ArrayList;

/**
 * Created by fred on 12/01/15.
 */
public class GetNearPlayersAsyncTask extends GetPlayersAsyncTask {

    public GetNearPlayersAsyncTask(NearPlayerPickerActivity context) {
        super(context);
    }

    @Override
    protected ArrayList<NetworkPlayer> doInBackground(Void... params) {
        return NetworkComm.getInstance().getNearPlayers();
    }

}
