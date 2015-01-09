package com.puissance4.controller;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.view.View;
import android.widget.Toast;
import com.example.Puissance4.R;
import com.puissance4.exceptions.*;
import com.puissance4.model.Party;
import com.puissance4.model.Player;
import com.puissance4.view.GameActivity;

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
            }
            else {
                party.nextMove(column - 1, 1);
            }
            context.setContentView(R.layout.puissance2);
            ((GameActivity)context).buildGrid();
            Player winner = party.getWinner();
            if(winner!=null) {
                Toast.makeText(context.getApplicationContext(), winner.getName() + " has won", Toast.LENGTH_LONG).show();
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
