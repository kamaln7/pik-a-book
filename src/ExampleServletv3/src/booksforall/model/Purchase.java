package booksforall.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import booksforall.AppConstants;

public class Purchase {
	public Integer user_id, ebook_id;

	public Purchase() {
	}

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

		return purchase;
	}

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
}
