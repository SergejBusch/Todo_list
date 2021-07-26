package ru.job4j.todo.servlet;

import com.google.gson.JsonParser;
import ru.job4j.todo.model.UserItem;
import ru.job4j.todo.store.HbnStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class RegServlet extends HttpServlet {
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
        var store = HbnStore.instOf();
        UserItem user = null;
        if (store.getUserByEmail(jsonObj.get("email").getAsString()) == null) {
            user = new UserItem();
            user.setEmail(jsonObj.get("email").getAsString());
            user.setPassword(jsonObj.get("password").getAsString());
            store.saveOrUpdate(user);
        }
        if (user != null) {
            HttpSession sc = req.getSession();
            sc.setAttribute("user", user);
            resp.sendRedirect(req.getContextPath() + "/index.html");
            return;
//            new RedirectionServlet().doGet(req, resp);
//            System.out.println(req.getServletPath());
//            getServletConfig()
//                    .getServletContext().getRequestDispatcher("/redir.do").include(req, resp);
//            req.getRequestDispatcher("/WEB-INF/test.html").forward(req, resp);

//            resp.setStatus(302);
//            resp.setContentType("text/html");
//            resp.setHeader("Location", req.getContextPath() + "/index.html");
//            resp.setHeader("Connection", "close");
//            resp.sendRedirect(req.getContextPath() + "/redir.do");
        } else {
            req.setAttribute("error", "User with this email is already registered");
            req.getRequestDispatcher("register.html").forward(req, resp);
        }
        return;
    }
}
