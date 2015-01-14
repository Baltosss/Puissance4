package com.example.Puissance4.
import android.content.Intent;
import android.view.View;
import android.widget.Toast;
import com.example.Puissance4.configuration.GameConfiguration;
import com.example.Puissance4.view.activities.FriendListActivity;
import com.example.Puissance4.view.activities.MainActivity;

/**
 * Created by fred on 09/01/15.
 */
public class FriendListButtonListener extends com.example.Puissance4.controller.button_controllers.ActivityListener {
    public FriendListButtonListener(MainActivity mainActivity) {
        super(mainActivity);
    }

    @Override
    public void onClick(View view) {
        if(GameConfiguration.USERNAME == null) {
            Toast.makeText(context, R.string.connectBeforeAccessFriends, Toast.LENGTH_SHORT).show();
        }
        else {
            Intent intent = new Intent(context, FriendListActivity.class);
            context.startActivity(intent);
        }
    }
}
