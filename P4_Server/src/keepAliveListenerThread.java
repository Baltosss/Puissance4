import java.io.IOException;
import java.util.TimerTask;


public class keepAliveListenerThread extends TimerTask {

  @Override
  public void run() {
    System.out.println("Current : " + System.currentTimeMillis());
    for (Client client : Server.dataBase.getConnectedClients()) {
      System.out.println(client.getName() + client.getLastPing());
      if (client.getLastPing() + 20000 < System.currentTimeMillis()) {
        System.out.println("Client " + client.getName() + " timed out.");
        client.close();
        try {
          client.closeSocket();
        } catch (IOException e) {
          e.printStackTrace();
        }
        Server.dataBase.disconnectClient(client);
      }
    }
  }

}
