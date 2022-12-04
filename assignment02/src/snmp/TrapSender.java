package snmp;

import java.io.IOException;
import java.net.InetAddress;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.IpAddress;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

public class TrapSender {
  public static final String ADDRESS_NAME = "127.0.0.1";
  public static final int PORT = 162;

  public static void main(String[] args) {
    TrapSender trapSender = new TrapSender();

    try {
      trapSender.sendTrap();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void sendTrap() throws IOException {
    CommunityTarget communityTarget = new CommunityTarget();
    communityTarget.setCommunity(new OctetString("public"));
    communityTarget.setVersion(SnmpConstants.version2c);
    communityTarget.setAddress(new UdpAddress(InetAddress.getByName(ADDRESS_NAME), PORT));

    PDU pdu = new PDU();
    pdu.add(new VariableBinding(SnmpConstants.snmpTrapAddress, new IpAddress(ADDRESS_NAME)));
    pdu.setType(PDU.TRAP);

    Snmp snmp = new Snmp(new DefaultUdpTransportMapping());
    snmp.listen();

    System.out.println("Sending Trap to " + ADDRESS_NAME + "/" + PORT);
    snmp.send(pdu, communityTarget);

    snmp.close();
  }
}
