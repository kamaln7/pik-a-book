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
import booksforall.exceptions.NoSuchUser;
import booksforall.model.User;

@WebServlet(urlPatterns = { "/auth/registration" })
public class RegistrationServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public class FormInput {
		public String username, password, email, fullname, city, street, nickname, bio, photo, telephone, street_number,
				zip;

		public Boolean valid() {
			Boolean v = true;

			v = v && Helpers.intBetween(1, this.username.length(), 10);
			v = v && Helpers.intBetween(1, this.password.length(), 8);
			v = v && Helpers.intBetween(1, this.email.length(), 500) && Helpers.isEmail(this.email);
			v = v && Helpers.intBetween(1, this.nickname.length(), 20);
			v = v && (this.bio == null || Helpers.intBetween(1, this.bio.length(), 50));
			v = v && (this.photo == null || this.photo.startsWith("http://") || this.photo.startsWith("https://"));
			v = v && Helpers.intBetween(1, this.fullname.length(), 255);
			v = v && Helpers.intBetween(3, this.street.length(), 500) && Helpers.isStreet(this.street);
			v = v && Helpers.isStreetNumber(this.street_number);
			v = v && Helpers.isCity(this.city);
			v = v && Helpers.isPostalCode(this.zip);
			v = v && Helpers.isTelephone(this.telephone);

			return v;
		}
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String body = Helpers.getRequestBody(request);
		FormInput input = new Gson().fromJson(body, FormInput.class);

		if (!input.valid()) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			Helpers.JSONError("Invalid form data", response);
			return;
		}

		User user = new User(input.username, input.email, input.password, input.fullname, input.street, input.city,
				input.zip, input.telephone, input.nickname, input.bio, input.photo, false,
				Integer.parseInt(input.street_number));

		try {
			Helpers.getSessionUserId(request);
			Helpers.JSONError("Already logged in.", response);
			return;
		} catch (NoSuchUser e) {
			// continue
		}

		Connection conn = null;

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
