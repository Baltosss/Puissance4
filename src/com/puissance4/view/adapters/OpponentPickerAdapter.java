package com.puissance4.view.adapters;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
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
    private Location myLocation;

    public OpponentPickerAdapter(Activity context, int resource, ArrayList<NetworkPlayer> objects) {
        super(context, resource, objects);
        LocationManager locationManager = ((LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE));
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_MEDIUM);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        myLocation = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, true));
    }

    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.friend_item, null);
        }

        final NetworkPlayer opponent = objects.get(position);

        String opponentDistanceText = null;
        if (myLocation != null) {
            float[] results = new float[3];
            Location.distanceBetween(myLocation.getLatitude(), myLocation.getLongitude(), opponent.getLatitude(), opponent.getLongitude(), results);
            float opponentDistance = results[0];
            if (opponentDistance < 1000) {
                opponentDistanceText = Integer.toString(Math.round(opponentDistance)) + "m";
            } else {
                opponentDistanceText = Integer.toString(Math.round(opponentDistance / 1000)) + "km";
            }
        }

        if (opponent != null) {
            ImageView statusView = (ImageView) v.findViewById(R.id.imageFriendItem);
            TextView friendText = (TextView) v.findViewById(R.id.textFriendItem);
            TextView distanceText = (TextView) v.findViewById(R.id.textFriendDistanceItem);
            Button playButton = (Button) v.findViewById(R.id.buttonFriendItem);
            switch (opponent.getStatus()) {
                case 0:
                    if (opponentDistanceText != null) {
                        distanceText.setText(opponentDistanceText);
                    }
                    statusView.setImageResource(R.drawable.icon_green_dot);
                    playButton.setText(R.string.playButton);
                    break;
                case 1:
                    if (opponentDistanceText != null) {
                        distanceText.setText(opponentDistanceText);
                    }
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
                    new StartGameAsyncTask((Activity) context, opponent).execute();
                }
            });
        }
        return v;
    }


}
