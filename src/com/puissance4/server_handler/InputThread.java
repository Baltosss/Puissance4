package com.puissance4.server_handler;

import java.io.IOException;

public class InputThread implements Runnable {
  NetworkInput INPUT;

  public InputThread(NetworkInput INPUT) {
    this.INPUT = INPUT;
  }

  @Override
  public void run() {
    try {
      INPUT.listen();
    } catch (IOException e) {
      System.out.println("IOException lancée par listen, fin du thread.");
    }
  }
}
