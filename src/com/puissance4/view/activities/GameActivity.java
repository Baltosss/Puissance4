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
import android.widget.TextView;
import android.widget.Toast;

import com.example.puissance4.R;
import com.puissance4.configuration.GameConfiguration;
import com.puissance4.controller.button_controllers.OnGameButtonClickListener;
import com.puissance4.controller.sensor_controllers.ShakeDetector;
import com.puissance4.controller.sensor_controllers.ShakeListener;
import com.puissance4.model.Party;
import com.puissance4.model.Player;
import com.puissance4.model.exceptions.NotPlayerTurnException;
import com.puissance4.model.exceptions.WrongHeightException;
import com.puissance4.model.exceptions.WrongWidthException;
import com.puissance4.server_com.network_handlers.NetworkComm;
import com.puissance4.server_com.network_service.AdversaryMessagesReceiver;

public class GameActivity extends Activity {
    private LinearLayout gameGrid;
    private Party party;
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private ShakeDetector shakeDetector;
    private boolean testMode = false;
    private boolean isInGame = true;
    private boolean isEndGameScreen = false;
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

        /*if(isInGame) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    NetworkComm.getInstance().leaveGame();
                }
            }).start();
        }*/ //WHEN YOU TURN THE DEVICE YOU CALL ONDESTROY
        //YOU MUST FIND ANOTHER WAY

