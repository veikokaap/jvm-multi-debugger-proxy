package kaap.veiko.debuggerforker.packet;

import java.util.Arrays;

public class Packet {

    static final int HEADER_LENGTH = 11;

    private int length;
    private int id;
    private short flags;
    private short commandSet;
    private short command;
    private short errorCode;
    private byte[] data = new byte[]{};
    private boolean fromVirtualMachine;

    public Packet() {
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public short getFlags() {
        return flags;
    }

    public void setFlags(short flags) {
        this.flags = flags;
    }

    public short getCommandSet() {
        return commandSet;
    }

    public void setCommandSet(short commandSet) {
        this.commandSet = commandSet;
    }

    public short getCommand() {
        return command;
    }

    public void setCommand(short command) {
        this.command = command;
    }

    public short getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(short errorCode) {
        this.errorCode = errorCode;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public boolean isReply() {
        return getFlags() == 128;
    }

    public boolean isFromVirtualMachine() {
        return fromVirtualMachine;
    }

    public void setFromVirtualMachine(boolean fromVirtualMachine) {
        this.fromVirtualMachine = fromVirtualMachine;
    }

    public boolean isFromDebugger() {
        return !isFromVirtualMachine();
    }

    public boolean hasData() {
        return getLength() > HEADER_LENGTH;
    }

    @Override
    public String toString() {
        String string = "Packet{" +
                "length=" + length +
                ", id=" + id +
                ", flags=" + flags;

        if (isReply()) {
            string += ", errorCode=" + errorCode;
        } else {
            string += ", commandSet=" + commandSet + ", command=" + command;
        }

        return string +
                ", data=" + Arrays.toString(data) +
                '}';
    }
}
