package kaap.veiko.debuggerforker.commands.commandsets.eventrequest.filters

import kaap.veiko.debuggerforker.types.DataReader
import kaap.veiko.debuggerforker.types.DataWriter

data class ClassExcludeEventRequestFilter(val classPattern: String) : EventRequestFilter() {
    override fun write(writer: DataWriter?) = writer!!.run {
        writeByte(IDENTIFIER)
        writeString(classPattern)
    }

    companion object {
        const val IDENTIFIER: Byte = 6
        @JvmStatic fun read(reader: DataReader) = ClassExcludeEventRequestFilter(reader.readString())
    }
}