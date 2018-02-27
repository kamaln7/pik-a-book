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
import booksforall.exceptions.NoSuchEbook;
import booksforall.exceptions.NoSuchUser;
import booksforall.model.Ebook;

/**
 * Servlet implementation class EbookViewServlet
 */
@WebServlet("/ebooks/*")
public class EbookViewServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public EbookViewServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * Get info about an ebook
	 * 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String pathInfo = request.getPathInfo();
		String[] pathParts = pathInfo.split("/");

		if (pathParts[1] == "") {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		Connection conn = null;
		try {
			Integer ebook_id = Integer.parseInt(pathParts[1]);
			conn = Helpers.getConnection(request.getServletContext());

			try {
				Ebook ebook = Ebook.find(ebook_id, conn);

				ebook.getLikes(conn);
				ebook.getReviews(conn, true);

				try {
					Integer user_id = Helpers.getSessionUserId(request);
					ebook.checkPurchased(user_id, conn);
				} catch (NoSuchUser e) {
					// not logged in
					ebook.has_purchased = false;
				}

				Helpers.JSONObject(response, ebook);
			} catch (NoSuchEbook e) {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				Helpers.JSONError("E-book does not exist.", response);
			}
		} catch (NamingException | SQLException e) {
			Helpers.internalServerError(response, e);
		} finally {
			Helpers.closeConnection(conn);
		}
	}

}
