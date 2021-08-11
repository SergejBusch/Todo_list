package ru.job4j.todo.servlet;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.model.Item;
import ru.job4j.todo.model.UserItem;
import ru.job4j.todo.store.HbnStore;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
        var array = getCategories(jsonObj.get("categories").getAsJsonArray());
        var item = new Item(
                jsonObj.get("id").getAsInt(),
                jsonObj.get("name").getAsString(),
                jsonObj.get("description").getAsString(),
                new Date(System.currentTimeMillis()),
                jsonObj.get("done").getAsBoolean(),
                (UserItem) req.getSession().getAttribute("user"),
                array);
        System.out.println(array);
        HbnStore.instOf().saveOrUpdate(item);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        var user = ((UserItem) req.getSession().getAttribute("user"));
        boolean all = Boolean.parseBoolean(req.getParameter("all"));
        Collection<Item> todos;
        if (all) {
            todos = HbnStore.instOf().getAllTasks(user.getId());
        } else {
            todos = HbnStore.instOf().getUnfinishedTasks(user.getId());
        }
        var json = new Gson().toJson(todos);
        var email = new Gson().toJson(user.getEmail());
        var writer = resp.getWriter();
        writer.print(List.of(json, email));
        writer.flush();
    }

    private List<Category> getCategories(JsonArray ids) {
//        List<Category> categories = new ArrayList<>();
//        for (int i = 0; i < ids.size(); i++) {
//
//        }
        return IntStream
                .range(0, ids.size())
                .mapToObj(i -> ids.get(i))
                .map(c -> HbnStore.instOf().findById(c.getAsInt()))
                .collect(Collectors.toList());
    }
}
