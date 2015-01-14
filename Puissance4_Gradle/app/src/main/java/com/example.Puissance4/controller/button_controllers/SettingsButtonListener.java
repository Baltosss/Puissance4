package com.example.Puissance4.controller.button_controllers;

import android.content.Intent;
import android.view.View;
import com.example.Puissance4.view.activities.MainActivity;
import com.example.Puissance4.view.activities.SettingActivity;

/**
 * Created by fred on 09/01/15.
 */
public class SettingsButtonListener extends ActivityListener {
    public SettingsButtonListener(MainActivity mainActivity) {
        super(mainActivity);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(context, SettingActivity.class);
        context.startActivity(intent);
    }
}
