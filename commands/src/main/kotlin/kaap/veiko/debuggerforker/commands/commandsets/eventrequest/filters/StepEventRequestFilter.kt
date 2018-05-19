package kaap.veiko.debuggerforker.commands.commandsets.eventrequest.filters

import kaap.veiko.debuggerforker.types.DataReader
import kaap.veiko.debuggerforker.types.DataWriter
import kaap.veiko.debuggerforker.types.jdwp.ThreadId

data class StepEventRequestFilter(
        val thread: ThreadId,
        val size: Int,
        val depth: Int
) : EventRequestFilter() {
    override fun write(writer: DataWriter) = writer.run {
        writeByte(IDENTIFIER)
        writeType(thread)
        writeInt(size)
        writeInt(depth)
    }

    companion object {
        const val IDENTIFIER: Byte = 10
        @JvmStatic fun read(reader: DataReader) = StepEventRequestFilter(
                ThreadId.read(reader),
                reader.readInt(),
                reader.readInt()
        )
    }
}