package com.puissance4.server_handler;

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

      if (token.equals("PROPMATCH")) {

      } else {
        result = token;
        NetworkComm.getInstance().notifyLatch();
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
