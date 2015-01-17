import java.io.IOException;
import java.util.TimerTask;


public class keepAliveListenerThread extends TimerTask {

  @Override
  public void run() {
    for (Client client : Server.dataBase.getConnectedClients()) {
      if (client.getLastPing() + 20000 < System.currentTimeMillis()) {
        System.out.println("Client " + client.getName() + " timed out.");
        client.close();
        try {
          client.closeSocket();
        } catch (IOException e) {
          e.printStackTrace();
        } finally {
          Server.dataBase.disconnectClient(client);
        }
      }
    }
  }

}
