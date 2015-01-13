package com.puissance4.server_com.async_tasks;

import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;
import com.example.Puissance4.R;
import com.puissance4.server_com.network_handlers.NetworkComm;
import com.puissance4.view.activities.MainActivity;
import com.puissance4.view.activities.SettingActivity;

/**
 * Created by fred on 12/01/15.
 */
public class MakeAccountAsyncTask extends AsyncTask<String, Void, Integer> {
    private SettingActivity context;

    public MakeAccountAsyncTask(SettingActivity context) {
        this.context = context;
    }
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