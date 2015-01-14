package com.example.Puissance4.controller.button_controllers;

import android.content.Intent;
import android.view.View;
import android.widget.Toast;
import com.example.Puissance4.R;
import com.example.Puissance4.configuration.GameConfiguration;
import com.example.Puissance4.view.activities.FriendPickerActivity;
import com.example.Puissance4.view.activities.MainActivity;

/**
 * Created by fred on 09/01/15.
 */
public class FriendPlayerButtonListener extends ActivityListener {
    public FriendPlayerButtonListener(MainActivity mainActivity) {
        super(mainActivity);
    }

    @Override
    public void onClick(View view) {
        if(GameConfiguration.USERNAME == null) {
            Toast.makeText(context, R.string.connectBeforePlay, Toast.LENGTH_SHORT).show();
        }
        else {
            Intent intent = new Intent(context, FriendPickerActivity.class);
            context.startActivity(intent);
        }
    }
}
