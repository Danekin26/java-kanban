package task;

public class Subtask extends Task {
    private int idToEpic;

    public Subtask(String title, String description, String status) {
        super(title, description, status);
    }

    public int getIdToEpic() {
        return idToEpic;
    }

    public void setIdToEpic(int idToEpic) {
        this.idToEpic = idToEpic;
    }
}