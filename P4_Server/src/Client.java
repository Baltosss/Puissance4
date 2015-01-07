import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.concurrent.locks.ReentrantLock;

public class Client {
  protected String name;
  protected String password;
  protected double latitude;
  protected double longitude;
  protected boolean authenticated;
  protected Socket CLIENT_SOCKET;
  protected PrintWriter OUTPUT;
  protected BufferedReader INPUT;
  protected ReentrantLock LOCK;
  protected long lastPing;
  protected Client adversary;
  protected ClientState currentState;
  protected Boolean stopListening;

  public Client(Socket socket) throws IOException {
    CLIENT_SOCKET = socket;
    OUTPUT = new PrintWriter(CLIENT_SOCKET.getOutputStream(), true);
    INPUT = new BufferedReader(new InputStreamReader(CLIENT_SOCKET.getInputStream()));
    LOCK = new ReentrantLock(true);
    adversary = null;
    currentState = ClientState.NAUTH;
    stopListening = false;
    latitude = 0;
    longitude = 0;
    lastPing = System.currentTimeMillis();
    authenticated = false;
  }

  public Client(String name) {
    this.name = name;
    currentState = ClientState.NAUTH;
    latitude = 0;
    longitude = 0;
    authenticated = false;
  }

  // 0 : success
  // 1 : uname nonexistant
  // 2 : wrong password
  public int authenticate() {
    return Server.dataBase.authenticate(this);
  }

  // 0 : success
  // 1 : uname nonexistant
  // 2 : already friends
  public int addFriend(String friendName) {
    return Server.dataBase.addFriend(this, friendName);
  }

  public void listen() throws IOException {
    String request;

    while (!stopListening && ((request = INPUT.readLine()) != null)) {
      LOCK.lock();

      System.out.println("--> " + request);

      StringTokenizer tokenizer = new StringTokenizer(request, "_");
      String token = null;

      if (tokenizer.hasMoreTokens()) {
        token = tokenizer.nextToken();
      }

      if (token.equals("STILLALIVE")) {
        setLastPing(System.currentTimeMillis());
        latitude = Long.parseLong(token);
        longitude = Long.parseLong(token);

      } else {

        switch (currentState) {

          case NAUTH:
            switch (token) {
              case "MAKEACC":
                if (tokenizer.countTokens() >= 2) {
                  String t_name = tokenizer.nextToken();
                  String t_pw = tokenizer.nextToken();

                  if (Server.dataBase.checkNameAvailable(t_name)) {
                    name = t_name;
                    password = t_pw;
                    Server.dataBase.registerClient(this);
                    send("SUCCESS");
                  } else {
                    send("UNAMETAKEN");
                  }
                } else {
                  send("ERROR");
                }
                break;

              case "AUTH":
                if (tokenizer.countTokens() >= 2) {
                  name = tokenizer.nextToken();
                  password = tokenizer.nextToken();

                  switch (authenticate()) {
                    case 0:
                      send("SUCCESS");
                      authenticated = true;
                      currentState = ClientState.STDBY;
                      break;
                    case 1:
                      send("UNAMENONEXISTANT");
                      break;
                    case 2:
                      send("WRONGPASSWORD");
                      break;
                  }
                } else {
                  send("ERROR");
                }
                break;

              case "DISCONNECT":
                close();
                break;

              default:
                send("ERROR");
                break;
            }
            break;

          case STDBY:
            switch (token) {
              case "REQNEARPLAYERS":
                List<Client> playerlist = getNearPlayers();
                String retnp = "NEARPLAYERS";

                for (Client c : playerlist) {
                  retnp += "_" + c.getName() + ";" + c.getLatitude() + ";" + c.getLongitude();
                  if (!c.isAvailable()) {
                    retnp += "BUSY";
                  } else {
                    retnp += "AVAILABLE";
                  }
                }

                send(retnp);
                break;

              case "REQFRIENDS":
                List<Client> friendlist = getFriends();
                String retfr = "FRIENDS";

                for (Client c : friendlist) {
                  retfr += "_" + c.getName() + ";" + c.getLatitude() + ";" + c.getLongitude() + ";";
                  if (!c.isAuthenticated()) {
                    retfr += "DISCONNECTED";
                  } else if (!c.isAvailable()) {
                    retfr += "BUSY";
                  } else {
                    retfr += "AVAILABLE";
                  }
                }

                send(retfr);
                break;

              case "ADDFRIEND":
                if (tokenizer.hasMoreTokens()) {
                  // TODO
                } else {
                  send("ERROR");
                }
                break;

              case "STARTGAME":
                if (tokenizer.hasMoreTokens()) {
                  adversary = Server.dataBase.getConnectedClient(tokenizer.nextToken());

                  if (adversary == null || !adversary.isAvailable()) {
                    send("ERRORUNAVAILABLEPLAYER");
                  } else {
                    Boolean response = adversary.propMatch(name);

                    if (response) {
                      Random random = new Random();
                      Boolean firstPlayer = random.nextBoolean();

                      if (firstPlayer) {
                        send("STARTMATCH_0");
                      } else {
                        send("STARTMATCH_1");
                      }

                      currentState = ClientState.MATCH;
                      adversary.sendFirstPlayer(!firstPlayer);
                    } else {
                      send("ERRORREFUSEDMATCH");
                      adversary = null;
                    }
                  }

                } else {
                  send("ERROR");
                }
                break;

              case "DISCONNECT":
                Server.dataBase.disconnectClient(this);
                close();
                break;

              default:
                send("ERROR");
                break;
            }
            break;

          case MATCH:
            switch (token) {
              case "MOVE":
                break;

              case "RAND":
                break;

              case "WIN":
                send("WIN");
                adversary.sendWin(false);
                adversary = null;
                currentState = ClientState.STDBY;
                break;

              case "LOSS":
                send("LOSS");
                adversary.sendWin(true);
                adversary = null;
                currentState = ClientState.STDBY;
                break;

              case "DISCONNECT":
                Server.dataBase.disconnectClient(this);
                adversary = null;
                close();
                break;

              default:
                send("ERROR");
                break;
            }

            break;
        }
      }

      LOCK.unlock();
    }

    closeSocket();
  }

