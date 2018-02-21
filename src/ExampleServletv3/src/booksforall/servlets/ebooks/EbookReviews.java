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
import booksforall.exceptions.NoSuchReview;
import booksforall.exceptions.NoSuchUser;
import booksforall.model.Review;
import booksforall.model.User;

/**
 * Servlet implementation class EbookReviews
 */
@WebServlet("/ebooks/reviews/*")
public class EbookReviews extends HttpServlet {
	private static final long serialVersionUID = 1L;

	class FormInput {
		public String content;

		public Boolean valid() {
			return this.content != null && Helpers.intBetween(1, this.content.length(), 20000);
		}
	}

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public EbookReviews() {
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

		if (!input.valid()) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			Helpers.JSONError("Invalid form data", response);
			return;
		}

		try {
			Integer user_id = Helpers.getSessionUserId(request), ebook_id = Integer.parseInt(pathParts[1]);
			Connection conn = null;

			try {
				try {
					conn = Helpers.getConnection(request.getServletContext());

					if (!Helpers.hasPurchased(user_id, ebook_id, conn)) {
						response.setStatus(HttpServletResponse.SC_FORBIDDEN);
						Helpers.JSONError("You cannot review a book that you haven't purchased", response);
						return;
					}

					Review.find(user_id, ebook_id, conn);

					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					Helpers.JSONError(
							"You have already reviewed this book. Please wait while an admin reviews your review.",
							response);
				} catch (NoSuchReview e) {
					// good, we can like this book
					Review review = new Review();
					review.user_id = user_id;
					review.ebook_id = ebook_id;
					review.content = input.content;
					review.is_published = 0;

					review.insert(conn);
					response.setStatus(HttpServletResponse.SC_CREATED);
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

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
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
				Gson gson = new Gson();
				Helpers.JSONType(response);
				response.getWriter().write(gson.toJson(user));
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