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

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import booksforall.Helpers;
import booksforall.exceptions.NoSuchUser;
import booksforall.model.User;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet(urlPatterns = { "/auth/login" })
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoginServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Connection conn = null;
		String body = Helpers.getRequestBody(request);
		JsonObject input = new JsonParser().parse(body).getAsJsonObject();

		String username = input.get("username").getAsString(), password = input.get("password").getAsString();

		try {
			conn = Helpers.getConnection(request.getServletContext());

			try {
				User user = User.login(username, password, conn);
				HttpSession session = request.getSession();
				session.setAttribute("user_id", user.id);

				request.getServletContext().getRequestDispatcher("/auth/state").forward(request, response);
			} catch (NoSuchUser e) {
				response.setStatus(403);
				Helpers.JSONError("Incorrect login details", response);
			}
		} catch (NamingException | SQLException e) {
			Helpers.internalServerError(response, e);
		} finally {
			Helpers.closeConnection(conn);
		}
	}

}
