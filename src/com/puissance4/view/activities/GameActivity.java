package com.puissance4.view.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.puissance4.R;
import com.puissance4.configuration.GameConfiguration;
import com.puissance4.controller.button_controllers.OnGameButtonClickListener;
import com.puissance4.controller.sensor_controllers.ShakeDetector;
import com.puissance4.controller.sensor_controllers.ShakeListener;
import com.puissance4.model.Party;
import com.puissance4.model.Player;
import com.puissance4.server_com.network_handlers.NetworkComm;
import com.puissance4.server_com.ping_service.AdversaryMessagesReceiver;

public class GameActivity extends Activity {
    private LinearLayout gameGrid;
    private Party party;
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private ShakeDetector shakeDetector;
    private boolean testMode = false;
    private boolean isInGame = true;
    private AdversaryMessagesReceiver adversaryMessagesReceiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.puissance2);
        adversaryMessagesReceiver = new AdversaryMessagesReceiver();
        adversaryMessagesReceiver.setView(this);
        LocalBroadcastManager.getInstance(this).registerReceiver(adversaryMessagesReceiver, new IntentFilter("ADVMESS"));
        shakeDetector = new ShakeDetector(new ShakeListener(this));
        setupParty(savedInstanceState);
        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(shakeDetector, senAccelerometer, SensorManager.SENSOR_DELAY_GAME);
        buildGrid();
    }

    @Override
    public void onPause() {
        super.onPause();
        senSensorManager.unregisterListener(shakeDetector);
    }

    @Override
    protected void onResume() {
        super.onResume();
        senSensorManager.registerListener(shakeDetector, senAccelerometer, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(adversaryMessagesReceiver);
    }

    public void buildGrid() {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        gameGrid = (LinearLayout) findViewById(R.id.gamegrid);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            gameGrid.setWeightSum(GameConfiguration.GRID_HEIGHT);
            for (int i = 0; i < GameConfiguration.GRID_HEIGHT; i++) {
                LinearLayout row = new LinearLayout(this);
                row.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f));
                row.setWeightSum(GameConfiguration.GRID_WIDTH);
                for (int j = 0; j < GameConfiguration.GRID_WIDTH; j++) {
                    Button slot = buildButton(i, j);
                    row.addView(slot);
                }
                gameGrid.addView(row);
            }
        } else {
            gameGrid.setWeightSum(GameConfiguration.GRID_WIDTH);
            for (int i = 0; i < GameConfiguration.GRID_WIDTH; i++) {
                LinearLayout row = new LinearLayout(this);
                row.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f));
                row.setWeightSum(GameConfiguration.GRID_HEIGHT);
                for (int j = 0; j < GameConfiguration.GRID_HEIGHT; j++) {
                    Button slot = buildButton(i, j);
                    row.addView(slot);
                }
                gameGrid.addView(row);
            }
        }
    }

    private void setupParty(Bundle savedInstanceState) {
        //if (savedInstanceState == null) {
        if (getIntent().hasExtra("party")) { //GAME JUST STARTED
            party = (Party) getIntent().getSerializableExtra("party");
        } else {  //TEST MODE
            System.out.println("IT IS NULL --> TEST MODE");
            String[] players = {"Fred", "Cyrille"};
            testMode = true;
            party = new Party(players, GameConfiguration.GRID_HEIGHT, GameConfiguration.GRID_WIDTH);
        }
//        } else {  //GAME ALREADY STARTED
        if (party == null) {
            party = (Party) savedInstanceState.getSerializable("party");
            isInGame = savedInstanceState.getBoolean("isInGame", true);
            if (party == null) {
                String[] players = {savedInstanceState.getString("player1"), savedInstanceState.getString("player2")};
                if (players[0] == null || players[1] == null) {
                    Toast.makeText(getApplicationContext(), "Error while setting game", Toast.LENGTH_LONG);
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                }
                int userId = -1;
                for (int i = 0; i < players.length; i++) {
                    if (players[i].equals(GameConfiguration.USERNAME)) {
                        userId = i;
                    }
                }
                if (userId != -1) {
                    party = new Party(players, userId, GameConfiguration.GRID_HEIGHT - 1, GameConfiguration.GRID_WIDTH - 1);
                }
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
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                slotValue = party.getGrid().getGrid()[column - 1][row - 1];
            } else {
                slotValue = party.getGrid().getGrid()[row - 1][column - 1];
            }
            switch (slotValue) {
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

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putSerializable("party", party);
        savedInstanceState.putBoolean("isInGame", isInGame);
    }

    public void shuffle() {
        party.shuffle();
        new Thread(new Runnable() {
            @Override
            public void run() {
                NetworkComm.getInstance().sendRandom(party.getGrid().getGrid());
                setContentView(R.layout.puissance2);
                buildGrid();
            }
        }).start();
    }

    public boolean isInGame() {
        return isInGame;
    }

    public boolean isInGame(boolean isInGame) {
        return this.isInGame = isInGame;
    }

    public boolean isTestMode() {
        return testMode;
    }

    public void setTestMode(boolean testMode) {
        this.testMode = testMode;
    }

    public void opponentMove(int columnId) {
        if (isInGame) {
            int orientation = 0;
            int opponentId = 0;
            if (columnId >= GameConfiguration.GRID_WIDTH) {
                columnId = columnId - GameConfiguration.GRID_WIDTH;
                orientation = 1;
            }
            if (party.getPlayers()[0].getName().equals(GameConfiguration.USERNAME)) {
                opponentId = 1;
            }
            try {
                party.nextOpponentMove(columnId, orientation, party.getPlayers()[opponentId]);
                setContentView(R.layout.puissance2);
                buildGrid();
                Player winner = party.getWinner();
                if (winner != null) {
                    //////////////////////////// SEND WINNER INSTRUCTIONS (ONLY IF OPPONENT WIN)////////////////////////////////
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            NetworkComm.getInstance().sendWin(1);
                        }
                    }).start();
                    Toast.makeText(getApplicationContext(), winner.getName() + R.string.hasWon, Toast.LENGTH_LONG).show();
                    /*Intent intent = new Intent(context, MainActivity.class);
                    context.startActivity(intent);*/
                    isInGame = false;
                    //finish();   //MUST BE MODIFIED TO DISPLAY WIN SCREEN
                }
                if (party.isPartyNull()) {
                    Toast.makeText(this, R.string.partyNull, Toast.LENGTH_SHORT);
                    isInGame = false;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            NetworkComm.getInstance().sendWin(2);
                        }
                    }).start();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, R.string.impossibleOpponentMove, Toast.LENGTH_SHORT).show();
            }
        }
    }

    //réception d'un random de l'adversaire
    public void opponentRandom(Integer[][] grid) {
    }

    //réception d'une notification de victoire ou non
    //0 : perdu
    //1 : gagné
    //2 : égalité
    public void opponentWin(int result) {

    }
}
