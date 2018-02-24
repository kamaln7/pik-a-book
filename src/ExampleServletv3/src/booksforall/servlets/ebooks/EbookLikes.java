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
import booksforall.exceptions.NoSuchLike;
import booksforall.exceptions.NoSuchUser;
import booksforall.model.Like;

/**
 * Servlet implementation class EbookLikes
 */
@WebServlet("/ebooks/likes")
public class EbookLikes extends HttpServlet {
	private static final long serialVersionUID = 1L;

	class FormInput {
		public Integer ebook_id;

		public Boolean valid() {
			return ebook_id != null;
		}
	}

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public EbookLikes() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String body = Helpers.getRequestBody(request);
		FormInput input = new Gson().fromJson(body, FormInput.class);

		if (!input.valid()) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		try {
			Integer user_id = Helpers.getSessionUserId(request), ebook_id = input.ebook_id;
			Connection conn = null;

			try {
				try {
					conn = Helpers.getConnection(request.getServletContext());
					if (!Helpers.hasPurchased(user_id, ebook_id, conn)) {
						response.setStatus(HttpServletResponse.SC_FORBIDDEN);
						Helpers.JSONError("You cannot like a book that you haven't purchased", response);
						return;
					}

					Like.find(user_id, ebook_id, conn);
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					Helpers.JSONError("Already liked this book", response);
				} catch (NoSuchLike e) {
					// good, we can like this book
					Like like = new Like();
					like.user_id = user_id;
					like.ebook_id = ebook_id;
					like.insert(conn);
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

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String body = Helpers.getRequestBody(request);
		FormInput input = new Gson().fromJson(body, FormInput.class);

		if (!input.valid()) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		try {
			Integer user_id = Helpers.getSessionUserId(request), ebook_id = input.ebook_id;
			Connection conn = null;

			try {
				try {
					conn = Helpers.getConnection(request.getServletContext());

					if (!Helpers.hasPurchased(user_id, ebook_id, conn)) {
						response.setStatus(HttpServletResponse.SC_FORBIDDEN);
						Helpers.JSONError("You cannot dislike a book that you haven't purchased", response);
						return;
					}

					Like like = Like.find(user_id, ebook_id, conn);

					like.delete(conn);
					response.setStatus(HttpServletResponse.SC_CREATED);
				} catch (NoSuchLike e) {
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					Helpers.JSONError("You haven't liked this book", response);
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
