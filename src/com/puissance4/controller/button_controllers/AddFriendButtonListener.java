package com.puissance4.controller.button_controllers;

import android.view.View;
import android.widget.EditText;

import com.example.puissance4.R;
import com.puissance4.server_com.async_tasks.AddFriendAsyncTask;
import com.puissance4.view.activities.FriendListActivity;

/**
 * Created by fred on 14/01/15.
 * Listener of the add friend button in FriendListActivity
 */
public class AddFriendButtonListener extends ActivityListener {
    public AddFriendButtonListener(FriendListActivity context) {
        super(context);
    }

    @Override
    public void onClick(View v) {
        String friendName = ((EditText) context.findViewById(R.id.editFriendUsername)).getText().toString();
        new AddFriendAsyncTask((FriendListActivity) context).execute(friendName);
    }
}
