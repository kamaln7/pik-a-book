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

import com.google.gson.Gson;

import booksforall.Helpers;
import booksforall.exceptions.NoSuchReview;
import booksforall.model.Review;

/**
 * Servlet implementation class adminReviewsServlet
 */
@WebServlet(urlPatterns = { "/admin/reviews/*" })
public class adminReviewsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public adminReviewsServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		Connection conn = null;
		try {
			conn = Helpers.getConnection(request.getServletContext());
			Collection<Review> reviews = Review.getUnApprovedReviews(conn);
			Gson gson = new Gson();
			Helpers.JSONType(response);
			response.getWriter().write(gson.toJson(reviews));
		} catch (NamingException | SQLException e) {
			Helpers.internalServerError(response, e);
		} finally {
			Helpers.closeConnection(conn);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doGet(request, response);
	}

	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String pathInfo = request.getPathInfo();
		String[] pathParts = pathInfo.split("/");

		if (pathParts[1] == "" || pathParts[2] == "") {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		String user_id = pathParts[1];
		String ebook_id = pathParts[2];

		Connection conn = null;

		try {
			try {
				conn = Helpers.getConnection(request.getServletContext());
				Review review = Review.find(Integer.parseInt(user_id), Integer.parseInt(ebook_id), conn);
				review.delete(conn);
				response.setStatus(HttpServletResponse.SC_CREATED);
			} catch (NoSuchReview e) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				Helpers.JSONError("review doesn't exist", response);
			}
		} catch (NamingException | SQLException e) {
			Helpers.internalServerError(response, e);
		} finally {
			Helpers.closeConnection(conn);
		}
	}
}
