package managers;

import enums.Status;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;
import task.Task;

class InMemoryTaskManagerTest {

    private TaskManager inMemoryTaskManager = Managers.getDefault();

    @BeforeEach
    public void typeTasksDeleteFromTaskManager() {
        inMemoryTaskManager.deleteAllTasks();
        inMemoryTaskManager.deleteAllEpics();
        inMemoryTaskManager.deleteAllSubtasks();
    }

    @Test
    public void checkTasksEquals() {
        Task task = new Task("task name", "task description");
        inMemoryTaskManager.createTask(task);
        Integer taskId = task.getId();
        Task newTask = inMemoryTaskManager.getTaskById(taskId);

        Assertions.assertNotNull(task, "Task not found");
        Assertions.assertEquals(task, newTask, "Tasks not equal");
    }

    @Test
    public void checkTasksInheritansEquals() {
        Epic epic = new Epic("epic name", "epic description");
        inMemoryTaskManager.createEpic(epic);
        Integer epicId = epic.getId();
        Epic newEpic = inMemoryTaskManager.getEpicById(epicId);

        Subtask subtask = new Subtask("subtask name", "subtask description", epicId);
        inMemoryTaskManager.createSubtask(subtask);
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
        inMemoryTaskManager.createEpic(epic);
        Integer epicId = epic.getId();
        Subtask epicAsSubtask = inMemoryTaskManager.getSubtaskById(epicId);
        epic.addSubtask(epicAsSubtask);
        Assertions.assertNull(epic.getSubtasks().get(0));
    }

    @Test
    public void utilityClassExamplesReturnsClassObjects() {
        Assertions.assertInstanceOf(InMemoryTaskManager.class, Managers.getDefault());
        Assertions.assertInstanceOf(InMemoryHistoryManager.class, Managers.getDefaultHistory());
    }

    @Test
    public void getDifferentTypeTasksFromTaskManagerById() {
        Task task1 = new Task("name task1", "description task1");
        inMemoryTaskManager.createTask(task1);
        Epic epic1 = new Epic("name epic1", "description epic1");
        inMemoryTaskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask("name subtask1", "description subtask1", epic1.getId());
        inMemoryTaskManager.createSubtask(subtask1);
        Assertions.assertNotNull(inMemoryTaskManager.getTaskById(task1.getId()));
        Assertions.assertEquals(epic1, inMemoryTaskManager.getEpicById(epic1.getId()));
        Assertions.assertEquals(subtask1, inMemoryTaskManager.getSubtaskById(subtask1.getId()));
    }

    @Test
    public void nonConflictSelfAddedIdvsGeneratedId() {
        Task task1 = new Task(100, "name task1", "description task1");
        inMemoryTaskManager.createTask(task1);
        Assertions.assertTrue(task1.getId() != 100);
    }

    @Test
    public void noTaskChangesWhenAddedToTaskManager() {
        Task task1 = new Task(1, "name task1", "description task1");
        Task newTask = task1;
        inMemoryTaskManager.createTask(task1);
        Assertions.assertEquals(task1, newTask);
    }

    @Test
    public void previosTaskVersionAfterHistoryAdded() {
        Task task1 = new Task("name task1", "description task1");
        inMemoryTaskManager.createTask(task1);
        Task task2 = new Task("name task1", "description task2");
        inMemoryTaskManager.createTask(task2);
        inMemoryTaskManager.getTaskById(task1.getId());
        task1.setStatus(Status.IN_PROGRESS);
        Assertions.assertEquals(task1, inMemoryTaskManager.getHistory().getLast());
        Epic epic1 = new Epic("name epic1", "description epic1");
        inMemoryTaskManager.createEpic(epic1);
        inMemoryTaskManager.getEpicById(epic1.getId());
        epic1.setName("new name epic1");
        Assertions.assertEquals(epic1, inMemoryTaskManager.getHistory().getLast());
    }


  
}