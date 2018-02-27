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

/**
 * Contains Helper functions used throughout the app
 */
public class Helpers {

	/**
	 * Set the response Content-Type to application/json
	 * 
	 * @param response
	 */
	public static void JSONType(ServletResponse response) {
		response.setContentType("application/json");
	}

	/**
	 * Return a JSON error object as the response
	 * 
	 * @param message
	 *            Error message string
	 * @param response
	 * @throws IOException
	 */
	public static void JSONError(String message, ServletResponse response) throws IOException {
		JsonObject o = new JsonObject();
		o.addProperty("error", true);
		o.addProperty("message", message);

		Helpers.JSONType(response);
		response.getWriter().write(o.toString());
	}

	/**
	 * Return a JSON object as the response
	 * 
	 * @param response
	 * @param obj
	 *            Object to serialize to JSON
	 * @throws IOException
	 */
	public static void JSONObject(ServletResponse response, Object obj) throws IOException {
		Helpers.JSONType(response);
		Gson gson = new Gson();
		response.getWriter().write(gson.toJson(obj));
	}

	/**
	 * Return an SQL Connection using the defined data source
	 * 
	 * @param cntx
	 * @return
	 * @throws SQLException
	 * @throws NamingException
	 */
	public static Connection getConnection(ServletContext cntx) throws SQLException, NamingException {
		Context context;
		context = new InitialContext();
		BasicDataSource ds = (BasicDataSource) context
				.lookup(cntx.getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
		return ds.getConnection();
	}

	/**
	 * Return the request body as a string
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public static String getRequestBody(HttpServletRequest request) throws IOException {
		return request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
	}

	/**
	 * Return a generic internal server error JSON error object and print the
	 * exception's stack trace
	 * 
	 * @param response
	 * @param e
	 *            Exception whose stack trace we should print
	 * @throws IOException
	 */
	public static void internalServerError(HttpServletResponse response, Exception e) throws IOException {
		e.printStackTrace();
		response.setStatus(500);
		Helpers.JSONError("A server error occured", response);
	}

	/**
	 * Close an SQL connection
	 * 
	 * @param conn
	 */
	public static void closeConnection(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Fetch the user id from the session if exists
	 * 
	 * @param request
	 * @return User id
	 * @throws NoSuchUser
	 */
	public static Integer getSessionUserId(HttpServletRequest request) throws NoSuchUser {
		HttpSession session = request.getSession();
		Object uId = session.getAttribute("user_id");
		if (uId == null) {
			throw new NoSuchUser();
		}

		return (Integer) uId;
	}

	/**
	 * Fetch the User object associated with the session if exists
	 * 
	 * @param request
	 * @param conn
	 * @return User object
	 * @throws SQLException
	 * @throws NoSuchUser
	 */
	public static User getSessionUser(HttpServletRequest request, Connection conn) throws SQLException, NoSuchUser {
		Integer userId = Helpers.getSessionUserId(request);

		return User.find(userId, conn);
	}

	/**
	 * Check if a user has purchased an ebook
	 * 
	 * @param user_id
	 * @param ebook_id
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public static Boolean hasPurchased(Integer user_id, Integer ebook_id, Connection conn) throws SQLException {
		try {
			Purchase.find(user_id, ebook_id, conn);
			return true;
		} catch (NoSuchPurchase e) {
			return false;
		}
	}

	/**
	 * Check if an int is between [min,max]
	 * 
	 * @param min
	 * @param val
	 * @param max
	 * @return
	 */
	public static Boolean intBetween(Integer min, Integer val, Integer max) {
		return val >= min && val <= max;
	}

	/**
	 * Validate if a string is an email address
	 * 
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email) {
		return email.matches("^.*@.*\\..*$");
	}

	/**
	 * Validate if a string is a street
	 * 
	 * @param street
	 * @return
	 */
	public static boolean isStreet(String street) {
		return street.matches("^[A-Za-z\\s]{3,500}$");
	}

	/**
	 * Validate if a string is a street number
	 * 
	 * @param streetNumber
	 * @return
	 */
	public static boolean isStreetNumber(String streetNumber) {
		return streetNumber.matches("^[1-9]|(\\d{2,})$");
	}

	/**
	 * Validate if a string is a city
	 * 
	 * @param city
	 * @return
	 */
	public static boolean isCity(String city) {
		return city.matches("^[a-zA-Z\\s]+$");
	}

	/**
	 * Validate if a string is a ZIP code
	 * 
	 * @param zip
	 * @return
	 */
	public static boolean isPostalCode(String zip) {
		return zip.matches("^[0-9]{7}$");
	}

	/**
	 * Validate if a string is a telephone number
	 * 
	 * @param telephone
	 * @return
	 */
	public static boolean isTelephone(String telephone) {
		return telephone.matches("^0((5([0-9]))|([23489]))[0-9]{7}$");
	}

	/**
	 * Get the credit card company type from the cc number
	 * 
	 * @param number
	 * @return
	 */
	public static String getCreditCardType(String number) {
		if (number.startsWith("4")) {
			return "visa";
		} else if (number.startsWith("34") || number.startsWith("37")) {
			return "amex";
		} else if (number.startsWith("5") && Helpers.intBetween(0, Integer.parseInt(number.substring(1, 2)), 5)) {
			return "mastercard";
		} else {
			return null;
		}
	}

	/**
	 * Check if a CVV matches the credit card company type
	 * 
	 * @param cvv
	 * @param type
	 * @return
	 */
	public static Boolean validCreditCardCVV(String cvv, String type) {
		switch (type) {
		case "visa":
			return cvv.length() == 3;
		case "amex":
			return cvv.length() == 4;
		case "mastercard":
			return cvv.length() == 3;
		default:
			return false;
		}
	}

	/**
	 * Return the session;'s user id
	 * 
	 * @param request
	 * @return
	 */
	public static Integer getSessionUserIdmsg(HttpServletRequest request) {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();
		Object uId = session.getAttribute("user_id");
		return (Integer) uId;
	}
}
