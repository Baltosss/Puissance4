package com.puissance4.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.example.Puissance4.R;
import com.puissance4.network_handler.NetworkComm;
import com.puissance4.view.MainActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by fred on 08/01/15.
 */
public class OnRegisterClickListener extends ActivityListener {
    private class MakeAccountAsyncTask extends AsyncTask<String, Void, Integer> {
        @Override
        protected Integer doInBackground(String... params) {
            return NetworkComm.getInstance().makeAccount(params[0], params[1]);
        }

        @Override
        protected void onPostExecute(Integer result) {
            switch(result) {
                case 0:
                    Toast.makeText(context, R.string.registrationSuccess, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, MainActivity.class);
                    context.startActivity(intent);
                    break;
                case 1:
                    Toast.makeText(context, R.string.registrationUsernameTaken, Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(context, R.string.registrationTimeout, Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(context, R.string.registrationError, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(context, R.string.registrationUnhandledError, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    public OnRegisterClickListener(Activity context) {
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
            if (!matcherUsername.find() && !matcherPassword.find()) {
                //////////////////////////////// REGISTRATION INSTRUCTIONS ////////////////////////
                new MakeAccountAsyncTask().execute(username, password);
                /*try {
                    NetworkComm.getInstance().connect();*/
                /*
                    int result = NetworkComm.getInstance().makeAccount(username, password);
                    switch(result) {
                        case 0:
                            Toast.makeText(context, R.string.registrationSuccess, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context, MainActivity.class);
                            context.startActivity(intent);
                            break;
                        case 1:
                            Toast.makeText(context, R.string.registrationUsernameTaken, Toast.LENGTH_SHORT).show();
                            break;
                        case 2:
                            Toast.makeText(context, R.string.registrationTimeout, Toast.LENGTH_SHORT).show();
                            break;
                        case 3:
                            Toast.makeText(context, R.string.registrationError, Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(context, R.string.registrationUnhandledError, Toast.LENGTH_SHORT).show();
                            break;
                    }
                    */
                    /*NetworkComm.getInstance().disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(context, R.string.connectCommunicationError, Toast.LENGTH_SHORT).show();
                }*/
            }
        }
    }
}
