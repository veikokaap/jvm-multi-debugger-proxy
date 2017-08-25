package kaap.veiko.debuggerforker.packet;

import java.io.Closeable;
import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

public class PacketStream implements Closeable {

    private final SocketChannel socketChannel;
    private final PacketReader packetReader;
    private final PacketWriter packetWriter;
    private final boolean fromVirtualMachine;

    private final List<Packet> readPacketHistory = new ArrayList<>();

    public PacketStream(SocketChannel socketChannel, boolean fromVirtualMachine) throws IOException {
        socketChannel.configureBlocking(false);
        this.socketChannel = socketChannel;
        this.packetReader = new PacketReader(socketChannel);
        this.packetWriter = new PacketWriter(socketChannel);
        this.fromVirtualMachine = fromVirtualMachine;
    }

    public Packet read() throws IOException {
        Packet read = packetReader.read();
        if (read != null) {
            read.setFromVirtualMachine(fromVirtualMachine);
            readPacketHistory.add(read);
        }
        return read;
    }

    public void write(Packet packet) throws IOException {
        packetWriter.write(packet);
    }

    public List<Packet> getReadPacketHistory() {
        return readPacketHistory;
    }

    @Override
    public void close() throws IOException {
        socketChannel.close();
    }
}
