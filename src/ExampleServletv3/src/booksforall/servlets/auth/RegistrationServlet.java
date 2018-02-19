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

import com.google.gson.Gson;

import booksforall.Helpers;
import booksforall.model.User;

@WebServlet(urlPatterns = { "/auth/registration" })
public class RegistrationServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public class FormInput {
		public String username, password, email, fullname, city, street, nickname, bio, photo, telephone, street_number,
				zip;
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
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

				HttpSession session = request.getSession();
				session.setAttribute("user_id", userId);

				request.getServletContext().getRequestDispatcher("/auth/state").forward(request, response);
			} catch (SQLException e) {
				// check if error is unique violation
				if (e.getSQLState() != "23505") {
					// not a unique violation, use outer try/catch
					throw e;
				}

				// username already exists
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				Helpers.JSONError("Username already exists", response);
			}
		} catch (NamingException | SQLException e) {
			Helpers.internalServerError(response, e);
		} finally {
			Helpers.closeConnection(conn);
		}
	}
}
