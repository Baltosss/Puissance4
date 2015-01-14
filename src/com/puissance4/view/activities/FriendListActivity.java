package com.puissance4.view.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

import com.example.puissance4.R;
import com.puissance4.controller.button_controllers.AddFriendButtonListener;
import com.puissance4.server_com.async_tasks.GetFriendListAsyncTask;

/**
 * Created by fred on 08/01/15.
 */
public class FriendListActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading);
        //Toast.makeText(this, R.string.connectBeforePlay, Toast.LENGTH_SHORT).show();
        buildFriendList();
    }

    public void buildFriendList() {
        ///////////////////////////////// GET FRIEND LIST INSTRUCTIONS ///////////////////////////////
        new GetFriendListAsyncTask(this).execute();
    }
}