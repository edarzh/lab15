package org.suai.lab15.controller;

import org.suai.lab15.repository.BulletinBoardRepository;
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
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

@WebServlet(name = "bulletinBoardServlet", value = "/bulletin-board/*")
public class BulletinBoardController extends HttpServlet {
	private static final String MAIN_PAGE_URI = "/lab15_war_exploded/bulletin-board/main-page";
	private static final String LOGIN_URI = "/lab15_war_exploded/bulletin-board/login";
	private static final String LOGOUT_URI = "/lab15_war_exploded/bulletin-board/logout";
	private static final String ADD_BULLETIN_URI = "/lab15_war_exploded/bulletin-board/add-bulletin";

	private final BulletinBoardRepository bulletinBoard = new BulletinBoardRepository();
	private final UserRepository users = new UserRepository();

	@Override
	public void init() {
		String usersFile = "/home/nt/Projects/IdeaProjects/lab15/src/main/resources/users.json";

		try (BufferedReader in = new BufferedReader(new FileReader(usersFile))) {
			String content = in.lines().collect(Collectors.joining()).replaceAll("[{}\":,\\[\\]]", "");
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
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String uri = req.getRequestURI();
		HttpSession session = req.getSession();
		String name = (String) session.getAttribute("name");
		boolean loggedIn = name != null;

		switch (uri) {
			case LOGOUT_URI -> {
				session.invalidate();
				req.getRequestDispatcher("/resources/jsp/logout.jsp").forward(req, resp);
			}
			case ADD_BULLETIN_URI -> {
				if (loggedIn) {
					bulletinBoard.add(name, ZonedDateTime.now().toString());
					serveMainPage(req, resp, true);
				} else {
					req.getRequestDispatcher("/resources/jsp/access-denied.jsp").forward(req, resp);
				}
			}
			case MAIN_PAGE_URI -> serveMainPage(req, resp, loggedIn);

			default -> req.getRequestDispatcher("/resources/jsp/not-found.jsp").forward(req, resp);
		}
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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

				serveMainPage(req, resp, true);
			} else {
				req.getRequestDispatcher("/resources/jsp/login.jsp").forward(req, resp);
			}
		} else {
			req.getRequestDispatcher("/resources/jsp/not-found.jsp").forward(req, resp);
		}
	}

	private void serveMainPage(HttpServletRequest req,
			HttpServletResponse resp,
			boolean loggedIn) throws ServletException, IOException {
		List<Map.Entry<String, String>> bulletinBoard = this.bulletinBoard.get();
		req.setAttribute("bulletinBoard", bulletinBoard);
		req.setAttribute("loggedIn", loggedIn);
		req.getRequestDispatcher("/index.jsp").forward(req, resp);
	}

	@Override
	public void destroy() {
	}
}