package com.puissance4.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.Puissance4.R;
import com.puissance4.controller.OnConnectClickListener;
import com.puissance4.controller.OnDisconnectClickListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by fred on 08/01/15.
 */
public class SettingActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        if(GameConfiguration.USERNAME == null) {
            getRememberedSettings();
        }
        if(GameConfiguration.USERNAME != null) {
            buildConnectedSettings();
        }
        else {
            buildDisconnectedSettings();
        }
    }

    private void getRememberedSettings() {
        SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
        String username = preferences.getString("username", null);
        if(username != null) {
            String password = preferences.getString("password", null);
            if(password != null) {
                GameConfiguration.USERNAME = username;
                GameConfiguration.PASSWORD = password;
            }
            else {
                SharedPreferences.Editor editor = preferences.edit();
                editor.remove("username");
                editor.commit();
            }
        }
    }

    private void buildConnectedSettings() {
        findViewById(R.id.textPassword).setVisibility(View.GONE);
        findViewById(R.id.rememberCheck).setVisibility(View.GONE);
        findViewById(R.id.editUsername).setVisibility(View.GONE);
        findViewById(R.id.editPassword).setVisibility(View.GONE);
        findViewById(R.id.connectButtonsLayout).setVisibility(View.GONE);
        TextView username = (TextView) findViewById(R.id.textUsername);
        username.setText(GameConfiguration.USERNAME);
        username.setVisibility(View.VISIBLE);
        Button disconnectButton = (Button) findViewById(R.id.buttonDisconnect);
        disconnectButton.setVisibility(View.VISIBLE);
        disconnectButton.setOnClickListener(new OnDisconnectClickListener(this));
    }

    private void buildDisconnectedSettings() {
        Button connectButton = (Button) findViewById(R.id.buttonConnect);
        Button registerButton = (Button) findViewById(R.id.buttonRegister);
        connectButton.setOnClickListener(new OnConnectClickListener(this));
    }
}