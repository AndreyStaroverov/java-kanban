package control;

import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private Integer id = 0;

    private final HashMap<Integer, Task> taskMap = new HashMap<>(); // Integer = Id
    private final HashMap<Integer, Epic> epicMap = new HashMap<>();
    private final HashMap<Integer, Subtask> subTaskMap = new HashMap<>();

    private final HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public void createTask(Task task) {
        int taskId = id;
        task.setId(id);
        taskMap.put(taskId, task);
        id++;
    }

    @Override
    public void createEpic(Epic epic) {
        int epicId = id;
        epic.setId(id);
        epicMap.put(epicId, epic);
        id++;
    }

    @Override
    public void createSubtask(Subtask subtask, Integer epicId) {
        int subTaskId = id;
        subtask.setId(id);
        subTaskMap.put(subTaskId, subtask);
        id++;
        epicMap.get(epicId).getSubtaskList().add(subtask);
        epicMap.get(epicId).setEpicStatus();
    }

    @Override
    public List<Task> getTasks() {
        ArrayList<Task> allTasks = new ArrayList<>();
        for (int k: taskMap.keySet()){
            allTasks.add(taskMap.get(k));
        }
       return allTasks;
    }

    @Override
    public List<Subtask> getSubTasks() {
        ArrayList<Subtask> allSubTasks = new ArrayList<>();
        for (int k : subTaskMap.keySet()) {
            allSubTasks.add(subTaskMap.get(k));
        }
        return allSubTasks;
    }

    @Override
    public List<Epic> getEpics() {
        ArrayList<Epic> allEpics = new ArrayList<>();
        for (int k : epicMap.keySet()) {
            allEpics.add(epicMap.get(k));
        }
        return allEpics;
    }
    public void updateTask(Task task) {
        taskMap.put(task.getId(), task);
    }

    public void updateEpic(Epic epic) {
        epicMap.put(epic.getId(), epic);
    }

    @Override
    public void updateSubtask(int epicId, Subtask subtask) {
        subTaskMap.put(subtask.getId(), subtask);
        for (int k = 0; k < epicMap.get(epicId).getSubtaskList().size(); k++) {
            boolean check;
            check = (epicMap.get(epicId).getSubtaskList().get(k).getId()) == (subtask.getId());
            if (check) {
                epicMap.get(epicId).getSubtaskList().remove(k);
            }
        }
        epicMap.get(epicId).getSubtaskList().add(subtask);
        epicMap.get(epicId).setEpicStatus();
    }

    @Override
    public void deleteTask(int id) {
        taskMap.remove(id);
    }

    @Override
    public void deleteAllTasks(){
        epicMap.clear();
        taskMap.clear();
        subTaskMap.clear();
        for (Integer k: epicMap.keySet()) {
            epicMap.get(k).getSubtaskList().clear();
        }
    }

    @Override
    public void deleteSubtask(int id) {
        int eId = subTaskMap.get(id).getEpicId();
        for (int k = 0; k < epicMap.get(eId).getSubtaskList().size(); k++) {
            boolean check;
            check = (epicMap.get(eId).getSubtaskList().get(k).getId()) == id;
            if (check) {
                epicMap.get(eId).getSubtaskList().remove(k);
            }
        }
        subTaskMap.remove(id);
        epicMap.get(eId).setEpicStatus();
    }

    @Override
    public void deleteEpic(int id) {
        ArrayList<Integer> delete = new ArrayList<>();
        for (int i: subTaskMap.keySet()) {
            int eId = subTaskMap.get(i).getEpicId();
            if (id == eId) {
                delete.add(i);
            }
        }
        for (Integer integer : delete) {
            subTaskMap.remove(integer);
        }

        epicMap.remove(id);
    }

    @Override
    public Task getTaskById(int id) {
        historyManager.add(taskMap.get(id));
        return taskMap.get(id);
    }

    @Override
    public Subtask getSubTaskById(int id) {
        historyManager.add(subTaskMap.get(id));
        return subTaskMap.get(id);
    }

    @Override
    public Epic getEpicById(int id) {
        historyManager.add(epicMap.get(id));
        return epicMap.get(id);
    }

    @Override
    public List<Subtask> getEpicSubtasks(int epicId){
        if (epicMap.containsKey(epicId)){
            List<Subtask> subTasksList = epicMap.get(epicId).getSubtaskList();
            return subTasksList;
        }  else {
            return null;
        }
    }

    @Override
    public List<Task> getHistory(){
        return historyManager.getHistory();
    }
}
