package taskManager;

import task.Epic;
import task.Subtask;
import task.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    List<Task> getAllTasks();

    void deleteAllTasks();

    Task createTask(Task task);

    Task updateTask(Task task);

    Task getTaskById(Integer taskId);

    void deleteTaskById(Integer taskId);

    List<Subtask> getAllSubtasks();

    void deleteAllSubtasks();

    Subtask createSubtask(Subtask subtask);

    Subtask updateSubtask(Subtask subtask);

    Subtask getSubtaskById(Integer subtaskId);

    void deleteSubtaskById(Integer subtaskId);

    List<Epic> getAllEpics();

    void deleteAllEpics();

    Epic createEpic(Epic epic);

    Epic updateEpic(Epic epic);

    Epic getEpicById(Integer epicId);

    void deleteEpicById(Integer epicId);

    List<Task> getHistory();
}
