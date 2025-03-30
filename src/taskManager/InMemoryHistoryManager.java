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
        /*¬ одном из пунктов к созданию тестов - убедитесь, что задачи, добавл€емые в HistoryManager,
        сохран€ют предыдущую версию задачи и еЄ данных.
        я вот собственно и подумал что штатным методом это сделать невозможно так как в истории и
        таск менеджере ссылка на задачу одна и та же и при изменении в таск менеджере измен€етс€ в истории.
        ѕоэтому € создавал временную задачу с точно такими же параметрами(фактически клон) и клал в историю*/
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
