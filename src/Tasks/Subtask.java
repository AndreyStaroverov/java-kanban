package Tasks;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task{

    protected TypeOfTask subtask = TypeOfTask.SUBTASK;
    protected int epicId;

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }


    public Integer getEpicId() {
        return epicId;
    }

    public Subtask (String name, String description, StatusOfTask statusOfTask, int epicId) {
        super(name, description,statusOfTask);
        this.epicId = epicId;
    }
    public Subtask (String name, String description, StatusOfTask statusOfTask, int epicId, int id) {
        super(name, description,statusOfTask);
        this.epicId = epicId;
        this.taskid = id;
    }

    public Subtask (String name, String description, StatusOfTask statusOfTask, int epicId, int id, TypeOfTask type) {
        super(name, description,statusOfTask);
        this.epicId = epicId;
        this.taskid = id;
        this.subtask = type;
    }

    public Subtask (String name, String description, StatusOfTask statusOfTask, int epicId, TypeOfTask type,
                    LocalDateTime startTime, int duration) {
        super(name, description,statusOfTask);
        this.epicId = epicId;
        this.subtask = type;
        this.startTime = startTime;
        this.duration = Duration.ofMinutes(duration);
    }
    public Subtask (String name, String description, StatusOfTask statusOfTask, int epicId,int id, TypeOfTask type,
                    LocalDateTime startTime, Duration duration) {
        super(name, description,statusOfTask);
        this.epicId = epicId;
        this.subtask = type;
        this.taskid = id;
        this.startTime = startTime;
        this.duration = duration;
    }

    @Override
    public Integer getId() {
        return super.getId();
    }

    public TypeOfTask getType() {
        return subtask;
    }
}
