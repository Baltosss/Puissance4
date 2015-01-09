package com.puissance4.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.example.Puissance4.R;
import com.puissance4.model.Player;
import com.puissance4.server_handler.NetworkComm;
import com.puissance4.view.FriendListActivity;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by fred on 09/01/15.
 */
public class FriendListAdapter extends ArrayAdapter<Player> {
    private ArrayList<Player> objects;
    private Context context;
    public FriendListAdapter(Context context, int resource, ArrayList<Player> objects) {
        super(context, resource, objects);
        this.objects = objects;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.friend_item, null);
        }
        final Player friend = objects.get(position);
        if(friend != null) {
            TextView friendText = (TextView) v.findViewById(R.id.textFriendItem);
            Button deleteButton = (Button) v.findViewById(R.id.buttonFriendItem);
            friendText.setText(friend.getName());
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        ////////////////// REMOVE A FRIEND /////////////////////////////
                        NetworkComm.getInstance().connect();
                        NetworkComm.getInstance().removeFriend(friend.getName());
                        NetworkComm.getInstance().disconnect();
                        Toast.makeText(context, R.string.friendRemoved, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, FriendListActivity.class);
                        context.startActivity(intent);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(context, R.string.connectCommunicationError, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        return v;
    }
}
