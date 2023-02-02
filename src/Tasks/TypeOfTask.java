package Tasks;

public enum TypeOfTask {
    TASK,
    SUBTASK,
    EPIC;

    public static TypeOfTask getType(String value) {
        switch (value) {
            case "TASK":
                return TypeOfTask.TASK;
            case "SUBTASK":
                return TypeOfTask.SUBTASK;
            case "EPIC":
                return TypeOfTask.EPIC;
            default:
                return null;
        }
    }
}
