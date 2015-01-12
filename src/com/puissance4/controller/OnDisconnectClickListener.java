package com.puissance4.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import com.puissance4.network_handler.NetworkComm;
import com.puissance4.configuration.GameConfiguration;
import com.puissance4.view.MainActivity;

/**
 * Created by fred on 08/01/15.
 */
public class OnDisconnectClickListener extends ActivityListener {
    public OnDisconnectClickListener(Activity context) {
        super(context);
    }

    @Override
    public void onClick(View view) {
        ////////////////////// DISCONNECTION INSTRUCTIONS ///////////
        NetworkComm.getInstance().unauthenticate();
        GameConfiguration.PASSWORD = null;
        GameConfiguration.USERNAME = null;
        SharedPreferences preferences = context.getPreferences(Context.MODE_PRIVATE);
        if(preferences.contains("username")) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.remove("username");
            if (preferences.contains("password")) {
                editor.remove("password");
            }
            editor.commit();
        }
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }
}
