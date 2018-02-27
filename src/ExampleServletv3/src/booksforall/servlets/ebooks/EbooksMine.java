package booksforall.servlets.ebooks;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import booksforall.Helpers;
import booksforall.exceptions.NoSuchUser;
import booksforall.model.Ebook;

/**
 * Servlet implementation class EbooksMine
 */
@WebServlet("/ebooks/mine")
public class EbooksMine extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public EbooksMine() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * Get ebooks owned by the logged in user
	 * 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {

			Integer user_id = Helpers.getSessionUserId(request);
			Connection conn = null;
			try {
				conn = Helpers.getConnection(request.getServletContext());
				Collection<Ebook> ebooks = Ebook.ownedByUser(user_id, conn);

				for (Ebook ebook : ebooks) {
					ebook.getLikes(conn);
					ebook.getReviews(conn, true);
				}

				Helpers.JSONObject(response, ebooks);
			} catch (NamingException | SQLException e) {
				Helpers.internalServerError(response, e);
			} finally {
				Helpers.closeConnection(conn);
			}

		} catch (NoSuchUser e) {
			Helpers.JSONError("Unauthenticated.", response);
		}
	}

}
