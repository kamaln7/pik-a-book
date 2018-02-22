package booksforall.servlets.admin;

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
import booksforall.admin.Methods;
import booksforall.admin.Numbers;
import booksforall.admin.PurchaseHistory;

/**
 * Servlet implementation class StatsServlet
 */
@WebServlet("/admin/stats")
public class StatsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	class Stats {
		public Numbers numbers;
		public Collection<PurchaseHistory> purchase_history;
	}

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public StatsServlet() {
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
			Stats stats = new Stats();

			stats.numbers = Methods.statsNumbers(conn);
			stats.purchase_history = Methods.purchasesLast7Days(conn);

			Helpers.JSONObject(response, stats);
		} catch (NamingException | SQLException e) {
			Helpers.internalServerError(response, e);
		} finally {
			Helpers.closeConnection(conn);
		}
	}

}
