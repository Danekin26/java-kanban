package manager;

import task.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private CustomLinkedList<Task> customLinkedList = new CustomLinkedList<>();
    private Map<Integer, Node<Task>> idNodes = new HashMap<>();

    @Override
    public void add(Task task) {
        int idTask = task.getId();
        if (idNodes.containsKey(idTask)) {
            customLinkedList.removeNode(idNodes.get(idTask));
            idNodes.remove(idTask);
        }
        idNodes.put(idTask, customLinkedList.linkLast(task));
    }

    @Override
    public void remove(int id) {
        customLinkedList.removeNode(idNodes.get(id)); // Удаление задачи из истории
        idNodes.remove(id);
    }

    @Override
    public List<Task> getHistory() {
        return customLinkedList.getTask();
    }
}

class CustomLinkedList<T> {
    public Node<T> head; // Голова
    public Node<T> tail; // Хвост
    private int size = 0;

    public Node<T> linkLast(T t) {
        final Node<T> oldTail = tail;
        final Node<T> newNode = new Node<>(oldTail, t, null);
        tail = newNode;
        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.next = newNode;
        }
        size++;
        return newNode;
    }

    public List<T> getTask() {
        ArrayList<T> historyTasks = new ArrayList<>();
        Node<T> lastHead = head;
        T node = head.data;
        for (int i = 0; i < size; i++) {
            historyTasks.add(node);
            lastHead = lastHead.next;
            if (lastHead != null) {
                node = lastHead.data;
            }
        }
        return historyTasks;
    }

    public void removeNode(Node node) {
        List<T> listTasks = new ArrayList<>(getTask());
        Node<T> nextHead = head;
        T n = (T) node.data;
        for (int i = 0; i < size; i++) {
            if (nextHead.data.equals(n)) {
                if ((nextHead.prev != null) && (nextHead.next != null)) {
                    nextHead.prev.next = nextHead.next;
                    nextHead.next.prev = nextHead.prev;
                } else if ((nextHead.prev == null) && (nextHead.next == null)) {
                    if (n.equals(nextHead.data)) {
                        head = null;
                        tail = null;
                    } else {
                        nextHead.next.prev = null;
                        nextHead.prev.next = null;
                    }
                } else if (nextHead.prev == null) {
                    nextHead.next.prev = null;
                    nextHead.data = null;
                    head = nextHead.next;
                } else {
                    nextHead.prev.next = null;
                    nextHead.data = null;
                    tail = nextHead.prev;
                }
                listTasks.remove(i);

                size--;
                return;
            }
            nextHead = nextHead.next;
        }
    }
}

