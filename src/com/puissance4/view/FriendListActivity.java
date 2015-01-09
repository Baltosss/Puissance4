package com.puissance4.view;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.example.Puissance4.R;
import com.puissance4.adapter.FriendListAdapter;

/**
 * Created by fred on 08/01/15.
 */
public class FriendListActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_list);
        String[] myStringArray = {"Fred", "Cyrille", "Lucas", "Anthony", "Jessie", "Julie", "Olivier", "Yannick", "Julien"};
        FriendListAdapter adapter = new FriendListAdapter(this,R.layout.friend_item, myStringArray);
        ListView listView = (ListView) findViewById(R.id.friendList);
        listView.setAdapter(adapter);
    }
}