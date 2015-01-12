package com.puissance4.view.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.example.Puissance4.R;
import com.puissance4.view.adapters.FriendListAdapter;
import com.puissance4.server_com.network_handlers.NetworkPlayer;
import com.puissance4.server_com.network_handlers.NetworkComm;

import java.util.ArrayList;

/**
 * Created by fred on 08/01/15.
 */
public class FriendListActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading);
        setContentView(R.layout.friend_list_settings);
        Toast.makeText(this, R.string.connectBeforePlay, Toast.LENGTH_SHORT).show();
        /*String[] myStringArray = {"Fred", "Cyrille", "Lucas", "Anthony", "Jessie", "Julie", "Olivier", "Yannick", "Julien"};
        PseudoFriendListAdapter adapter = new PseudoFriendListAdapter(this,R.layout.friend_item, myStringArray);
        ListView listView = (ListView) findViewById(R.id.friendList);
        listView.setAdapter(adapter);*/
        ///////////////////////////////// GET FRIEND LIST INSTRUCTIONS ///////////////////////////////
        ArrayList<NetworkPlayer> friendList = NetworkComm.getInstance().getFriends();  //Must modify getFriends to return ArrayList<Player>
        setContentView(R.layout.friend_list_settings);
        FriendListAdapter adapter = new FriendListAdapter(this, R.layout.friend_item, friendList);
        ListView listView = (ListView) findViewById(R.id.friendList);
        listView.setAdapter(adapter);
    }
}