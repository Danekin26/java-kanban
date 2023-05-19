package manager;

class Node <T> {

    public T data; // Текущие данные
    public Node<T> next; // Ссылка на следующие
    public Node<T> prev; // Ссылка на предыдущие

    public Node(Node<T> prev, T data, Node<T> next) {
        this.prev = prev;
        this.data = data;
        this.next = next;
    }
}
