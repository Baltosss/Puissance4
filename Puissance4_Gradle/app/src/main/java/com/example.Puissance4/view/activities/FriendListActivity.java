package com.example.Puissance4.view.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;
import com.example.Puissance4.server_com.async_tasks.GetFriendListAsyncTask;
import android.R;
/**
 * Created by fred on 08/01/15.
 */
public class FriendListActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading);
        setContentView(R.layout.loading);
        Toast.makeText(this, R.string.connectBeforePlay, Toast.LENGTH_SHORT).show();
        ///////////////////////////////// GET FRIEND LIST INSTRUCTIONS ///////////////////////////////
        new GetFriendListAsyncTask(this).execute();
    }
}