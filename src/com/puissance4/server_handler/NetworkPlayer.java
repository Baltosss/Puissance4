package com.puissance4.server_handler;

/**
 * Created by fred on 10/01/15.
 */
public class NetworkPlayer {
    private String name;
    private int status;

    public NetworkPlayer(String name, int status) {
        this.name = name;
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
