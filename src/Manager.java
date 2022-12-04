import java.util.*;

public class Manager {
    Integer id = 0;
    HashMap<Integer,Task> taskMap = new HashMap<>(); // Integer = ID
    HashMap<Integer,Epic> epicMap = new HashMap<>();
    HashMap<Integer, Subtask> subTaskMap = new HashMap<>();
    public Manager() {
    }

    public void createTask(Task task2) {
        int taskId = id;
        taskMap.put(taskId, task2);
        id = id + 1;
    }

    public void createEpic(Epic epic1) {
        int epicId = id;
        epicMap.put(epicId, epic1);
        id = id + 1;
    }

    public void createSubtask(Subtask subtask1, Integer epicId) {
        int subTaskId = id;
        subTaskMap.put(subTaskId, subtask1);
        id = id + 1;
        epicMap.get(epicId).getSubtaskList().add(subtask1);
        epicMap.get(epicId).setEpicStatus();
    }
    public List<String> printArrayAllTasks() {
        ArrayList<String> allTasks = new ArrayList<>();
        for (int k: taskMap.keySet()){
           String content = taskMap.get(k).name;
            String content1 = taskMap.get(k).nameof;
            allTasks.add(content);
            allTasks.add(content1);
        }
       return allTasks;
    }
    public List<String> printArrayAllSubTasks() {
        ArrayList<String> allSubTasks = new ArrayList<>();
        for (int k : subTaskMap.keySet()) {
            String content = subTaskMap.get(k).name + subTaskMap.get(k).nameof;
            allSubTasks.add(content);
        }
        return allSubTasks;
    }
    public List<String> printArrayEpic() {
        ArrayList<String> allEpics = new ArrayList<>();
        for (int k : epicMap.keySet()) {
            String content = epicMap.get(k).name;
            allEpics.add(content);
        }
        return allEpics;
    }
    public void updateTask(Task task) {
        taskMap.put(task.getId(), task);
    }

    public void updateEpic(Epic epic) {
        epicMap.put(epic.getId(), epic);
    }

    public void updateSubtask(int epicId, Subtask subtask) {
        subTaskMap.put(subtask.getId(), subtask);
    }

    public void deleteAllTasks() {
        taskMap.clear();
        subTaskMap.clear();
        epicMap.clear();
    }

    public void deleteTask(int id) {
        taskMap.remove(id);
    }

    public void deleteSubtask(int id) {
        int eId = subTaskMap.get(id).getEpicId();
        epicMap.get(eId).setEpicStatus();
        subTaskMap.remove(id);
    }

    public void deleteEpic(int id) {
        ArrayList<Integer> delete = new ArrayList<>();
        for (int i: subTaskMap.keySet()) {
            int eId = subTaskMap.get(i).getEpicId();
            if (id == eId) {
                delete.add(eId);
               // subTaskMap.remove(i);
            }
        }
        for (int i = 0; i < delete.size(); i++) {
           subTaskMap.remove(delete.get(i));
        }

        epicMap.remove(id);
    }

    public Task getTaskById(int id) {
        return taskMap.get(id);
    }
    public Subtask getSubTaskById(int id) {
        return subTaskMap.get(id);
    }
    public Epic getEpicById(int id) {
        return epicMap.get(id);
    }
    public List<Subtask> printEpicSubtasks(int epicId){
        if (epicMap.containsKey(epicId)){
            ArrayList<Subtask> subTasksList = epicMap.get(epicId).getSubtaskList();
            return subTasksList;
        }  else {
            return null;
        }
    }

}
