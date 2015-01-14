package com.puissance4.server_com.async_tasks;

import android.os.AsyncTask;
import android.widget.Toast;

import com.example.puissance4.R;
import com.puissance4.server_com.network_handlers.NetworkComm;
import com.puissance4.view.activities.FriendListActivity;

/**
 * Created by fred on 14/01/15.
 * Asynchronous task to add a friend in FriendListActivity
 */
public class AddFriendAsyncTask extends AsyncTask<String, Void, Integer> {
    private FriendListActivity context;

    public AddFriendAsyncTask(FriendListActivity context) {
        this.context = context;
    }

    @Override
    protected Integer doInBackground(String... params) {
        return NetworkComm.getInstance().addFriend(params[0]);
    }

    @Override
    protected void onPostExecute(Integer result) {
        switch (result) {
            case 0:
                //FRIEND ADDED, REBUILD PAGE
                Toast.makeText(context, R.string.friendAdded, Toast.LENGTH_SHORT);
                context.setContentView(R.layout.friend_list_settings);
                context.buildFriendList();
                break;
            case 1:
                //UNKNOWN USERNAME
                Toast.makeText(context, R.string.unknownUsername, Toast.LENGTH_SHORT);
                break;
            case 2:
                //ALREADY FRIEND
                Toast.makeText(context, R.string.alreadyFriendError, Toast.LENGTH_SHORT);
                break;
            case 3:
                Toast.makeText(context, R.string.addFriendTimeout, Toast.LENGTH_SHORT);
                break;
            case 4:
                //ERROR
                Toast.makeText(context, R.string.addFriendError, Toast.LENGTH_SHORT);
        }
    }
}
