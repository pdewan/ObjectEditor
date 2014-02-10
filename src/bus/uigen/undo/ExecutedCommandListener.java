package bus.uigen.undo;

import util.undo.ExecutedCommand;

public interface ExecutedCommandListener {
    public void commandExecuted(ExecutedCommand executedCommand);
}