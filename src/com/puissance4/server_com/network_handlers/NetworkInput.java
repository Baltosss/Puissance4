package com.puissance4.server_com.network_handlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.concurrent.locks.ReentrantLock;

public class NetworkInput {
    protected BufferedReader INPUT;
    protected String result;
    protected Boolean stopListening;
    protected ReentrantLock LOCK;

    public NetworkInput(BufferedReader INPUT) {
        this.INPUT = INPUT;
        stopListening = false;
        LOCK = new ReentrantLock(true);
    }

    public String getResult() {
        return result;
    }

    public void listen() throws IOException {
        String response;

        while (!stopListening && ((response = INPUT.readLine()) != null)) {
            LOCK.lock();

            StringTokenizer tokenizer = new StringTokenizer(response, "_");
            String token = null;

            if (tokenizer.hasMoreTokens()) {
                token = tokenizer.nextToken();
            }

            switch (token) {
                case "PROPMATCH":
                    if (tokenizer.countTokens() >= 4) {
                        String advname = tokenizer.nextToken();
                        int x = Integer.parseInt(tokenizer.nextToken());
                        int y = Integer.parseInt(tokenizer.nextToken());
                        int n = Integer.parseInt(tokenizer.nextToken());
                        NetworkComm.getInstance().proposalReceived(advname, x, y, n);
                    }
                    break;
                case "MOVE":
                    if (tokenizer.hasMoreTokens()) {
                        int move = Integer.parseInt(tokenizer.nextToken());
                        NetworkComm.getInstance().moveReceived(move);
                    }
                    break;
                case "RAND":
                    if (tokenizer.countTokens() >= 2) {
                        int xcols = Integer.parseInt(tokenizer.nextToken());
                        int ycols = Integer.parseInt(tokenizer.nextToken());

                        Integer[][] grid = new Integer[xcols][ycols];

                        for (int x = 0; x < xcols; x++) {
                            for (int y = 0; y < ycols; y++) {
                                if (tokenizer.hasMoreTokens()) {
                                    grid[x][y] = Integer.parseInt(tokenizer.nextToken());
                                } else {
                                    grid[x][y] = 0;
                                }
                            }
                        }

                        NetworkComm.getInstance().randomReceived(grid);
                    }
                    break;
                case "WIN":
                    NetworkComm.getInstance().winReceived(1);
                    break;
                case "LOSS":
                    NetworkComm.getInstance().winReceived(0);
                    break;
                case "TIE":
                    NetworkComm.getInstance().winReceived(2);
                    break;
                case "PLAYERDISCONNECTED":
                    NetworkComm.getInstance().playerDisconnected();
                    break;
                default:
                    //result = response.substring(token.length());
                    result = response;
                    NetworkComm.getInstance().notifyLatch();
                    break;
            }

            LOCK.unlock();
        }

        closeSocket();
    }

    public void close() {
        stopListening = true;
    }

    public void closeSocket() throws IOException {
        INPUT.close();
    }
}
