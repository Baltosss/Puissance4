package com.puissance4.view.activities;

import android.app.Activity;
import android.os.Bundle;

import com.example.puissance4.R;
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
    }
}