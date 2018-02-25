package booksforall.servlets.ebooks;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import booksforall.Helpers;
import booksforall.exceptions.NoSuchReading;
import booksforall.exceptions.NoSuchUser;
import booksforall.model.Reading;

/**
 * Servlet implementation class EbookReviews
 */
@WebServlet("/ebooks/scrolling-position/*")
public class EbookScrollingPosition extends HttpServlet {
	private static final long serialVersionUID = 1L;

	class FormInput {
		public String position;

		public Boolean valid() {
			return this.position != null && this.position.matches("^\\d+$");
		}
	}

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public EbookScrollingPosition() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String pathInfo = request.getPathInfo();
		String[] pathParts = pathInfo.split("/");

		if (pathParts[1] == "") {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		String body = Helpers.getRequestBody(request);
		FormInput input = new Gson().fromJson(body, FormInput.class);

		if (input == null || !input.valid()) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			Helpers.JSONError("Invalid form data", response);
			return;
		}

		try {
			Integer user_id = Helpers.getSessionUserId(request), ebook_id = Integer.parseInt(pathParts[1]);
			Connection conn = null;

			try {
				conn = Helpers.getConnection(request.getServletContext());

				if (!Helpers.hasPurchased(user_id, ebook_id, conn)) {
					response.setStatus(HttpServletResponse.SC_FORBIDDEN);
					Helpers.JSONError("You have not purchased this book.", response);
					return;
				}

				Reading reading = Reading.get(user_id, ebook_id, conn);
				reading.position = input.position;
				reading.update(conn);

				response.setStatus(HttpServletResponse.SC_CREATED);
				Helpers.JSONObject(response, reading);
			} catch (NamingException | SQLException e) {
				Helpers.internalServerError(response, e);
			} finally {
				Helpers.closeConnection(conn);
			}
		} catch (NoSuchUser e) {
			Helpers.JSONError("Unauthenticated.", response);
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String pathInfo = request.getPathInfo();
		String[] pathParts = pathInfo.split("/");

		if (pathParts[1] == "") {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		try {
			Integer user_id = Helpers.getSessionUserId(request), ebook_id = Integer.parseInt(pathParts[1]);
			Connection conn = null;

			try {
				conn = Helpers.getConnection(request.getServletContext());

				if (!Helpers.hasPurchased(user_id, ebook_id, conn)) {
					response.setStatus(HttpServletResponse.SC_FORBIDDEN);
					Helpers.JSONError("You have not purchased this book.", response);
					return;
				}

				try {
					Reading reading = Reading.find(user_id, ebook_id, conn);
					Helpers.JSONObject(response, reading);
				} catch (NoSuchReading e) {
					response.setStatus(HttpServletResponse.SC_NOT_FOUND);
					Helpers.JSONError("User has not started this book yet.", response);
				}
			} catch (NamingException | SQLException e) {
				Helpers.internalServerError(response, e);
			} finally {
				Helpers.closeConnection(conn);
			}
		} catch (NoSuchUser e) {
			Helpers.JSONError("Unauthenticated.", response);
		}
	}
}