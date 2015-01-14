package com.example.Puissance4.server_com.async_tasks;

import android.os.AsyncTask;
import com.example.Puissance4.configuration.GameConfiguration;
import com.example.Puissance4.server_com.network_handlers.NetworkComm;
import com.example.Puissance4.view.activities.SettingActivity;

/**
 * Created by fred on 12/01/15.
 */
public class AuthenticateAsyncTask extends AsyncTask<String, Void, Integer> {
    protected SettingActivity context;

    public AuthenticateAsyncTask(SettingActivity context) {
        this.context = context;
    }

    @Override
    protected Integer doInBackground(String... params) {
        return NetworkComm.getInstance().authenticate(params[0], params[1]);
    }

    @Override
    protected void onPostExecute(Integer result) {
        if (result > 0) {
            GameConfiguration.USERNAME = null;
            GameConfiguration.PASSWORD = null;
        }
    }
}
