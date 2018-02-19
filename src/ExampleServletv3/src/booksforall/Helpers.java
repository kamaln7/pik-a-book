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

import booksforall.model.NoSuchPurchase;
import booksforall.model.Purchase;

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

	public static Integer getSessionUserId(HttpServletRequest request) {
		HttpSession session = request.getSession();
		Object uId = session.getAttribute("user_id");
		if (uId == null) {
			return null;
		}

		return (Integer) uId;
	}

	public static Boolean hasPurchased(Integer user_id, Integer ebook_id, Connection conn) throws SQLException {
		try {
			Purchase.find(user_id, ebook_id, conn);
			return true;
		} catch (NoSuchPurchase e) {
			return false;
		}
	}
}
