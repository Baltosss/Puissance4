package com.puissance4.view.activities;

import android.app.Activity;
import android.os.Bundle;
import com.example.Puissance4.R;
import com.puissance4.server_com.async_tasks.GetFriendPlayersAsyncTask;

/**
 * Created by fred on 10/01/15.
 */
public class FriendPickerActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading);
        new GetFriendPlayersAsyncTask(this).execute();
        /*super.onCreate(savedInstanceState);
        setContentView(R.layout.loading);
        ArrayList<NetworkPlayer> friendList = NetworkComm.getInstance().getFriends();
        setContentView(R.layout.list_pick);
        OpponentPickerAdapter adapter = new OpponentPickerAdapter(this, R.layout.friend_item, friendList);
        ListView listView = (ListView) findViewById(R.id.pickList);
        listView.setAdapter(adapter);*/
    }
}