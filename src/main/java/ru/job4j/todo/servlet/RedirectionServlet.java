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
//            resp.setStatus(307);
//            resp.setContentType("document/Redirect");
//            resp.setHeader("Location", "index.html");

//            resp.setCharacterEncoding("UTF-8");
//            var url = resp.encodeRedirectURL("index.html");
//            resp.setStatus(302);
//            resp.setContentType("text/html;charset=UTF-8");
//            resp.setHeader("Location", url);
//            System.out.println("index go");
            var disp = req.getRequestDispatcher("index.html");
            System.out.println(disp);
            disp.forward(req, resp);
//            resp.sendRedirect(req.getContextPath() + "/index.html");
//            System.out.println("auch da");
        }
        return;
    }
}
