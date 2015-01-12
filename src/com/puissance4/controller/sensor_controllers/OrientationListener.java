package com.puissance4.controller.sensor_controllers;

import android.content.Context;
import android.view.OrientationEventListener;
import android.widget.Toast;
import com.example.Puissance4.R;
import com.puissance4.view.activities.GameActivity;
import com.puissance4.configuration.GameConfiguration;

/**
 * Created by fred on 04/01/15.
 */
@Deprecated
public class OrientationListener extends OrientationEventListener{
    private Context context;
    private boolean isPortrait;
    public OrientationListener(Context context) {
        super(context);
        this.context = context;
        isPortrait = true;
    }

    @Override
    public void onOrientationChanged(int i) {
        if(isPortrait && i>70 && i<110) {
            isPortrait = false;
            Toast toast = Toast.makeText(context, "Landscape",Toast.LENGTH_SHORT );
            toast.show();
            for(int index=0 ; i< GameConfiguration.GRID_HEIGHT; i++) {

                //((MyActivity)context).getGrid().get(index).get(0).postInvalidate();
                ((GameActivity) context).getGrid().get(index).get(0).setBackground(context.getResources().getDrawable(R.drawable.slot_red_star));
            }
            for(int index=0 ; i< GameConfiguration.GRID_WIDTH; i++) {
                ((GameActivity) context).getGrid().get(0).get(index).setBackground(context.getResources().getDrawable(R.drawable.slot_star));
            }
            ((GameActivity)context).findViewById(R.id.gamegrid).getRootView().invalidate();
        }
        else if(!isPortrait && i<20 && i>-20) {
            isPortrait = true;
            Toast toast = Toast.makeText(context, "Portrait",Toast.LENGTH_SHORT );
            toast.show();
            for(int index=0 ; i< GameConfiguration.GRID_WIDTH; i++) {
                ((GameActivity)context).getGrid().get(0).get(index).setBackground(context.getResources().getDrawable(R.drawable.slot_red_star));
            }
            for(int index=0 ; i< GameConfiguration.GRID_HEIGHT; i++) {
                ((GameActivity) context).getGrid().get(index).get(0).setBackground(context.getResources().getDrawable(R.drawable.slot_star));
            }
            ((GameActivity)context).findViewById(R.id.gamegrid).invalidate();
        }
    }
}
