package taskManager;

import task.Task;
import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final static int MAX_LENGTH_TASKS_HISTORY = 10;
    private final List<Task> tasksHistoryList = new ArrayList<>();

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        /*� ����� �� ������� � �������� ������ - ���������, ��� ������, ����������� � HistoryManager,
        ��������� ���������� ������ ������ � � ������.
        � ��� ���������� � ������� ��� ������� ������� ��� ������� ���������� ��� ��� � ������� �
        ���� ��������� ������ �� ������ ���� � �� �� � ��� ��������� � ���� ��������� ���������� � �������.
        ������� � �������� ��������� ������ � ����� ������ �� �����������(���������� ����) � ���� � �������*/
        if (tasksHistoryList.size() > MAX_LENGTH_TASKS_HISTORY) {
            tasksHistoryList.removeFirst();
        }
        tasksHistoryList.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(tasksHistoryList);
    }

}
