package managers;

import enums.Status;
import enums.TaskType;
import exceptions.ManagerSaveException;
import task.Epic;
import task.Subtask;
import task.Task;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class FileBackendTaskManager extends InMemoryTaskManager {

    private final File filename;

    public FileBackendTaskManager(File filename) {
        this.filename = filename;
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void deleteTaskById(Integer taskId) {
        super.deleteTaskById(taskId);
        save();
    }

    @Override
    public void deleteEpicById(Integer epicId) {
        super.deleteEpicById(epicId);
        save();
    }

    @Override
    public void deleteSubtaskById(Integer subtaskId) {
        super.deleteSubtaskById(subtaskId);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }


    private void save() {
        if (!Files.exists(filename.toPath())) {
            try {
                Files.createFile(filename.toPath());
            } catch (IOException e) {
                throw new ManagerSaveException();
            }
        }
        try (FileWriter fileWriter = new FileWriter(filename, StandardCharsets.UTF_8)) {
            fileWriter.write("id,type,name,status,description,epic\n");
            for (Task task: getAllTasks()) {
                fileWriter.write(taskToString(task) + "\n");
            }
            for (Epic epic: getAllEpics()) {
                fileWriter.write(taskToString(epic) + "\n");
            }
            for (Subtask subtask: getAllSubtasks()) {
                fileWriter.write(taskToString(subtask) + "\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException();
        }
    }


    private static String taskToString(Task task) {
        return String.format("%d,%S,%s,%S,%s,%s",
                task.getId(),
                task.getClass().getSimpleName().toUpperCase(),
                task.getName(),
                task.getStatus(),
                task.getDescription(),
                (task instanceof Subtask) ? ((Subtask)task).getEpicLink() : ""
        );
    }

    private static Task stringToTask(String value) {
        String[] valueArray = value.split(",");
        if (valueArray[1].equalsIgnoreCase(String.valueOf(TaskType.SUBTASK))) {
            Subtask subtask = new Subtask(
                    Integer.parseInt(valueArray[0]),
                    valueArray[2],
                    valueArray[4],
                    Integer.parseInt(valueArray[5])
            );
            subtask.setStatus(Status.valueOf(valueArray[3]));
            return subtask;
        } else if (valueArray[1].equalsIgnoreCase(String.valueOf(TaskType.EPIC))) {
            Epic epic = new Epic(
                    Integer.parseInt(valueArray[0]),
                    valueArray[2],
                    valueArray[4]
            );
            epic.setStatus(Status.valueOf(valueArray[3]));
            return epic;
        } else {
            Task task = new Task(
                    Integer.parseInt(valueArray[0]),
                    valueArray[2],
                    valueArray[4]
            );
            task.setStatus(Status.valueOf(valueArray[3]));
            return task;
        }
    }

    public static FileBackendTaskManager loadFromFile(File filename) {
        FileBackendTaskManager taskManager = new FileBackendTaskManager(filename);
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filename, StandardCharsets.UTF_8))) {
            String line = bufferedReader.readLine();
            while (bufferedReader.ready()) {
                line = bufferedReader.readLine();
                if (line != null) {
                    taskManager.uploadTask(line);
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException();
        }
        return taskManager;
    }

    private void uploadTask(String value) {
        Task task = stringToTask(value);
        if (task.getClass().getSimpleName().equalsIgnoreCase(String.valueOf(TaskType.EPIC))) {
            createEpic((Epic) task);
        } else if (task.getClass().getSimpleName().equalsIgnoreCase(String.valueOf(TaskType.SUBTASK))) {
            createSubtask((Subtask) task);
        } else {
            createTask(task);
        }
    }
}
