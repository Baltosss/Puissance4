package com.puissance4.server_handler;

import com.puissance4.model.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class NetworkComm {
  private static NetworkComm INSTANCE = null;
  protected static String SERVER_NAME = "127.0.0.1";
  protected static int SERVER_PORT = 1337;
  protected Socket SOCKET;
  protected NetworkOutput OUTPUT;
  protected NetworkInput INPUT;
  protected CountDownLatch LATCH;

  public static synchronized NetworkComm getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new NetworkComm();
    }

    return INSTANCE;
  }

  private NetworkComm() {
    LATCH = new CountDownLatch(1);
  }

  public void connect() throws UnknownHostException, IOException {
    SOCKET = new Socket(SERVER_NAME, SERVER_PORT);
    OUTPUT = new NetworkOutput(new PrintWriter(SOCKET.getOutputStream(), true));
    INPUT = new NetworkInput(new BufferedReader(new InputStreamReader(SOCKET.getInputStream())));
    new Thread(new InputThread(INPUT)).start();
  }

  // 0 : success
  // 1 : uname nonexistant
  // 2 : wrong password
  // 3 : timeout
  // 4 : error
  public int authenticate(String username, String password) {
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

    switch(result) {
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

  public void unauthenticate() {}

  // 0 : success
  // 1 : uname taken
  // 2 : timeout
  // 3 : error
  public int makeAccount(String username, String password) {
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

  }

  public int addFriend(String friendName) {

    return 0;
  }

  public int removeFriend(String friendName) {
    return 0;
  }

  public ArrayList<NetworkPlayer> getNearPlayers() {

    return null;
  }

  public ArrayList<NetworkPlayer> getFriends() {

    return null;
  }

  // 0 : first player
  // 1 : second player
  // 2 : game denied
  // 3 : error
  public int proposeGame(String adversary, int x, int y) {

    return 0;
  }

  public void sendMove(int x) {

  }

  public void moveReceived() {

  }

  public void sendRandom(int[][] grid) {

  }

  public void randomReceived(int[][] grid) {

  }

  public void sendWin(boolean winner) {

  }

  public void winReceived(boolean winner) {

  }

  public void notifyLatch() {
    LATCH.countDown();
    LATCH = new CountDownLatch(1);
  }

  public void closeSocket() throws IOException {
    OUTPUT.send("DISCONNECT");
    OUTPUT.close();
    INPUT.close();
    SOCKET.close();
  }
}
