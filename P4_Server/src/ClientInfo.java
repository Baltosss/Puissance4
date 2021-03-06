import java.io.Serializable;
import java.util.ArrayList;


public class ClientInfo implements Serializable {
  private static final long serialVersionUID = 8604414346866884332L;
  protected String password;
  protected ArrayList<String> friends;

  public ClientInfo() {
    friends = new ArrayList<String>();
  }

  public ClientInfo(String pw) {
    password = pw;
    friends = new ArrayList<String>();
  }

  public ClientInfo(String pw, ArrayList<String> f) {
    password = pw;
    friends = f;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public ArrayList<String> getFriends() {
    return friends;
  }

  public void setFriends(ArrayList<String> friends) {
    this.friends = friends;
  }

}
