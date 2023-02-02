package control;

import Tasks.*;
import exception.ManagerSaveException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {

    protected File file;

    protected FileBackedTasksManager(File file) {
        this.file = file;
    }


    protected void save() {
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write("id,type,name,status,description,epic \n");
            for (Task i : super.getTasks()) {
                String ts = toString(i);
                fileWriter.write(ts + "\n");
            }
            for (Task i : super.getEpics()) {
                String ts = toString(i);
                fileWriter.write(ts + "\n");
            }
            for (Task i : super.getSubTasks()) {
                String ts = toString(i);
                fileWriter.write(ts + "\n");
            }
            fileWriter.write("\n");
            fileWriter.write(historyToString(getHistoryManager()));

        } catch (IOException e) {
            throw new ManagerSaveException();
        }
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public Subtask getSubTaskById(int id) {
        Subtask task = super.getSubTaskById(id);
        save();
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic task = super.getEpicById(id);
        save();
        return task;
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void createSubtask(Subtask subtask, Integer epicId) {
        super.createSubtask(subtask, epicId);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(int epicId, Subtask subtask) {
        super.updateSubtask(epicId, subtask);
        save();
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteSubtask(int id) {
        super.deleteSubtask(id);
        save();
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    public FileBackedTasksManager loadFromFile(File file) {
        try (FileReader reader = new FileReader(file)) {
            BufferedReader br = new BufferedReader(reader);
            int lastId = 0;
            br.readLine();

            while (br.ready()) {
                String line = br.readLine();
                if (line.isEmpty()) {
                    List<Integer> history = historyFromString(br.readLine());
                    for (Integer i : history) {
                        if (getTaskMap().containsKey(i)) {
                            getHistoryManager().add(getTaskMap().get(i));
                        }
                        if (getEpicMap().containsKey(i)) {
                            getHistoryManager().add(getEpicMap().get(i));
                        }
                        if (getSubTaskMap().containsKey(i)) {
                            getHistoryManager().add(getSubTaskMap().get(i));
                        }
                    }
                } else {
                    lastId = fromString(line).getId();
                }
            }
            super.setId(lastId + 1);
        } catch (IOException e) {
            System.out.println("Error read");
        }
        return this;
    }


    public String toString(Task task) {
        String taskToString = "";

        if (task.getType() == TypeOfTask.SUBTASK) {
            Subtask subtask = (Subtask) task;
            taskToString = task.getId() + "," +
                    task.getType() + "," +
                    task.getName() + "," +
                    task.getStatus() + "," +
                    task.getDescription() + "," +
                    subtask.getEpicId();
        } else if (task.getType() == TypeOfTask.TASK) {
            taskToString = task.getId() + "," +
                    task.getType() + "," +
                    task.getName() + "," +
                    task.getStatus() + "," +
                    task.getDescription();
        } else {
            taskToString = task.getId() + "," +
                    task.getType() + "," +
                    task.getName() + "," +
                    task.getStatus();
        }
        return taskToString;
    }

    public Task fromString(String value) {
        String[] tsk = value.split(",");
        Integer id = Integer.parseInt(tsk[0]);
        StatusOfTask st;
        TypeOfTask tp;
        switch (tsk[3]) {
            case "NEW":
                st = StatusOfTask.NEW;
                break;
            case "IN_PROGRESS":
                st = StatusOfTask.IN_PROGRESS;
                break;
            case "DONE":
                st = StatusOfTask.DONE;
                break;
            default:
                st = null;
        }
        switch (tsk[1]) {
            case "TASK":
                tp = TypeOfTask.TASK;
                break;
            case "EPIC":
                tp = TypeOfTask.EPIC;
                break;
            case "SUBTASK":
                tp = TypeOfTask.SUBTASK;
                break;
            default:
                tp = null;
        }
        if (tp == TypeOfTask.SUBTASK) {
            int epid = Integer.parseInt(tsk[5]);
            Subtask task = new Subtask(tsk[2], tsk[4], st, epid, id, tp);
            super.setSubtaskMap(task);
            super.getEpicMap().get(epid).getSubtaskList().add(task);
            super.getEpicMap().get(epid).setEpicStatus();
            return task;
        } else if (tp == TypeOfTask.TASK) {
            Task task = new Task(tsk[2], tsk[4], st, id, tp);
            super.setTaskMap(task);
            return task;
        } else {
            Epic task = new Epic(tsk[2], id, st, tp);
            super.setEpicMap(task);
            return task;
        }
    }

    static String historyToString(HistoryManager manager) {
        List<Task> history = manager.getHistory();
        List<String> hs = new ArrayList<>();
        for (Task t : history) {
            String id = t.getId().toString();
            hs.add(id);
        }
        String hist = String.join(",", hs);
        return hist;
    }

    static List<Integer> historyFromString(String value) {
        List<Integer> historyFromString = new ArrayList<>();
        String[] hst = value.split(",");
        for (int i = 0; i < hst.length; i++) {
            Integer el = Integer.parseInt(hst[i]);
            historyFromString.add(el);
        }
        return historyFromString;
    }
}
