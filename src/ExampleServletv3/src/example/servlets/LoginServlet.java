package example.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.stream.Collectors;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import example.Helpers;
import example.exceptions.NoSuchUser;
import example.model.User;

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
		String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
		JsonObject input = new JsonParser().parse(body).getAsJsonObject();

		String username = input.get("username").getAsString(), password = input.get("password").getAsString();

		try {
			Connection conn = Helpers.getConnection(request.getServletContext());

			try {
				User user = User.login(username, password, conn);

				JsonObject o = new JsonObject();
				o.addProperty("id", user.id);
				o.addProperty("username", user.username);
				o.addProperty("is_admin", user.is_admin);

				response.getWriter().write(o.toString());
			} catch (NoSuchUser e) {
				response.setStatus(403);
				Helpers.JSONError("Incorrect login details", response);
			}
		} catch (NamingException | SQLException e) {
			e.printStackTrace();
			response.setStatus(500);
			Helpers.JSONError("A server error occured", response);
		}
	}

}
