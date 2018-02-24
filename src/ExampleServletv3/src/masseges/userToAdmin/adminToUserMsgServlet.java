/*
 * 
 * this servlet handles massges that was sent to the admin
 * admin get: open his new msgs
 * admin post: reply to user i
 * 
 * */
package masseges.userToAdmin;

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
 * Servlet implementation class adminMsgServlet
 */
@WebServlet("/adminToUser")
public class adminToUserMsgServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public class FormInput {
		public Integer user_id;
		public Integer to;
		public String content;
	}

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public adminToUserMsgServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @throws SQLException
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	/*
	 * find msges that the users sent to the admin and havn't been readn yet
	 */
	private ArrayList<Msg> find(Connection conn) throws SQLException {
		PreparedStatement pstmt = conn.prepareStatement(AppConstants.DB_MSG_FIND_NEW_TO_ADMIN);
		ResultSet rs = pstmt.executeQuery();
		ArrayList<Msg> msgs = new ArrayList<Msg>();
		if (!rs.next()) {
			Msg msg = new Msg();
			msg.id = 0;
			msg.user_id = 0;
			msg.user_to = 1;
			msg.content = "";
			msg.photo = "";
			msg.timestamp = null;
			msg.username = "";
			msgs.add(msg);
			return msgs;
		}
		while (rs.next()) {
			Msg msg = new Msg();
			msg.id = rs.getInt("id");
			msg.user_id = rs.getInt("user_id");
			msg.user_to = 1;
			msg.username = rs.getString("username");
			msg.content = rs.getString("content");
			msg.timestamp = rs.getTimestamp("timestamp");
			msg.photo = rs.getString("photo");
			msgs.add(msg);
		}
		return msgs;
	}

	/* get all msges from user to admin */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		Connection conn = null;
		try {
			try {
				conn = Helpers.getConnection(request.getServletContext());
			} catch (SQLException | NamingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			ArrayList<Msg> msgs = new ArrayList<Msg>();
			msgs = find(conn);
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

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	/* send replies to user i */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String body = Helpers.getRequestBody(request);
		FormInput input = new Gson().fromJson(body, FormInput.class);
		Connection conn = null;

		try {
			conn = Helpers.getConnection(request.getServletContext());
			Msg msg = new Msg();
			msg.content = input.content;
			msg.user_to = input.to;
			msg.user_id = 1;
			msg.insertFromAdminToUser(input.to, conn);

			response.setStatus(HttpServletResponse.SC_CREATED);
			Helpers.JSONObject(response, msg);
		} catch (NamingException | SQLException e) {
			Helpers.internalServerError(response, e);
		} finally {
			Helpers.closeConnection(conn);
		}
	}
}