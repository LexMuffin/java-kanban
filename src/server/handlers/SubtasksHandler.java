package server.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import exceptions.ManagerTimeConflictException;
import exceptions.NotFoundException;
import managers.TaskManager;
import task.Subtask;

import java.io.IOException;
import java.util.Optional;

public class SubtasksHandler extends BaseHttpHandler {
    private Gson gson;

    public SubtasksHandler(TaskManager taskManager, Gson gson) {
        super(taskManager);
        this.gson = gson;
    }

    @Override
    public void handleGet(HttpExchange exchange) throws IOException {
        String urlPath = exchange.getRequestURI().getPath();
        if (urlPath.equals("/subtasks")) {
            sendText(exchange, gson.toJson(taskManager.getAllSubtasks()), 200);
        } else {
            try {
                int subtaskId = getTaskId(exchange);
                Optional<Subtask> subtask = Optional.of(taskManager.getSubtaskById(subtaskId));
                if (subtask.isEmpty()) {
                    sentNotFound(exchange);
                } else {
                    sendText(exchange, gson.toJson(subtask.get()), 200);
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
            Subtask subtask = gson.fromJson(json, Subtask.class);
            if (subtask.getName() == null || subtask.getDescription() == null || subtask.getEpicLink() == 0) {
                sendText(exchange, "Поля должны быть заполнены", 400);
            } else if (taskManager.getSubtaskById(subtask.getId()) == null) {
                taskManager.createSubtask(subtask);
                int createdSubtaskId = taskManager.getAllSubtasks().getLast().getId();
                sendText(exchange, "Задача " + createdSubtaskId + "создана", 201);
            } else if (taskManager.getSubtaskById(subtask.getId()) != null) {
                taskManager.updateSubtask(subtask);
                int updatedSubtaskId = subtask.getId();
                sendText(exchange, "Задача " + updatedSubtaskId + "обновлена", 201);
            }
        } catch (NotFoundException e) {
            sentNotFound(exchange);
        } catch (ManagerTimeConflictException e) {
            sendHasInteractions(exchange);
        }
    }

    @Override
    public void handleDelete(HttpExchange exchange) throws IOException {
        String urlPath = exchange.getRequestURI().getPath();
        if (urlPath.endsWith("/subtasks")) {
            taskManager.deleteAllSubtasks();
            sendText(exchange, "Задачи типа Subtask все удалены", 200);
        } else {
            try {
                Optional<Integer> subtaskId = Optional.of(getTaskId(exchange));
                if (subtaskId.isEmpty()) {
                    sentNotFound(exchange);
                } else {
                    taskManager.deleteSubtaskById(subtaskId.get());
                    sendText(exchange, "Задача " + subtaskId.get() + " типа Subtask удалена", 200);
                }
            } catch (NotFoundException e) {
                sentNotFound(exchange);
            }
        }
    }

}

