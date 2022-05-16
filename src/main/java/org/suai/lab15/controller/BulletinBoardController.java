package org.suai.lab15.controller;

import org.suai.lab15.repository.BulletinBoardRepository;
import org.suai.lab15.repository.UsersRepository;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

@WebServlet(name = "bulletinBoardServlet", value = "/bulletin-board/*")
public class BulletinBoardController extends HttpServlet {
	private static final String LOGIN_URI = "/lab15_war_exploded/bulletin-board/login";
	private static final String LOGOUT_URI = "/lab15_war_exploded/bulletin-board/logout";
	private static final String ADD_BULLETIN_URI = "/lab15_war_exploded/bulletin-board/add-bulletin";
	private static final String MAIN_PAGE_URI = "/lab15_war_exploded/bulletin-board/main-page";

	private final BulletinBoardRepository bulletinBoardRepository = new BulletinBoardRepository();
	private final UsersRepository usersRepository = new UsersRepository();

	@Override
	public void init() {
		usersRepository.add("Alice", "123");
		usersRepository.add("Bob", "123");
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String uri = req.getRequestURI();

		switch (uri) {
			case LOGOUT_URI -> {
				HttpSession session = req.getSession();
				session.invalidate();
				req.getRequestDispatcher("/logout.jsp").forward(req, resp);
			}
			case ADD_BULLETIN_URI -> {
				HttpSession session = req.getSession();
				String name = (String) session.getAttribute("name");

				if (name != null) {
					bulletinBoardRepository.add(name, ZonedDateTime.now().toString());
					getMainPage(req, resp);
				} else {
					req.getRequestDispatcher("/access-denied.jsp").forward(req, resp);
				}
			}
			case MAIN_PAGE_URI -> getMainPage(req, resp);
		}
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String uri = req.getRequestURI();

		switch (uri) {
			case LOGIN_URI -> {
				String name = req.getParameter("name");
				String password = req.getParameter("password");

				if (name == null || password == null || name.length() < 1 || password.length() < 1) {
					throw new IllegalArgumentException();
				}

				if (usersRepository.verify(name, password)) {
					HttpSession session = req.getSession();
					session.setAttribute("name", name);

					getMainPage(req, resp);
				} else {
					req.getRequestDispatcher("/login.jsp").forward(req, resp);
				}
			}
		}
	}

	private void getMainPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<Map.Entry<String, String>> bulletinBoard = bulletinBoardRepository.get();
		req.setAttribute("bulletinBoard", bulletinBoard);
		req.getRequestDispatcher("/index.jsp").forward(req, resp);
	}

	@Override
	public void destroy() {
	}
}