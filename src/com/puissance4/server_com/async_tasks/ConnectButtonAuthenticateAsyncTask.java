package com.puissance4.server_com.async_tasks;

import android.content.Intent;
import android.widget.Toast;
import com.example.Puissance4.R;
import com.puissance4.configuration.GameConfiguration;
import com.puissance4.view.activities.MainActivity;
import com.puissance4.view.activities.SettingActivity;

/**
 * Created by fred on 12/01/15.
 */
public class ConnectButtonAuthenticateAsyncTask extends AuthenticateAsyncTask {
    private String tempUsername;
    private String tempPassword;

    public ConnectButtonAuthenticateAsyncTask(SettingActivity context) {
        super(context);
    }

    @Override
    protected Integer doInBackground(String... params) {
        tempUsername = params[0];
        tempPassword = params[1];
        return super.doInBackground(params);
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