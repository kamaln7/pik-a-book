package booksforall.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import booksforall.AppConstants;

public class Msg {
	public String content;
	public String username;
	public Integer user_id, user_to;
	public Integer id;
	public Timestamp timestamp;
	public String photo;

	public Msg() {
	}

	/**
	 * Fetch messages sent from admin to user
	 * 
	 * @param user_id
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<Msg> findNewMsgToUsers(Integer user_id, Connection conn) throws SQLException {
		PreparedStatement pstmt = conn.prepareStatement(AppConstants.DB_MSG_FIND_NEW_FROM_ADMIN);
		pstmt.setInt(1, this.user_id);
		ResultSet rs = pstmt.executeQuery();
		ArrayList<Msg> msgs = new ArrayList<Msg>();
		while (rs.next()) {
			Msg msg = new Msg();
			msg.id = rs.getInt("id");
			msg.user_id = rs.getInt("user_id");
			msg.user_to = 1;
			msg.username = rs.getString("user_username");
			msg.content = rs.getString("content");
			msg.timestamp = rs.getTimestamp("timestamp");
			msg.photo = rs.getString("photo");
			msgs.add(msg);
		}
		return msgs;
	}

	/**
	 * Send a message from an admin to user
	 * 
	 * @param conn
	 * @throws SQLException
	 */
	public void insertFromAdminToUser(Connection conn) throws SQLException {
		PreparedStatement pstmt = conn.prepareStatement(AppConstants.DB_MSG_CREATE_ADMIN_TO_USER);
		pstmt.setInt(1, this.user_to);
		pstmt.setString(2, this.content);
		pstmt.executeUpdate();
		// commit update
		conn.commit();
		// close statements
		pstmt.close();
	}

	/**
	 * Send a message from a user to admin
	 * 
	 * @param user_id
	 * @param conn
	 * @throws SQLException
	 */
	public void insertFromUserToAdmin(Integer user_id, Connection conn) throws SQLException {
		// TODO Auto-generated method stub
		PreparedStatement pstmt = conn.prepareStatement(AppConstants.DB_MSG_CREATE_USER_TO_ADMIN);
		pstmt.setInt(1, this.user_id);
		pstmt.setString(2, this.content);
		pstmt.executeUpdate();
		// commit update
		conn.commit();
		// close statements
		pstmt.close();

	}

	/**
	 * Delete a message by its id
	 * 
	 * @param id2
	 * @param conn
	 * @throws SQLException
	 */
	public static void delete(Integer id2, Connection conn) throws SQLException {
		PreparedStatement pstmt = conn.prepareStatement(AppConstants.DB_MSG_DELETE);
		pstmt.setInt(1, id2);
		pstmt.executeUpdate();
		// commit update
		conn.commit();
		// close statements
		pstmt.close(); // TODO Auto-generated method stub

	}

	/**
	 * Set a message to read
	 * 
	 * @param conn
	 * @param user_id2
	 * @throws SQLException
	 */
	public static void updateMsg(Connection conn, Integer user_id2) throws SQLException {
		// TODO Auto-generated method stub
		PreparedStatement pstmt = conn.prepareStatement(AppConstants.DB_UPDATE_MSG_READ);
		pstmt.setInt(1, user_id2);
		pstmt.executeUpdate();
		// commit update
		conn.commit();
		// close statements
		pstmt.close(); // TODO Auto-generated method stub

	}

}
