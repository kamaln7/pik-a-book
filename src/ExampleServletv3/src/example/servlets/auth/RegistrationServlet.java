package example.servlets.auth;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.NamingException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import example.Helpers;
import example.model.User;

@WebServlet(urlPatterns = { "/auth/registration" })
public class RegistrationServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public class FormInput {
		public String username, password, email, fullname, city, street, nickname, bio, photo, telephone, street_number,
				zip;
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Connection conn = null;
		String body = Helpers.getRequestBody(request);
		FormInput input = new Gson().fromJson(body, FormInput.class);
		User user = new User(input.username, input.email, input.password, input.fullname, input.street, input.city,
				input.zip, input.telephone, input.nickname, input.bio, input.photo, false,
				Integer.parseInt(input.street_number));

		try {
			conn = Helpers.getConnection(request.getServletContext());
			Integer userId;

			try {
				userId = user.insert(conn);
			} catch (SQLException e) {
				// check if error is unique violation
				if (e.getSQLState() != "23505") {
					// not a unique violation, use outer try/catch
					throw e;
				}

				// username already exists
				response.setStatus(response.SC_BAD_REQUEST);
				Helpers.JSONError("Username already exists", response);
				return;
			}

			JsonObject o = new JsonObject();
			o.addProperty("id", userId);
			o.addProperty("username", user.username);
			o.addProperty("nickname", user.nickname);
			o.addProperty("is_admin", user.is_admin);

			Helpers.JSONType(response);
			response.getWriter().write(o.toString());
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
