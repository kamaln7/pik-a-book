package booksforall.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;

import booksforall.AppConstants;
import booksforall.exceptions.NoSuchPurchase;

public class Purchase {
	public Integer user_id, ebook_id;
	public String user_username, user_nickname, ebook_name, user_photo, ebook_price;
	public Timestamp timestamp;

	public Purchase() {
	}

	/**
	 * Find a purchase by its user id and ebook id - check if user has purchased
	 * ebook
	 * 
	 * @param user_id
	 * @param ebook_id
	 * @param conn
	 * @return
	 * @throws SQLException
	 * @throws NoSuchPurchase
	 */
	public static Purchase find(Integer user_id, Integer ebook_id, Connection conn)
			throws SQLException, NoSuchPurchase {
		PreparedStatement pstmt = conn.prepareStatement(AppConstants.DB_PURCHASE_FIND);

		pstmt.setInt(1, ebook_id);
		pstmt.setInt(2, user_id);

		ResultSet rs = pstmt.executeQuery();

		if (!rs.next()) {
			throw new NoSuchPurchase();
		}

		Purchase purchase = new Purchase();
		purchase.ebook_id = rs.getInt("ebook_id");
		purchase.user_id = rs.getInt("user_id");
		purchase.timestamp = rs.getTimestamp("timestamp");

		return purchase;
	}

	/**
	 * Insert purchase into the database
	 * 
	 * @param conn
	 * @throws SQLException
	 */
	public void insert(Connection conn) throws SQLException {
		PreparedStatement pstmt = conn.prepareStatement(AppConstants.DB_PURCHASE_CREATE);

		pstmt.setInt(1, this.ebook_id);
		pstmt.setInt(2, this.user_id);

		pstmt.executeUpdate();

		// commit update
		conn.commit();
		// close statements
		pstmt.close();
	}

	/**
	 * Update the timestamp property of the current Purchase in the database
	 * 
	 * @param conn
	 * @throws SQLException
	 */
	public void setTimestamp(Connection conn) throws SQLException {
		PreparedStatement pstmt = conn.prepareStatement(AppConstants.DB_PURCHASE_SETTIMESTAMP);

		pstmt.setTimestamp(1, this.timestamp);
		pstmt.setInt(2, this.ebook_id);
		pstmt.setInt(3, this.user_id);

		pstmt.executeUpdate();

		// commit update
		conn.commit();
		// close statements
		pstmt.close();
	}

	/**
	 * Loop through a ResultSet and return a collection of corresponding Purchase
	 * objects
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private static Collection<Purchase> RSToCollection(ResultSet rs) throws SQLException {
		ArrayList<Purchase> purchases = new ArrayList<Purchase>();

		while (rs.next()) {
			Purchase purchase = new Purchase();
			purchase.user_id = rs.getInt("user_id");
			purchase.ebook_id = rs.getInt("ebook_id");
			purchase.user_nickname = rs.getString("user_nickname");
			purchase.user_username = rs.getString("user_username");
			purchase.ebook_name = rs.getString("ebook_name");
			purchase.user_photo = rs.getString("user_photo");
			purchase.timestamp = rs.getTimestamp("timestamp");
			purchase.ebook_price = rs.getString("ebook_price");
			purchases.add(purchase);
		}

		return purchases;
	}

	/**
	 * Return recent purchases in the system
	 * 
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public static Collection<Purchase> getRecentPurchases(Connection conn) throws SQLException {
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(AppConstants.DB_GET_RECENT_PURCHASES);
		Collection<Purchase> purchases = Purchase.RSToCollection(rs);
		stmt.close();

		return purchases;
		// TODO Auto-generated method stub
	}

}
