package org.suai.lab15.controller;

import org.suai.lab15.repository.UserRepository;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

@WebServlet(name = "authServlet", value = "/auth")
public class AuthServlet extends HttpServlet {
    private static final String LOGIN_URI = "/lab15/auth";

    private final UserRepository users = new UserRepository();

    @Override
    public void init() {
        String usersFile = "/home/nt/Projects/IdeaProjects/lab15/src/main/resources/users.json";

        try (BufferedReader in = new BufferedReader(new FileReader(usersFile))) {
            String content = in.lines()
                               .collect(Collectors.joining())
                               .replaceAll("[{}\":,\\[\\]]", "");
            StringTokenizer tokenizer = new StringTokenizer(content);

            while (tokenizer.hasMoreTokens()) {
                String name = tokenizer.nextToken();
                String password = tokenizer.nextToken();

                users.add(name, password);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void doPost(HttpServletRequest req,
                       HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();

        if (LOGIN_URI.equals(uri)) {
            String name = req.getParameter("name");
            String password = req.getParameter("password");

            if (name == null || password == null || name.length() < 1 || password.length() < 1) {
                throw new IllegalArgumentException();
            }

            if (users.verify(name, password)) {
                HttpSession session = req.getSession();
                session.setAttribute("name", name);

                resp.sendRedirect(BoardController.MAIN_PAGE_URI);
            } else {
                req.getRequestDispatcher("/resources/jsp/login.jsp").forward(req, resp);
            }
        } else {
            req.getRequestDispatcher("/resources/jsp/not-found.jsp").forward(req, resp);
        }
    }
}