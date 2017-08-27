package kaap.veiko.debuggerforker;

import kaap.veiko.debuggerforker.commands.Command;
import kaap.veiko.debuggerforker.commands.parser.CommandParser;
import kaap.veiko.debuggerforker.commands.sets.virtualmachine.IDSizesReplyCommand;
import kaap.veiko.debuggerforker.connections.DebuggerConnection;
import kaap.veiko.debuggerforker.connections.VirtualMachineConnection;
import kaap.veiko.debuggerforker.connections.connectors.DebuggerConnector;
import kaap.veiko.debuggerforker.connections.connectors.VMConnector;
import kaap.veiko.debuggerforker.packet.Packet;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public class DebuggerForker implements AutoCloseable {

    private final VirtualMachineConnection vm;
    private final List<DebuggerConnection> debuggers = new ArrayList<>();

    private final Thread debuggerConnectionThread;
    private IDSizesReplyCommand idSizes = null;

    private DebuggerForker(VirtualMachineConnection vm, int debuggerPort) {
        this.vm = vm;

        debuggerConnectionThread = new Thread(() -> {
            try {
                DebuggerConnection debugger = DebuggerConnector.waitForConnectionFromDebugger(debuggerPort);
                synchronized (debuggers) {
                    debuggers.add(debugger);
                    debuggers.notifyAll();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static void start(InetSocketAddress virtualMachineAddress, int debuggerPort) throws IOException, InterruptedException {
        VirtualMachineConnection vm = VMConnector.connectToVM(virtualMachineAddress);

        DebuggerForker debuggerForker = new DebuggerForker(vm, debuggerPort);
        debuggerForker.start();
    }

    private void start() throws IOException, InterruptedException {
        debuggerConnectionThread.start();
        waitForFirstDebuggerConnection();

        while (true) {
            Packet vmPacket = vm.getPacketStream().read();

            if (vmPacket != null) {
                if (vmPacket.isReply()) {
                    Packet packet = debuggers.get(0).getPacketStream().getReadPacketHistory().stream()
                            .filter(pkt -> pkt.getId() == vmPacket.getId())
                            .findFirst().get();

                    vmPacket.setCommandSet(packet.getCommandSet());
                    vmPacket.setCommand(packet.getCommand());
                }

                System.out.println("VMachine: " + vmPacket);
                Command command = new CommandParser(this).parse(vmPacket);
                if (command != null) {
                    System.out.println(command);
                    if (command instanceof IDSizesReplyCommand) {
                        this.idSizes = (IDSizesReplyCommand) command;
                    }
                }
                synchronized (debuggers) {
                    for (DebuggerConnection debugger : debuggers) {
                        debugger.getPacketStream().write(vmPacket);
                    }
                }
            }

            synchronized (debuggers) {
                for (DebuggerConnection debugger : debuggers) {
                    Packet debuggerPacket = debugger.getPacketStream().read();
                    if (debuggerPacket != null) {
                        System.out.println("Debugger: " + debuggerPacket);
                        System.out.println(new CommandParser(this).parse(debuggerPacket));
                        vm.getPacketStream().write(debuggerPacket);
                    }
                }
            }
        }
    }

    private void waitForFirstDebuggerConnection() throws InterruptedException {
        synchronized (debuggers) {
            while (debuggers.isEmpty()) {
                debuggers.wait();
            }
        }
    }


    @Override
    public void close() throws Exception {
        debuggerConnectionThread.interrupt();
        vm.close();
        synchronized (debuggers) {
            for (DebuggerConnection debugger : debuggers) {
                debugger.close();
            }
        }
    }

    public IDSizesReplyCommand getIdSizes() {
        return idSizes;
    }
}
