package ru.job4j.todo.servlet;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import ru.job4j.todo.model.Item;
import ru.job4j.todo.store.HbnStore;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Enumeration;

public class TodoServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
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
        HbnStore.instOf().saveOrUpdate(item);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        boolean all = Boolean.parseBoolean(req.getParameter("all"));
        Collection<Item> todos;
        if (all) {
            todos = HbnStore.instOf().getAllTasks();
        } else {
            todos = HbnStore.instOf().getUnfinishedTasks();
        }
        var json = new Gson().toJson(todos);
        var writer = resp.getWriter();
        writer.print(json);
        writer.flush();
    }
}
