package com.cabinet.servlet;

import com.cabinet.util.DatabaseUtil;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

@WebServlet(value = "/init", loadOnStartup = 1)
public class StartupServlet extends HttpServlet {
    @Override
    public void init() throws ServletException {
        super.init();
        DatabaseUtil.initDatabase();
        System.out.println("Database initialized on startup");
    }
}