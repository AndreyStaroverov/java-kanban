package Tasks;

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

    @Override
    public Integer getId() {
        return super.getId();
    }

    public TypeOfTask getType() {
        return subtask;
    }
}
