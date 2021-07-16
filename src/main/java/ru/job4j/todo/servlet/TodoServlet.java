package ru.job4j.todo.servlet;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ru.job4j.todo.model.Item;
import ru.job4j.todo.store.HbnStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;

public class TodoServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var sb = new StringBuilder();
        String line;
        var reader = req.getReader();
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        String everything = sb.toString();
        System.out.println(everything);
        var jsonObj = JsonParser.parseString(everything).getAsJsonObject();
        var item = new Item(jsonObj.get("id").getAsInt(),
                jsonObj.get("name").getAsString(),
                jsonObj.get("description").getAsString(),
                new Timestamp(System.currentTimeMillis()),
                jsonObj.get("done").getAsBoolean()
                );

        try (var store = new HbnStore()) {
            store.saveOrUpdate(item);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        try (var store = new HbnStore()) {
            var todos = store.getAllTasks();
            var json = new Gson().toJson(todos);
            var writer = resp.getWriter();
            writer.print(json);
            writer.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
