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

	public void insertFromAdminToUser(Integer user_to, Connection conn) throws SQLException {
		PreparedStatement pstmt = conn.prepareStatement(AppConstants.DB_MSG_CREATE_ADMIN_TO_USER);
		pstmt.setInt(1, user_to);
		pstmt.setString(2, this.content);
		pstmt.executeUpdate();
		// commit update
		conn.commit();
		// close statements
		pstmt.close();
	}

	public void insertFromUserToAdmin(Integer user_id, Connection conn) throws SQLException {
		// TODO Auto-generated method stub
		PreparedStatement pstmt = conn.prepareStatement(AppConstants.DB_MSG_CREATE_USER_TO_ADMIN);
		pstmt.setInt(1, user_id);
		pstmt.setString(2, this.content);
		pstmt.executeUpdate();
		// commit update
		conn.commit();
		// close statements
		pstmt.close();

	}

}
