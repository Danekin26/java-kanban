package task;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> idToSubtask = new ArrayList<>();

    public Epic(String title, String description, String status, int id, ArrayList<Integer> idToSubtask) {
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
        for(int i = 0; i < idToSubtask.size(); i++) {
            if(idToSubtask.get(i) == id) {
                idToSubtask.remove(i);
            }
        }
    }
}