/* BEGIN_COPYRIGHT
 *
 * IBM Confidential
 * OCO Source Materials
 *
 * (C) Copyright IBM Corp. 2011, 2017 All Rights Reserved.
 *
 * The source code for this program is not published or otherwise
 * divested of its trade secrets, irrespective of what has been
 * deposited with the U.S. Copyright Office.
 *
 * END_COPYRIGHT
 */

package example.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.stream.Collectors;

import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import example.Helpers;
import example.model.User;

@WebServlet(urlPatterns = { "/auth/registration" })
public class RegistrationServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		final ServletContext sc = getServletContext();
		sc.getRequestDispatcher("./pages/auth/register.html").forward(request, response);
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
		JsonObject input = new JsonParser().parse(body).getAsJsonObject();
		String username = input.get("username").getAsString(), password = input.get("password").getAsString();
		String email = input.get("email").getAsString(), fullname = input.get("fullname").getAsString();
		String telephone = input.get("telephone").getAsString(), city = input.get("city").getAsString();
		String zip = input.get("zip").getAsString(), street = input.get("street").getAsString();
		Integer street_number = input.get("street_number").getAsInt();
		String photo = "";
		String nickname = "";
		String bio = "";
		if (input.get("bio") != null) {
			bio = input.get("bio").getAsString();
		}
		if (input.get("nickname") != null) {
			nickname = input.get("nickname").getAsString();
		}
		if (input.get("photo") != null) {
			photo = input.get("photo").getAsString();
		}
		try {
			Connection conn = Helpers.getConnection(request.getServletContext());

			User user = new User(username, email, password, fullname, street, city, zip, telephone, nickname, bio,
					photo, false, street_number);
			Integer userId = user.insert(conn);
			JsonObject o = new JsonObject();
			o.addProperty("id", userId);
			o.addProperty("username", user.username);
			o.addProperty("email", user.email);
			o.addProperty("password", user.password);
			o.addProperty("fullname", user.fullname);
			o.addProperty("street", user.street);
			o.addProperty("steet_number", user.street_number);
			o.addProperty("city", user.city);
			o.addProperty("zip", user.zip);
			o.addProperty("telephone", user.telephone);
			o.addProperty("nickname", user.nickname);
			o.addProperty("bio", user.bio);
			o.addProperty("photo", user.photo);
			o.addProperty("is_admin", user.is_admin);
			response.getWriter().write(o.toString());

		} catch (NamingException | SQLException e) {
			e.printStackTrace();
			response.setStatus(500);
			Helpers.JSONError("A server error occured", response);
		}

		/*
		 * final String username = request.getParameter("username"); final String
		 * password = request.getParameter("password"); final String email =
		 * request.getParameter("email"); final String city =
		 * request.getParameter("city"); final String street =
		 * request.getParameter("street"); final String nickname =
		 * request.getParameter("nickname"); final String bio =
		 * request.getParameter("bio"); final String photoURL =
		 * request.getParameter("photo"); final Integer zip =
		 * Integer.parseInt(request.getParameter("zip")); final Integer telephone =
		 * Integer.parseInt(request.getParameter("phone")); final Integer streetNumber =
		 * Integer.parseInt(request.getParameter("sNumber"));
		 * 
		 * if (!email.isEmpty() && !username.isEmpty() && !password.isEmpty() &&
		 * !city.isEmpty() && !street.isEmpty()) {
		 * response.setStatus(HttpServletResponse.SC_OK); } else {
		 * response.setStatus(HttpServletResponse.SC_FORBIDDEN); }
		 * 
		 * // just to illustrate the use of Json in Servlets, we return the input to the
		 * // client final JsonObject jsonObject = new JsonObject();
		 * jsonObject.addProperty("username", username); jsonObject.addProperty("email",
		 * email); jsonObject.addProperty("password", password);
		 * jsonObject.addProperty("city", city); jsonObject.addProperty("street",
		 * street); jsonObject.addProperty("streetNumber", streetNumber);
		 * jsonObject.addProperty("zip", zip); jsonObject.addProperty("telephone",
		 * telephone); jsonObject.addProperty("nickname", nickname);
		 * jsonObject.addProperty("bio", bio); jsonObject.addProperty("photo",
		 * photoURL);
		 * 
		 * response.setContentType("application/json"); // Get the printwriter object
		 * from response to write the required json object to // the output stream final
		 * PrintWriter out = response.getWriter(); // Assuming your json object is
		 * **jsonObject**, perform the following, it will // return your json object
		 * out.print(jsonObject.toString());
		 */
	}
}
