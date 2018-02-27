package booksforall.servlets.ebooks;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import booksforall.Helpers;
import booksforall.exceptions.NoSuchUser;
import booksforall.model.Purchase;

/**
 * Servlet implementation class EbookBuy
 */
@WebServlet(name = "EbookPurchase", urlPatterns = { "/ebooks/purchases/*" })
public class EbookPurchase extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public class FormInput {
		public String cc_number, cc_company, cc_expiry_month, cc_expiry_year, fullname, cc_cvv;

		/**
		 * Validate input
		 * 
		 * @return
		 */
		public boolean valid() {
			Boolean v = true;
			Calendar cal = Calendar.getInstance();
			Integer year = Integer.parseInt(cc_expiry_year), month = Integer.parseInt(cc_expiry_month);

			v = v && fullname != null && Helpers.intBetween(1, fullname.length(), 255);
			v = v && cc_number != null && cc_company != null
					&& cc_number.matches("^(((37|34)\\d)|((4\\d{3})|(5[0-5]\\d{2})))(\\d{12})$")
					&& cc_company.equals(Helpers.getCreditCardType(cc_number));
			v = v && cc_company != null && cc_cvv != null && Helpers.validCreditCardCVV(cc_cvv, cc_company);
			v = v && cc_expiry_year != null && cc_expiry_month != null && cal.get(Calendar.YEAR) < year
					&& (cal.get(Calendar.YEAR) == year ? cal.get(Calendar.MONTH) < month : true);
			return v;
		}
	}

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public EbookPurchase() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * Purchase a book
	 * 
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String pathInfo = request.getPathInfo();
		String[] pathParts = pathInfo.split("/");

		if (pathParts[1] == "") {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		String body = Helpers.getRequestBody(request);
		FormInput input = new Gson().fromJson(body, FormInput.class);

		if (!input.valid()) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			Helpers.JSONError("Invalid form data", response);
			return;
		}

		try {
			Integer user_id = Helpers.getSessionUserId(request), ebook_id = Integer.parseInt(pathParts[1]);
			Connection conn = null;

			try {
				conn = Helpers.getConnection(request.getServletContext());

				if (Helpers.hasPurchased(user_id, ebook_id, conn)) {
					response.setStatus(HttpServletResponse.SC_FORBIDDEN);
					Helpers.JSONError("You already own this e-book", response);
					return;
				}

				Purchase purchase = new Purchase();
				purchase.ebook_id = ebook_id;
				purchase.user_id = user_id;
				purchase.insert(conn);

				response.setStatus(HttpServletResponse.SC_CREATED);
				Helpers.JSONObject(response, purchase);
			} catch (NamingException | SQLException e) {
				Helpers.internalServerError(response, e);
			} finally {
				Helpers.closeConnection(conn);
			}
		} catch (NoSuchUser e) {
			Helpers.JSONError("Unauthenticated.", response);
		}
	}

}
