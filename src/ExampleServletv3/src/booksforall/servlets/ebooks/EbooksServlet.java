package booksforall.servlets.ebooks;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

import javax.naming.NamingException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import booksforall.Helpers;
import booksforall.exceptions.NoSuchUser;
import booksforall.model.Ebook;

/**
 * Servlet implementation class EbooksServlet
 */
@WebServlet("/ebooks")
public class EbooksServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public EbooksServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * Get all ebooks alphabetically
	 * 
	 * @throws IOException
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Connection conn = null;
		try {
			conn = Helpers.getConnection(request.getServletContext());
			Collection<Ebook> ebooks = Ebook.alphabetic(conn);

			for (Ebook ebook : ebooks) {
				ebook.getLikes(conn);

				try {
					Integer user_id = Helpers.getSessionUserId(request);
					ebook.checkPurchased(user_id, conn);
				} catch (NoSuchUser e) {
					// not logged in
					ebook.has_purchased = false;
				}
			}

			Helpers.JSONObject(response, ebooks);
		} catch (NamingException | SQLException e) {
			Helpers.internalServerError(response, e);
		} finally {
			Helpers.closeConnection(conn);
		}
	}

}
