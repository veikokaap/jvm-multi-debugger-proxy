package kaap.veiko.debuggerforker.packet.reader;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.Test;

import kaap.veiko.debuggerforker.packet.CommandPacket;
import kaap.veiko.debuggerforker.packet.Packet;
import kaap.veiko.debuggerforker.packet.PacketSource;
import kaap.veiko.debuggerforker.packet.VirtualMachinePacketStream;

public class PacketReaderTest {

  @Test(timeout = 5_000)
  public void testReadPacket() throws Throwable {
    testReadCommandPacketWithData(12, (byte) 4, (byte) 12, new byte[]{1, 2, 3, 4, 5, 6, 7, 8});
    testReadCommandPacketWithData(Integer.MAX_VALUE, Byte.MIN_VALUE, Byte.MAX_VALUE, new byte[0]);
    testReadCommandPacketWithData(Integer.MIN_VALUE, Byte.MAX_VALUE, Byte.MIN_VALUE, new byte[0]);
    testReadCommandPacketWithData(Integer.MIN_VALUE, Byte.MAX_VALUE, Byte.MIN_VALUE, bigRandomBytesArray());
  }

  private byte[] bigRandomBytesArray() {
    byte[] randomBytes = new byte[Short.MAX_VALUE];
    new Random().nextBytes(randomBytes);
    return randomBytes;
  }

  private void testReadCommandPacketWithData(int id, byte commandSet, byte command, byte[] data) throws Throwable {
    AtomicReference<SocketAddress> socketAddressRef = new AtomicReference<>();
    ByteBuffer writtenBuffer = createCommandPacket(id, commandSet, command, data);

    AtomicReference<Throwable> vmThrowableRef = startMockVmChannelThread(socketAddressRef, writtenBuffer);
    SocketChannel socketChannel = connectToMockVm(socketAddressRef);

    Packet packet = readPacketFromVm(socketChannel);

    if (vmThrowableRef.get() != null) {
      throw vmThrowableRef.get();
    }

    assertThat(packet, is(instanceOf(CommandPacket.class)));
    assertEquals(11 + data.length, packet.getLength());
    assertEquals(id, packet.getId());
    assertEquals(commandSet, ((CommandPacket) packet).getCommandSetId());
    assertEquals(command, ((CommandPacket) packet).getCommandId());
    assertArrayEquals(data, packet.getData());
  }

  private Packet readPacketFromVm(SocketChannel socketChannel) throws IOException {
    VirtualMachinePacketStream stream = new VirtualMachinePacketStream(socketChannel);
    PacketSource source = stream.getSource();
    PacketReader reader = new PacketReader(socketChannel, source);

    Packet packet = null;
    while (packet == null) {
      packet = reader.read();
    }
    return packet;
  }

  private SocketChannel connectToMockVm(AtomicReference<SocketAddress> socketAddressRef) throws InterruptedException, IOException {
    SocketAddress address = socketAddressRef.get();
    while (address == null) {
      address = socketAddressRef.get();
      Thread.sleep(100);
    }

    return SocketChannel.open(address);
  }

  private AtomicReference<Throwable> startMockVmChannelThread(AtomicReference<SocketAddress> socketAddress, ByteBuffer writtenBuffer) {
    AtomicReference<Throwable> throwableRef = new AtomicReference<>();
    Thread thread = new Thread(() -> {
      try (ServerSocketChannel serverChannel = ServerSocketChannel.open()) {
        serverChannel.bind(new InetSocketAddress(0));
        socketAddress.set(serverChannel.getLocalAddress());
        serverChannel.configureBlocking(true);
        SocketChannel sc = serverChannel.accept();
        while (writtenBuffer.hasRemaining()) {
          sc.write(writtenBuffer);
        }
      }
      catch (Throwable e) {
        throwableRef.set(e);
      }
    });

    thread.start();
    return throwableRef;
  }

  private static ByteBuffer createCommandPacket(int id, byte commandSet, byte command, byte[] data) {
    ByteBuffer buffer = ByteBuffer.allocate(11 + data.length);
    buffer.putInt(11 + data.length);
    buffer.putInt(id);
    buffer.put((byte) 0);
    buffer.put(commandSet);
    buffer.put(command);
    buffer.put(data);
    buffer.flip();
    return buffer;
  }
}