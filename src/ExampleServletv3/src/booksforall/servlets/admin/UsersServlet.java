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
import booksforall.exceptions.NoSuchUser;
import booksforall.model.User;

/**
 * Servlet implementation class AdminServlet
 */
@WebServlet(urlPatterns = { "/admin/users/*" })
public class UsersServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UsersServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * Get all users
	 * 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		Connection conn = null;
		try {
			conn = Helpers.getConnection(request.getServletContext());
			Collection<User> users = User.getAllUsers(conn);

			for (User user : users) {
				user.password = "";
			}

			Helpers.JSONObject(response, users);
		} catch (NamingException | SQLException e) {
			Helpers.internalServerError(response, e);
		} finally {
			Helpers.closeConnection(conn);
		}

	}

	/**
	 * Delete a user
	 */
	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String pathInfo = request.getPathInfo();
		String[] pathParts = pathInfo.split("/");

		if (pathParts[1] == "") {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		String user_id = pathParts[1];
		Connection conn = null;

		try {
			try {
				conn = Helpers.getConnection(request.getServletContext());
				User user = User.find(Integer.parseInt(user_id), conn);

				if (user.id == Helpers.getSessionUserId(request)) {
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					Helpers.JSONError("You cannot delete yourself!", response);
				}

				user.delete(conn);
				response.setStatus(HttpServletResponse.SC_CREATED);
			} catch (NoSuchUser e) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				Helpers.JSONError("User doesn't exist", response);
			}
		} catch (NamingException | SQLException e) {
			Helpers.internalServerError(response, e);
		} finally {
			Helpers.closeConnection(conn);
		}
	}
}
