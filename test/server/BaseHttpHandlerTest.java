package server;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.http.HttpClient;

import managers.Managers;
import managers.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import task.Epic;
import task.Subtask;
import task.Task;

public abstract class BaseHttpHandlerTest {
    protected HttpTaskServer httpServer;
    protected TaskManager taskManager;
    protected HttpClient client;
    protected Gson gson;

    protected Task createTask() {
        return new Task("name task1", "description task1");
    }

    protected Epic createEpic() {
        return new Epic("name epic1", "description epic1");
    }

    protected Subtask createSubtask(int epicId) {
        return new Subtask("name subtask1", "description subtask1", epicId);
    }

    @BeforeEach
    void serverStart() throws IOException {
        taskManager = Managers.getDefault();
        httpServer = new HttpTaskServer(taskManager);
        httpServer.start();
        gson = HttpTaskServer.createGson();
        client = HttpClient.newHttpClient();
    }

    @AfterEach
    void serverStop() {
        httpServer.stop();
    }
}
