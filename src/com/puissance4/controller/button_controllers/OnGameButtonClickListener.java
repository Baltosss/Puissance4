package com.puissance4.controller.button_controllers;

import android.content.Intent;
import android.content.res.Configuration;
import android.view.View;
import android.widget.Toast;
import com.example.Puissance4.R;
import com.puissance4.configuration.GameConfiguration;
import com.puissance4.model.exceptions.*;
import com.puissance4.model.Party;
import com.puissance4.model.Player;
import com.puissance4.server_com.network_handlers.NetworkComm;
import com.puissance4.view.activities.GameActivity;
import com.puissance4.view.activities.MainActivity;

/**
 * Created by fred on 08/01/15.
 */
public class OnGameButtonClickListener extends ActivityListener {
    private Party party;
    private int column;
    public OnGameButtonClickListener(GameActivity context, Party party, int column) {
        super(context);
        this.party = party;
        this.column = column;
    }

    @Override
    public void onClick(View view) {
        try {
            if(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                party.nextMove(column - 1, 0);
                /////////////////////////// SEND MOVE INSTRUCTIONS /////////////////////////////////////
                NetworkComm.getInstance().sendMove(column - 1);
            }
            else {
                party.nextMove(column - 1, 1);
                NetworkComm.getInstance().sendMove(GameConfiguration.GRID_HEIGHT + column - 1);
            }
            context.setContentView(R.layout.puissance2);
            ((GameActivity)context).buildGrid();
            Player winner = party.getWinner();
            if(winner!=null) {
                //////////////////////////// SEND WINNER INSTRUCTIONS (ONLY IF I WIN)////////////////////////////////
                if(winner.equals(GameConfiguration.USERNAME)) {
                    NetworkComm.getInstance().sendWin(true);
                }
                Toast.makeText(context.getApplicationContext(), winner.getName() + " has won", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);
            }
        } catch (FullColumnException e) {
            e.printStackTrace();
        } catch (ImpossibleColumnPlayException e) {
            e.printStackTrace();
        } catch (NoneMoveException e) {
            e.printStackTrace();
        } catch (FullRowException e) {
            e.printStackTrace();
        } catch (ImpossibleRowPlayException e) {
            e.printStackTrace();
        }
    }
}
