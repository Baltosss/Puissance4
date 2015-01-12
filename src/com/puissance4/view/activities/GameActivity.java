package com.puissance4.view.activities;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.*;
import com.example.Puissance4.R;
import com.puissance4.configuration.GameConfiguration;
import com.puissance4.controller.button_controllers.OnGameButtonClickListener;
import com.puissance4.controller.sensor_controllers.ShakeDetector;
import com.puissance4.controller.sensor_controllers.ShakeListener;
import com.puissance4.model.Party;

import java.util.ArrayList;

public class GameActivity extends Activity {
    private LinearLayout gameGrid;
    private ArrayList<ArrayList<Button>> grid;
    private Party party;
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private ShakeDetector shakeDetector;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.puissance2);
        shakeDetector = new ShakeDetector(new ShakeListener(this));
        setupParty(savedInstanceState);
        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(shakeDetector, senAccelerometer , SensorManager.SENSOR_DELAY_GAME);
        buildGrid();
    }

    public void onPause() {
        super.onPause();
        senSensorManager.unregisterListener(shakeDetector);
    }

    protected void onResume() {
        super.onResume();
        senSensorManager.registerListener(shakeDetector, senAccelerometer, SensorManager.SENSOR_DELAY_GAME);
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
            if(getIntent().hasExtra("party")) { //GAME JUST STARTED
                party = (Party)getIntent().getSerializableExtra("party");
            }
            else {  //TEST MODE
                System.out.println("IT IS NULL --> TEST MODE");
                String[] players = {"Fred", "Cyrille"};
                party = new Party(players, GameConfiguration.GRID_HEIGHT, GameConfiguration.GRID_WIDTH);
            }
        }
        else {  //GAME ALREADY STARTED
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
        savedInstanceState.putSerializable("party", party);
    }
}