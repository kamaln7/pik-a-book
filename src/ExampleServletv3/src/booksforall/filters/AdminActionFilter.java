package booksforall.filters;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.NamingException;
import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import booksforall.Helpers;
import booksforall.exceptions.NoSuchUser;
import booksforall.model.User;

/**
 * Servlet Filter implementation class AdminActionFilter
 */
@WebFilter(dispatcherTypes = { DispatcherType.REQUEST, DispatcherType.FORWARD }, urlPatterns = { "/admin/*" })
public class AdminActionFilter implements Filter {

	/**
	 * Default constructor.
	 */
	public AdminActionFilter() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		Connection conn = null;
		try {
			conn = Helpers.getConnection(request.getServletContext());
			User user = Helpers.getSessionUser((HttpServletRequest) request, conn);
			if (!user.is_admin) {
				throw new NoSuchUser();
			}

			chain.doFilter(request, response);
		} catch (NoSuchUser e) {
			Helpers.JSONError("Unauthenticated.", response);
		} catch (NamingException | SQLException e) {
			Helpers.internalServerError((HttpServletResponse) response, e);
		} finally {
			Helpers.closeConnection(conn);
		}
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
