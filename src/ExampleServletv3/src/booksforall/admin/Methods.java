package booksforall.admin;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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

}
