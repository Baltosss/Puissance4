package com.puissance4.view;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;
import com.example.Puissance4.R;
import com.puissance4.adapter.FriendListAdapter;
import com.puissance4.adapter.PseudoFriendListAdapter;
import com.puissance4.model.Player;
import com.puissance4.server_handler.NetworkComm;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by fred on 08/01/15.
 */
public class FriendListActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_list);
        if(GameConfiguration.USERNAME == null) {
            Toast.makeText(this, R.string.connectBeforePlay, Toast.LENGTH_SHORT).show();
            String[] myStringArray = {"Fred", "Cyrille", "Lucas", "Anthony", "Jessie", "Julie", "Olivier", "Yannick", "Julien"};
            PseudoFriendListAdapter adapter = new PseudoFriendListAdapter(this,R.layout.friend_item, myStringArray);
            ListView listView = (ListView) findViewById(R.id.friendList);
            listView.setAdapter(adapter);
        }
        else {
            ///////////////////////////////// GET FRIEND LIST INSTRUCTIONS ///////////////////////////////
            ArrayList<Player> friendList = NetworkComm.getInstance().getFriends();  //Must modify getFriends to return ArrayList<Player>
            FriendListAdapter adapter = new FriendListAdapter(this, R.layout.friend_item, friendList);
        }
    }
}