  private List<Client> getFriends() {
    return Server.dataBase.getFriends(this);
  }

  private List<Client> getNearPlayers() {
    return Server.dataBase.getNearPlayers(this);
  }

  public void setCurrentState(ClientState state) {
    currentState = state;
  }

  // Match proposé par name2, si l'utilisateur accepte, retourner true sinon false
  public Boolean propMatch(String name2) {

    return null;
  }

  public boolean isAvailable() {
    if (currentState == ClientState.STDBY) {
      return true;
    } else {
      return false;
    }
  }

  public void sendMove() {}

  public void sendRandom() {}

  public synchronized void send(String message) {
    System.out.println(message);
    OUTPUT.println(message);
  }

  public void notifyDisconnect() {
    LOCK.lock();
    send("PLAYERDISCONNECTED");
    currentState = ClientState.STDBY;
    LOCK.unlock();
  }

  public void sendFirstPlayer(boolean first) {
    LOCK.lock();
    if (first) {
      send("STARTMATCH_0");
    } else {
      send("STARTMATCH_1");
    }

    currentState = ClientState.MATCH;
    LOCK.unlock();
  }

  public void sendWin(boolean win) {
    LOCK.lock();
    if (win) {
      send("WIN");
    } else {
      send("LOSS");
    }

    currentState = ClientState.STDBY;
    LOCK.unlock();
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public double getLatitude() {
    return latitude;
  }

  public void setLatitude(double latitude) {
    this.latitude = latitude;
  }

  public double getLongitude() {
    return longitude;
  }

  public void setLongitude(double longitude) {
    this.longitude = longitude;
  }

  public boolean isAuthenticated() {
    return authenticated;
  }

  public void setAuthenticated(boolean authenticated) {
    this.authenticated = authenticated;
  }

  public void close() {
    stopListening = true;
  }

  public void closeSocket() throws IOException {
    if (adversary != null) {
      adversary.notifyDisconnect();
    }

    OUTPUT.close();
    INPUT.close();
    CLIENT_SOCKET.close();
  }

  public long getLastPing() {
    return lastPing;
  }

  public void setLastPing(long lastPing) {
    this.lastPing = lastPing;
  }

}
