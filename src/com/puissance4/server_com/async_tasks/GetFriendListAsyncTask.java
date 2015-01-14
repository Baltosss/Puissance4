package com.puissance4.server_com.async_tasks;

import android.os.AsyncTask;
import android.widget.ListView;

import com.example.puissance4.R;
import com.puissance4.server_com.network_handlers.NetworkComm;
import com.puissance4.server_com.network_handlers.NetworkPlayer;
import com.puissance4.view.activities.FriendListActivity;
import com.puissance4.view.adapters.FriendListAdapter;

import java.util.ArrayList;

/**
 * Created by fred on 12/01/15.
 */
public class GetFriendListAsyncTask extends AsyncTask<Void, Void, ArrayList<NetworkPlayer>> {
    private FriendListActivity context;

    public GetFriendListAsyncTask(FriendListActivity context) {
        this.context = context;
    }

    @Override
    protected ArrayList<NetworkPlayer> doInBackground(Void... voids) {
        return NetworkComm.getInstance().getFriends();
    }

    @Override
    protected void onPostExecute(ArrayList<NetworkPlayer> friendList) {
        context.setContentView(R.layout.friend_list_settings);
        FriendListAdapter adapter = new FriendListAdapter(context, R.layout.friend_item, friendList);
        ListView listView = (ListView) context.findViewById(R.id.friendList);
        listView.setAdapter(adapter);
    }
}
