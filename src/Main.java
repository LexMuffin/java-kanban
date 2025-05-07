import enums.Status;
import task.Epic;
import task.Subtask;
import task.Task;
import taskManager.Managers;
import taskManager.TaskManager;

public class Main {
    public static void main(String[] args) {

        TaskManager inMemoryTaskManager = Managers.getDefault();
        Task task1 = new Task("Завтрак", "Сделать бутерброд");
        inMemoryTaskManager.createTask(task1);
        Task task2 = new Task("Работа", "Собрать на работы");
        inMemoryTaskManager.createTask(task2);
        Epic epic1 = new Epic("Проект", "Сделать проект на Java");
        inMemoryTaskManager.createEpic(epic1);
        Subtask epic1subtask1 = new Subtask("Создать классы", "Сделать классы для проекта", epic1.getId());
        inMemoryTaskManager.createSubtask(epic1subtask1);
        Subtask epic1subtask2 = new Subtask("Создать управляющий класс", "Создать исполняющий класс", epic1.getId());
        inMemoryTaskManager.createSubtask(epic1subtask2);
        Epic epic2 = new Epic("Сдать проект", "Отправить решенный проект");
        inMemoryTaskManager.createEpic(epic2);
        Subtask epic2subtask1 = new Subtask("Завести репозиторий", "Создать репозиторий, чтобы залить проект", epic2.getId());
        inMemoryTaskManager.createSubtask(epic2subtask1);
        //
        System.out.println(inMemoryTaskManager.getAllTasks());
        System.out.println(inMemoryTaskManager.getAllEpics());
        System.out.println(inMemoryTaskManager.getAllSubtasks());
        //
        epic2subtask1.setStatus(Status.DONE);
        inMemoryTaskManager.updateSubtask(epic2subtask1);
        System.out.println(inMemoryTaskManager.getEpicById(epic2.getId()));
        epic2subtask1.setStatus(Status.IN_PROGRESS);
        inMemoryTaskManager.updateSubtask(epic2subtask1);
        System.out.println(inMemoryTaskManager.getEpicById(epic2.getId()));
        //
        System.out.println("начало_тест1");
        System.out.println(inMemoryTaskManager.getAllEpics());
        System.out.println(inMemoryTaskManager.getAllSubtasks());
        System.out.println("тест1");
        inMemoryTaskManager.deleteEpicById(epic2.getId());
        System.out.println(inMemoryTaskManager.getAllEpics());
        System.out.println(inMemoryTaskManager.getAllSubtasks());
        System.out.println("конец_тест1");
        //
        epic1subtask1.setStatus(Status.DONE);
        inMemoryTaskManager.updateSubtask(epic1subtask2);
        epic1subtask2.setStatus(Status.DONE);
        inMemoryTaskManager.updateSubtask(epic1subtask2);
        System.out.println(inMemoryTaskManager.getEpicById(epic1.getId()));
        inMemoryTaskManager.deleteAllSubtasks();
        System.out.println(inMemoryTaskManager.getEpicById(epic1.getId()));
        //
        inMemoryTaskManager.deleteTaskById(task1.getId());
        System.out.println(inMemoryTaskManager.getAllTasks());
        //
        System.out.println(inMemoryTaskManager.getHistory().size());
        System.out.println(inMemoryTaskManager.getHistory());
    }
}