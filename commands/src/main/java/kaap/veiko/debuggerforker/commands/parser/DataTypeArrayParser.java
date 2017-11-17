package kaap.veiko.debuggerforker.commands.parser;

import java.util.List;

import kaap.veiko.debuggerforker.types.DataType;
import kaap.veiko.debuggerforker.types.DataWriter;

public interface DataTypeArrayParser<T extends DataType> {
  List<T> readList(CommandDataReader reader) throws Exception;

  void writeList(DataWriter writer, List<T> list);
}