package control;

import java.io.File;

public class Managers {

    static TaskManager InMemoryTaskManager;
    static HistoryManager InMemoryHistoryManager;

    public static TaskManager getDefault() {
        File file = new File(".\\resources\\HistorySaver.csv");
        return new InMemoryTaskManager();
       // return new FileBackedTasksManager(file).loadFromFile(file);
    }

     public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static FileBackedTasksManager getFileBackedTaskManager(File file){
        return new FileBackedTasksManager(file);
    }

}
