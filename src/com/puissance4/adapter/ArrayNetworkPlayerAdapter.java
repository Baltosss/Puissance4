package com.puissance4.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;
import com.puissance4.network_handler.NetworkPlayer;

import java.util.ArrayList;

/**
 * Created by fred on 10/01/15.
 */
public abstract class ArrayNetworkPlayerAdapter extends ArrayAdapter<NetworkPlayer> {
    protected ArrayList<NetworkPlayer> objects;
    protected Context context;
    public ArrayNetworkPlayerAdapter(Context context, int resource, ArrayList<NetworkPlayer> objects) {
        super(context, resource, objects);
        this.objects = objects;
        this.context = context;
    }
}
