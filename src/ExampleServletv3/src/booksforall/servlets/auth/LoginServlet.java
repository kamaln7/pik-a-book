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

				JsonObject o = new JsonObject();
				o.addProperty("id", user.id);
				o.addProperty("username", user.username);
				o.addProperty("nickname", user.nickname);
				o.addProperty("is_admin", user.is_admin);

				Helpers.JSONType(response);
				response.getWriter().write(o.toString());
			} catch (NoSuchUser e) {
				response.setStatus(403);
				Helpers.JSONError("Incorrect login details", response);
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
