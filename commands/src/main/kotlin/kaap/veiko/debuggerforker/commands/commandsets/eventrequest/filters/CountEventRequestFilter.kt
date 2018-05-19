package kaap.veiko.debuggerforker.commands.commandsets.eventrequest.filters

import kaap.veiko.debuggerforker.types.DataReader
import kaap.veiko.debuggerforker.types.DataWriter

data class CountEventRequestFilter(val count: Int) : EventRequestFilter() {
    override fun write(writer: DataWriter) = writer.run {
        writeByte(IDENTIFIER)
        writeInt(count)
    }

    companion object {
        const val IDENTIFIER: Byte = 1
        @JvmStatic fun read(reader: DataReader) = CountEventRequestFilter(reader.readInt())
    }
}