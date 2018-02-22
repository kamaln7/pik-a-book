package booksforall.admin;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import booksforall.AppConstants;

public class Methods {

	public static Numbers statsNumbers(Connection conn) throws SQLException {
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(AppConstants.DB_ADMIN_NUMBERS);

		Numbers numbers = new Numbers();
		rs.next();

		numbers.users = rs.getInt("users");
		numbers.pending_reviews = rs.getInt("pending_reviews");
		numbers.ebooks = rs.getInt("ebooks");
		numbers.sales = rs.getInt("purchases");

		stmt.close();

		return numbers;
	}

	public static Collection<PurchaseHistory> purchasesLast7Days(Connection conn) throws SQLException {
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(AppConstants.DB_ADMIN_PURCHASES_LAST7);

		ArrayList<PurchaseHistory> purchases = new ArrayList<PurchaseHistory>();

		while (rs.next()) {
			PurchaseHistory ph = new PurchaseHistory();
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.DAY_OF_MONTH, rs.getInt("day"));
			ph.cal = cal;
			ph.purchases = rs.getInt("purchases");

			purchases.add(ph);
		}

		stmt.close();

		return purchases;
	}

}
