package com.puissance4.view.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.puissance4.R;
import com.puissance4.server_com.async_tasks.StartGameAsyncTask;
import com.puissance4.server_com.network_handlers.NetworkPlayer;

import java.util.ArrayList;

/**
 * Created by fred on 10/01/15.
 */
public class OpponentPickerAdapter extends ArrayNetworkPlayerAdapter {

    public OpponentPickerAdapter(Activity context, int resource, ArrayList<NetworkPlayer> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.friend_item, null);
        }
        final NetworkPlayer opponent = objects.get(position);
        if(opponent != null) {
            ImageView statusView = (ImageView) v.findViewById(R.id.imageFriendItem);
            TextView friendText = (TextView) v.findViewById(R.id.textFriendItem);
            Button playButton = (Button) v.findViewById(R.id.buttonFriendItem);
            switch(opponent.getStatus()) {
                case 0:
                    statusView.setImageResource(R.drawable.icon_green_dot);
                    playButton.setText(R.string.playButton);
                    break;
                case 1:
                    statusView.setImageResource(R.drawable.icon_orange_dot);
                    playButton.setVisibility(View.GONE);
                    break;
                case 2:
                    statusView.setImageResource(R.drawable.icon_red_dot);
                    playButton.setVisibility(View.GONE);
                    break;
                default:
                    statusView.setImageResource(R.drawable.icon_unknown);
                    playButton.setVisibility(View.GONE);
                    break;
            }
            friendText.setText(opponent.getName());
            playButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ////////////////// ASK FOR PARTY /////////////////////////////
                    ((Activity) context).setContentView(R.layout.loading);
                    ((TextView) ((Activity) context).findViewById(R.id.loadingText)).setText(R.string.waitingPlayerAnswer);
                    new StartGameAsyncTask((Activity)context, opponent).execute();
                }
            });
        }
        return v;
    }


    }
