package kaap.veiko.debuggerforker.commands.constants;

public class EventKindConstants {
    public static final short SINGLE_STEP = 1;
    public static final short BREAKPOINT = 2;
    public static final short FRAME_POP = 3;
    public static final short EXCEPTION = 4;
    public static final short USER_DEFINED = 5;
    public static final short THREAD_START = 6;
    public static final short THREAD_DEATH = 7;
    public static final short CLASS_PREPARE = 8;
    public static final short CLASS_UNLOAD = 9;
    public static final short CLASS_LOAD = 10;
    public static final short FIELD_ACCESS = 20;
    public static final short FIELD_MODIFICATION = 21;
    public static final short EXCEPTION_CATCH = 30;
    public static final short METHOD_ENTRY = 40;
    public static final short METHOD_EXIT = 41;
    public static final short METHOD_EXIT_WITH_RETURN_VALUE = 42;
    public static final short MONITOR_CONTENDED_ENTER = 43;
    public static final short MONITOR_CONTENDED_ENTERED = 44;
    public static final short MONITOR_WAIT = 45;
    public static final short MONITOR_WAITED = 46;
    public static final short VM_START = 90;
    public static final short VM_DEATH = 99;
    public static final short VM_DISCONNECTED = 100;
}
