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

import booksforall.AppConstants;
import booksforall.Helpers;
import booksforall.exceptions.NoSuchEbook;
import booksforall.model.Ebook;

/**
 * Servlet implementation class EbooksTopHomepage
 */
@WebServlet("/ebooks/top-homepage")
public class EbooksTopHomepage extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public EbooksTopHomepage() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Connection conn = null;
		try {
			conn = Helpers.getConnection(request.getServletContext());
			Collection<Ebook> ebooks = Ebook.bestSelling(AppConstants.HOMEPAGE_TOP_COUNT, conn);

			Helpers.JSONObject(response, ebooks);
		} catch (NoSuchEbook | NamingException | SQLException e) {
			Helpers.internalServerError(response, e);
		} finally {
			Helpers.closeConnection(conn);
		}
	}

}
