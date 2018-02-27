package booksforall.servlets.messages;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import booksforall.AppConstants;
import booksforall.Helpers;
import booksforall.model.Msg;

/**
 * Servlet implementation class userConversation
 */
@WebServlet("/userConversation/*")
public class userConversation extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public class FormInput {
		public Integer user_id;
	}

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public userConversation() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */

	private ArrayList<Msg> find(Connection conn, Integer user_id) throws SQLException {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		PreparedStatement pstmt = conn.prepareStatement(AppConstants.DB_FIND_All_USER_MSG);
		pstmt.setInt(1, user_id);
		ResultSet rs = pstmt.executeQuery();
		ArrayList<Msg> msgs = new ArrayList<Msg>();
		while (rs.next()) {
			Msg msg = new Msg();
			msg.id = rs.getInt("id");
			msg.user_id = rs.getInt("user_id");
			msg.user_to = rs.getInt("user_to");
			msg.username = rs.getString("username");
			msg.content = rs.getString("content");
			msg.timestamp = rs.getTimestamp("timestamp");
			msg.photo = rs.getString("photo");
			msgs.add(msg);
		}
		pstmt.close();
		return msgs;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String pathInfo = request.getPathInfo();
		String[] pathParts = pathInfo.split("/");
		if (pathParts[1] == "") {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		Integer user_id = Integer.parseInt(pathParts[1]);

		Connection conn = null;
		try {
			try {
				conn = Helpers.getConnection(request.getServletContext());
			} catch (SQLException | NamingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			ArrayList<Msg> msgs = new ArrayList<Msg>();
			msgs = find(conn, user_id);
			Gson gson = new Gson();
			Helpers.JSONType(response);
			response.getWriter().write(gson.toJson(msgs));

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			Helpers.closeConnection(conn);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String pathInfo = request.getPathInfo();
		String[] pathParts = pathInfo.split("/");
		if (pathParts[1] == "") {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		Integer user_id = Integer.parseInt(pathParts[1]);
		Connection conn = null;
		try {
			conn = Helpers.getConnection(request.getServletContext());
			Msg.updateMsg(conn, user_id);
			response.setStatus(HttpServletResponse.SC_CREATED);
		} catch (NamingException | SQLException e) {
			Helpers.internalServerError(response, e);
		} finally {
			Helpers.closeConnection(conn);
		}
	}
}
