package com.puissance4.controller;

import android.content.Intent;
import android.view.View;
import com.puissance4.view.MainActivity;
import com.puissance4.view.SettingActivity;

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
