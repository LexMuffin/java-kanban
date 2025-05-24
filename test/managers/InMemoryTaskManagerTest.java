package managers;

import enums.Status;
import exceptions.ManagerTimeConflictException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;
import task.Task;

import java.time.Duration;
import java.time.LocalDateTime;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    public void setUp() {
        taskManager = new InMemoryTaskManager();
    }

    @Test
    public void checkTasksEquals() {
        Task task = new Task("task name", "task description");
        taskManager.createTask(task);
        Integer taskId = task.getId();
        Task newTask = taskManager.getTaskById(taskId);

        Assertions.assertNotNull(task, "Task not found");
        Assertions.assertEquals(task, newTask, "Tasks not equal");
    }

    @Test
    public void checkTasksInheritansEquals() {
        Epic epic = new Epic("epic name", "epic description");
        taskManager.createEpic(epic);
        Integer epicId = epic.getId();
        Epic newEpic = taskManager.getEpicById(epicId);

        Subtask subtask = new Subtask("subtask name", "subtask description", epicId);
        taskManager.createSubtask(subtask);
        Subtask newSubtask = epic.getSubtasks().get(0);


        Assertions.assertNotNull(epic, "Epic not found");
        Assertions.assertEquals(epic, newEpic, "Epics not equal");
        Assertions.assertNotNull(subtask, "Subtask not found");
        Assertions.assertEquals(1, epic.getSubtasks().size(), "Wrong Epic Subtasks list");
        Assertions.assertEquals(subtask, newSubtask, "Epics not equal");
    }

    @Test
    public void imposssibleToAddEpicAsSubtask() {
        Epic epic = new Epic("epic name", "epic description");
        taskManager.createEpic(epic);
        Integer epicId = epic.getId();
        Subtask epicAsSubtask = taskManager.getSubtaskById(epicId);
        epic.addSubtask(epicAsSubtask);
        Assertions.assertNull(epic.getSubtasks().get(0));
    }

    @Test
    public void getDifferentTypeTasksFromTaskManagerById() {
        Task task1 = new Task(
                "name task1",
                "description task1",
                LocalDateTime.of(2025, 5, 1, 10, 0),
                Duration.ofMinutes(30)
        );
        taskManager.createTask(task1);
        Epic epic1 = new Epic(
                "name epic1",
                "description epic1"
        );
        taskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask(
                "name subtask1",
                "description subtask1",
                LocalDateTime.of(2025, 5, 2, 11, 5),
                Duration.ofMinutes(45),
                epic1.getId()
        );
        taskManager.createSubtask(subtask1);
        Assertions.assertNotNull(taskManager.getTaskById(task1.getId()));
        Assertions.assertEquals(epic1, taskManager.getEpicById(epic1.getId()));
        Assertions.assertEquals(subtask1, taskManager.getSubtaskById(subtask1.getId()));
    }

    @Test
    public void nonConflictSelfAddedIdvsGeneratedId() {
        Task task1 = new Task(100, "name task1", "description task1");
        taskManager.createTask(task1);
        Assertions.assertTrue(task1.getId() != 100);
    }

    @Test
    public void noTaskChangesWhenAddedToTaskManager() {
        Task task1 = new Task(1, "name task1", "description task1");
        Task newTask = task1;
        taskManager.createTask(task1);
        Assertions.assertEquals(task1, newTask);
    }

    @Test
    public void previosTaskVersionAfterHistoryAdded() {
        Task task1 = new Task("name task1", "description task1");
        taskManager.createTask(task1);
        Task task2 = new Task("name task1", "description task2");
        taskManager.createTask(task2);
        taskManager.getTaskById(task1.getId());
        task1.setStatus(Status.IN_PROGRESS);
        Assertions.assertEquals(task1, taskManager.getHistory().getLast());
        Epic epic1 = new Epic("name epic1", "description epic1");
        taskManager.createEpic(epic1);
        taskManager.getEpicById(epic1.getId());
        epic1.setName("new name epic1");
        Assertions.assertEquals(epic1, taskManager.getHistory().getLast());
    }

    @Test
    public void ThrowExceptionWhenTestAreTasksOverlapping() {
        Task task1 = new Task(
                "name task1",
                "description task1",
                LocalDateTime.of(2025, 5, 1, 12, 30),
                Duration.ofMinutes(45)
        );
        Task task2 = new Task(
                "name task2",
                "description task2",
                LocalDateTime.of(2025, 5, 1, 12, 40),
                Duration.ofMinutes(20)
        );
        taskManager.createTask(task1);
        Assertions.assertThrows(
                ManagerTimeConflictException.class,
                () -> taskManager.createTask(task2),
                "Ошибка! Есть пересечения в задачах"
        );
    }

    @Test
    public void doesNotThrowExceptionWhenTestAreTasksOverlapping() {
        Task task1 = new Task(
                "name task1",
                "description task1",
                LocalDateTime.of(2025, 5, 1, 11, 30),
                Duration.ofMinutes(45)
        );
        Task task2 = new Task(
                "name task2",
                "description task2",
                LocalDateTime.of(2025, 5, 1, 12, 40),
                Duration.ofMinutes(20)
        );
        taskManager.createTask(task1);
        Assertions.assertDoesNotThrow(
                () -> taskManager.createTask(task2),
                "Нет ошибки! Задачи не пересекаются"
        );
    }


  
}