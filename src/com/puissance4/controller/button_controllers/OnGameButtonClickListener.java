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
            if (((GameActivity) context).isInGame()) {
                int gColumn = column - 1;
                if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    party.nextMove(gColumn, 0);
                } else {
                    party.nextMove(column - 1, 1);
                    gColumn = GameConfiguration.GRID_WIDTH + gColumn;
                }
                if (!((GameActivity) context).isTestMode()) {
                    /////////////////////////// SEND MOVE INSTRUCTIONS /////////////////////////////////////
                    final int finalGColumn = gColumn;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            NetworkComm.getInstance().sendMove(finalGColumn);
                        }
                    }).start();
                }
                context.setContentView(R.layout.puissance2);
                ((GameActivity) context).buildGrid();
                if(((GameActivity) context).isTestMode()) {
                    Player winner = party.getWinner();
                    if (winner != null) {
                        ((GameActivity) context).opponentWin(1);
                    } else if (party.isPartyNull()) {
                        Toast.makeText(context, R.string.partyNull, Toast.LENGTH_SHORT);
                        ((GameActivity) context).isInGame(false);
                    }
                }
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