        LocalBroadcastManager.getInstance(this).unregisterReceiver(adversaryMessagesReceiver);
    }

    public void buildGrid() {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        gameGrid = (LinearLayout) findViewById(R.id.gamegrid);
        TextView currentTurnText = (TextView) findViewById(R.id.currentTurnText);
        currentTurnText.setText(party.getCurrentPlayer().getName());
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            gameGrid.setWeightSum(GameConfiguration.GRID_HEIGHT + 1);
            for (int i = 0; i < (GameConfiguration.GRID_HEIGHT + 1); i++) {
                LinearLayout row = new LinearLayout(this);
                row.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f));
                row.setWeightSum(GameConfiguration.GRID_WIDTH + 1);
                for (int j = 0; j < (GameConfiguration.GRID_WIDTH + 1); j++) {
                    Button slot = buildButton(i, j);
                    row.addView(slot);
                }
                gameGrid.addView(row);
            }
        } else {
            gameGrid.setWeightSum(GameConfiguration.GRID_WIDTH + 1);
            for (int i = 0; i < (GameConfiguration.GRID_WIDTH + 1); i++) {
                LinearLayout row = new LinearLayout(this);
                row.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f));
                row.setWeightSum(GameConfiguration.GRID_HEIGHT);
                for (int j = 0; j < (GameConfiguration.GRID_HEIGHT + 1); j++) {
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
        } else if(savedInstanceState == null) {
            if (party == null) {
                //TEST MODE
                System.out.println("IT IS NULL --> TEST MODE");
                String[] players = {"Fred", "Cyrille"};
                testMode = true;
                party = new Party(players, GameConfiguration.GRID_HEIGHT, GameConfiguration.GRID_WIDTH);
            }
        }
        else {  //GAME ALREADY STARTED
            party = (Party) savedInstanceState.getSerializable("party");
            isInGame = savedInstanceState.getBoolean("isInGame", true);
            isEndGameScreen = savedInstanceState.getBoolean("isEndGameScreen", false);
            if (party == null) {
                String[] players = {savedInstanceState.getString("player1"), savedInstanceState.getString("player2")};
                if (players[0] == null || players[1] == null) {
                    Toast.makeText(getApplicationContext(), R.string.partySettingError, Toast.LENGTH_LONG);
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
                    party = new Party(players, userId, GameConfiguration.GRID_HEIGHT, GameConfiguration.GRID_WIDTH);
                }
            }
        }
    }

    //Row and column are coordinates of the button in the screen grid, not the game one.
    private Button buildButton(final int row, final int column) {
        boolean isLastMove = false;
        Button slot = new Button(this);
        int endRow = row - 1;
        int endColumn = column - 1;
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
                isLastMove = ((party.getLastSlotColumn() == (column - 1)) && (party.getLastSlotRow() == (row - 1)));
                slotValue = party.getGrid().getGrid()[column - 1][row - 1];

            } else {
                isLastMove = ((party.getLastSlotColumn() == row - 1) && (party.getLastSlotRow() == column - 1));
                slotValue = party.getGrid().getGrid()[row - 1][column - 1];
                endColumn = endRow;
                endRow = column - 1;
            }
            switch (slotValue) {
                case -2:
                    slot.setBackground(getResources().getDrawable(R.drawable.slot_black));
                case -1:
                    slot.setBackground(getResources().getDrawable(R.drawable.slot_white));
                    break;
                case 0:
                    if(isEndGameScreen) {
                        buildEndGameScreenButton(slot, slotValue, isLastMove, endRow, endColumn);
                    }
                    else {
                        if (isLastMove) {
                            slot.setBackground(getResources().getDrawable(R.drawable.slot_last_red));
                        } else {
                            slot.setBackground(getResources().getDrawable(R.drawable.slot_red));
                        }
                    }
                    break;
                case 1:
                    if(isEndGameScreen) {
                        buildEndGameScreenButton(slot, slotValue, isLastMove, endRow, endColumn);
                    }
                    else {
                        if (isLastMove) {
                            slot.setBackground(getResources().getDrawable(R.drawable.slot_last_yellow));
                        } else {
                            slot.setBackground(getResources().getDrawable(R.drawable.slot_yellow));
                        }
                    }
                    break;
            }
            slot.setEnabled(false);
        }
        return slot;
    }

    private void buildEndGameScreenButton(Button slot, int slotValue, boolean isLastMove, int row, int column) {
        switch (party.getWinDirection()) {
            case horizontal:
                if(slotValue==0) {
                    if (isLastMove) {
                        slot.setBackground(getResources().getDrawable(R.drawable.slot_last_hori_red));
                    } else {
                        if(party.isInWinSlots(column, row)) {
                            slot.setBackground(getResources().getDrawable(R.drawable.slot_hori_red));
                        }
                        else {
                            slot.setBackground(getResources().getDrawable(R.drawable.slot_red));
                        }
                    }
                }
                else {
                    if (isLastMove) {
                        slot.setBackground(getResources().getDrawable(R.drawable.slot_last_hori_yellow));
                    } else {
                        if(party.isInWinSlots(column, row)) {
                            slot.setBackground(getResources().getDrawable(R.drawable.slot_hori_yellow));
                        }
                        else {
                            slot.setBackground(getResources().getDrawable(R.drawable.slot_yellow));
                        }
                    }
                }
                break;
            case vertical:
                if(slotValue==0) {
                    if (isLastMove) {
                        slot.setBackground(getResources().getDrawable(R.drawable.slot_last_verti_red));
                    } else {
                        if(party.isInWinSlots(column, row)) {
                            slot.setBackground(getResources().getDrawable(R.drawable.slot_verti_red));
                        }
                        else {
                            slot.setBackground(getResources().getDrawable(R.drawable.slot_red));
                        }
                    }
                }
                else {
                    if (isLastMove) {
                        slot.setBackground(getResources().getDrawable(R.drawable.slot_last_verti_yellow));
                    } else {
                        if(party.isInWinSlots(column, row)) {
                            slot.setBackground(getResources().getDrawable(R.drawable.slot_hori_yellow));
                        }
                        else {
                            slot.setBackground(getResources().getDrawable(R.drawable.slot_yellow));
                        }
                    }
                }
                break;
            case upLeftDiagonal:
                if(slotValue==0) {
                    if (isLastMove) {
                        slot.setBackground(getResources().getDrawable(R.drawable.slot_last_diag1_red));
                    } else {
                        if(party.isInWinSlots(column, row)) {
                            slot.setBackground(getResources().getDrawable(R.drawable.slot_diag1_red));
                        }
                        else {
                            slot.setBackground(getResources().getDrawable(R.drawable.slot_red));
                        }
                    }
                }
                else {
                    if (isLastMove) {
                        slot.setBackground(getResources().getDrawable(R.drawable.slot_last_diag1_yellow));
                    } else {
                        if(party.isInWinSlots(column, row)) {
                            slot.setBackground(getResources().getDrawable(R.drawable.slot_diag1_yellow));
                        }
                        else {
                            slot.setBackground(getResources().getDrawable(R.drawable.slot_yellow));
                        }
                    }
                }
                break;
            case upRightDiagonal:
                if(slotValue==0) {
                    if (isLastMove) {
                        slot.setBackground(getResources().getDrawable(R.drawable.slot_last_diag2_red));
                    } else {
                        if(party.isInWinSlots(column, row)) {
                            slot.setBackground(getResources().getDrawable(R.drawable.slot_diag2_red));
                        }
                        else {
                            slot.setBackground(getResources().getDrawable(R.drawable.slot_red));
                        }
                    }
                }
                else {
                    if (isLastMove) {
                        slot.setBackground(getResources().getDrawable(R.drawable.slot_last_diag2_yellow));
                    } else {
                        if(party.isInWinSlots(column, row)) {
                            slot.setBackground(getResources().getDrawable(R.drawable.slot_diag2_yellow));
                        }
                        else {
                            slot.setBackground(getResources().getDrawable(R.drawable.slot_yellow));
                        }
                    }
                }
                break;
            case none:
                break;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putSerializable("party", party);
        savedInstanceState.putBoolean("isInGame", isInGame);
        savedInstanceState.putBoolean("isEndGameScreen", isEndGameScreen);
    }

    @Override
    public void onBackPressed() {
        if(!isInGame) {
            super.onBackPressed();
        }
    }

    public void shuffle() {
        try {
            party.shuffle();
        } catch (NotPlayerTurnException e) {
            e.printStackTrace();
        }
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
            if (party.getPlayers()[0].getName().equals(GameConfiguration.USERNAME)) {
                opponentId = 1;
            }
            if (columnId >= GameConfiguration.GRID_WIDTH) {
                columnId = columnId - GameConfiguration.GRID_WIDTH;
                orientation = 1;
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
        setContentView(R.layout.puissance2);
        int opponentId = 0;
        if (party.getPlayers()[0].getName().equals(GameConfiguration.USERNAME)) {
            opponentId = 1;
        }
        int[][] convertedGrid = new int[grid.length][]; //Convert to model specifications
        for(int i=0; i<grid.length; i++) {
            convertedGrid[i] = new int[grid[i].length];
            for(int j=0; j<grid.length; j++) {
                convertedGrid[i][j] = grid[i][j];
            }
        }
        try {
            party.opponentShuffle(convertedGrid, party.getPlayers()[opponentId]);
            buildGrid();
            Toast.makeText(this, R.string.opponentShuffle, Toast.LENGTH_SHORT).show();
        } catch (WrongWidthException e) {
            e.printStackTrace();
        } catch (WrongHeightException e) {
            e.printStackTrace();
        } catch (NotPlayerTurnException e) {
            e.printStackTrace();
        }
    }

    //réception d'une notification de victoire ou non
    //0 : perdu
    //1 : gagné
    //2 : égalité
    public void opponentWin(int result) {
        int opponentId = 0;
        if (party.getPlayers()[0].getName().equals(GameConfiguration.USERNAME)) {
            opponentId = 1;
        }
        switch (result) {
            case 0:
                Toast.makeText(this,party.getPlayers()[opponentId].getName() + getResources().getString(R.string.hasWon), Toast.LENGTH_LONG).show();
                buildEndGameScreen();
                break;
            case 1:
                Toast.makeText(this, GameConfiguration.USERNAME + getResources().getString(R.string.hasWon), Toast.LENGTH_LONG).show();
                buildEndGameScreen();
                break;
            case 2:
                Toast.makeText(this, R.string.partyNull, Toast.LENGTH_LONG);
                break;
            default:
                Toast.makeText(this, R.string.winnerUnhandledError, Toast.LENGTH_SHORT);
                break;
        }

        /*Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);*/
        isInGame = false;
    }

    public void buildEndGameScreen() {
        setContentView(R.layout.puissance2);
        isEndGameScreen = true;
        buildGrid();
    }

    public void adversaryDisconnected() {
        Toast.makeText(this, R.string.opponentDisconnected, Toast.LENGTH_SHORT).show();
        isInGame = false;
    }
}
