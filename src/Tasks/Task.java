package Tasks;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Task {
    protected String name;
    protected String description;
    protected TypeOfTask task = TypeOfTask.TASK;

    protected LocalDateTime startTime;
    protected Duration duration;

    protected StatusOfTask statusOfTask;
    protected Integer taskid;
    public Task(String name, String description, StatusOfTask statusOfTask) {
        this.name = name;
        this.description = description;
        this.statusOfTask = statusOfTask;
    }
    public Task(String name, String description, StatusOfTask statusOfTask, int id) {
        this.name = name;
        this.description = description;
        this.statusOfTask = statusOfTask;
        this.taskid = id;
    }

    public Task(String name, String description, StatusOfTask statusOfTask, int id, TypeOfTask type) {
        this.name = name;
        this.description = description;
        this.statusOfTask = statusOfTask;
        this.taskid = id;
        this.task = type;
    }

    public Task(String name, String description, StatusOfTask statusOfTask, int id, TypeOfTask type,
                LocalDateTime startTime, int duration) {
        this.name = name;
        this.description = description;
        this.statusOfTask = statusOfTask;
        this.taskid = id;
        this.task = type;
        this.startTime = startTime;
        this.duration = Duration.ofMinutes(duration);
    }

    public Task(String name, String description, StatusOfTask statusOfTask, TypeOfTask type,
                LocalDateTime startTime, int duration) {
        this.name = name;
        this.description = description;
        this.statusOfTask = statusOfTask;
        this.task = type;
        this.startTime = startTime;
        this.duration = Duration.ofMinutes(duration);
    }

    public Task(String name){
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public StatusOfTask getStatus() {
        return statusOfTask;
    }

    public void setStatus(StatusOfTask status) {
        this.statusOfTask = status;
    }

    public void setId(Integer taskid) {
        this.taskid = taskid;
    }

    public Integer getId() {
        return taskid;
    }

    public TypeOfTask getType(){
        return task;
    }

    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public Duration getDuration() {
        return duration;
    }
}