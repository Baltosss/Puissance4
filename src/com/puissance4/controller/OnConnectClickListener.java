package com.puissance4.controller;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.example.Puissance4.R;
import com.puissance4.view.GameConfiguration;
import com.puissance4.view.MainActivity;

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
        String username = ((EditText)context.findViewById(R.id.editUsername)).getText().toString();
        String password = ((EditText)context.findViewById(R.id.editPassword)).getText().toString();
        if(!username.equals("") && !password.equals("")) {
            Pattern pattern = Pattern.compile("\\s");
            Matcher matcherUsername = pattern.matcher(username);
            Matcher matcherPassword = pattern.matcher(password);
            if(!matcherUsername.find() && !matcherPassword.find()) {
                /////////////////////// CONNECTION INSTRUCTIONS //////////////////////////////////
                boolean worked = false;
                if(worked) {
                    GameConfiguration.USERNAME = username;
                    GameConfiguration.PASSWORD = password;
                    Toast.makeText(context, "Connected", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, MainActivity.class);
                    context.startActivity(intent);
                }
            }
        }
    }
}
