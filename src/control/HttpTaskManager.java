package control;

import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;

import java.io.File;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpTaskManager extends FileBackedTasksManager {
    TaskManager manager;

    URI uri = URI.create("https://www.getpostman.com/collections/a83b61d9e1c81c10575c");

    private final Map<Integer, Task> tasks = new HashMap<>();

    public HttpTaskManager(URI uri,File file) {
        super(file);
        this.manager =  Managers.getFileBackedTaskManager();
        this.uri = uri;
    }
}
