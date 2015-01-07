import java.io.IOException;
import java.net.Socket;

public class ClientThread implements Runnable {
  protected DataBase dataBase;
  protected Client client;

  public ClientThread(DataBase dataBase, Socket socket) {
    this.dataBase = dataBase;
    try {
      client = new Client(socket);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  @Override
  public void run() {
    System.out.println("Client thread started");

    try {
      client.listen();
    } catch (IOException e) {
      System.out.println("IOException lancée par listen, fin du thread.");
    }

    System.out.println("Client thread stopped");
  }

}
