package Tasks;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    protected TypeOfTask epic = TypeOfTask.EPIC;
    protected List<Subtask> subtaskList = new ArrayList<>();
    protected LocalDateTime endTime;

    public Epic (String name, List<Subtask> subtaskList, int id) {
        super(name);
        this.subtaskList = subtaskList;
        this.taskid = id;
    }
    public Epic(String name, StatusOfTask statusOfTask){
        super(name);
        this.statusOfTask = statusOfTask;
    }

    public Epic (String name, int id, StatusOfTask st, TypeOfTask type) {
        super(name);
        this.taskid = id;
        this.statusOfTask = st;
        this.epic = type;
    }

    public List<Subtask> getSubtaskList() {
        return subtaskList;
    }

    public void setSubtaskList(Subtask subtask) {
        subtaskList.add(subtask);
    }
    public void setEpicStatus() {
        ArrayList<Enum> subtaskStatuses = new ArrayList<>();
        for (int i = 0; i < subtaskList.size(); i++) {
            Enum status = subtaskList.get(i).getStatus();
            subtaskStatuses.add(status);
        }
            if (subtaskStatuses.contains(StatusOfTask.NEW) && subtaskStatuses.contains(StatusOfTask.DONE)) {
                statusOfTask = StatusOfTask.IN_PROGRESS;
            } else if (subtaskStatuses.contains(StatusOfTask.NEW) || subtaskList.isEmpty()) {
                statusOfTask = StatusOfTask.NEW;
            } else if (subtaskStatuses.contains(StatusOfTask.DONE)) {
                statusOfTask = StatusOfTask.DONE;
            } else if (subtaskStatuses.contains(StatusOfTask.IN_PROGRESS)){
                statusOfTask =StatusOfTask.IN_PROGRESS;
            } else {
                statusOfTask = null;
        }

    }

    public TypeOfTask getType(){
        return epic;
    }
}