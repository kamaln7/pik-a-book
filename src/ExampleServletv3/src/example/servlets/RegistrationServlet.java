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
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.*;

@WebServlet("/Registration")
public class RegistrationServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		final ServletContext sc = getServletContext();
		sc.getRequestDispatcher("/pages/auth/register.html").forward(request, response);
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		final String username = request.getParameter("username");
		final String password = request.getParameter("password");
		final String email = request.getParameter("email");
		final String city = request.getParameter("city");
		final String street = request.getParameter("street");
		final String nickname = request.getParameter("nickname");
		final String bio = request.getParameter("bio");
		final String photoURL = request.getParameter("photo");
		final Integer zip = Integer.parseInt(request.getParameter("zip"));
		final Integer telephone = Integer.parseInt(request.getParameter("pre") +
				request.getParameter("phone"));
		final Integer streetNumber = Integer.parseInt(request.getParameter("sNumber"));
		
		if ( !email.isEmpty() && !username.isEmpty() &&!password.isEmpty() &&!city.isEmpty()
				&& !street.isEmpty()) {
			response.setStatus(HttpServletResponse.SC_OK);
		} else {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		}

		// just to illustrate the use of Json in Servlets, we return the input to the
		// client
		final JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("username",username);
		jsonObject.addProperty("email",email);
		jsonObject.addProperty("password",password);
		jsonObject.addProperty("city",city);
		jsonObject.addProperty("street",street);
		jsonObject.addProperty("streetNumber",streetNumber);
		jsonObject.addProperty("zip",zip);
		jsonObject.addProperty("telephone",telephone);
		jsonObject.addProperty("nickname",nickname);
		jsonObject.addProperty("bio",bio);
		jsonObject.addProperty("photo",photoURL);		


		response.setContentType("application/json");
		System.out.println(username);
		// Get the printwriter object from response to write the required json object to
		// the output stream
		final PrintWriter out = response.getWriter();
		// Assuming your json object is **jsonObject**, perform the following, it will
		// return your json object
		out.print(jsonObject.toString());
	}
}
