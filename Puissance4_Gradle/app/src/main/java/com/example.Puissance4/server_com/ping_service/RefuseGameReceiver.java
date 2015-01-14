package com.example.Puissance4.server_com.ping_service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.example.Puissance4.server_com.network_handlers.NetworkComm;

/**
 * Created by cyrille on 13/01/15.
 */
public class RefuseGameReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NetworkComm.getInstance().answerProposal(false);
    }
}
