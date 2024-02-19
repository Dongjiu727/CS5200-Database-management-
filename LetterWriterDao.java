package Application.dal;

import Application.model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Data access object (DAO) class to interact with the underlying LetterWriter table in your
 * MySQL instance. This is used to store {@link LetterWriter} into your MySQL instance and 
 * retrieve {@link LetterWriter} from MySQL instance.
 */
public class LetterWriterDao extends UserDao {
	// Single pattern: instantiation is limited to one object.
	private static LetterWriterDao instance = null;
	protected LetterWriterDao() {
		super();
	}
	public static LetterWriterDao getInstance() {
		if(instance == null) {
			instance = new LetterWriterDao();
		}
		return instance;
	}
	/**
	 * 1. public LetterWriter create(LetterWriter letterWriter.
	 */
	public LetterWriter create(LetterWriter letterWriter) throws SQLException {
		// Insert into the superclass table first.
		Integer user_id = letterWriter.getUserID();
		if (user_id == null) {
			User user = super.create(new User(letterWriter.getusername(), letterWriter.getFirstName(), 
					letterWriter.getLastName(), letterWriter.getemail()));
			user_id = user.getUserID();
			letterWriter.setUserID(user.getUserID());
		}

		String insertLetterWriter = "INSERT INTO LetterWriter(userID,university) VALUES(?,?);";
		Connection connection = null;
		PreparedStatement insertStmt = null;
		try {
			connection = connectionManager.getConnection();
			insertStmt = connection.prepareStatement(insertLetterWriter);
			insertStmt.setInt(1, user_id);
			insertStmt.setString(2, letterWriter.getUniversity());
			insertStmt.executeUpdate();
			return letterWriter;
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(connection != null) {
				connection.close();
			}
			if(insertStmt != null) {
				insertStmt.close();
			}
		}
	}
	
	/**
	 * 2. public LetterWriter getLetterWriterByUserID(int userID).
	 */
	
	public LetterWriter getLetterWriterByUserID(Integer userID) throws SQLException {
		// To build an LetterWriter object, we need the User record, too.
		String selectLetterWriter =
			"SELECT LetterWriter.userID AS userID, username,firstName, lastName, email, university " +
			"FROM LetterWriter INNER JOIN User " +
			"  ON LetterWriter.userID = User.userID " +
			"WHERE LetterWriter.userID=?;";
		Connection connection = null;
		PreparedStatement selectStmt = null;
		ResultSet results = null;
		try {
			connection = connectionManager.getConnection();
			selectStmt = connection.prepareStatement(selectLetterWriter);
			selectStmt.setInt(1, userID);
			results = selectStmt.executeQuery();
			if(results.next()) {
				Integer resultuserID = results.getInt("userID");
				String username = results.getString("username");
				String firstName = results.getString("firstName");
				String lastName = results.getString("lastName");
				String email = results.getString("email");
				String university = results.getString("university");
				LetterWriter letterWriter = new LetterWriter(resultuserID, username, firstName, lastName, email, university);
				return letterWriter;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(connection != null) {
				connection.close();
			}
			if(selectStmt != null) {
				selectStmt.close();
			}
			if(results != null) {
				results.close();
			}
		}
		return null;
	}
}
	