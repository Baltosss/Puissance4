package com.puissance4.controller.button_controllers;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;

import com.example.Puissance4.R;
import com.puissance4.configuration.GameConfiguration;
import com.puissance4.server_com.async_tasks.ConnectButtonAuthenticateAsyncTask;
import com.puissance4.view.activities.SettingActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by fred on 08/01/15.
 */
public class OnConnectClickListener extends ActivityListener {

    public OnConnectClickListener(Activity context) {
        super(context);
    }

    @Override
    public void onClick(View view) {
        String username = ((EditText) context.findViewById(R.id.editUsername)).getText().toString();
        String password = ((EditText) context.findViewById(R.id.editPassword)).getText().toString();
        if (!username.equals("") && !password.equals("")) {
            Pattern pattern = Pattern.compile("\\s");
            Matcher matcherUsername = pattern.matcher(username);
            Matcher matcherPassword = pattern.matcher(password);
            if (!matcherUsername.find() && !matcherPassword.find()) {
                /////////////////////// AUTHENTICATION INSTRUCTIONS //////////////////////////////////
                new ConnectButtonAuthenticateAsyncTask((SettingActivity)context).execute(username, password);
            }
        }
    }
}
