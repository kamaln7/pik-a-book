package booksforall.servlets.auth;

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

import com.google.gson.Gson;

import booksforall.Helpers;
import booksforall.exceptions.NoSuchUser;
import booksforall.model.User;

/**
 * Servlet implementation class AdminServlet
 */
@WebServlet(urlPatterns = { "/auth/admin/*" })
public class AdminServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AdminServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		Connection conn = null;
		try {
			conn = Helpers.getConnection(request.getServletContext());
			Collection<User> Users = User.getAllUsers(conn);

			Gson gson = new Gson();
			Helpers.JSONType(response);
			response.getWriter().write(gson.toJson(Users));
		} catch (NamingException | SQLException e) {
			Helpers.internalServerError(response, e);
		} finally {
			Helpers.closeConnection(conn);
		}

	}

	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String pathInfo = request.getPathInfo();
		String[] pathParts = pathInfo.split("/");

		if (pathParts[1] == "") {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		System.out.println("user id is" + pathParts[1]);
		String user_id = pathParts[1];
		Connection conn = null;

		try {
			try {
				conn = Helpers.getConnection(request.getServletContext());
				User user = User.find(Integer.parseInt(user_id), conn);

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
