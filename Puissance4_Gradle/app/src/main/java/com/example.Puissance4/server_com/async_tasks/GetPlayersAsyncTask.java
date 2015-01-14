package com.example.Puissance4.server_com.async_tasks;

/**
 * Created by fred on 12/01/15.
 */

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.ListView;
import com.example.Puissance4.R;
import com.example.Puissance4.server_com.network_handlers.NetworkPlayer;
import com.example.Puissance4.view.adapters.OpponentPickerAdapter;

import java.util.ArrayList;

public abstract class GetPlayersAsyncTask extends AsyncTask<Void, Void, ArrayList<NetworkPlayer>> {
    protected Activity context;

    public GetPlayersAsyncTask(Activity context) {
        this.context = context;
    }

    @Override
    protected void onPostExecute(ArrayList<NetworkPlayer> nearPlayerList) {
        context.setContentView(R.layout.list_pick);
        OpponentPickerAdapter adapter = new OpponentPickerAdapter(context, R.layout.friend_item, nearPlayerList);
        ListView listView = (ListView) context.findViewById(R.id.pickList);
        listView.setAdapter(adapter);
    }
}