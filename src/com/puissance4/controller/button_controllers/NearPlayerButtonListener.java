package com.puissance4.controller.button_controllers;

import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.puissance4.configuration.GameConfiguration;
import com.puissance4.view.activities.MainActivity;
import com.puissance4.view.activities.NearPlayerPickerActivity;
import com.example.puissance4.R;

/**
 * Created by fred on 09/01/15.
 */
public class NearPlayerButtonListener extends ActivityListener {
    public NearPlayerButtonListener(MainActivity mainActivity) {
        super(mainActivity);
    }

    @Override
    public void onClick(View view) {
        if(GameConfiguration.USERNAME == null) {
            Toast.makeText(context, R.string.connectBeforePlay, Toast.LENGTH_SHORT).show();
        }
        else {
            Intent intent = new Intent(context, NearPlayerPickerActivity.class);
            context.startActivity(intent);
        }
    }
}
