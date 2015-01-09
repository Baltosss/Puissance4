package com.puissance4.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import com.example.Puissance4.R;
import com.puissance4.controller.OnGameButtonClickListener;
import com.puissance4.exceptions.*;
import com.puissance4.model.Grid;
import com.puissance4.model.Party;
import com.puissance4.model.Player;

import java.util.ArrayList;

public class GameActivity extends Activity {
    private LinearLayout gameGrid;
    private ArrayList<ArrayList<Button>> grid;
    private Party party;
    /**
     * Called when the activity is first created.
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.puissance2);
        setupParty(savedInstanceState);
        buildGrid();
    }

    public void buildGrid() {
        grid = new ArrayList<ArrayList<Button>>();  //not necessary
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        gameGrid = (LinearLayout) findViewById(R.id.gamegrid);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            gameGrid.setWeightSum(GameConfiguration.GRID_HEIGHT);
            for (int i = 0; i < GameConfiguration.GRID_HEIGHT; i++) {
                grid.add(new ArrayList<Button>());
                //LinearLayout row = (LinearLayout) inflater.inflate(R.layout.row, null);
                LinearLayout row = new LinearLayout(this);
                row.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f));
                row.setWeightSum(GameConfiguration.GRID_WIDTH);
                for (int j = 0; j < GameConfiguration.GRID_WIDTH; j++) {
                    //Button slot = (Button) inflater.inflate(R.layout.button, null);
                    Button slot = buildButton(i, j);
                    row.addView(slot);
                    grid.get(i).add(slot);
                }
                gameGrid.addView(row);
            }
        }
        else {
            gameGrid.setWeightSum(GameConfiguration.GRID_WIDTH);
            for (int i = 0; i < GameConfiguration.GRID_WIDTH; i++) {
                grid.add(new ArrayList<Button>());
                LinearLayout row = new LinearLayout(this);
                row.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f));
                row.setWeightSum(GameConfiguration.GRID_HEIGHT);
                for (int j = 0; j < GameConfiguration.GRID_HEIGHT; j++) {
                    Button slot = buildButton(i, j);
                    row.addView(slot);
                    grid.get(i).add(slot);
                }
                gameGrid.addView(row);
            }
        }
    }

    private void setupParty(Bundle savedInstanceState) {
        if(savedInstanceState == null) {
            System.out.println("IT IS NULL --> TEST MODE");
            String[] players = {"Fred", "Cyrille"};
            party = new Party(players, GameConfiguration.GRID_HEIGHT, GameConfiguration.GRID_WIDTH);
        }
        else {
            party = (Party) savedInstanceState.getSerializable("party");
            if(party == null) {
                String[] players = {savedInstanceState.getString("player1"), savedInstanceState.getString("player2")};
                if(players[0] == null || players[1] == null) {
                    Toast.makeText(getApplicationContext(), "Error while setting game",Toast.LENGTH_LONG);
                    return; //HAS TO BE MODIFIED TO GO BACK ON MAIN SCREEN
                }
                party = new Party(players, GameConfiguration.GRID_HEIGHT-1, GameConfiguration.GRID_WIDTH-1);
            }
        }
    }
    //Row and column are coordinates of the button in the screen grid, not the game one.
    private Button buildButton(final int row, final int column) {
        Button slot = new Button(this);
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f);
        slot.setLayoutParams(buttonParams);
        if (row == 0 && column == 0) {
            slot.setBackground(getResources().getDrawable(R.drawable.slot_black));
            slot.setEnabled(false);
        } else if (row == 0) {
            slot.setBackground(getResources().getDrawable(R.drawable.slot_red_star));
            slot.setEnabled(true);
            slot.setOnClickListener(new OnGameButtonClickListener(this, party, column));
        } else if (column == 0) {
            slot.setBackground(getResources().getDrawable(R.drawable.slot_star));
            slot.setEnabled(false);
        } else {
            int slotValue;
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            {
                slotValue = party.getGrid().getGrid()[column - 1][row - 1];
            }
            else {
                slotValue = party.getGrid().getGrid()[row - 1][column - 1];
            }
            switch(slotValue) {
                case -2:
                    slot.setBackground(getResources().getDrawable(R.drawable.slot_black));
                case -1:
                    slot.setBackground(getResources().getDrawable(R.drawable.slot_white));
                    break;
                case 0:
                    slot.setBackground(getResources().getDrawable(R.drawable.slot_red));
                    break;
                case 1:
                    slot.setBackground(getResources().getDrawable(R.drawable.slot_yellow));
                    break;
            }
            slot.setEnabled(false);
        }
        return slot;
    }

    public ArrayList<ArrayList<Button>> getGrid() {
        return grid;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        savedInstanceState.putSerializable("party", party);
        // etc.
    }
}
