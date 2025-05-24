package managers;

import enums.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;
import task.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest <T extends TaskManager> {

    protected T taskManager;

    private Task addTask(int days, int minutes) {
        return new Task(
                "Task name",
                "Task description",
                LocalDateTime.now().minus(Period.ofDays(days)),
                Duration.ofMinutes(minutes)
        );
    }

    private Epic addEpic() {
        return new Epic(
                "Epic name",
                "Epic description"
        );
    }

    private Subtask addSubtask(Epic epic, int days, int minutes) {
        return new Subtask(
                "Subtask name",
                "Subtask description",
                LocalDateTime.now().minus(Period.ofDays(days)),
                Duration.ofMinutes(minutes),
                epic.getId()
        );
    }

    @Test
    public void checkTaskAdded() {
        Task task1 = addTask(1, 30);
        taskManager.createTask(task1);
        assertEquals(taskManager.getAllTasks().getFirst().getId(), task1.getId());
    }

    @Test
    public void checkEpicAdded() {
        Epic epic1 = addEpic();
        taskManager.createEpic(epic1);
        assertEquals(taskManager.getAllEpics().getFirst().getId(), epic1.getId());
    }

    @Test
    public void checkSubtaskAdded() {
        Epic epic1 = addEpic();
        taskManager.createEpic(epic1);
        Subtask subtask1 = addSubtask(epic1, 2, 45);
        taskManager.createSubtask(subtask1);
        assertEquals(taskManager.getAllSubtasks().getFirst().getId(), subtask1.getId());
    }

    @Test
    public void checkEpicStatusIsNewByAllNewSubtasks() {
        Epic epic1 = addEpic();
        taskManager.createEpic(epic1);
        Subtask subtask1 = addSubtask(epic1, 2, 10);
        taskManager.createSubtask(subtask1);
        Subtask subtask2 = addSubtask(epic1, 1, 30);
        taskManager.createSubtask(subtask2);
        assertEquals(epic1.getStatus(), Status.NEW);
    }

    @Test
    public void checkEpicStatusIsDoneByAllDoneSubtasks() {
        Epic epic1 = addEpic();
        taskManager.createEpic(epic1);
        Subtask subtask1 = addSubtask(epic1, 2, 10);
        taskManager.createSubtask(subtask1);
        subtask1.setStatus(Status.DONE);
        Subtask subtask2 = addSubtask(epic1, 3, 30);
        taskManager.createSubtask(subtask2);
        subtask2.setStatus(Status.DONE);
        taskManager.updateSubtask(subtask1);
        taskManager.updateSubtask(subtask2);
        assertEquals(epic1.getStatus(), Status.DONE);
    }

    @Test
    public void checkEpicStatusIsInProgressByAllNewAndDoneSubtasks() {
        Epic epic1 = addEpic();
        taskManager.createEpic(epic1);
        Subtask subtask1 = addSubtask(epic1, 2, 10);
        taskManager.createSubtask(subtask1);
        subtask1.setStatus(Status.DONE);
        Subtask subtask2 = addSubtask(epic1, 1, 30);
        taskManager.createSubtask(subtask2);
        assertEquals(epic1.getStatus(), Status.IN_PROGRESS);
    }

    @Test
    public void checkEpicStatusIsInProgressByAllInProgressSubtasks() {
        Epic epic1 = addEpic();
        taskManager.createEpic(epic1);
        Subtask subtask1 = addSubtask(epic1, 2, 10);
        taskManager.createSubtask(subtask1);
        subtask1.setStatus(Status.IN_PROGRESS);
        Subtask subtask2 = addSubtask(epic1, 1, 30);
        taskManager.createSubtask(subtask2);
        subtask2.setStatus(Status.IN_PROGRESS);
        assertEquals(epic1.getStatus(), Status.IN_PROGRESS);
    }

    @Test
    public void checkSubtaskHasEpicLink() {
        Epic epic1 = addEpic();
        taskManager.createEpic(epic1);
        Subtask subtask1 = addSubtask(epic1, 2, 10);
        taskManager.createSubtask(subtask1);
        assertEquals(epic1.getId(), subtask1.getEpicLink());
    }

}
