package booksforall.servlets.ebooks;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import booksforall.Helpers;
import booksforall.exceptions.NoSuchLike;
import booksforall.model.Like;

/**
 * Servlet implementation class EbookLikes
 */
@WebServlet("/ebooks/likes/*")
public class EbookLikes extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public EbookLikes() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String pathInfo = request.getPathInfo();
		String[] pathParts = pathInfo.split("/");

		if (pathParts[1] == "") {
			response.sendError(response.SC_BAD_REQUEST);
			return;
		}

		Integer user_id = Integer.parseInt(request.getParameter("user_id")), ebook_id = Integer.parseInt(pathParts[1]);
		Connection conn = null;

		try {
			try {
				conn = Helpers.getConnection(request.getServletContext());
				Like like = Like.find(user_id, ebook_id, conn);

				response.setStatus(403);
				Helpers.JSONError("You cannot like this e-book", response);
			} catch (NoSuchLike e) {
				// good, we can like this book
				Like like = new Like();
				like.user_id = user_id;
				like.ebook_id = ebook_id;
				like.insert(conn);

				response.setStatus(response.SC_CREATED);
			}
		} catch (NamingException | SQLException e) {
			e.printStackTrace();
			response.setStatus(500);
			Helpers.JSONError("A server error occured", response);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String pathInfo = request.getPathInfo();
		String[] pathParts = pathInfo.split("/");

		if (pathParts[1] == "") {
			response.sendError(response.SC_BAD_REQUEST);
			return;
		}

		Integer user_id = Integer.parseInt(request.getParameter("user_id")), ebook_id = Integer.parseInt(pathParts[1]);
		Connection conn = null;

		try {
			try {
				conn = Helpers.getConnection(request.getServletContext());
				Like like = Like.find(user_id, ebook_id, conn);

				like.delete(conn);
				response.setStatus(response.SC_CREATED);
			} catch (NoSuchLike e) {
				response.setStatus(404);
				Helpers.JSONError("You cannot dislike this e-book", response);
			}
		} catch (NamingException | SQLException e) {
			e.printStackTrace();
			response.setStatus(500);
			Helpers.JSONError("A server error occured", response);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
