package booksforall.servlets.ebooks;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import booksforall.Helpers;
import booksforall.exceptions.NoSuchEbook;
import booksforall.exceptions.NoSuchUser;
import booksforall.model.Ebook;
import booksforall.model.Review;

/**
 * Servlet implementation class EbooksMine
 */
@WebServlet("/ebooks/reviews/mine")
public class EbooksReviewsMine extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public EbooksReviewsMine() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {

			Integer user_id = Helpers.getSessionUserId(request);
			Connection conn = null;
			try {
				conn = Helpers.getConnection(request.getServletContext());
				ArrayList<Ebook> ebooks = new ArrayList<Ebook>();
				Collection<Review> reviews = Review.ownedByUser(user_id, conn);

				for (Review review : reviews) {
					try {
						Ebook ebook = Ebook.find(review.ebook_id, conn);
						ArrayList<Review> ebookReviews = new ArrayList<Review>();
						ebookReviews.add(review);
						ebook.reviews = ebookReviews;

						ebooks.add(ebook);
					} catch (NoSuchEbook e) {
						e.printStackTrace();
						continue;
					}
				}

				if (ebooks.size() == 0) {
					response.setStatus(HttpServletResponse.SC_NO_CONTENT);
					Helpers.JSONError("You don't have any reviews", response);
					return;
				}

				Gson gson = new Gson();
				Helpers.JSONType(response);
				response.getWriter().write(gson.toJson(ebooks));
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
