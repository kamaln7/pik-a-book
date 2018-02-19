package booksforall;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.stream.Collectors;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import booksforall.exceptions.NoSuchPurchase;
import booksforall.exceptions.NoSuchUser;
import booksforall.model.Purchase;
import booksforall.model.User;

public class Helpers {
	public static void JSONType(ServletResponse response) {
		response.setContentType("application/json");
	}

	public static void JSONError(String message, ServletResponse response) throws IOException {
		JsonObject o = new JsonObject();
		o.addProperty("error", true);
		o.addProperty("message", message);

		Helpers.JSONType(response);
		response.getWriter().write(o.toString());
	}

	public static void JSONObject(ServletResponse response, Object obj) throws IOException {
		Helpers.JSONType(response);
		Gson gson = new Gson();
		response.getWriter().write(gson.toJson(obj));
	}

	public static Connection getConnection(ServletContext cntx) throws SQLException, NamingException {
		Context context;
		context = new InitialContext();
		BasicDataSource ds = (BasicDataSource) context
				.lookup(cntx.getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
		return ds.getConnection();
	}

	public static String getRequestBody(HttpServletRequest request) throws IOException {
		return request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
	}

	public static void internalServerError(HttpServletResponse response, Exception e) throws IOException {
		e.printStackTrace();
		response.setStatus(500);
		Helpers.JSONError("A server error occured", response);
	}

	public static void closeConnection(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static Integer getSessionUserId(HttpServletRequest request) throws NoSuchUser {
		HttpSession session = request.getSession();
		Object uId = session.getAttribute("user_id");
		if (uId == null) {
			throw new NoSuchUser();
		}

		return (Integer) uId;
	}

	public static User getSessionUser(HttpServletRequest request, Connection conn) throws SQLException, NoSuchUser {
		Integer userId = Helpers.getSessionUserId(request);

		return User.find(userId, conn);
	}

	public static Boolean hasPurchased(Integer user_id, Integer ebook_id, Connection conn) throws SQLException {
		try {
			Purchase.find(user_id, ebook_id, conn);
			return true;
		} catch (NoSuchPurchase e) {
			return false;
		}
	}

	public static Boolean intBetween(Integer min, Integer val, Integer max) {
		return val >= min && val <= max;
	}

	public static boolean isEmail(String email) {
		return email.matches("^.*@.*\\..*$");
	}

	public static boolean isStreet(String street) {
		return street.matches("^[A-Za-z\\s]{3,500}$");
	}

	public static boolean isStreetNumber(String streetNumber) {
		return streetNumber.matches("^[1-9]|(\\d{2,})$");
	}

	public static boolean isCity(String city) {
		return city.matches("^[a-zA-Z\\s]+$");
	}

	public static boolean isPostalCode(String zip) {
		return zip.matches("^[0-9]{7}$");
	}

	public static boolean isTelephone(String telephone) {
		return telephone.matches("^0((5([0-9]))|([23489]))[0-9]{7}$");
	}
}
