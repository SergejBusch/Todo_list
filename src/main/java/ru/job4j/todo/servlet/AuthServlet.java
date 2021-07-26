package ru.job4j.todo.servlet;

import com.google.gson.JsonParser;
import ru.job4j.todo.store.HbnStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AuthServlet extends HttpServlet {
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
        var hbn = HbnStore.instOf();
        var user = hbn.getUserByEmail(jsonObj.get("email").getAsString());
        if (user != null && user.getPassword().equals(jsonObj.get("password").getAsString())) {
            HttpSession sc = req.getSession();
            sc.setAttribute("user", user);
            resp.sendRedirect(req.getContextPath() + "/index.html");
            System.out.println(req.getContextPath());
        } else {
            req.setAttribute("error", "Wrong email or password");
            req.getRequestDispatcher("login.html").forward(req, resp);
        }
    }
}
