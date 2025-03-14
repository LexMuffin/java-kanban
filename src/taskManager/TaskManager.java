package taskManager;

import enums.Status;
import task.Epic;
import task.Subtask;
import task.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {

    private static int taskId = 1;
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();

    private int getNextId() {
        return taskId++;
    }

    /*task.Task*/

    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public void deleteAllTasks() {
        tasks.clear();
    }

    public Task createTask(Task task) {
        task.setId(getNextId());
        tasks.put(task.getId(), task);
        return task;
    }

    public Task updateTask(Task task) {
        Integer taskId = task.getId();
        if (taskId == null || !tasks.containsKey(taskId)) {
            return null;
        }
        tasks.put(taskId, task);
        return task;
    }

    public Task getTaskById(Integer taskId) {
        return tasks.get(taskId);
    }

    public void deleteTaskById(Integer taskId) {
        tasks.remove(taskId);
    }

    /*task.Subtask*/

    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public void deleteAllSubtasks() {
        /*удаляем все substask*/
        subtasks.clear();
        /*Удаляем все subtask в каждом epic и обновляем статусы*/
        for (Epic epic: epics.values()) {
            epic.getSubtasks().clear();
            checkEpicStatus(epic);
        }
    }

    public Subtask createSubtask(Subtask subtask) {
        subtask.setId(getNextId());
        subtasks.put(subtask.getId(), subtask);
        /*добавление сабтаски в эпик*/
        Epic epic = epics.get(subtask.getEpicLink());
        epic.addSubtask(subtask);
        /*Обновляем статус*/
        checkEpicStatus(epic);
        return subtask;
    }

    public Subtask updateSubtask(Subtask subtask) {
        Integer subtaskId = subtask.getId();
        if (subtaskId == null || !subtasks.containsKey(subtaskId)) {
            return null;
        }
        /*Удаляем предыдущую версию подзадачи и добавляем новую верстю*/
        Subtask oldEpicSubtask = subtasks.get(subtaskId);
        subtasks.replace(subtaskId, subtask);
        Epic epic = epics.get(subtask.getEpicLink());
        ArrayList<Subtask> epicSubtasksList = epic.getSubtasks();
        epicSubtasksList.remove(oldEpicSubtask);
        epicSubtasksList.add(subtask);
        /*Обновляем статус*/
        checkEpicStatus(epic);
        return subtask;
    }

    public Subtask getSubtaskById(Integer subtaskId) {
        return subtasks.get(subtaskId);
    }

    public void deleteSubtaskById(Integer subtaskId) {
        Subtask subtask = getSubtaskById(subtaskId);
        subtasks.remove(subtaskId);
        /*Удаляем subtask из epic*/
        Epic epic = epics.get(subtask.getEpicLink());
        ArrayList<Subtask> epicSubtasks = epic.getSubtasks();
        epicSubtasks.remove(subtask);
        epic.setSubtasks(epicSubtasks);
        /*Обновляем статус*/
        checkEpicStatus(epic);
    }


    /*task.Epic*/

    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    public void deleteAllEpics() {
        epics.clear();
        /*Если удаляются epic то и все subtask должны быть удалены*/
        subtasks.clear();
    }

    public Epic createEpic(Epic epic) {
        epic.setId(getNextId());
        epics.put(epic.getId(), epic);
        return epic;
    }

    public Epic updateEpic(Epic epic) {
        Integer epicId = epic.getId();
        if (epicId == null || !subtasks.containsKey(epicId)) {
            return null;
        }
        /*т.к. при обновлении епика могли и поменяться подзадачи, то удаляем старые и добавляем новые*/
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
        /*Обновляем статусы*/
        checkEpicStatus(epic);
        return epic;
    }

    public Epic getEpicById(Integer epicId) {
        return epics.get(epicId);
    }

    public void deleteEpicById(Integer epicId) {
        /*Удаляем substask, т.к. удаляем epic и все subtask должны быть тоже удалены*/
        ArrayList<Subtask> epicSubtasksList = epics.get(epicId).getSubtasks();
        for (Subtask subtask: epicSubtasksList) {
            subtasks.remove(subtask.getId());
        }
        /*Удаляем сам epic*/
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

}
