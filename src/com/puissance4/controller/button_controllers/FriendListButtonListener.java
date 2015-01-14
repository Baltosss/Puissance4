package com.puissance4.controller.button_controllers;

import android.content.Intent;
import android.view.View;
import android.widget.Toast;
import com.puissance4.view.activities.FriendListActivity;
import com.puissance4.configuration.GameConfiguration;
import com.puissance4.view.activities.MainActivity;
import com.example.puissance4.R;

/**
 * Created by fred on 09/01/15.
 */
public class FriendListButtonListener extends ActivityListener {
    public FriendListButtonListener(MainActivity mainActivity) {
        super(mainActivity);
    }

    @Override
    public void onClick(View view) {
        if(GameConfiguration.USERNAME == null) {
            Toast.makeText(context, R.string.connectBeforeAccessFriends, Toast.LENGTH_SHORT).show();
        }
        else {
            Intent intent = new Intent(context, FriendListActivity.class);
            context.startActivity(intent);
        }
    }
}
