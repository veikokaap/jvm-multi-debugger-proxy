package kaap.veiko.debuggerforker.commands.commandsets.eventrequest.filters

import kaap.veiko.debuggerforker.types.DataReader
import kaap.veiko.debuggerforker.types.DataWriter
import kaap.veiko.debuggerforker.types.jdwp.ThreadId

data class ThreadOnlyEventRequestFilter(val thread: ThreadId) : EventRequestFilter() {
    override fun write(writer: DataWriter?) = writer!!.run {
        writeByte(IDENTIFIER)
        writeType(thread)
    }

    companion object {
        const val IDENTIFIER: Byte = 3
        @JvmStatic fun read(reader: DataReader) = ThreadOnlyEventRequestFilter(ThreadId.read(reader))
    }
}