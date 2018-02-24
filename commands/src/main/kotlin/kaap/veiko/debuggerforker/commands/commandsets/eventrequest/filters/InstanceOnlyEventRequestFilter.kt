package kaap.veiko.debuggerforker.commands.commandsets.eventrequest.filters

import kaap.veiko.debuggerforker.types.DataReader
import kaap.veiko.debuggerforker.types.DataWriter
import kaap.veiko.debuggerforker.types.jdwp.ObjectId

data class InstanceOnlyEventRequestFilter(val instance: ObjectId) : EventRequestFilter() {
    override fun write(writer: DataWriter?) = writer!!.run {
        writeByte(IDENTIFIER)
        writeType(instance)
    }

    companion object {
        const val IDENTIFIER: Byte = 11
        @JvmStatic fun read(reader: DataReader) = InstanceOnlyEventRequestFilter(ObjectId.read(reader))
    }
}