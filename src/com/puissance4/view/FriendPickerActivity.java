package com.puissance4.view;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import com.example.Puissance4.R;
import com.puissance4.adapter.OpponentPickerAdapter;
import com.puissance4.network_handler.NetworkComm;
import com.puissance4.network_handler.NetworkPlayer;

import java.util.ArrayList;

/**
 * Created by fred on 10/01/15.
 */
public class FriendPickerActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading);
        ArrayList<NetworkPlayer> friendList = NetworkComm.getInstance().getFriends();
        setContentView(R.layout.list_pick);
        OpponentPickerAdapter adapter = new OpponentPickerAdapter(this, R.layout.friend_item, friendList);
        ListView listView = (ListView) findViewById(R.id.pickList);
        listView.setAdapter(adapter);
    }
}