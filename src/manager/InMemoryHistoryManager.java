package manager;

import task.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    CustomLinkedList<Task> customLinkedList = new CustomLinkedList<>();
    HashMap<Integer, Node<Task>> idNodes = new HashMap<>();

    @Override
    public void add(Task task) {
        int idTask = task.getId();
        if(idNodes.containsKey(idTask)) {
            customLinkedList.removeNode(idNodes.get(idTask));
            idNodes.remove(idTask);
        }
        idNodes.put(idTask, customLinkedList.linkLast(task));
    }

    @Override
    public void remove(int id){
// удаление задач из истории
        customLinkedList.removeNode(idNodes.get(id));
        idNodes.remove(id);
    }

    @Override
    public ArrayList getHistory() {
        return customLinkedList.getHistory();
    }
}

class CustomLinkedList<T>  {
    public Node<T> head = null; // Голова
    public Node<T> tail = null; // Хвост
    private int size = 0;
    public List<Node<T>> listNodes = new ArrayList<>();
    public Node<T> linkLast(T t){
        final Node<T> oldTail = tail;
        final Node<T> newNode = new Node<>(oldTail, t, null);
        tail = newNode;
        if(oldTail == null){
            head = newNode;
        } else{
            oldTail.next = newNode;
        }
        size++;
        listNodes.add(newNode);
        return newNode;
    }

    public ArrayList getHistory(){
        return new ArrayList<>(listNodes);
    }

    public void removeNode(Node node) {
        int index = listNodes.indexOf(node);
        if(index !=0){
        listNodes.get(index-1).next = listNodes.get(index).next;
        }
        if(index != listNodes.size()- 1 ) {
            listNodes.get(index + 1).prev = listNodes.get(index).prev;
        }
        listNodes.remove(node);
        size--;
    }
}
