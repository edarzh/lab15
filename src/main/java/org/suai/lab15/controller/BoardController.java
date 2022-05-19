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
import java.util.List;
import java.util.Map;

@WebServlet(name = "boardServlet", value = "/board/*")
public class BoardController extends HttpServlet {
	static final String MAIN_PAGE_URI = "/lab15/board/main-page";
	static final String LOGOUT_URI = "/lab15/board/logout";
	static final String ADD_BULLETIN_URI = "/lab15/board/add-bulletin";

	private final BulletinBoardRepository bulletinBoard = new BulletinBoardRepository();

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String uri = req.getRequestURI();
		HttpSession session = req.getSession();
		String name = (String) session.getAttribute("name");
		boolean loggedIn = name != null;

		switch (uri) {
			case LOGOUT_URI -> {
				session.invalidate();
//				req.getRequestDispatcher("/resources/jsp/logout.jsp").forward(req, resp);
				serveMainPage(req, resp, false);
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

	private void serveMainPage(HttpServletRequest req, HttpServletResponse resp, boolean loggedIn) throws ServletException, IOException {
		List<Map.Entry<String, String>> bulletinBoard = this.bulletinBoard.get();
		req.setAttribute("bulletinBoard", bulletinBoard);
		req.setAttribute("loggedIn", loggedIn);
		req.getRequestDispatcher("/index.jsp").forward(req, resp);
	}
}