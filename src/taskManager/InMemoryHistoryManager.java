package taskManager;

import task.Task;
import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final static int MAX_LENGTH_TASKS_HISTORY = 10;
    private final List<Task> tasksHistoryList = new ArrayList<>();

    @Override
    public void add(Task task) {
        Task tempTask = new Task(task.getId(), task.getName(), task.getDescription());
        tempTask.setStatus(task.getStatus());
        if (tasksHistoryList.size() == MAX_LENGTH_TASKS_HISTORY) {
            tasksHistoryList.removeFirst();
        }
        tasksHistoryList.add(tempTask);
    }

    @Override
    public List<Task> getHistory() {
        return tasksHistoryList;
    }

}
