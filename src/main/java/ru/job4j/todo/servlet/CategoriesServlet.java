package ru.job4j.todo.servlet;

import com.google.gson.Gson;
import ru.job4j.todo.store.HbnStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CategoriesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        var categories = HbnStore.instOf().getAllCategories();
        var json = new Gson().toJson(categories);
        var writer = resp.getWriter();
        writer.print(json);
        writer.flush();
    }
}
