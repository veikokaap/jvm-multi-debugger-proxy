package kaap.veiko.debuggerforker.commands.commandsets.eventrequest.filters

import kaap.veiko.debuggerforker.types.DataReader
import kaap.veiko.debuggerforker.types.DataWriter

data class SourceNameMatchEventRequestFilter(val sourceNamePattern: String) : EventRequestFilter() {
    override fun write(writer: DataWriter) = writer.run {
        writeByte(IDENTIFIER)
        writeString(sourceNamePattern)
    }

    companion object {
        const val IDENTIFIER: Byte = 12
        @JvmStatic fun read(reader: DataReader) = SourceNameMatchEventRequestFilter(reader.readString())
    }
}