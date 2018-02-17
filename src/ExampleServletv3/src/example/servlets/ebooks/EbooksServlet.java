package example.servlets.ebooks;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

import javax.naming.NamingException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import example.Helpers;
import example.model.Ebook;

/**
 * Servlet implementation class EbooksServlet
 */
@WebServlet("/ebooks")
public class EbooksServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public EbooksServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @throws IOException
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Connection conn = null;
		try {
			conn = Helpers.getConnection(request.getServletContext());
			Collection<Ebook> ebooks = Ebook.alphabetic(conn);

			Gson gson = new Gson();

			Helpers.JSONType(response);
			response.getWriter().write(gson.toJson(ebooks));
		} catch (SQLException | NamingException e) {
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
