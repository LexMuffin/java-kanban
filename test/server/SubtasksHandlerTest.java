package server;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;
import task.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class SubtasksHandlerTest extends BaseHttpHandlerTest {

    @Test
    public void checkEmptySubtasksList_statusCode200() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals("[]", response.body());
    }

    @Test
    public void checkSubtaskCreated_statusCode200() throws IOException, InterruptedException {
        Epic epic = createEpic();
        String epicJson = gson.toJson(epic);
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics"))
                .POST(HttpRequest.BodyPublishers.ofString(epicJson))
                .build();

        client.send(request1, HttpResponse.BodyHandlers.ofString());

        Subtask subtask = createSubtask(taskManager.getAllEpics().getLast().getId());
        String subtaskJson = gson.toJson(subtask);
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks"))
                .POST(HttpRequest.BodyPublishers.ofString(subtaskJson))
                .build();

        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response2.statusCode());
        Assertions.assertEquals(taskManager.getAllSubtasks().size(), 1);
    }

    @Test
    public void checkSubtaskCreatedWithEmptyRequiredField_statusCode400() throws IOException, InterruptedException {
        Epic epic = createEpic();
        String epicJson = gson.toJson(epic);
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics"))
                .POST(HttpRequest.BodyPublishers.ofString(epicJson))
                .build();

        client.send(request1, HttpResponse.BodyHandlers.ofString());

        Subtask subtask = new Subtask(
                null,
                "description subtask1",
                taskManager.getAllEpics().getLast().getId()
        );
        String subtaskJson = gson.toJson(subtask);
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks"))
                .POST(HttpRequest.BodyPublishers.ofString(subtaskJson))
                .build();

        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(400, response2.statusCode());
        Assertions.assertEquals(taskManager.getAllSubtasks().size(), 0);
    }

    @Test
    public void checkSubtaskCreatedThenDeleted_statusCode200() throws IOException, InterruptedException {
        Epic epic = createEpic();
        String epicJson = gson.toJson(epic);
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics"))
                .POST(HttpRequest.BodyPublishers.ofString(epicJson))
                .build();

        client.send(request1, HttpResponse.BodyHandlers.ofString());

        Subtask subtask = createSubtask(taskManager.getAllEpics().getLast().getId());
        String subtaskJson = gson.toJson(subtask);
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks"))
                .POST(HttpRequest.BodyPublishers.ofString(subtaskJson))
                .build();

        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response2.statusCode());
        Assertions.assertEquals(taskManager.getAllSubtasks().size(), 1);

        HttpRequest request3 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks"))
                .DELETE()
                .build();
        HttpResponse<String> response3 = client.send(request3, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response3.statusCode());
        Assertions.assertEquals(taskManager.getAllSubtasks().size(), 0);
    }
}
