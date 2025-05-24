package managers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import task.Task;

public class HistoryManagerTest {

    private HistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();

    @Test
    public void checkEmptyHistoryManager() {
        Assertions.assertTrue(inMemoryHistoryManager.getHistory().isEmpty());
    }

    @Test
    public void checkThenSameTaskAddedSizeUnchangeable() {
        Task task1 = new Task("name task1", "description task1");
        task1.setId(1);
        Task task2 = new Task("name task2", "description task2");
        task2.setId(2);
        inMemoryHistoryManager.add(task1);
        inMemoryHistoryManager.add(task1);
        Assertions.assertEquals(1, inMemoryHistoryManager.getHistory().size());
        inMemoryHistoryManager.add(task2);
        Assertions.assertEquals(2, inMemoryHistoryManager.getHistory().size());
    }

    @Test
    public void historyManagerTasksTestRemoval() {
        Task task1 = new Task("name task1", "description task1");
        task1.setId(1);
        Task task2 = new Task("name task2", "description task2");
        task2.setId(2);
        Task task3 = new Task("name task3", "description task3");
        task3.setId(3);
        Task task4 = new Task("name task4", "description task4");
        task4.setId(4);
        Task task5 = new Task("name task5", "description task5");
        task5.setId(5);
        inMemoryHistoryManager.add(task1);
        inMemoryHistoryManager.add(task2);
        inMemoryHistoryManager.add(task3);
        inMemoryHistoryManager.add(task4);
        inMemoryHistoryManager.add(task5);
        Assertions.assertTrue(inMemoryHistoryManager.getHistory().getFirst().getId() == 1);
        inMemoryHistoryManager.remove(1);
        Assertions.assertTrue(inMemoryHistoryManager.getHistory().getFirst().getId() == 2);
        inMemoryHistoryManager.remove(5);
        Assertions.assertTrue(inMemoryHistoryManager.getHistory().getLast().getId() == 4);
        inMemoryHistoryManager.remove(2);
        Assertions.assertEquals(2, inMemoryHistoryManager.getHistory().size());
    }

}
