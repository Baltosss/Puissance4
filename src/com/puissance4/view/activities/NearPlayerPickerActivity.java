package com.puissance4.view.activities;

import android.app.Activity;
import android.os.Bundle;

import com.example.Puissance4.R;
import com.puissance4.server_com.async_tasks.GetNearPlayersAsyncTask;

/**
 * Created by fred on 10/01/15.
 */
public class NearPlayerPickerActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading);
        new GetNearPlayersAsyncTask(this).execute();
        /*
        ArrayList<NetworkPlayer> nearPlayerList = NetworkComm.getInstance().getNearPlayers();
        setContentView(R.layout.list_pick);
        OpponentPickerAdapter adapter = new OpponentPickerAdapter(this, R.layout.friend_item, nearPlayerList);
        ListView listView = (ListView) findViewById(R.id.pickList);
        listView.setAdapter(adapter);
        */
    }
}