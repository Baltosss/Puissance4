package com.puissance4.view.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.puissance4.R;
import com.puissance4.server_com.network_handlers.NetworkComm;
import com.puissance4.view.activities.FriendListActivity;

import java.io.IOException;

/**
 * Created by fred on 09/01/15.
 */
public class PseudoFriendListAdapter extends ArrayAdapter<String>{
    private String[] objects;
    private Context context;
    public PseudoFriendListAdapter(Context context, int textViewResourceId, String[] objects) {
        super(context, textViewResourceId, objects);
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
        final String friend = objects[position];
        if(friend != null) {
            ImageView statusView = (ImageView) v.findViewById(R.id.imageFriendItem);
            TextView friendText = (TextView) v.findViewById(R.id.textFriendItem);
            Button deleteButton = (Button) v.findViewById(R.id.buttonFriendItem);
            statusView.setImageResource(R.drawable.icon_red_dot);
            friendText.setText(friend);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                ((Activity)context).setContentView(R.layout.loading);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        NetworkComm.getInstance().removeFriend(friend);
                        Toast.makeText(context, R.string.friendRemoved, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, FriendListActivity.class);
                        context.startActivity(intent);
                    }
                }).start();
                }
            });
        }
        return v;
    }
}
