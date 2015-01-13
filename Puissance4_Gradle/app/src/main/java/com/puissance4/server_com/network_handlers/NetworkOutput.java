package com.puissance4.server_com.network_handlers;

import java.io.PrintWriter;


public class NetworkOutput {
    protected PrintWriter OUTPUT;

    public NetworkOutput(PrintWriter OUTPUT) {
        this.OUTPUT = OUTPUT;
    }

    public void send(String message) {
        OUTPUT.println(message);
    }

    public void close() {
        OUTPUT.close();
    }

}
