package booksforall.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import booksforall.AppConstants;
import booksforall.exceptions.NoSuchEbook;
import booksforall.exceptions.NoSuchPurchase;

public class Ebook {
	public String name, path, description, price;
	public Integer id;
	public Collection<Like> likes;
	public Collection<Review> reviews;
	public Boolean has_purchased = false;

	public Ebook() {
	}

	public Ebook(String name, String path, String description, String price) {
		this.name = name;
		this.path = path;
		this.description = description;
		this.price = price;
	}

	/**
	 * Find an ebook by its id
	 * 
	 * @param id
	 * @param conn
	 * @return
	 * @throws SQLException
	 * @throws NoSuchEbook
	 */
	public static Ebook find(Integer id, Connection conn) throws SQLException, NoSuchEbook {
		Ebook ebook = new Ebook();

		PreparedStatement pstmt = conn.prepareStatement(AppConstants.DB_EBOOK_BYID);
		pstmt.setInt(1, id);

		ResultSet rs = pstmt.executeQuery();

		if (!rs.next()) {
			throw new NoSuchEbook();
		}

		ebook.id = rs.getInt("id");
		ebook.name = rs.getString("name");
		ebook.path = rs.getString("path");
		ebook.price = rs.getString("price");
		ebook.description = rs.getString("description");

		return ebook;
	}

	/**
	 * Insert an ebook object into the database
	 * 
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public Integer insert(Connection conn) throws SQLException {
		PreparedStatement pstmt = conn.prepareStatement(AppConstants.DB_EBOOK_CREATE, Statement.RETURN_GENERATED_KEYS);

		pstmt.setString(1, this.name);
		pstmt.setString(2, this.path);
		pstmt.setString(3, this.description);
		pstmt.setString(4, this.price);

		pstmt.executeUpdate();

		ResultSet rs = pstmt.getGeneratedKeys();

		if (rs.next()) {
			this.id = rs.getInt(1);
		}

		// commit update
		conn.commit();
		// close statements
		pstmt.close();

		return this.id;
	}

	/**
	 * Loop through a ResultSet and return a collection of corresponding Ebook
	 * objects
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	public static Collection<Ebook> RSToCollection(ResultSet rs) throws SQLException {
		ArrayList<Ebook> ebooks = new ArrayList<Ebook>();

		while (rs.next()) {
			Ebook ebook = new Ebook();
			ebook.id = rs.getInt("id");
			ebook.name = rs.getString("name");
			ebook.path = rs.getString("path");
			ebook.description = rs.getString("description");
			ebook.price = rs.getString("price");

			ebooks.add(ebook);
		}

		return ebooks;
	}

	/**
	 * Fetch all ebooks in the database alphabetically
	 * 
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public static Collection<Ebook> alphabetic(Connection conn) throws SQLException {
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(AppConstants.DB_EBOOK_ALPHABETICAL);

		Collection<Ebook> ebooks = Ebook.RSToCollection(rs);
		stmt.close();

		return ebooks;
	}

	/**
	 * Fetch ebooks in the order that they were inserted (most recent first)
	 * 
	 * @param limit
	 *            Limit result set to X books
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public static Collection<Ebook> latest(Integer limit, Connection conn) throws SQLException {
		Statement stmt = conn.createStatement();
		stmt.setMaxRows(limit);
		ResultSet rs = stmt.executeQuery(AppConstants.DB_EBOOK_LATEST);

		Collection<Ebook> ebooks = Ebook.RSToCollection(rs);
		stmt.close();

		return ebooks;
	}

	/**
	 * Fetch ebooks, ordered by their total sales amount
	 * 
	 * @param limit
	 *            Limit result set to X books
	 * @param conn
	 * @return
	 * @throws SQLException
	 * @throws NoSuchEbook
	 */
	public static Collection<Ebook> bestSelling(Integer limit, Connection conn) throws SQLException, NoSuchEbook {
		Statement stmt = conn.createStatement();
		if (limit != 0)
			stmt.setMaxRows(limit);
		ResultSet rs = stmt.executeQuery(AppConstants.DB_ADMIN_EBOOKS_MOST_PURCHASES);

		ArrayList<Ebook> ebooks = new ArrayList<Ebook>();
		while (rs.next()) {
			ebooks.add(Ebook.find(rs.getInt("id"), conn));
		}
		stmt.close();

		return ebooks;
	}

	/**
	 * Fetch an ebook's likes and insert them into the likes collection in the
	 * object
	 * 
	 * @param conn
	 * @throws SQLException
	 */
	public void getLikes(Connection conn) throws SQLException {
		PreparedStatement pstmt = conn.prepareStatement(AppConstants.DB_LIKE_BYEBOOKID);
		pstmt.setInt(1, this.id);

		ResultSet rs = pstmt.executeQuery();
		ArrayList<Like> likes = new ArrayList<Like>();
		while (rs.next()) {
			Like like = new Like();
			like.ebook_id = this.id;
			like.user_id = rs.getInt("user_id");
			like.user_nickname = rs.getString("user_nickname");
			like.user_username = rs.getString("user_username");

			likes.add(like);
		}

		pstmt.close();
		this.likes = likes;
	}

	/**
	 * Fetch an ebook's reviews and insert them into the reviews collection in the
	 * object
	 * 
	 * @param conn
	 * @throws SQLException
	 */
	public void getReviews(Connection conn, Boolean publishedOnly) throws SQLException {
		PreparedStatement pstmt = conn.prepareStatement(
				publishedOnly ? AppConstants.DB_REVIEW_BYEBOOKID_PUBLISHEDONLY : AppConstants.DB_REVIEW_BYEBOOKID);
		pstmt.setInt(1, this.id);

		ResultSet rs = pstmt.executeQuery();
		ArrayList<Review> reviews = new ArrayList<Review>();
		while (rs.next()) {
			Review review = new Review();
			review.ebook_id = this.id;
			review.user_id = rs.getInt("user_id");
			review.content = rs.getString("content");
			review.is_published = rs.getInt("is_published");
			review.user_username = rs.getString("user_username");
			review.user_nickname = rs.getString("user_nickname");
			review.user_photo = rs.getString("user_photo");

			reviews.add(review);
		}

		pstmt.close();
		this.reviews = reviews;
	}

	/**
	 * check if a user has purchased an ebook
	 * 
	 * @param user_id
	 * @param conn
	 * @throws SQLException
	 */
	public void checkPurchased(Integer user_id, Connection conn) throws SQLException {
		try {
			Purchase.find(user_id, this.id, conn);

			this.has_purchased = true;
		} catch (NoSuchPurchase e) {
			this.has_purchased = false;
		}
	}

	/**
	 * Get all ebooks owned/purchased by a user
	 * 
	 * @param user_id
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public static Collection<Ebook> ownedByUser(Integer user_id, Connection conn) throws SQLException {
		PreparedStatement pstmt = conn.prepareStatement(AppConstants.DB_EBOOK_OWNEDBYUSER);
		pstmt.setInt(1, user_id);

		ResultSet rs = pstmt.executeQuery();
		Collection<Ebook> ebooks = Ebook.RSToCollection(rs);
		pstmt.close();

		for (Ebook ebook : ebooks) {
			ebook.has_purchased = true;
		}

		return ebooks;
	}
}
