import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;


public class DataBase {
  protected Map<String, ClientInfo> registered_clients = Collections
      .synchronizedMap(new HashMap<String, ClientInfo>());
  protected Vector<Client> nonauth_clients = new Vector<Client>();
  protected Map<String, Client> auth_clients = Collections
      .synchronizedMap(new HashMap<String, Client>());

  public DataBase() {}

  public synchronized Client getConnectedClient(String name) {
    return auth_clients.get(name);
  }

  public synchronized void disconnectClient(Client client) {
    auth_clients.remove(client.getName());
    nonauth_clients.remove(client);
  }

  public synchronized void registerClient(Client client) {
    registered_clients.put(client.name, new ClientInfo(client.getPassword()));
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
    if (!registered_clients.containsKey(friendName)) {
      return 1;
    } else if (registered_clients.get(client.getName()).getFriends().contains(friendName)) {
      return 2;
    } else {
      registered_clients.get(client.getName()).getFriends().add(friendName);
      registered_clients.get(friendName).getFriends().add(client.getName());
      return 0;
    }
  }

  public synchronized void removeFriend(Client client, String friendName) {
    registered_clients.get(client.getName()).getFriends().remove(friendName);
    registered_clients.get(friendName).getFriends().remove(client.getName());
  }

  public List<Client> getNearPlayers(Client client) {
    // TODO Auto-generated method stub
    return new ArrayList<Client>(auth_clients.values());
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
