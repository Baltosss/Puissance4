package com.puissance4.server_com.async_tasks;

import com.puissance4.server_com.network_handlers.NetworkComm;
import com.puissance4.server_com.network_handlers.NetworkPlayer;
import com.puissance4.view.activities.FriendPickerActivity;

import java.util.ArrayList;

/**
 * Created by fred on 12/01/15.
 */
public class GetFriendPlayersAsyncTask extends GetPlayersAsyncTask {
    public GetFriendPlayersAsyncTask(FriendPickerActivity context) {
        super(context);
    }

    @Override
    protected ArrayList<NetworkPlayer> doInBackground(Void... params) {
        return NetworkComm.getInstance().getFriends();
    }
}
