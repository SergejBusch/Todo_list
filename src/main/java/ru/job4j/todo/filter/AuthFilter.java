package ru.job4j.todo.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthFilter implements Filter {
    @Override
    public void doFilter(ServletRequest sreq, ServletResponse sresp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) sreq;
        HttpServletResponse resp = (HttpServletResponse) sresp;
        String uri = req.getRequestURI();
        if (uri.endsWith("to.do") && req.getSession().getAttribute("user") == null) {
            var disp = req.getRequestDispatcher("login.html");
            disp.forward(req, resp);
            return;
        }
        if (uri.endsWith(".do")
                || uri.endsWith(".css") || uri.endsWith(".js") || uri.endsWith("register.html")) {
            chain.doFilter(sreq, sresp);
            return;
        }
        if (req.getSession().getAttribute("user") == null) {
            var disp = req.getRequestDispatcher("login.html");
            System.out.println(disp);
            disp.forward(req, resp);
            return;
        }
        chain.doFilter(sreq, sresp);
    }
}
