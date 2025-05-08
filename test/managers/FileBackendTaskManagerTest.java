package managers;

import exceptions.ManagerSaveException;
import org.junit.jupiter.api.BeforeEach;
import task.Subtask;
import task.Task;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FileBackendTaskManagerTest {

    @Test
    void shouldThrowExceptionWhenFileNotExists() {
        File filename = new File("unknown_file.csv");
        assertThrows(ManagerSaveException.class, () -> FileBackendTaskManager.loadFromFile(filename));
    }

    @Test
    void shouldLoadTasksFromFileAndCheck() throws IOException {
        File filename = new File("tasks.csv");
        String content = """
                id,type,name,status,description,epic
                1,TASK,Task1,NEW,Description task1,
                2,EPIC,Epic1,DONE,Description epic1,
                3,SUBTASK,Subtask2,DONE,Description subtask3,2
                """;
        Files.write(Paths.get(filename.getPath()), content.getBytes());
        FileBackendTaskManager taskManager = FileBackendTaskManager.loadFromFile(filename);

        assertEquals(1, taskManager.getAllTasks().size());
        assertEquals(1, taskManager.getAllEpics().size());
        assertEquals(1, taskManager.getAllSubtasks().size());

        Task task = taskManager.getTaskById(1);
        assertEquals("Task1", task.getName());

        Subtask subtask = taskManager.getSubtaskById(3);
        assertEquals(2, subtask.getEpicLink());

        Files.delete(Paths.get(filename.getPath()));
    }
}
