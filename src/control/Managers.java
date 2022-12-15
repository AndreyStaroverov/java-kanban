package control;

public class Managers {

    static TaskManager InMemoryTaskManager;

    static HistoryManager InMemoryHistoryManager;

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

     public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
