package com.puissance4.view;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;

import com.example.Puissance4.R;
import com.puissance4.adapter.OpponentPickerAdapter;
import com.puissance4.network_handler.NetworkComm;
import com.puissance4.network_handler.NetworkPlayer;

import java.util.ArrayList;

/**
 * Created by fred on 10/01/15.
 */
public class NearPlayerPickerActivity extends Activity {
    private NearPlayerPickerActivity myself = this;
    private class GetPlayersAsyncTask extends AsyncTask<Void, Void, ArrayList<NetworkPlayer>> {
        private String tempUsername;
        private String tempPassword;

        @Override
        protected ArrayList<NetworkPlayer> doInBackground(Void... params) {
            return NetworkComm.getInstance().getNearPlayers();
        }

        @Override
        protected void onPostExecute(ArrayList<NetworkPlayer> nearPlayerList) {
            setContentView(R.layout.list_pick);
            OpponentPickerAdapter adapter = new OpponentPickerAdapter(myself, R.layout.friend_item, nearPlayerList);
            ListView listView = (ListView) findViewById(R.id.pickList);
            listView.setAdapter(adapter);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading);
        new GetPlayersAsyncTask().execute();
        /*
        ArrayList<NetworkPlayer> nearPlayerList = NetworkComm.getInstance().getNearPlayers();
        setContentView(R.layout.list_pick);
        OpponentPickerAdapter adapter = new OpponentPickerAdapter(this, R.layout.friend_item, nearPlayerList);
        ListView listView = (ListView) findViewById(R.id.pickList);
        listView.setAdapter(adapter);
        */
    }
}