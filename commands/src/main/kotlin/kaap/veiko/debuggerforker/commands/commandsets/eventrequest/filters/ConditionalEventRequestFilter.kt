package kaap.veiko.debuggerforker.commands.commandsets.eventrequest.filters

import kaap.veiko.debuggerforker.types.DataReader
import kaap.veiko.debuggerforker.types.DataWriter

data class ConditionalEventRequestFilter(val exprId: Int) : EventRequestFilter() {
    override fun write(writer: DataWriter) = writer.run {
        writeByte(IDENTIFIER)
        writeInt(exprId)
    }

    companion object {
        const val IDENTIFIER: Byte = 2
        @JvmStatic fun read(reader: DataReader) = ConditionalEventRequestFilter(reader.readInt())
    }
}