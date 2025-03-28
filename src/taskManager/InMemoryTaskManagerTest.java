package taskManager;

import enums.Status;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;
import task.Task;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    private InMemoryTaskManager inMemoryTaskManager = Managers.getDefault();

    @AfterEach
    public void typeTasksDeleteFromTaskManager() {
        inMemoryTaskManager.deleteAllTasks();
        inMemoryTaskManager.deleteAllEpics();
    }

    @Test
    public void checkTasksEquals() {
        Task task = inMemoryTaskManager.createTask(new Task("task name", "task description"));
        Integer taskId = task.getId();
        Task newTask = inMemoryTaskManager.getTaskById(taskId);

        Assertions.assertNotNull(task, "Task not found");
        Assertions.assertEquals(task, newTask, "Tasks not equal");
    }

    @Test
    public void checkTasksInheritansEquals() {
        Epic epic = inMemoryTaskManager.createEpic(new Epic("epic name", "epic description"));
        Integer epicId = epic.getId();
        Epic newEpic = inMemoryTaskManager.getEpicById(epicId);

        Subtask subtask = inMemoryTaskManager.createSubtask(new Subtask("subtask name", "subtask description", epicId));
        Subtask newSubtask = epic.getSubtasks().get(0);


        Assertions.assertNotNull(epic, "Epic not found");
        Assertions.assertEquals(epic, newEpic, "Epics not equal");
        Assertions.assertNotNull(subtask, "Subtask not found");
        Assertions.assertEquals(1, epic.getSubtasks().size(), "Wrong Epic Subtasks list");
        Assertions.assertEquals(subtask, newSubtask, "Epics not equal");
    }

    @Test
    public void imposssibleToAddEpicAsSubtask() {
        Epic epic = inMemoryTaskManager.createEpic(new Epic("epic name", "epic description"));
        Integer epicId = epic.getId();
        Subtask epicAsSubtask = inMemoryTaskManager.getSubtaskById(epicId);
        epic.addSubtask(epicAsSubtask);
        Assertions.assertNull(epic.getSubtasks().get(0));
    }

    @Test
    public void utilityClassExamplesReturnsClassObjects() {
        /*Или можно создать объект класса Managers*/
        Assertions.assertInstanceOf(InMemoryTaskManager.class, Managers.getDefault());
        Assertions.assertInstanceOf(InMemoryHistoryManager.class, Managers.getDefaultHistory());
    }

    @Test
    public void getDifferentTypeTasksFromTaskManagerById() {
        Task task1 = inMemoryTaskManager.createTask(new Task("name task1", "description task1"));
        Epic epic1 = inMemoryTaskManager.createEpic(new Epic("name epic1", "description epic1"));
        Subtask subtask1 = inMemoryTaskManager.createSubtask(new Subtask("name subtask1", "description subtask1", epic1.getId()));
        Assertions.assertNotNull(inMemoryTaskManager.getTaskById(task1.getId()));
        Assertions.assertEquals(epic1, inMemoryTaskManager.getEpicById(epic1.getId()));
        Assertions.assertEquals(subtask1, inMemoryTaskManager.getSubtaskById(subtask1.getId()));
    }

    @Test
    public void nonConflictSelfAddedIdvsGeneratedId() {
        Task task1 = inMemoryTaskManager.createTask(new Task(10,  "name task1", "description task1"));
        Task task2 = inMemoryTaskManager.createTask(new Task(1,"name task2", "description task2"));
        Assertions.assertEquals(2, task2.getId());
        Assertions.assertTrue(task1.getId() != 10);
        Assertions.assertTrue(task1.getId() == 1);
    }

    @Test
    public void noTaskChangesWhenAddedToTaskManager() {
        Task task1 = new Task(1, "name task1", "description task1");
        Task newTask = inMemoryTaskManager.createTask(task1);
        Assertions.assertEquals(task1, newTask);
    }

    @Test
    public void previosTaskVersionAfterHistoryAdded() {
        Task task1 = inMemoryTaskManager.createTask(new Task("name task1", "description task1"));
        inMemoryTaskManager.getTaskById(task1.getId());
        task1.setStatus(Status.IN_PROGRESS);
        Assertions.assertNotEquals(task1, inMemoryTaskManager.getHistory().getLast());
        // попробуем с епиком
        Epic epic1 = inMemoryTaskManager.createEpic(new Epic("name epic1", "description epic1"));
        inMemoryTaskManager.getEpicById(epic1.getId());
        epic1.setName("new name epic1");
        Assertions.assertNotEquals(epic1, inMemoryTaskManager.getHistory().getLast());
    }
  
}