package taskManager;

import task.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private static class Node {

        Task value;
        Node next;
        Node prev;

        public Node(Task value) {
            this.value = value;
        }
    }

    private void removeNode(Node node) {
        if (node.prev != null) {
            Node previousNode = node.prev;
            previousNode.next = node.next;
        } else {
            head = node.next;
        }
        if (node.next != null) {
            Node nextNode = node.next;
            nextNode.prev = node.prev;
        } else {
            tail = node.prev;
        }
    }

    private final Map<Integer, Node> tasksHistoryMap = new LinkedHashMap<>();
    private Node head;
    private Node tail;

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        remove(task.getId());
        Node newNode = new Node(task);
        linkLast(newNode);
        tasksHistoryMap.put(task.getId(), newNode);
    }

    private void linkLast(Node node) {
        if (head != null) {
            tail.next = node;
            node.prev = tail;
        } else {
            head = node;
        }
        tail = node;
    }

    @Override
    public void remove(int id) {
        Node node = tasksHistoryMap.get(id);
        if (node != null) {
            removeNode(node);
            // если я не ошибаюсь можно отдельно не прописывать,
            // т.к. ссылок на объект нет и сборщик мусора подчистит остается
            tasksHistoryMap.remove(node);
        }
    }

    @Override
    public List<Task> getHistory() {
        List<Task> tasks = new ArrayList<>();
        Node currentNode = head;
        while (currentNode != null) {
            tasks.add(currentNode.value);
            currentNode = currentNode.next;
        }
        return tasks;
    }

}
