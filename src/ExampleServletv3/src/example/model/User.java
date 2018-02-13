package example.model;

/**
 * A simple bean to hold data
 */
public class User {
	public String username, email, password, address_fullname, address_street_name, address_street_number, address_city,
			address_zip, telephone, nickname, bio, photo_url, timestamp;
	public Integer id, is_admin;

	public static User Find(Integer id) {
		User user = new User();

		// SQL id

		return user;
	}

	public void Insert() {
		// insert
	}
}