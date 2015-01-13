package com.puissance4.view.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.example.Puissance4.R;
import com.puissance4.server_com.async_tasks.GetFriendListAsyncTask;
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
        Toast.makeText(this, R.string.connectBeforePlay, Toast.LENGTH_SHORT).show();
        ///////////////////////////////// GET FRIEND LIST INSTRUCTIONS ///////////////////////////////
        new GetFriendListAsyncTask(this).execute();
    }
}