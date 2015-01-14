package com.puissance4.controller.button_controllers;

import android.content.res.Configuration;
import android.view.View;
import android.widget.Toast;

import com.example.puissance4.R;
import com.puissance4.configuration.GameConfiguration;
import com.puissance4.model.Party;
import com.puissance4.model.Player;
import com.puissance4.model.exceptions.FullColumnException;
import com.puissance4.model.exceptions.FullRowException;
import com.puissance4.model.exceptions.ImpossibleColumnPlayException;
import com.puissance4.model.exceptions.ImpossibleRowPlayException;
import com.puissance4.model.exceptions.NoneMoveException;
import com.puissance4.model.exceptions.NotPlayerTurnException;
import com.puissance4.server_com.network_handlers.NetworkComm;
import com.puissance4.view.activities.GameActivity;

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
            int gColumn = column -1;
            if(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                party.nextMove(gColumn, 0);
            }
            else {
                party.nextMove(column - 1, 1);
                gColumn = GameConfiguration.GRID_HEIGHT + gColumn;
            }
            /////////////////////////// SEND MOVE INSTRUCTIONS /////////////////////////////////////
            final int finalGColumn = gColumn;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    NetworkComm.getInstance().sendMove(finalGColumn);
                }
            }).start();
            context.setContentView(R.layout.puissance2);
            ((GameActivity)context).buildGrid();
            Player winner = party.getWinner();
            if(winner!=null) {
                //////////////////////////// SEND WINNER INSTRUCTIONS (ONLY IF I WIN)////////////////////////////////
                if(winner.equals(GameConfiguration.USERNAME)) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            NetworkComm.getInstance().sendWin(true);
                        }
                    }).start();
                }
                Toast.makeText(context.getApplicationContext(), winner.getName() + " has won", Toast.LENGTH_LONG).show();
                /*Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);*/
                context.finish();
            }
        } catch (FullColumnException e) {
            e.printStackTrace();
            Toast.makeText(context, R.string.fullColumnError, Toast.LENGTH_SHORT).show();
        } catch (ImpossibleColumnPlayException e) {
            e.printStackTrace();
            Toast.makeText(context, R.string.impossibleColumnPlayError, Toast.LENGTH_SHORT).show();
        } catch (NoneMoveException e) {
            e.printStackTrace();
            Toast.makeText(context, R.string.noneMoveError, Toast.LENGTH_SHORT).show();
        } catch (FullRowException e) {
            e.printStackTrace();
            Toast.makeText(context, R.string.fullRowError, Toast.LENGTH_SHORT).show();
        } catch (ImpossibleRowPlayException e) {
            e.printStackTrace();
            Toast.makeText(context, R.string.impossibleRowPlayError, Toast.LENGTH_SHORT).show();
        } catch (NotPlayerTurnException e) {
            e.printStackTrace();
            Toast.makeText(context, R.string.notYourTurnError, Toast.LENGTH_SHORT).show();
        }
    }
}
