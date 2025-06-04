package server.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import exceptions.NotFoundException;
import managers.TaskManager;
import task.Epic;

import java.io.IOException;
import java.util.Optional;

public class EpicsHandler extends BaseHttpHandler {
    private Gson gson;

    public EpicsHandler(TaskManager taskManager, Gson gson) {
        super(taskManager);
        this.gson = gson;
    }

    @Override
    public void handleGet(HttpExchange exchange) throws IOException {
        String urlPath = exchange.getRequestURI().getPath();
        if (urlPath.equals("/epics")) {
            sendText(exchange, gson.toJson(taskManager.getAllEpics()), 200);
        }
        if ((urlPath.split("/").length == 4) && (urlPath.split("/")[1].equals("epics"))) {
            try {
                int epicId = getTaskId(exchange);
                Optional<Epic> epic = Optional.of(taskManager.getEpicById(epicId));
                if (epic.isEmpty()) {
                    sentNotFound(exchange);
                } else {
                    sendText(exchange, gson.toJson(epic.get().getSubtasks()), 200);
                }
            } catch (NotFoundException e) {
                sentNotFound(exchange);
            }
        }
        if ((urlPath.split("/").length == 3) && (urlPath.split("/")[1].equals("epics"))) {
            try {
                int epicId = getTaskId(exchange);
                Optional<Epic> epic = Optional.of(taskManager.getEpicById(epicId));
                if (epic.isEmpty()) {
                    sentNotFound(exchange);
                } else {
                    sendText(exchange, gson.toJson(epic.get()), 200);
                }
            } catch (NotFoundException e) {
                sentNotFound(exchange);
            }
        }

    }

    @Override
    public void handlePost(HttpExchange exchange) throws IOException {
        String json = readText(exchange);
        try {
            Epic epic = gson.fromJson(json, Epic.class);
            if (epic.getName() == null || epic.getDescription() == null) {
                sendText(exchange, "Поля должны быть заполнены", 400);
            }
            if (epic.getId() == 0) {
                taskManager.createEpic(epic);
                int createdEpicId = taskManager.getAllEpics().getLast().getId();
                sendText(exchange, "Задача " + createdEpicId + "создана", 201);
            }
            if (epic.getId() != 0) {
                taskManager.updateEpic(epic);
                int updatedEpicId = epic.getId();
                sendText(exchange, "Задача " + updatedEpicId + "обновлена", 201);
            }
        } catch (NotFoundException e) {
            sentNotFound(exchange);
        }
    }

    @Override
    public void handleDelete(HttpExchange exchange) throws IOException {
        String urlPath = exchange.getRequestURI().getPath();
        if (urlPath.endsWith("/epics")) {
            taskManager.deleteAllEpics();
            sendText(exchange, "Задачи типа Epic все удалены", 200);
        } else {
            try {
                Optional<Integer> epicId = Optional.of(getTaskId(exchange));
                if (epicId.isEmpty()) {
                    sentNotFound(exchange);
                } else {
                    taskManager.deleteEpicById(epicId.get());
                    sendText(exchange, "Задача " + epicId.get() + " типа Epic удалена", 200);
                }
            } catch (NotFoundException e) {
                sentNotFound(exchange);
            }
        }
    }
}
