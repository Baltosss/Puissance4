package com.puissance4.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.puissance4.R;
import com.puissance4.server_com.async_tasks.RemoveFriendAsyncTask;
import com.puissance4.server_com.network_handlers.NetworkPlayer;
import com.puissance4.view.activities.FriendListActivity;

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
        if (friend != null) {
            ImageView statusView = (ImageView) v.findViewById(R.id.imageFriendItem);
            TextView friendText = (TextView) v.findViewById(R.id.textFriendItem);
            Button deleteButton = (Button) v.findViewById(R.id.buttonFriendItem);
            switch (friend.getStatus()) {
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
                    ////////////////// REMOVE A FRIEND /////////////////////////////
                    new RemoveFriendAsyncTask((FriendListActivity) context).execute(friend.getName());
                }
            });
        }
        return v;
    }
}
