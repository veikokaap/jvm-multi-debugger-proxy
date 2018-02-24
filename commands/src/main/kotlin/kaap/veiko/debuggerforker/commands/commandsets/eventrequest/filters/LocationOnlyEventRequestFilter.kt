package kaap.veiko.debuggerforker.commands.commandsets.eventrequest.filters

import kaap.veiko.debuggerforker.types.DataReader
import kaap.veiko.debuggerforker.types.DataWriter
import kaap.veiko.debuggerforker.types.jdwp.Location

data class LocationOnlyEventRequestFilter(val location: Location) : EventRequestFilter() {
    override fun write(writer: DataWriter?) = writer!!.run {
        writeByte(IDENTIFIER)
        writeType(location)
    }

    companion object {
        const val IDENTIFIER: Byte = 7
        @JvmStatic fun read(reader: DataReader) = LocationOnlyEventRequestFilter(Location.read(reader))
    }
}