package taskManager;

import enums.Status;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;
import task.Task;

import java.util.List;

class InMemoryHistoryManagerTest {

    private TaskManager inMemoryTaskManager = Managers.getDefault();
    private HistoryManager inMemoryHistoryManager = Managers.getDefaultHistory();

    @Test
    public void checkHistoryManagerTaskAdded() {
        Task task1 = new Task("name task1", "description task1");
        inMemoryTaskManager.createTask(task1);
        inMemoryHistoryManager.add(task1);
        List<Task> taskHistory = inMemoryHistoryManager.getHistory();
        Assertions.assertEquals(1, taskHistory.size());
        Assertions.assertEquals(1, taskHistory.getLast().getId());
    }

    @Test
    public void checkEmptyHistoryManagerWhenTaskDeleted() {
        Task task1 = new Task("name task1", "description task1");
        inMemoryTaskManager.createTask(task1);
        inMemoryHistoryManager.add(task1);
        Task task2 = new Task("name task2", "description task2");
        inMemoryTaskManager.createTask(task2);
        inMemoryHistoryManager.add(task2);
        List<Task> taskHistory = inMemoryHistoryManager.getHistory();
        Assertions.assertEquals(2, taskHistory.size());
        taskHistory.remove(task1);
        taskHistory.remove(task2);
        Assertions.assertEquals(0, taskHistory.size());
    }

    @Test
    public void checkHistoryManagerTasksOrder() {
        Task task1 = new Task("name task1", "description task1");
        inMemoryTaskManager.createTask(task1);
        Task task2 = new Task("name task2", "description task2");
        inMemoryTaskManager.createTask(task2);
        inMemoryHistoryManager.add(task1);
        inMemoryHistoryManager.add(task2);
        Assertions.assertEquals(task1, inMemoryHistoryManager.getHistory().getFirst());
        Assertions.assertEquals(task2, inMemoryHistoryManager.getHistory().getLast());
        //
        task1.setStatus(Status.IN_PROGRESS);
        inMemoryHistoryManager.add(task1);
        Assertions.assertEquals(task1, inMemoryHistoryManager.getHistory().getLast());
    }

    @Test
    public void checkHistoryManagerTaskVersion() {
        Task task1 = new Task("name task1", "description task1");
        inMemoryTaskManager.createTask(task1);
        inMemoryHistoryManager.add(task1);
        List<Task> taskHistory = inMemoryHistoryManager.getHistory();
        Assertions.assertEquals(Status.NEW, taskHistory.getLast().getStatus());
        task1.setStatus(Status.IN_PROGRESS);
        Assertions.assertEquals(Status.IN_PROGRESS, taskHistory.getLast().getStatus());
    }

    @Test
    public void checkHistoryManagerDuplicatesRemove() {
        Task task1 = new Task("name task1", "description task1");
        inMemoryTaskManager.createTask(task1);
        Task task2 = new Task("name task2", "description task2");
        inMemoryTaskManager.createTask(task2);
        task1.setId(1);
        task2.setId(1);
        inMemoryHistoryManager.add(task1);
        inMemoryHistoryManager.add(task2);
        Assertions.assertEquals(1, inMemoryHistoryManager.getHistory().size());
        Assertions.assertEquals("name task2", inMemoryHistoryManager.getHistory().getLast().getName());
    }

}