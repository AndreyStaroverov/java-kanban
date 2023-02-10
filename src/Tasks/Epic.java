package Tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

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

    public Epic (String name, int id, StatusOfTask st, TypeOfTask type, Duration duration, LocalDateTime localDateTime) {
        super(name);
        this.taskid = id;
        this.statusOfTask = st;
        this.epic = type;
        this.duration = duration;
        this.startTime = localDateTime;
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

    public TreeSet<Task> prioritezedSubTasks(){
        Comparator<Task> comparator = new Comparator<Task>() {
            @Override
            public int compare(Task o1, Task o2) {
                if (o1.getStartTime() == null) return 1;
                if (o2.getStartTime() == null) return -1;
                return o1.getStartTime().compareTo(o2.getStartTime());
            }
        };

        TreeSet<Task> prioritizedSubTasks = new TreeSet<>(comparator);
        prioritizedSubTasks.addAll(subtaskList);

        return prioritizedSubTasks;
    }

    public Duration getDuration(){
        if(!subtaskList.isEmpty()) {
            Duration sum = subtaskList.get(0).getDuration();
            for (int i = 1; i < subtaskList.size(); i++) {
                Duration d = subtaskList.get(i).duration;
                duration = sum.plus(d);
            }
        }
        return duration;
    }

    public LocalDateTime getStartTime(){
        TreeSet<Task> priorSubs = prioritezedSubTasks();
        if(!priorSubs.isEmpty()) {
            startTime = priorSubs.first().getStartTime();
        }
        return startTime;
    }

    @Override
    public LocalDateTime getEndTime(){
        TreeSet<Task> priorSubs = prioritezedSubTasks();
        if(!priorSubs.isEmpty()) {
            endTime = priorSubs.last().getEndTime();
        }
       return endTime;
    }
}