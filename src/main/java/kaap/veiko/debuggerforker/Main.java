package kaap.veiko.debuggerforker;

import kaap.veiko.debuggerforker.connections.*;
import kaap.veiko.debuggerforker.connections.connectors.DebuggerConnector;
import kaap.veiko.debuggerforker.connections.connectors.VMConnector;
import kaap.veiko.debuggerforker.packet.Packet;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 5123);

        VirtualMachineConnection vm = VMConnector.connectToVM(address);
        DebuggerConnection debugger = DebuggerConnector.waitForConnectionFromDebugger(5456);

        while (true) {
            Packet vmPacket = vm.getPacketStream().read();
            Packet debuggerPacket = debugger.getPacketStream().read();
            if (debuggerPacket != null) {
                System.out.println("Debugger: " + debuggerPacket);
                vm.getPacketStream().write(debuggerPacket);
            }
            if (vmPacket != null) {
                System.out.println("VMachine: " + vmPacket);
                debugger.getPacketStream().write(vmPacket);
            }
        }
    }
}
