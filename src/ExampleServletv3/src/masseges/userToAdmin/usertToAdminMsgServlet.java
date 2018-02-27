/*
 * user to admin servlet 
 *  get : get all replies from admin
 * post: send msg to admin
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
import booksforall.exceptions.NoSuchUser;
import booksforall.model.Msg;

/**
 * Servlet implementation class adminMsgServlet
 */
@WebServlet("/userToAdmin")
public class usertToAdminMsgServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public class FormInput {
		public Integer user_id, user_to, id;
		public String content;
	}

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public usertToAdminMsgServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param conn1
	 * @throws SQLException
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	/* find msges that the admin sent to user and the user didnt read them yet */

	private ArrayList<Msg> find(Integer user_to, Connection conn, Connection conn1) throws SQLException {
		PreparedStatement pstmt = conn.prepareStatement(AppConstants.DB_MSG_FIND_NEW_FROM_ADMIN);
		pstmt.setInt(1, user_to);
		ResultSet rs = pstmt.executeQuery();
		ArrayList<Msg> msgs = new ArrayList<Msg>();
		while (rs.next()) {
			Msg msg = new Msg();
			msg.id = rs.getInt("id");
			msg.user_to = rs.getInt("user_id");
			msg.user_id = 1;
			msg.username = rs.getString("username");
			msg.content = rs.getString("content");
			msg.timestamp = rs.getTimestamp("timestamp");
			msg.photo = rs.getString("photo");
			msgs.add(msg);
		}

		PreparedStatement pstmt1 = conn1.prepareStatement(AppConstants.DB_UPDATE_MSG1_READ);
		pstmt1.setInt(1, user_to);
		pstmt1.executeUpdate();
		conn1.close();
		return msgs;
	}

	/* get all replies from admin */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		Integer user_id;
		Connection conn = null;
		Connection conn1 = null;
		try {
			user_id = Helpers.getSessionUserId(request);
			try {
				conn = Helpers.getConnection(request.getServletContext());
				conn1 = Helpers.getConnection(request.getServletContext());
			} catch (SQLException | NamingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			ArrayList<Msg> msgs = new ArrayList<Msg>();
			msgs = find(user_id, conn, conn1);
			Gson gson = new Gson();
			Helpers.JSONType(response);
			response.getWriter().write(gson.toJson(msgs));
		} catch (NoSuchUser e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			Helpers.closeConnection(conn);
			Helpers.closeConnection(conn1);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	/* send help request to admin */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String body = Helpers.getRequestBody(request);
		FormInput input = new Gson().fromJson(body, FormInput.class);
		Connection conn = null;
		int user_id;

		try {
			conn = Helpers.getConnection(request.getServletContext());
			user_id = Helpers.getSessionUserId(request);
			Msg msg = new Msg();
			msg.content = input.content;
			msg.user_to = 1;
			msg.user_id = user_id;
			msg.insertFromUserToAdmin(user_id, conn);
			response.setStatus(HttpServletResponse.SC_CREATED);
		} catch (NamingException | SQLException | NoSuchUser e) {
			Helpers.internalServerError(response, e);
		} finally {
			Helpers.closeConnection(conn);
		}
	}

	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String body = Helpers.getRequestBody(request);
		FormInput input = new Gson().fromJson(body, FormInput.class);
		Connection conn = null;

		try {
			conn = Helpers.getConnection(request.getServletContext());
			Msg.delete(input.id, conn);
		} catch (NamingException | SQLException e) {
			Helpers.internalServerError(response, e);
		} finally {
			Helpers.closeConnection(conn);
		}

	}
}
