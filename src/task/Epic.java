package task;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> idToSubtask = new ArrayList<>();

    public Epic(String title, String description, TasksStatus status, int id, ArrayList<Integer> idToSubtask) {
        super(title, description, status, id);
        this.idToSubtask = idToSubtask;
    }


    public ArrayList<Integer> getIdToSubtask() {
        return idToSubtask;
    }

    public void setIdToSubtask(int numIdSubtask) {
        this.idToSubtask.add(numIdSubtask);
    }

    public Epic(String title, String description) {
        super(title, description);
    }

    public void deleteAllIdToSubtask() {
        idToSubtask.clear();
    }

    public void deleteIdSubtask(int id) {
        if (idToSubtask.contains(id)) idToSubtask.remove(idToSubtask.indexOf(id));
    }
}