package snmp;

import java.io.IOException;
import java.net.InetAddress;

import org.snmp4j.CommandResponder;
import org.snmp4j.CommandResponderEvent;
import org.snmp4j.MessageDispatcher;
import org.snmp4j.MessageDispatcherImpl;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.mp.MPv1;
import org.snmp4j.mp.MPv2c;
import org.snmp4j.security.Priv3DES;
import org.snmp4j.security.SecurityProtocols;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.MultiThreadedMessageDispatcher;
import org.snmp4j.util.ThreadPool;

public class TrapReceiver implements CommandResponder {
  public static final String ADDRESS_NAME = "127.0.0.1";
  public static final int PORT = 162;

  public static void main(String[] args) {
    TrapReceiver trapReceiver = new TrapReceiver();

    try {
      trapReceiver.listen(new UdpAddress(InetAddress.getByName(ADDRESS_NAME), PORT));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public synchronized void listen(UdpAddress address) throws IOException {
    ThreadPool threadPool = ThreadPool.create("DispatcherPool", 10);
    MessageDispatcher mDispathcher = new MultiThreadedMessageDispatcher(threadPool, new MessageDispatcherImpl());

    // add message processing models
    mDispathcher.addMessageProcessingModel(new MPv1());
    mDispathcher.addMessageProcessingModel(new MPv2c());

    // add all security protocols
    SecurityProtocols.getInstance().addDefaultProtocols();
    SecurityProtocols.getInstance().addPrivacyProtocol(new Priv3DES());

    Snmp snmp = new Snmp(mDispathcher, new DefaultUdpTransportMapping(address));

    snmp.addCommandResponder(this);

    snmp.listen();
    System.out.println("Listening on " + address);

    try {
      this.wait();
    } catch (InterruptedException ex) {
      Thread.currentThread().interrupt();
    }
  }

  public synchronized void processPdu(CommandResponderEvent cmdRespEvent) {
    System.out.println(">> Received PDU");
    PDU pdu = cmdRespEvent.getPDU();

    if (pdu != null && pdu.getType() == PDU.TRAP) {
      System.out.println(">> PDU is TRAP");
      System.out.println(">> PDU Type = " + pdu.getType());
      System.out.println(">> Variables = " + pdu.getVariableBindings());
    }
  }
}
