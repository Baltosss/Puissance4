package com.puissance4.view;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.Puissance4.R;
import com.puissance4.controller.FriendListButtonListener;
import com.puissance4.controller.FriendPlayerButtonListener;
import com.puissance4.controller.NearPlayerButtonListener;
import com.puissance4.controller.SettingsButtonListener;
import com.puissance4.server_handler.NetworkComm;

/**
 * Created by fred on 08/01/15.
 */
public class MainActivity extends Activity {
    private boolean isLoading;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NetworkComm.getInstance();  //CONNECTION TO THE SERVER IF NEEDED
        if(savedInstanceState != null) {
            isLoading = savedInstanceState.getBoolean("isLoading");
        }
        else {
            isLoading = false;
        }
        if(isLoading) {
            setContentView(R.layout.loading);
        }
        else {
            setContentView(R.layout.main);
            Button nearPlayerButton = (Button) findViewById(R.id.buttonPlayNearPlayer);
            Button friendPlayerButton = (Button) findViewById(R.id.buttonPlayFriend);
            Button friendListButton = (Button) findViewById(R.id.buttonFriendList);
            Button settingsButton = (Button) findViewById(R.id.buttonSettings);
            nearPlayerButton.setOnClickListener(new NearPlayerButtonListener(this));
            friendPlayerButton.setOnClickListener(new FriendPlayerButtonListener(this));
            friendListButton.setOnClickListener(new FriendListButtonListener(this));
            settingsButton.setOnClickListener(new SettingsButtonListener(this));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean("isLoading", isLoading);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        NetworkComm.getInstance().disconnect();
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean isLoading) {
        this.isLoading = isLoading;
    }
}