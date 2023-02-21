package control;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.net.URI;
import java.time.LocalDateTime;

public class Managers {

    static TaskManager InMemoryTaskManager;
    static HistoryManager InMemoryHistoryManager;

    public static TaskManager getDefault() {
        File file = new File(".\\resources\\HistorySaver.csv");
        URI uri = URI.create("http://localhost:8078");
        //return new InMemoryTaskManager();
        // return new FileBackedTasksManager(file);
        // return new FileBackedTasksManager(file).loadFromFile(file);
        return new HttpTaskManager(uri);
    }

     public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static FileBackedTasksManager getFileBackedTaskManager(){
        File file = new File(".\\resources\\HistorySaver.csv");
        return new FileBackedTasksManager(file).loadFromFile(file);
    }

    public static Gson getGson(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        return gsonBuilder.create();
    }
}
