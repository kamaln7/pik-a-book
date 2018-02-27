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
@WebServlet(urlPatterns = { "/admin/reviews" })
public class adminReviewsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public class FormInput {
		public Integer user_id, ebook_id;
	}

	/*
	 * 
	 * 
	 * @see HttpServlet#HttpServlet()
	 */
	public adminReviewsServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * Return all reviews
	 * 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
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

	/**
	 * Approve a review
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String body = Helpers.getRequestBody(request);
		FormInput input = new Gson().fromJson(body, FormInput.class);
		Connection conn = null;

		if (input.user_id == null) {
			Helpers.JSONError("Invalid input", response);
			return;
		}

		try {
			try {
				conn = Helpers.getConnection(request.getServletContext());
				Review.approve(input.user_id, input.ebook_id, conn);
			} catch (NoSuchReview e) {
				Helpers.JSONError("No such review", response);
			}
		} catch (NamingException | SQLException e) {
			Helpers.internalServerError(response, e);
		} finally {
			Helpers.closeConnection(conn);
		}

	}

	/**
	 * Delete a review from the database
	 */
	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String body = Helpers.getRequestBody(request);
		FormInput input = new Gson().fromJson(body, FormInput.class);
		Connection conn = null;

		try {
			conn = Helpers.getConnection(request.getServletContext());
			Review.delete(input.user_id, input.ebook_id, conn);
		} catch (NamingException | SQLException e) {
			Helpers.internalServerError(response, e);
		} finally {
			Helpers.closeConnection(conn);
		}
	}
}
