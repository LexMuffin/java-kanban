import enums.Status;
import task.Epic;
import task.Subtask;
import task.Task;
import taskManager.InMemoryTaskManager;
import taskManager.Managers;
import taskManager.TaskManager;

public class Main {
    public static void main(String[] args) {

        TaskManager inMemoryTaskManager = Managers.getDefault();
        Task task1 = inMemoryTaskManager.createTask(new Task("Завтрак", "Сделать бутерброд"));
        Task task2 = inMemoryTaskManager.createTask(new Task("Работа", "Собрать на работы"));
        Epic epic1 = inMemoryTaskManager.createEpic(new Epic("Проект", "Сделать проект на Java"));
        Subtask epic1subtask1 = inMemoryTaskManager.createSubtask(new Subtask("Создать классы", "Сделать классы для проекта", epic1.getId()));
        Subtask epic1subtask2 = inMemoryTaskManager.createSubtask(new Subtask("Создать управляющий класс", "Создать исполняющий класс", epic1.getId()));
        Epic epic2 = inMemoryTaskManager.createEpic(new Epic("Сдать проект", "Отправить решенный проект"));
        Subtask epic2subtask1 = inMemoryTaskManager.createSubtask(new Subtask("Завести репозиторий", "Создать репозиторий, чтобы залить проект", epic2.getId()));
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