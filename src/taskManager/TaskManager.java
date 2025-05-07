package taskManager;

import task.Epic;
import task.Subtask;
import task.Task;

import java.util.List;

public interface TaskManager {
    List<Task> getAllTasks();

    void deleteAllTasks();

    void createTask(Task task);

    void updateTask(Task task);

    Task getTaskById(Integer taskId);

    void deleteTaskById(Integer taskId);

    List<Subtask> getAllSubtasks();

    void deleteAllSubtasks();

    void createSubtask(Subtask subtask);

    void updateSubtask(Subtask subtask);

    Subtask getSubtaskById(Integer subtaskId);

    void deleteSubtaskById(Integer subtaskId);

    List<Epic> getAllEpics();

    void deleteAllEpics();

    void createEpic(Epic epic);

    void updateEpic(Epic epic);

    Epic getEpicById(Integer epicId);

    void deleteEpicById(Integer epicId);

    List<Task> getHistory();
}
