package server.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import managers.TaskManager;

import java.io.IOException;

public class HistoryHandler extends BaseHttpHandler {
    private Gson gson;

    public HistoryHandler(TaskManager taskManager, Gson gson) {
        super(taskManager);
        this.gson = gson;
    }

    @Override
    public void handleGet(HttpExchange exchange) throws IOException {
        String urlPath = exchange.getRequestURI().getPath();
        if (urlPath.equals("/hitory")) {
            sendText(exchange, gson.toJson(taskManager.getHistory()), 200);
        } else {
            sentNotFound(exchange);
        }
    }

    @Override
    public void handlePost(HttpExchange exchange) throws IOException {
        sendText(exchange, "Метод не поддерживается", 405);
    }

    @Override
    public void handleDelete(HttpExchange exchange) throws IOException {
        sendText(exchange, "Метод не поддерживается", 405);
    }
}
