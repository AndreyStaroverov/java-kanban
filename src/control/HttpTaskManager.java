package control;

import Servers.KVTaskClient;
import Tasks.*;
import com.google.gson.Gson;
import exception.ManagerSaveException;
import exception.ServerRegisterException;

import java.io.IOException;
import java.net.URI;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class HttpTaskManager extends FileBackedTasksManager {
    protected KVTaskClient kvTaskClient;
    private static final Gson gson = Managers.getGson();
    private static String key = "manager";

    public HttpTaskManager(URI uri) {
        try {
           kvTaskClient = new KVTaskClient(uri);
       } catch (ServerRegisterException e) {
            throw new ManagerSaveException(e.getMessage(), e);
       }
    }

    @Override
    protected void save()  {
        try  {
            StringBuilder sb = new StringBuilder();
            for (Task task : super.getTasks()) {
                String ts = toString(task);
                sb.append(ts);
                sb.append("/");
            }
            for (Task epic : super.getEpics()) {
                String ts = toString(epic);
                sb.append(ts);
                sb.append("/");
            }
            for (Task subtask : super.getSubTasks()) {
                String ts = toString(subtask);
                sb.append(ts);
                sb.append("/");
            }

            String hs = historyToString(getHistoryManager());

            kvTaskClient.put("manager", sb.toString());
            kvTaskClient.put("history", hs);
        } catch (ServerRegisterException e) {
            throw new ManagerSaveException();
        }
    }

    public static HttpTaskManager loadFromServer() {
        HttpTaskManager manager = new HttpTaskManager(URI.create("http://localhost:8078"));
         KVTaskClient kvTaskClient = manager.kvTaskClient;
        try  {
            int lastId = 0;
            String[] lines = kvTaskClient.load("manager").split("/");
            for (String lin: lines) {
                lastId = manager.fromString(lin).getId();
            }
                List<Integer> history = historyFromString(kvTaskClient.load("history"));
                for (Integer i : history) {
                    if (manager.getTaskMap().containsKey(i)) {
                        manager.getHistoryManager().add(manager.getTaskMap().get(i));
                    }
                    if (manager.getEpicMap().containsKey(i)) {
                        manager.getHistoryManager().add(manager.getEpicMap().get(i));
                    }
                    if (manager.getSubTaskMap().containsKey(i)) {
                        manager.getHistoryManager().add(manager.getSubTaskMap().get(i));
                    }
                }

            manager.setId(lastId + 1);
        } catch (ServerRegisterException e) {
            throw new ManagerSaveException("Error in server");
        }
        return manager;
    }


    @Override
    protected Task fromString(String value) {
        String[] tsk = value.split(",");
        Integer id = Integer.parseInt(tsk[0]);
        StatusOfTask st = StatusOfTask.getStatus(tsk[3]);
        TypeOfTask tp = TypeOfTask.getType(tsk[1]);
        switch (tp) {
            case SUBTASK:
                int epid = Integer.parseInt(tsk[5]);
                LocalDateTime timeSub = stringToDate(tsk[6]);
                Duration durSub = stringToDuration(tsk[7]);
                Subtask subtask = new Subtask(tsk[2], tsk[4], st, epid, id, tp,timeSub, durSub);
                super.createSubtask(subtask, epid);
                return subtask;
            case TASK:
                LocalDateTime time = stringToDate(tsk[5]);
                Duration dur = stringToDuration(tsk[6]);
                Task task = new Task(tsk[2], tsk[4], st, id, tp, time,dur);
                super.createTask(task);
                return task;
            case EPIC:
                LocalDateTime timeEp = stringToDate(tsk[4]);
                Duration durEp = stringToDuration(tsk[5]);
                Epic epic = new Epic(tsk[2], id, st, tp, durEp,timeEp);
                super.createEpic(epic);
                return epic;
            default:
                throw new IllegalArgumentException("Argument is invalid or null");
        }
    }
}
