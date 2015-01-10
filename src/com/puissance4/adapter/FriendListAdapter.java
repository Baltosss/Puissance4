package com.puissance4.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.Puissance4.R;
import com.puissance4.server_handler.NetworkComm;
import com.puissance4.server_handler.NetworkPlayer;
import com.puissance4.view.FriendListActivity;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by fred on 09/01/15.
 */
public class FriendListAdapter extends ArrayNetworkPlayerAdapter {

    public FriendListAdapter(Context context, int resource, ArrayList<NetworkPlayer> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.friend_item, null);
        }
        final NetworkPlayer friend = objects.get(position);
        if(friend != null) {
            ImageView statusView = (ImageView) v.findViewById(R.id.imageFriendItem);
            TextView friendText = (TextView) v.findViewById(R.id.textFriendItem);
            Button deleteButton = (Button) v.findViewById(R.id.buttonFriendItem);
            switch(friend.getStatus()) {
                case 0:
                    statusView.setImageResource(R.drawable.icon_green_dot);
                    break;
                case 1:
                    statusView.setImageResource(R.drawable.icon_orange_dot);
                    break;
                case 2:
                    statusView.setImageResource(R.drawable.icon_red_dot);
                    break;
                default:
                    statusView.setImageResource(R.drawable.icon_unknown);
                    break;
            }
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
