package com.puissance4.controller;

import android.content.Intent;
import android.view.View;
import com.puissance4.view.FriendListActivity;
import com.puissance4.view.MainActivity;

/**
 * Created by fred on 09/01/15.
 */
public class FriendListButtonListener extends ActivityListener {
    public FriendListButtonListener(MainActivity mainActivity) {
        super(mainActivity);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(context, FriendListActivity.class);
        context.startActivity(intent);
    }
}
