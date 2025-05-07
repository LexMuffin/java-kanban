package taskManager;

import enums.Status;
import task.Epic;
import task.Subtask;
import task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {

    private static int taskId = 1;
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();

    private HistoryManager historyManager = Managers.getDefaultHistory();

    private int getNextId() {
        return taskId++;
    }

    /*task.Task*/

    @Override
    public List<Task> getAllTasks() {
        for (Task task: tasks.values()) {
            historyManager.add(task);
        }
        return new ArrayList<>(tasks.values());
    }

    @Override
    public void deleteAllTasks() {
        tasks.clear();
    }

    @Override
    public void createTask(Task task) {
        task.setId(getNextId());
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateTask(Task task) {
        Integer taskId = task.getId();
        if (taskId == null || !tasks.containsKey(taskId)) {
            return;
        }
        tasks.put(taskId, task);
    }

    @Override
    public Task getTaskById(Integer taskId) {
        Task task = tasks.get(taskId);
        if (task != null) {
            historyManager.add(task);
        }
        return task;
    }

    @Override
    public void deleteTaskById(Integer taskId) {
        tasks.remove(taskId);
    }

    /*task.Subtask*/

    @Override
    public List<Subtask> getAllSubtasks() {
        for (Subtask subtask: subtasks.values()) {
            historyManager.add(subtask);
        }
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void deleteAllSubtasks() {
        subtasks.clear();

        for (Epic epic: epics.values()) {
            epic.getSubtasks().clear();
            checkEpicStatus(epic);
        }
    }

    @Override
    public void createSubtask(Subtask subtask) {
        subtask.setId(getNextId());
        subtasks.put(subtask.getId(), subtask);

        Epic epic = epics.get(subtask.getEpicLink());
        epic.addSubtask(subtask);

        checkEpicStatus(epic);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        Integer subtaskId = subtask.getId();
        if (subtaskId == null || !subtasks.containsKey(subtaskId)) {
            return;
        }

        Subtask oldEpicSubtask = subtasks.get(subtaskId);
        subtasks.replace(subtaskId, subtask);
        Epic epic = epics.get(subtask.getEpicLink());
        ArrayList<Subtask> epicSubtasksList = epic.getSubtasks();
        epicSubtasksList.remove(oldEpicSubtask);
        epicSubtasksList.add(subtask);
        checkEpicStatus(epic);
    }

    @Override
    public Subtask getSubtaskById(Integer subtaskId) {
        Subtask subtask = subtasks.get(subtaskId);
        if (subtask != null) {
            historyManager.add(subtask);
        }
        return subtask;
    }

    @Override
    public void deleteSubtaskById(Integer subtaskId) {
        Subtask subtask = subtasks.remove(subtaskId);

        Epic epic = epics.get(subtask.getEpicLink());
        ArrayList<Subtask> epicSubtasks = epic.getSubtasks();
        epicSubtasks.remove(subtask);
        epic.setSubtasks(epicSubtasks);

        checkEpicStatus(epic);
    }


    /*task.Epic*/

    @Override
    public List<Epic> getAllEpics() {
        for (Epic epic: epics.values()) {
            historyManager.add(epic);
        }
        return new ArrayList<>(epics.values());
    }

    @Override
    public void deleteAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void createEpic(Epic epic) {
        epic.setId(getNextId());
        epics.put(epic.getId(), epic);
    }

    @Override
    public void updateEpic(Epic epic) {
        Integer epicId = epic.getId();
        if (epicId == null || !subtasks.containsKey(epicId)) {
            return;
        }

        ArrayList<Subtask> epicSubtasksListOld = epic.getSubtasks();
        if (!epicSubtasksListOld.isEmpty()) {
            for (Subtask subtask: epicSubtasksListOld) {
                subtasks.remove(subtask.getId());
            }
        }
        epics.put(epicId, epic);
        ArrayList<Subtask> epicSubtasksListNew = epic.getSubtasks();
        if (!epicSubtasksListNew.isEmpty()) {
            for (Subtask subtask: epicSubtasksListNew) {
                subtasks.put(subtask.getId(), subtask);
            }
        }

        checkEpicStatus(epic);
    }

    @Override
    public Epic getEpicById(Integer epicId) {
        Epic epic = epics.get(epicId);
        if (epic != null) {
            historyManager.add(epic);
        }
        return epic;
    }

    @Override
    public void deleteEpicById(Integer epicId) {
        Epic epic = epics.remove(epicId);

        for (Subtask subtask: epic.getSubtasks()) {
            subtasks.remove(subtask.getId());
        }

        epics.remove(epicId);
    }

    private void checkEpicStatus(Epic epic) {
        int countSubtasksDone = 0;
        int countSubtasksNew = 0;
        ArrayList<Subtask> epicSubtasks = epic.getSubtasks();
        int countSubtasks = epicSubtasks.size();
        for (Subtask subtask: epicSubtasks) {
            if (subtask.getStatus() == Status.NEW) {
                countSubtasksNew++;
            } else if (subtask.getStatus() == Status.DONE) {
                countSubtasksDone++;
            }
        }
        if (countSubtasks == 0 || countSubtasks == countSubtasksNew) {
            epic.setStatus(Status.NEW);
        } else if (countSubtasks > 0 && countSubtasks == countSubtasksDone) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

}
