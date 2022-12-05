package client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.channels.SocketChannel;
import java.util.UUID;

import client.arrays.ArraysWindow;
import logger.LogTypes;
import logger.Logger;

public class AddArraysWindowClient implements Runnable {
  private final ArraysWindow m_ArraysWindow;
  private final SocketChannel m_Socket;

  private final int m_Port;
  private final UUID m_UUID;

  public AddArraysWindowClient(int port, ArraysWindow arraysWindow) throws IOException {
    m_ArraysWindow = arraysWindow;

    m_Socket = SocketChannel.open(new InetSocketAddress("localhost", port));

    m_Port = port;
    m_UUID = UUID.randomUUID();
  }

  @Override
  public void run() {
    try {
      sendArrays();

      readArraySum();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void sendArrays() throws IOException {
    Logger.log(LogTypes.CLIENT, m_Port, m_UUID, "Sending arrays ~ start");

    m_Socket.write(m_ArraysWindow.toByteBufferArr12());

    Logger.log(LogTypes.CLIENT, m_Port, m_UUID, "Sending arrays ~ end");
  }

  private void readArraySum() throws IOException {
    Logger.log(LogTypes.CLIENT, m_Port, m_UUID, "Reading array sum ~ start");

    ByteBuffer resultBuffer = m_ArraysWindow.allocateResultBuffer();
    FloatBuffer resultFloatBuffer = resultBuffer.asFloatBuffer();

    m_Socket.read(resultBuffer);

    m_ArraysWindow.copyResultBuffer(resultFloatBuffer);

    Logger.log(LogTypes.CLIENT, m_Port, m_UUID, "Reading array sum ~ end");
  }
}
