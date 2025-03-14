import enums.Status;
import task.Epic;
import task.Subtask;
import task.Task;
import taskManager.TaskManager;

public class Main {
    public static void main(String[] args) {

        TaskManager taskManager = new TaskManager();
        Task task1 = taskManager.createTask(new Task("Завтрак", "Сделать бутерброд"));
        Task task2 = taskManager.createTask(new Task("Работа", "Собрать на работы"));
        Epic epic1 = taskManager.createEpic(new Epic("Проект", "Сделать проект на Java"));
        Subtask epic1subtask1 = taskManager.createSubtask(new Subtask("Создать классы", "Сделать классы для проекта", epic1.getId()));
        Subtask epic1subtask2 = taskManager.createSubtask(new Subtask("Создать управляющий класс", "Создать исполняющий класс", epic1.getId()));
        Epic epic2 = taskManager.createEpic(new Epic("Сдать проект", "Отправить решенный проект"));
        Subtask epic2subtask1 = taskManager.createSubtask(new Subtask("Завести репозиторий", "Создать репозиторий, чтобы залить проект", epic2.getId()));
        //
        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubtasks());
        //
        epic2subtask1.setStatus(Status.DONE);
        taskManager.updateSubtask(epic2subtask1);
        System.out.println(taskManager.getEpicById(epic2.getId()));
        epic2subtask1.setStatus(Status.IN_PROGRESS);
        taskManager.updateSubtask(epic2subtask1);
        System.out.println(taskManager.getEpicById(epic2.getId()));
        //
        System.out.println("начало_тест1");
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubtasks());
        System.out.println("тест1");
        taskManager.deleteEpicById(epic2.getId());
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubtasks());
        System.out.println("конец_тест1");
        //
        epic1subtask1.setStatus(Status.DONE);
        taskManager.updateSubtask(epic1subtask2);
        epic1subtask2.setStatus(Status.DONE);
        taskManager.updateSubtask(epic1subtask2);
        System.out.println(taskManager.getEpicById(epic1.getId()));
        taskManager.deleteAllSubtasks();
        System.out.println(taskManager.getEpicById(epic1.getId()));
        //
        taskManager.deleteTaskById(task1.getId());
        System.out.println(taskManager.getAllTasks());
    }
}