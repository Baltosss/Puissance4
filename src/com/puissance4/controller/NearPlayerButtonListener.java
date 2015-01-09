package com.puissance4.controller;

import android.view.View;
import android.widget.Toast;
import com.example.Puissance4.R;
import com.puissance4.view.GameConfiguration;
import com.puissance4.view.MainActivity;

/**
 * Created by fred on 09/01/15.
 */
public class NearPlayerButtonListener extends ActivityListener {
    public NearPlayerButtonListener(MainActivity mainActivity) {
        super(mainActivity);
    }

    @Override
    public void onClick(View view) {
        if(GameConfiguration.USERNAME == null) {
            Toast.makeText(context, R.string.connectBeforePlay, Toast.LENGTH_SHORT).show();
        }
        else {
            //Start party
        }
    }
}
