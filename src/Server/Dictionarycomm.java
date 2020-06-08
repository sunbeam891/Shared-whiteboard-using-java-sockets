package Server;

//Shubham Parth , 1155825
import org.kohsuke.args4j.Option;
import java.io.File;

public class Dictionarycomm {

  @Option(required = true, name = "-p", usage = "Port number")
  private int port;


  public int getPort() {
      return port;
  }
}


