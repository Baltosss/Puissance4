import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;


public class DataBase {
  protected static String saveFile = "serverData.dat";
  protected Map<String, ClientInfo> registered_clients = Collections
      .synchronizedMap(new HashMap<String, ClientInfo>());
  protected Vector<Client> nonauth_clients = new Vector<Client>();
  protected Map<String, Client> auth_clients = Collections
      .synchronizedMap(new HashMap<String, Client>());

  public DataBase() {
    readData();
  }

  public synchronized void saveData() {
    try {
      OutputStream file = new FileOutputStream(saveFile);
      OutputStream buffer = new BufferedOutputStream(file);
      ObjectOutput output = new ObjectOutputStream(buffer);
      output.writeObject(registered_clients);
      output.close();
      buffer.close();
      file.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public synchronized void readData() {
    if (new File(saveFile).isFile()) {
      try {
        InputStream file = new FileInputStream(saveFile);
        InputStream buffer = new BufferedInputStream(file);
        ObjectInput input = new ObjectInputStream(buffer);

        Map<?, ?> readMap = (Map<?, ?>) input.readObject();
        Map<String, ClientInfo> tempMap = new HashMap<String, ClientInfo>();

        for (Map.Entry<?, ?> entry : readMap.entrySet()) {
          tempMap.put((String) entry.getKey(), (ClientInfo) entry.getValue());
        }

        registered_clients = Collections.synchronizedMap(tempMap);
        
        input.close();
        buffer.close();
        file.close();
      } catch (IOException e) {
        e.printStackTrace();
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      }
    }
  }

  public synchronized Client getConnectedClient(String name) {
    return auth_clients.get(name);
  }

  public synchronized void disconnectClient(Client client) {
    auth_clients.remove(client.getName());
    nonauth_clients.remove(client);
  }

  public synchronized void registerClient(Client client) {
    registered_clients.put(client.name, new ClientInfo(client.getPassword()));
    saveData();
  }

  public synchronized boolean checkNameAvailable(String name) {
    return !registered_clients.containsKey(name);
  }

  // 0 : success
  // 1 : uname nonexistant
  // 2 : wrong password
  public synchronized int authenticate(Client client) {
    ClientInfo infos = registered_clients.get(client.getName());

    if (infos == null) {
      return 1;
    } else if (!infos.getPassword().equals(client.getPassword())) {
      return 2;
    } else {
      nonauth_clients.remove(client);
      auth_clients.put(client.getName(), client);
      return 0;
    }
  }

  public synchronized void unauthenticate(Client client) {
    auth_clients.remove(client.getName());
    nonauth_clients.add(client);
  }

  public synchronized Collection<Client> getConnectedClients() {
    ArrayList<Client> ret = new ArrayList<Client>(auth_clients.values());
    ret.addAll(nonauth_clients);
    return ret;
  }

  // 0 : success
  // 1 : uname nonexistant
  // 2 : already friends
  public synchronized int addFriend(Client client, String friendName) {
    if (friendName.equals(client.getName())) {
      return 2;
    } else if (!registered_clients.containsKey(friendName)) {
      return 1;
    } else if (registered_clients.get(client.getName()).getFriends().contains(friendName)) {
      return 2;
    } else {
      registered_clients.get(client.getName()).getFriends().add(friendName);
      registered_clients.get(friendName).getFriends().add(client.getName());
      saveData();
      return 0;
    }
  }

  public synchronized boolean removeFriend(Client client, String friendName) {
    registered_clients.get(client.getName()).getFriends().remove(friendName);
    registered_clients.get(friendName).getFriends().remove(client.getName());
    saveData();
    return true;
  }

  public List<Client> getNearPlayers(Client client) {
    // TODO coordonées
    ArrayList<Client> ret = new ArrayList<Client>(auth_clients.values());
    ret.remove(client);
    return ret;
  }

  public List<Client> getFriends(Client client) {
    ArrayList<Client> ret = new ArrayList<Client>();
    ArrayList<String> friendList = registered_clients.get(client.getName()).getFriends();

    for (String friend : friendList) {
      if (auth_clients.containsKey(friend)) {
        ret.add(auth_clients.get(friend));
      } else {
        ret.add(new Client(friend));
      }
    }

    return ret;
  }
}
