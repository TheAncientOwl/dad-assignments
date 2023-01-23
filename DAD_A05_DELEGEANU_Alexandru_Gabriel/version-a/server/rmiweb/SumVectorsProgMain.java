package rmiweb;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;

public class SumVectorsProgMain {
  public static void main(String[] args) {
    String registryIP = "127.0.0.1";
    String registryPort = "1099";

    try {
      System.setSecurityManager(new RMISecurityManager());

      SumVectorsImpl server = new SumVectorsImpl();

      Naming.rebind("rmi://" + registryIP + ":" + registryPort + "/SUM-VECTORS", server);

      System.out.println("Server for vectors sum waiting...");
    } catch (MalformedURLException e) {
      System.out.println("Malformed URL: " + e.toString());
    } catch (RemoteException e) {
      System.out.println("Remote exception: " + e.toString());
    }
  }
}
