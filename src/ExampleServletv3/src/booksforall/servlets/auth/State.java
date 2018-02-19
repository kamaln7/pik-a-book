package booksforall.servlets.auth;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import booksforall.Helpers;
import booksforall.exceptions.NoSuchUser;
import booksforall.model.AuthState;
import booksforall.model.User;

/**
 * Servlet implementation class State
 */
@WebServlet("/auth/state")
public class State extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public State() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		AuthState state = new AuthState();
		state.authed = false;
		state.user = null;

		Connection conn = null;
		try {
			conn = Helpers.getConnection(request.getServletContext());

			try {
				User user = Helpers.getSessionUser(request, conn);

				// remove sensitive fields
				user.password = null;

				state.authed = true;
				state.user = user;
			} catch (NoSuchUser e) {
				session.invalidate();
			} finally {
				Helpers.JSONObject(response, state);
			}
		} catch (NamingException | SQLException e) {
			Helpers.internalServerError(response, e);
		} finally {
			Helpers.closeConnection(conn);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doGet(request, response);
	}

}
