package kaap.veiko.debuggerforker.commands.commandsets.event.events

import kaap.veiko.debuggerforker.types.DataReader
import kaap.veiko.debuggerforker.types.DataWriter
import kaap.veiko.debuggerforker.types.jdwp.EventKind

data class VmDeathEvent(val requestId: Int) : VirtualMachineEvent() {

    override fun write(writer: DataWriter?) = writer!!.run {
        writeType(EventKind.VM_DEATH)
        writeInt(requestId)
    }

    companion object {
        @JvmStatic
        fun read(reader: DataReader) = VmDeathEvent(
                reader.readInt()
        )
    }
}