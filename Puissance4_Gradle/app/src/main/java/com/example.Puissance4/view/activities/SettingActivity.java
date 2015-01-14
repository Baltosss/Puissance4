package com.example.Puissance4.view.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import android.R;
import com.example.Puissance4.configuration.GameConfiguration;
import com.example.Puissance4.controller.button_controllers.OnConnectClickListener;
import com.example.Puissance4.controller.button_controllers.OnDisconnectClickListener;
import com.example.Puissance4.controller.button_controllers.OnRegisterClickListener;
import com.example.Puissance4.server_com.async_tasks.AuthenticateAsyncTask;

/**
 * Created by fred on 08/01/15.
 */
public class SettingActivity extends Activity {
    private Context context = this;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        if (GameConfiguration.USERNAME == null) {
            getRememberedSettings();
        }
        if (GameConfiguration.USERNAME != null) {
            buildConnectedSettings();
        } else {
            buildDisconnectedSettings();
        }
        buildGridSettings();
    }

    private void getRememberedSettings() {
        SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
        String username = preferences.getString("username", null);
        if (username != null) {
            String password = preferences.getString("password", null);
            if (password != null) {
                GameConfiguration.USERNAME = username;
                GameConfiguration.PASSWORD = password;
                ////////////////////// CONNECTION TO SERVER INSTRUCTIONS///////////////////////
                new AuthenticateAsyncTask(this).execute(GameConfiguration.USERNAME, GameConfiguration.PASSWORD);
            } else {
                SharedPreferences.Editor editor = preferences.edit();
                editor.remove("username");
                editor.apply();
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
        registerButton.setOnClickListener(new OnRegisterClickListener(this));
    }

    private void buildGridSettings() {
        Spinner widthSpinner = (Spinner) findViewById(R.id.spinnerWidth);
        Spinner heightSpinner = (Spinner) findViewById(R.id.spinnerHeight);
        Spinner shuffleSpinner = (Spinner) findViewById(R.id.spinnerShuffle);
        Integer[] widths = new Integer[GameConfiguration.MAX_GRID_WIDTH - GameConfiguration.MIN_GRID_WIDTH];
        Integer[] heights = new Integer[GameConfiguration.MAX_GRID_HEIGHT - GameConfiguration.MIN_GRID_HEIGHT];
        Integer[] shuffles = new Integer[GameConfiguration.MAX_SHUFFLE - GameConfiguration.MIN_SHUFFLE];
        for (int i = 0; i < (GameConfiguration.MAX_GRID_HEIGHT - GameConfiguration.MIN_GRID_HEIGHT); i++) {
            heights[i] = i + GameConfiguration.MIN_GRID_HEIGHT;
        }
        for (int i = 0; i < (GameConfiguration.MAX_GRID_WIDTH - GameConfiguration.MIN_GRID_WIDTH); i++) {
            widths[i] = i + GameConfiguration.MIN_GRID_WIDTH;
        }
        for (int i = 0; i < (GameConfiguration.MAX_SHUFFLE - GameConfiguration.MIN_SHUFFLE); i++) {
            shuffles[i] = i + GameConfiguration.MIN_SHUFFLE;
        }
        ArrayAdapter<Integer> adapterHeight = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, heights);
        ArrayAdapter<Integer> adapterWidth = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, widths);
        ArrayAdapter<Integer> adapterShuffle = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, shuffles);
        widthSpinner.setAdapter(adapterWidth);
        heightSpinner.setAdapter(adapterHeight);
        shuffleSpinner.setAdapter(adapterShuffle);
        widthSpinner.setSelection(GameConfiguration.GRID_WIDTH - GameConfiguration.MIN_GRID_WIDTH);
        heightSpinner.setSelection(GameConfiguration.GRID_HEIGHT - GameConfiguration.MIN_GRID_HEIGHT);
        shuffleSpinner.setSelection(GameConfiguration.COUNT_SHUFFLE - GameConfiguration.MIN_SHUFFLE);

        Button modifyGridButton = (Button) findViewById(R.id.buttonModify);
        modifyGridButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View rootView = view.getRootView();
                Spinner width = (Spinner) rootView.findViewById(R.id.spinnerWidth);
                Spinner height = (Spinner) rootView.findViewById(R.id.spinnerHeight);
                Spinner shuffle = (Spinner) rootView.findViewById(R.id.spinnerShuffle);
                GameConfiguration.GRID_HEIGHT = (int) height.getSelectedItem();
                GameConfiguration.GRID_WIDTH = (int) width.getSelectedItem();
                GameConfiguration.COUNT_SHUFFLE = (int) shuffle.getSelectedItem();
                Toast.makeText(context, R.string.gridModified, Toast.LENGTH_SHORT);
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}