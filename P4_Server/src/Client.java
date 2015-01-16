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

  public void unauthenticate() {
    Server.dataBase.unauthenticate(this);
    authenticated = true;
    currentState = ClientState.NAUTH;
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
        if (tokenizer.countTokens() >= 2) {
          setLastPing(System.currentTimeMillis());
          latitude = Double.parseDouble(tokenizer.nextToken());
          longitude = Double.parseDouble(tokenizer.nextToken());
          System.out.println("Client " + name + " pinged at : " + latitude + " ; " + longitude
              + " STATUS : " + currentState.toString());
        }

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
                  retnp += "_" + c.getName() + ";" + c.getLatitude() + ";" + c.getLongitude() + ";";
                  if (!c.isAvailable()) {
                    retnp += "1";
                  } else {
                    retnp += "0";
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
                    retfr += "2";
                  } else if (!c.isAvailable()) {
                    retfr += "1";
                  } else {
                    retfr += "0";
                  }
                }

                send(retfr);
                break;

              case "ADDFRIEND":
                if (tokenizer.hasMoreTokens()) {
                  String friendname = tokenizer.nextToken();

                  switch (Server.dataBase.addFriend(this, friendname)) {
                    case 0:
                      send("SUCCESS");
                      break;
                    case 1:
                      send("UNAMENONEXISTANT");
                      break;
                    case 2:
                      send("ALREADYFRIENDS");
                      break;
                  }
                } else {
                  send("ERROR");
                }
                break;

              case "REMFRIEND":
                if (tokenizer.hasMoreTokens()) {
                  String friendname = tokenizer.nextToken();
                  if (Server.dataBase.removeFriend(this, friendname)) {
                    send("SUCCESS");
                  } else {
                    send("ERROR");
                  }
                } else {
                  send("ERROR");
                }
                break;

              case "PROPMATCH":
                if (tokenizer.countTokens() >= 3) {
                  adversary = Server.dataBase.getConnectedClient(tokenizer.nextToken());
                  int xcols = Integer.parseInt(tokenizer.nextToken());
                  int ycols = Integer.parseInt(tokenizer.nextToken());

                  if (adversary == null || !adversary.isAvailable()) {
                    send("ERRORUNAVAILABLEPLAYER");
                  } else {
                    adversary.propMatch(name, xcols, ycols);
                  }

                } else {
                  send("ERROR");
                }
                break;

              case "PROPRESPONSE":
                if (tokenizer.hasMoreTokens()) {
                  if (adversary != null) {
                    Boolean response = (Integer.parseInt(tokenizer.nextToken()) == 1);
                    adversary.notifyMatchResponse(response);

                    if (!response) {
                      adversary = null;
                    }
                  }
                  break;
                }

              case "UNAUTH":
                unauthenticate();
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
                if (tokenizer.hasMoreTokens()) {
                  if (adversary != null) {
                    adversary.send(request);
                  }
                } else {
                  send("ERROR");
                }
                break;

              case "RAND":
                if (tokenizer.hasMoreTokens()) {
                  if (adversary != null) {
                    adversary.send(request);
                  }
                } else {
                  send("ERROR");
                }
                break;

              case "WIN":
                send("WIN");
                adversary.sendWin(0);
                adversary = null;
                currentState = ClientState.STDBY;
                break;

              case "LOSS":
                send("LOSS");
                adversary.sendWin(1);
                adversary = null;
                currentState = ClientState.STDBY;
                break;

              case "TIE":
                send("TIE");
                adversary.sendWin(2);
                adversary = null;
                currentState = ClientState.STDBY;
                break;

              case "LEAVEGAME":
                adversary.notifyDisconnect();
                adversary = null;
                currentState = ClientState.STDBY;
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
        }
      }

      LOCK.unlock();
    }

    Server.dataBase.disconnectClient(this);
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

  public void propMatch(String advname, int xcols, int ycols) {
    adversary = Server.dataBase.getConnectedClient(advname);
    send("PROPMATCH_" + advname + "_" + xcols + "_" + ycols);
  }

  public void notifyMatchResponse(boolean response) {
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
      send("STARTMATCH_2"); // signifie un refus
      adversary = null;
    }
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

  public void sendWin(int win) {
    LOCK.lock();

    switch (win) {
      case 0:
        send("LOSS");
        break;
      case 1:
        send("WIN");
        break;
      case 2:
        send("TIE");
        break;
      default:
        break;
    }

    currentState = ClientState.STDBY;
    LOCK.unlock();
  }

  public double distanceTo(Client client) {
    double divisor = (double) (180 / Math.PI);

    double x1 = latitude / divisor;
    double y1 = longitude / divisor;
    double x2 = client.getLatitude() / divisor;
    double y2 = client.getLongitude() / divisor;

    double t1 = Math.cos(x1) * Math.cos(y1) * Math.cos(x2) * Math.cos(y2);
    double t2 = Math.cos(x1) * Math.sin(y1) * Math.cos(x2) * Math.sin(y2);
    double t3 = Math.sin(x1) * Math.sin(x2);

    double tt = Math.acos(t1 + t2 + t3);

    return 6366000 * tt;
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
