package ru.job4j.todo.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RedirectionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getSession().getAttribute("user") == null) {
            var disp = req.getRequestDispatcher("login.html");
            System.out.println(disp);
            disp.forward(req, resp);
        } else {
            var disp = req.getRequestDispatcher("index.html");
            System.out.println(disp);
            disp.forward(req, resp);
        }
    }
}
