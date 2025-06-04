package server.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import exceptions.NotFoundException;
import managers.TaskManager;
import task.Task;

import java.io.IOException;
import java.util.Optional;

public class TasksHandler extends BaseHttpHandler {
    private Gson gson;

    public TasksHandler(TaskManager taskManager, Gson gson) {
        super(taskManager);
        this.gson = gson;
    }

    @Override
    public void handleGet(HttpExchange exchange) throws IOException {
        String urlPath = exchange.getRequestURI().getPath();
        if (urlPath.equals("/tasks")) {
            sendText(exchange, gson.toJson(taskManager.getAllTasks()), 200);
        } else {
            try {
                int taskId = getTaskId(exchange);
                Optional<Task> task = Optional.of(taskManager.getTaskById(taskId));
                if (task.isEmpty()) {
                    sentNotFound(exchange);
                } else {
                    sendText(exchange, gson.toJson(task.get()), 200);
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
            Task task = gson.fromJson(json, Task.class);
            if (task.getName() == null || task.getDescription() == null) {
                sendText(exchange, "Поля должны быть заполнены", 400);
            }
            if (taskManager.getTaskById(task.getId()) == null) {
                taskManager.createTask(task);
                int createdTaskId = taskManager.getAllTasks().getLast().getId();
                sendText(exchange, "Задача " + createdTaskId + " создана", 201);
            }
            if (taskManager.getTaskById(task.getId()) != null) {
                taskManager.updateTask(task);
                int updatedTaskId = task.getId();
                sendText(exchange, "Задача " + updatedTaskId + " обновлена", 201);
            }
        } catch (NotFoundException e) {
            sentNotFound(exchange);
        }
    }

    @Override
    public void handleDelete(HttpExchange exchange) throws IOException {
        String urlPath = exchange.getRequestURI().getPath();
        if (urlPath.endsWith("/tasks")) {
            taskManager.deleteAllTasks();
            sendText(exchange, "Задачи типа Task все удалены", 200);
        } else {
            try {
                Optional<Integer> taskId = Optional.of(getTaskId(exchange));
                if (taskId.isEmpty()) {
                    sentNotFound(exchange);
                } else {
                    taskManager.deleteTaskById(taskId.get());
                    sendText(exchange, "Задача " + taskId.get() + " типа Task удалена", 200);
                }
            } catch (NotFoundException e) {
                sentNotFound(exchange);
            }
        }
    }

}
