package Application.dal;

import Application.model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Data access object (DAO) class to interact with the underlying Reviewer table in
 * MySQL instance. This is used to store {@link Reviewer} into MySQL instance and 
 * retrieve {@link Reviewer} from MySQL instance.
 */
public class ReviewerDao extends UserDao {
	// Single pattern: instantiation is limited to one object.
	private static ReviewerDao instance = null;
	protected ReviewerDao() {
		super();
	}
	public static ReviewerDao getInstance() {
		if(instance == null) {
			instance = new ReviewerDao();
		}
		return instance;
	}
	/**
	 * 1. public Reviewer create(Reviewer reviewer).
	 */
	public Reviewer create(Reviewer reviewer) throws SQLException {
		// Insert into the superclass table first.
		Integer user_id = reviewer.getUserID();
		if (user_id == null) {
			User user = super.create(new User(reviewer.getusername(), reviewer.getFirstName(), 
					reviewer.getLastName(), reviewer.getemail()));
			user_id = user.getUserID();
			reviewer.setUserID(user.getUserID());
		}
		
		String insertReviewer = "INSERT INTO Reviewer(userID,program) VALUES(?,?);";
		Connection connection = null;
		PreparedStatement insertStmt = null;
		try {
			connection = connectionManager.getConnection();
			insertStmt = connection.prepareStatement(insertReviewer);
			insertStmt.setInt(1, user_id);
			insertStmt.setString(2, reviewer.getPrograms().name());
			insertStmt.executeUpdate();
			return reviewer;
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
	 * 2. public Reviewer getReviewerByUserID(int userID).
	 */
	
	public Reviewer getReviewerByUserID(Integer userID) throws SQLException {
		// To build an Reviewer object, we need the User record, too.
		String selectReviewer =
			"SELECT Reviewer.userID AS userID, username,firstName, lastName, email, program " +
			"FROM Reviewer INNER JOIN User " +
			"  ON Reviewer.userID = User.userID " +
			"WHERE Reviewer.userID=?;";
		Connection connection = null;
		PreparedStatement selectStmt = null;
		ResultSet results = null;
		try {
			connection = connectionManager.getConnection();
			selectStmt = connection.prepareStatement(selectReviewer);
			selectStmt.setInt(1, userID);
			results = selectStmt.executeQuery();
			if(results.next()) {
				Integer resultuserID = results.getInt("userID");
				String username = results.getString("username");
				String firstName = results.getString("firstName");
				String lastName = results.getString("lastName");
				String email = results.getString("email");
				Reviewer.Programs program = Reviewer.Programs.valueOf(results.getString("program"));
				Reviewer reviewer = new Reviewer(resultuserID, username, firstName, lastName, email,program);
				return reviewer;
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
