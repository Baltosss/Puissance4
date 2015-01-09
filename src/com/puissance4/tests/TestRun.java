package com.puissance4.tests;

import com.puissance4.server_handler.NetworkComm;

import java.io.IOException;


public class TestRun {

  public static void main(String[] args) {
    // TODO Auto-generated method stub
    System.out.println("RUN");
    NetworkComm nc = NetworkComm.getInstance();
    try {
      nc.connect();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    System.out.println("connecté");

    System.out.println(nc.authenticate("test", "test"));
    System.out.println(nc.makeAccount("test", "test"));
    System.out.println(nc.authenticate("test", "test"));

    try {
      Thread.sleep(50000);
    } catch (InterruptedException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }
    try {
      nc.closeSocket();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    System.out.println("END");
  }

}
