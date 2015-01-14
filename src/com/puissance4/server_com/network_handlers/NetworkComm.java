package com.puissance4.server_com.network_handlers;

import android.content.Intent;

import com.puissance4.server_com.ping_service.PingService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class NetworkComm {
    private static NetworkComm INSTANCE = null;
    protected static String SERVER_NAME = "cyrille.no-ip.org";
    protected static int SERVER_PORT = 1337;
    protected Socket SOCKET;
    protected NetworkOutput OUTPUT;
    protected NetworkInput INPUT;
    protected CountDownLatch LATCH;
    protected boolean isConnected;
    protected PingService service;

    public static synchronized NetworkComm getInstance() {
        if (INSTANCE == null) {
                INSTANCE = new NetworkComm();
        }

        if(!INSTANCE.isConnected()) {
            try {
                INSTANCE.connect();
            } catch (IOException e) {
                INSTANCE.setIsConnected(false);
                e.printStackTrace();
            }
        }

        return INSTANCE;
    }

    private NetworkComm() {
        isConnected = false;
    }

    public void connect() throws UnknownHostException, IOException {
        SOCKET = new Socket(SERVER_NAME, SERVER_PORT);
        OUTPUT = new NetworkOutput(new PrintWriter(SOCKET.getOutputStream(), true));
        INPUT = new NetworkInput(new BufferedReader(new InputStreamReader(SOCKET.getInputStream())));
        new Thread(new InputThread(INPUT)).start();
        isConnected = true;
    }

    public void sendPing(double latitude, double longitude) {
        OUTPUT.send("STILLALIVE_" + latitude + "_" + longitude);
    }

    // 0 : success
    // 1 : uname nonexistant
    // 2 : wrong password
    // 3 : timeout
    // 4 : error
    public int authenticate(String username, String password) {
        LATCH = new CountDownLatch(1);
        OUTPUT.send("AUTH_" + username + "_" + password);

        try {
            if (!LATCH.await(20, TimeUnit.SECONDS)) {
                return 3;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            return 4;
        }

        String result = INPUT.getResult();

        switch (result) {
            case "SUCCESS":
                return 0;
            case "UNAMENONEXISTANT":
                return 1;
            case "WRONGPASSWORD":
                return 2;
            default:
                return 4;

        }
    }

    public void unauthenticate() {
        OUTPUT.send("UNAUTH");
    }

    // 0 : success
    // 1 : uname taken
    // 2 : timeout
    // 3 : error
    public int makeAccount(String username, String password) {
        LATCH = new CountDownLatch(1);
        OUTPUT.send("MAKEACC_" + username + "_" + password);

        try {
            if (!LATCH.await(20, TimeUnit.SECONDS)) {
                return 2;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            return 3;
        }

        String result = INPUT.getResult();

        switch (result) {
            case "SUCCESS":
                return 0;
            case "UNAMETAKEN":
                return 1;
            default:
                return 3;
        }
    }

    public void disconnect() {
        OUTPUT.send("DISCONNECT");
    }


    // 0 : success
    // 1 : uname nonexistant
    // 2 : already friends
    // 3 : timeout
    // 4 : error
    public int addFriend(String friendName) {
        LATCH = new CountDownLatch(1);
        OUTPUT.send("ADDFRIEND" + friendName);

        try {
            if (!LATCH.await(20, TimeUnit.SECONDS)) {
                return 3;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            return 4;
        }

        String result = INPUT.getResult();

        switch (result) {
            case "SUCCESS":
                return 0;
            case "UNAMENONEXISTANT":
                return 1;
            case "ALREADYFRIENDS":
                return 2;
            default:
                return 4;
        }
    }

    public void removeFriend(String friendName) {
        OUTPUT.send("REMFRIEND_" + friendName);
    }

    // retourne null en cas d'erreur/timeout
    public ArrayList<NetworkPlayer> getNearPlayers() {
        LATCH = new CountDownLatch(1);
        ArrayList<NetworkPlayer> playerList = new ArrayList<NetworkPlayer>();
        OUTPUT.send("REQNEARPLAYERS");

        try {
            if (!LATCH.await(20, TimeUnit.SECONDS)) {
                return null;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }

        String result = INPUT.getResult();

        StringTokenizer playerTokenizer = new StringTokenizer(result, "_");
        String playerToken = null;

        while (playerTokenizer.hasMoreTokens()) {
            playerToken = playerTokenizer.nextToken();
            StringTokenizer elemTokenizer = new StringTokenizer(playerToken, ";");

            if (elemTokenizer.countTokens() >= 3) {
                String name = elemTokenizer.nextToken();
                double latitude = Double.parseDouble(elemTokenizer.nextToken());
                double longitude = Double.parseDouble(elemTokenizer.nextToken());
                int status = Integer.parseInt(elemTokenizer.nextToken());

                playerList.add(new NetworkPlayer(name, latitude, longitude, status));
            }
        }

        return playerList;
    }

    public ArrayList<NetworkPlayer> getFriends() {
        LATCH = new CountDownLatch(1);
        ArrayList<NetworkPlayer> playerList = new ArrayList<NetworkPlayer>();
        OUTPUT.send("REQFRIENDS");

        try {
            if (!LATCH.await(20, TimeUnit.SECONDS)) {
                return null;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }

        String result = INPUT.getResult();

        StringTokenizer playerTokenizer = new StringTokenizer(result, "_");
        String playerToken = null;

        while (playerTokenizer.hasMoreTokens()) {
            playerToken = playerTokenizer.nextToken();
            StringTokenizer elemTokenizer = new StringTokenizer(playerToken, ";");

            if (elemTokenizer.countTokens() >= 3) {
                String name = elemTokenizer.nextToken();
                Double latitude = Double.parseDouble(elemTokenizer.nextToken());
                Double longitude = Double.parseDouble(elemTokenizer.nextToken());
                int status = Integer.parseInt(elemTokenizer.nextToken());

                playerList.add(new NetworkPlayer(name, latitude, longitude, status));
            }
        }

        return playerList;
    }

    // 0 : first player
    // 1 : second player
    // 2 : game denied
    // 3 : error
    public int proposeGame(String adversary, int x, int y) {
        LATCH = new CountDownLatch(1);
        OUTPUT.send("PROPMATCH_" + adversary + "_" + x + "_" + y);

        try {
            if (!LATCH.await(80, TimeUnit.SECONDS)) {
                return 3;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            return 3;
        }

        int result = 3;

        StringTokenizer tokenizer = new StringTokenizer(INPUT.getResult(), "_");
        if(tokenizer.countTokens()>=2) {
            if(tokenizer.nextToken().equals("STARTMATCH")) {
                result = Integer.parseInt(tokenizer.nextToken());
            }
        }

        return result;
    }

    public void proposalReceived(String advname, int x, int y) {
        if(service!=null) {
            service.proposalReceived(advname, x, y);
        }
    }

    // BLOQUANT SI response = true
    // dans ce cas :
    // 0 : first player
    // 1 : second player
    // 3 : error
    // si response = false, retoure 0 (mais sans importance)
    public int answerProposal(boolean response) {
        if (response) {
            OUTPUT.send("PROPRESPONSE_1");
            LATCH = new CountDownLatch(1);

            try {
                if (!LATCH.await(20, TimeUnit.SECONDS)) {
                    return 3;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                return 3;
            }

            int result = 3;

            StringTokenizer tokenizer = new StringTokenizer(INPUT.getResult(), "_");
            if(tokenizer.countTokens()>=2) {
                if(tokenizer.nextToken().equals("STARTMATCH")) {
                    result = Integer.parseInt(tokenizer.nextToken());
                }
            }

            return result;

        } else {
            OUTPUT.send("PROPRESPONSE_0");
            return 0;
        }
    }

    public void sendMove(int x) {
        OUTPUT.send("MOVE_" + Integer.toString(x));
    }

    public void moveReceived(int x) {
        Intent intent = new Intent("MOVE");
        intent.putExtra("COL", x);
        if(service!=null) {
            service.sendBroadcast(intent);
        }
    }

    public void sendRandom(int[][] grid) {
        String message = "RAND";
        message += "_" + grid.length + "_" + grid[0].length;
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[x].length; y++) {
                message += "_" + grid[x][y];
            }
        }
        OUTPUT.send(message);
    }

    public void randomReceived(Integer[][] grid) {
        Intent intent = new Intent("RAND");
        intent.putExtra("GRID", grid);
        if(service!=null) {
            service.sendBroadcast(intent);
        }
    }

    public void sendWin(boolean winner) {
        if (winner) {
            OUTPUT.send("WIN");
        } else {
            OUTPUT.send("LOSS");
        }
    }

    public void winReceived(boolean winner) {
        Intent intent = new Intent("WIN");
        intent.putExtra("ISWINNER", winner);
        if(service!=null) {
            service.sendBroadcast(intent);
        }
    }

    public void notifyLatch() {
        LATCH.countDown();
    }

    public void closeSocket() throws IOException {
        OUTPUT.send("DISCONNECT");
        OUTPUT.close();
        INPUT.close();
        SOCKET.close();
        isConnected = false;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setService(PingService service) {
        this.service = service;
    }

    public void setIsConnected(boolean isConnected) {
        this.isConnected = isConnected;
    }
}
