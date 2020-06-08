package Client;

//Shubham Parth , 1155825

import org.kohsuke.args4j.Option;

public class Clientcomm {
  @Option(required = true, name = "-h", usage = "Hostname")
  private String host;

  @Option(required = false, name = "-p", usage = "Port")
  private int port;
  
  @Option(required=true,name ="-u",usage ="Username")
  private String uname;

  public String Hostin() {
      return host;
  }

  public int Portin() {
      return port;
  }
  public String Uname() {
	  return uname;
  }
}
