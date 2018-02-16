package example;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.stream.Collectors;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;

import com.google.gson.JsonObject;

public class Helpers {
	public static void JSONError(String message, ServletResponse response) throws IOException {
		JsonObject o = new JsonObject();
		o.addProperty("error", true);
		o.addProperty("message", message);

		response.setContentType("application/json");
		response.getWriter().write(o.toString());
	}

	public static Connection getConnection(ServletContext cntx) throws SQLException, NamingException {
		Context context;
		context = new InitialContext();
		BasicDataSource ds = (BasicDataSource) context
				.lookup(cntx.getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
		return ds.getConnection();
	}

	public static String getRequestBody(ServletRequest request) throws IOException {
		return request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
	}
}
