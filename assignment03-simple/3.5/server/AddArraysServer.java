package server;

import java.io.IOException;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import logger.LogTypes;
import logger.Logger;
import server.arrays.MultiThreadArraySum;

public class AddArraysServer implements Runnable {
  private final Map<SocketChannel, ByteBuffer> m_Sockets = new ConcurrentHashMap<>();
  private final Map<SocketChannel, UUID> m_UUIDS = new ConcurrentHashMap<>();

  private final ServerSocketChannel m_ServerSocket;
  private final Selector m_Selector;

  private final int m_ArraySize;
  private final int m_Port;

  private boolean m_Running;

  public AddArraysServer(int port, int arraySize) throws IOException {
    m_ServerSocket = ServerSocketChannel.open();

    try {
      m_ServerSocket.bind(new InetSocketAddress(port));
    } catch (BindException e) {
      Logger.log(LogTypes.SERVER, port, null, "Port already in use! The program will exit...");
      e.printStackTrace();
      System.exit(0);
    }

    m_ServerSocket.configureBlocking(false);

    m_Selector = Selector.open();

    m_ServerSocket.register(m_Selector, SelectionKey.OP_ACCEPT);

    m_Running = false;
    m_ArraySize = arraySize;

    m_Port = port;
  }

  @Override
  public void run() {
    m_Running = true;

    try {
      while (m_Running) {
        m_Selector.select();

        final Set<SelectionKey> selectionKeys = m_Selector.selectedKeys();
        Iterator<SelectionKey> it = selectionKeys.iterator();

        while (it.hasNext()) {
          final SelectionKey key = it.next();
          it.remove();

          if (key.isValid()) {
            if (key.isAcceptable()) {
              accept(key);
            } else if (key.isReadable()) {
              read(key);
            } else if (key.isWritable()) {
              write(key);
            }
          }

          m_Sockets.keySet().removeIf(socketChannel -> !socketChannel.isOpen());
          m_UUIDS.keySet().removeIf(socketChannel -> !socketChannel.isOpen());
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
      m_Running = false;
    }
  }

  private void accept(final SelectionKey key) throws IOException {
    final UUID uuid = UUID.randomUUID();
    Logger.log(LogTypes.SERVER, m_Port, uuid, "Accepting connection...");

    final ServerSocketChannel socketChannel = (ServerSocketChannel) key.channel();
    final SocketChannel socket = socketChannel.accept();

    socket.configureBlocking(false);
    socket.register(key.selector(), SelectionKey.OP_READ);

    m_Sockets.put(socket, ByteBuffer.allocate(m_ArraySize * 4 * 2));
    m_UUIDS.put(socket, uuid);
  }

  private void read(final SelectionKey key) throws IOException {
    final SocketChannel socket = (SocketChannel) key.channel();
    final ByteBuffer byteBuffer = m_Sockets.get(socket);

    Logger.log(LogTypes.SERVER, m_Port, m_UUIDS.get(socket), "Reading...");

    try {
      if (socket.read(byteBuffer) == -1) {
        try {
          socket.close();
        } catch (IOException e) {
        }
        m_Sockets.remove(socket);
        return;
      }
    } catch (SocketException e) {
      return;
    }

    byteBuffer.flip();
    m_Sockets.put(socket, sumArrays(byteBuffer));

    socket.configureBlocking(false);

    key.interestOps(SelectionKey.OP_WRITE);
  }

  private void write(final SelectionKey key) throws IOException {
    final SocketChannel socket = (SocketChannel) key.channel();
    final ByteBuffer byteBuffer = m_Sockets.get(socket);

    Logger.log(LogTypes.SERVER, m_Port, m_UUIDS.get(socket), "Writing...");

    socket.write(byteBuffer);
    while (!byteBuffer.hasRemaining()) {
      byteBuffer.compact();
      key.interestOps(SelectionKey.OP_READ);
    }
  }

  private ByteBuffer sumArrays(final ByteBuffer byteBuffer) {
    final ByteBuffer resultBuffer = ByteBuffer.allocate(m_ArraySize * 4);

    final MultiThreadArraySum arraySumTask = new MultiThreadArraySum(byteBuffer, resultBuffer);
    arraySumTask.compute();

    return resultBuffer;
  }

}
