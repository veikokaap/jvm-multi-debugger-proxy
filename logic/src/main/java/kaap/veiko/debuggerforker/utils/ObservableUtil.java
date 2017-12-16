package kaap.veiko.debuggerforker.utils;

import java.io.IOException;

import io.reactivex.Observable;
import kaap.veiko.debuggerforker.commands.Command;
import kaap.veiko.debuggerforker.commands.CommandStream;

public class ObservableUtil {

  public static Observable<Command> commandStreamObservable(CommandStream stream) {
    return Observable.create(subscriber -> {
      try {
        while (!stream.isClosed()) {
          Command command = stream.read();
          if (command != null) {
            subscriber.onNext(command);
          }
        }
        subscriber.onComplete();
      }
      catch (IOException exception) {
        subscriber.onError(exception);
      }
    });
  }

}
