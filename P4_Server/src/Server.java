import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Timer;

public class Server {
  protected static int SERVER_PORT = 1337;
  protected static ServerSocket SERVER_SOCKET = null;
  protected static DataBase dataBase = new DataBase();

  public static void main(String[] args) {
    System.out.println("Program started");

    try {
      SERVER_SOCKET = new ServerSocket(SERVER_PORT);
    } catch (IOException e) {
      System.out.println("Error while opening ServerSocket on port " + SERVER_PORT);
      e.printStackTrace();
    }

    Timer kalTimer = new Timer();
    keepAliveListenerThread kalThread = new keepAliveListenerThread();
    kalTimer.schedule(kalThread, 0, 20000);

    while (true) {
      Socket CLIENT_SOCKET = null;

      try {
        CLIENT_SOCKET = SERVER_SOCKET.accept();
      } catch (IOException e) {
        System.out.println("Error while accepting client");
        e.printStackTrace();
      }

      new Thread(new ClientThread(dataBase, CLIENT_SOCKET)).start();
    }
  }

}
