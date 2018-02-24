package kaap.veiko.debuggerforker.commands.commandsets.eventrequest.filters

import kaap.veiko.debuggerforker.types.DataReader
import kaap.veiko.debuggerforker.types.DataWriter
import kaap.veiko.debuggerforker.types.jdwp.FieldId
import kaap.veiko.debuggerforker.types.jdwp.ReferenceTypeId

data class FieldOnlyEventRequestFilter(
        val declaring: ReferenceTypeId,
        val fieldId: FieldId
) : EventRequestFilter() {
    override fun write(writer: DataWriter?) = writer!!.run {
        writeByte(IDENTIFIER)
        writeType(declaring)
        writeType(fieldId)
    }

    companion object {
        const val IDENTIFIER: Byte = 9
        @JvmStatic fun read(reader: DataReader) = FieldOnlyEventRequestFilter(
                ReferenceTypeId.read(reader),
                FieldId.read(reader)
        )
    }
}