package com.puissance4.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.Puissance4.R;
import com.puissance4.model.Party;
import com.puissance4.network_handler.NetworkComm;
import com.puissance4.network_handler.NetworkPlayer;
import com.puissance4.view.GameActivity;
import com.puissance4.configuration.GameConfiguration;
import com.puissance4.view.MainActivity;

import java.util.ArrayList;

/**
 * Created by fred on 10/01/15.
 */
public class OpponentPickerAdapter extends ArrayNetworkPlayerAdapter {
    public OpponentPickerAdapter(Context context, int resource, ArrayList<NetworkPlayer> objects) {
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
                    int result = NetworkComm.getInstance().proposeGame(opponent.getName(), GameConfiguration.GRID_HEIGHT, GameConfiguration.GRID_WIDTH);
                    String[] players = new String[2];
                    switch (result) {
                        case 0:
                            //CREATE PARTY WITH SELF AS FIRST PLAYER
                            Intent gameIntent = new Intent(context, GameActivity.class);
                            players[0] = GameConfiguration.USERNAME;
                            players[1] = opponent.getName();
                            gameIntent.putExtra("party", new Party(players, GameConfiguration.GRID_HEIGHT, GameConfiguration.GRID_WIDTH));
                            context.startActivity(gameIntent);
                            break;
                        case 1:
                            //CREATE PARTY WITH SELF AS SECOND PLAYER
                            Intent gameIntent1 = new Intent(context, GameActivity.class);
                            players[1] = GameConfiguration.USERNAME;
                            players[0] = opponent.getName();
                            gameIntent1.putExtra("party", new Party(players, GameConfiguration.GRID_HEIGHT, GameConfiguration.GRID_WIDTH));
                            context.startActivity(gameIntent1);
                            break;
                        case 2:
                            //RETURN TO MAIN ACTIVITY
                            Toast.makeText(context, R.string.gameRefused, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context, MainActivity.class);
                            context.startActivity(intent);
                            break;
                        case 3:
                            //RETURN TO MAIN ACTIVITY
                            Toast.makeText(context, R.string.proposeGameError, Toast.LENGTH_SHORT).show();
                            Intent intent1 = new Intent(context, MainActivity.class);
                            context.startActivity(intent1);
                        default:
                            //RETURN TO MAIN ACTIVITY
                            Toast.makeText(context, R.string.proposeGameUnhandledError, Toast.LENGTH_SHORT).show();
                            Intent intent2 = new Intent(context, MainActivity.class);
                            context.startActivity(intent2);
                    }
                }
            });
        }
        return v;
    }
}
