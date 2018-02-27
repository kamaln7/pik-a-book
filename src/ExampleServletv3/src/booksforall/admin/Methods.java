package booksforall.admin;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import booksforall.AppConstants;

/**
 * Contains helper functions and methods use in the admin code
 */
public class Methods {

	/**
	 * Fetch the statistics numbers displayed on the admin homepage
	 * 
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
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

	/**
	 * Fetch the purchase history for the past 7 days
	 * 
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
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

	/**
	 * Fetch the best selling e-books
	 * 
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public static Collection<EbookSales> bestSellingEbooks(Connection conn) throws SQLException {
		Statement stmt = conn.createStatement();
		stmt.setMaxRows(AppConstants.BEST_SELLING_COUNT);

		ResultSet rs = stmt.executeQuery(AppConstants.DB_ADMIN_EBOOKS_MOST_PURCHASES);
		ArrayList<EbookSales> ebooks = new ArrayList<EbookSales>();
		while (rs.next()) {
			EbookSales es = new EbookSales();
			es.id = rs.getInt("id");
			es.name = rs.getString("name");
			es.sales = rs.getInt("purchases");

			ebooks.add(es);
		}

		stmt.close();

		return ebooks;
	}

}
