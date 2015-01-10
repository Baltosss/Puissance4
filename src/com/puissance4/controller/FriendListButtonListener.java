package com.puissance4.controller;

import android.content.Intent;
import android.view.View;
import android.widget.Toast;
import com.example.Puissance4.R;
import com.puissance4.view.FriendListActivity;
import com.puissance4.configuration.GameConfiguration;
import com.puissance4.view.MainActivity;

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
