package com.puissance4.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.Puissance4.R;
import com.puissance4.configuration.GameConfiguration;
import com.puissance4.network_handler.NetworkComm;
import com.puissance4.view.MainActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by fred on 08/01/15.
 */
public class OnConnectClickListener extends ActivityListener {
    private class AuthenticateAsyncTask extends AsyncTask<String, Void, Integer> {
        private String tempUsername;
        private String tempPassword;

        @Override
        protected Integer doInBackground(String... params) {
            tempUsername = params[0];
            tempPassword = params[1];
            return NetworkComm.getInstance().authenticate(tempUsername, tempPassword);
        }

        @Override
        protected void onPostExecute(Integer result) {
            switch (result) {
                case 0:
                    Toast.makeText(context, R.string.connected, Toast.LENGTH_SHORT).show();
                    GameConfiguration.USERNAME = tempUsername;
                    GameConfiguration.PASSWORD = tempPassword;
                    Intent intent = new Intent(context, MainActivity.class);
                    context.startActivity(intent);
                    break;
                case 1:
                    Toast.makeText(context, R.string.unknownUsername, Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(context, R.string.wrongPassword, Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(context, R.string.authenticateTimeout, Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    Toast.makeText(context, R.string.authenticateError, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(context, R.string.authenticateUnhandledError, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

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
                /*try {
                    NetworkComm.getInstance().connect();*/
                new AuthenticateAsyncTask().execute(username, password);
                /*
                    int result = NetworkComm.getInstance().authenticate(username, password);
                    switch (result) {
                        case 0:
                            Toast.makeText(context, R.string.connected, Toast.LENGTH_SHORT).show();
                            GameConfiguration.USERNAME = username;
                            GameConfiguration.PASSWORD = password;
                            Intent intent = new Intent(context, MainActivity.class);
                            context.startActivity(intent);
                            break;
                        case 1:
                            Toast.makeText(context, R.string.unknownUsername, Toast.LENGTH_SHORT).show();
                            break;
                        case 2:
                            Toast.makeText(context, R.string.wrongPassword, Toast.LENGTH_SHORT).show();
                            break;
                        case 3:
                            Toast.makeText(context, R.string.authenticateTimeout, Toast.LENGTH_SHORT).show();
                            break;
                        case 4:
                            Toast.makeText(context, R.string.authenticateError, Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(context, R.string.authenticateUnhandledError, Toast.LENGTH_SHORT).show();
                            break;
                    }
                /*
                /*} catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(context, R.string.connectCommunicationError, Toast.LENGTH_SHORT).show();
                }*/
            }
        }
    }
}
