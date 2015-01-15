package com.puissance4.server_com.async_tasks;

import android.os.AsyncTask;
import android.widget.Toast;

import com.example.puissance4.R;
import com.puissance4.server_com.network_handlers.NetworkComm;
import com.puissance4.view.activities.FriendListActivity;

/**
 * Created by fred on 14/01/15.
 */
public class RemoveFriendAsyncTask extends AsyncTask<String, Void, Boolean> {
    FriendListActivity context;

    public RemoveFriendAsyncTask(FriendListActivity context) {
        this.context = context;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        return NetworkComm.getInstance().removeFriend(params[0]);
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if (result) {
            Toast.makeText(context, R.string.friendRemoved, Toast.LENGTH_SHORT);
            context.setContentView(R.layout.loading);
            context.buildFriendList();
        }
    }
}
