package org.suai.lab15.controller;

import org.suai.lab15.repository.BulletinBoardRepository;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@WebServlet(name = "boardServlet", value = "/board/*")
public class BoardController extends HttpServlet {
	static final String MAIN_PAGE_URI = "/lab15/board/main-page";
	static final String LOGOUT_URI = "/lab15/board/logout";
	static final String ADD_BULLETIN_URI = "/lab15/board/add-bulletin";
	static final String REMOVE_BULLETIN_URI = "/lab15/board/remove-bulletin";

	private final BulletinBoardRepository bulletinBoard = new BulletinBoardRepository();
	private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String uri = req.getRequestURI();
		HttpSession session = req.getSession();
		String name = (String) session.getAttribute("name");
		boolean loggedIn = name != null;

		switch (uri) {
			case LOGOUT_URI -> {
				session.invalidate();
				serveMainPage(req, resp, false);
			}
			case REMOVE_BULLETIN_URI -> {
			}
			case MAIN_PAGE_URI -> serveMainPage(req, resp, loggedIn);

			default -> req.getRequestDispatcher("/resources/jsp/not-found.jsp").forward(req, resp);
		}
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String uri = req.getRequestURI();
		HttpSession session = req.getSession();
		String name = (String) session.getAttribute("name");
		boolean loggedIn = name != null;

		switch (uri) {
			case ADD_BULLETIN_URI -> {
				if (loggedIn) {
					String bulletinText = req.getParameter("bulletin-text");
					if (bulletinText != null) {
						String[] headers = new String[]{name, dateTimeFormatter.format(ZonedDateTime.now())};
						bulletinBoard.add(headers, bulletinText);
						serveMainPage(req, resp, true);
					}
				} else {
					req.getRequestDispatcher("/resources/jsp/access-denied.jsp").forward(req, resp);
				}
			}
		}
	}

	private void serveMainPage(HttpServletRequest req, HttpServletResponse resp, boolean loggedIn) throws ServletException, IOException {
		List<Map.Entry<String[], String>> bulletinBoard = this.bulletinBoard.get();
		req.setAttribute("bulletinBoard", bulletinBoard);
		req.setAttribute("loggedIn", loggedIn);
		if (loggedIn) {
			req.setAttribute("name", req.getSession().getAttribute("name"));
		}
		req.getRequestDispatcher("/index.jsp").forward(req, resp);
	}
}