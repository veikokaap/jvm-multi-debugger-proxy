package kaap.veiko.debuggerforker.commands.commandsets.eventrequest.filters

import kaap.veiko.debuggerforker.types.DataReader
import kaap.veiko.debuggerforker.types.DataWriter
import kaap.veiko.debuggerforker.types.jdwp.ReferenceTypeId

data class ExceptionOnlyEventRequestFilter(
        val exceptionOrNull: ReferenceTypeId,
        val caught: Boolean,
        val uncaught: Boolean
) : EventRequestFilter() {
    override fun write(writer: DataWriter?) = writer!!.run {
        writeByte(IDENTIFIER)
        writeType(exceptionOrNull)
        writeBoolean(caught)
        writeBoolean(uncaught)
    }

    companion object {
        const val IDENTIFIER: Byte = 8
        @JvmStatic fun read(reader: DataReader) = ExceptionOnlyEventRequestFilter(
                ReferenceTypeId.read(reader),
                reader.readBoolean(),
                reader.readBoolean()
        )
    }
}