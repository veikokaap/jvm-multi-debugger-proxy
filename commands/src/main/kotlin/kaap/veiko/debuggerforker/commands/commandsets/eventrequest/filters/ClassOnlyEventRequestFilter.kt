package kaap.veiko.debuggerforker.commands.commandsets.eventrequest.filters

import kaap.veiko.debuggerforker.types.DataReader
import kaap.veiko.debuggerforker.types.DataWriter
import kaap.veiko.debuggerforker.types.jdwp.ReferenceTypeId

data class ClassOnlyEventRequestFilter(val clazz: ReferenceTypeId) : EventRequestFilter() {
    override fun write(writer: DataWriter?) = writer!!.run {
        writeByte(IDENTIFIER)
        writeType(clazz)
    }

    companion object {
        const val IDENTIFIER: Byte = 4
        @JvmStatic fun read(reader: DataReader) = ClassOnlyEventRequestFilter(ReferenceTypeId.read(reader))
    }
}