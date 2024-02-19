package Application.dal;

import Application.model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * Data access object (DAO) class to interact with the underlying User table in MySQL
 * instance. This is used to store {@link User} into MySQL instance and retrieve 
 * {@link User} from MySQL instance.
 */
public class UserDao {
	protected ConnectionManager connectionManager;
	
	// Single pattern: instantiation is limited to one object.
	private static UserDao instance = null;
	protected UserDao() {
		connectionManager = new ConnectionManager();
	}
	public static UserDao getInstance() {
		if(instance == null) {
			instance = new UserDao();
		}
		return instance;
	}

	/**
	 * 1.public User create(User user).
	 */
	public User create(User user) throws SQLException {
		String insertUser = "INSERT INTO User(username,FirstName,LastName,email) VALUES(?,?,?,?);";
		Connection connection = null;
		PreparedStatement insertStmt = null;
		ResultSet resultKey = null;
		try {
			connection = connectionManager.getConnection();
			// Validate user data (example: check for null or empty strings)
			if (user.getusername() == null || user.getusername().isEmpty()) {
			    throw new IllegalArgumentException("Username cannot be null or empty.");
			}
			// User has an auto-generated key. So we want to retrieve that key.
			insertStmt = connection.prepareStatement(insertUser,Statement.RETURN_GENERATED_KEYS);
						
			// Set parameters for the insert statement
	        insertStmt.setString(1, user.getusername());
	        insertStmt.setString(2, user.getFirstName());
	        insertStmt.setString(3, user.getLastName());
	        
	        // Check and set email
	        if (user.getemail() != null) {
	            insertStmt.setString(4, user.getemail());
	        } else {
	            insertStmt.setNull(4, java.sql.Types.VARCHAR);
	        }
	   
			insertStmt.executeUpdate();
			
			// Retrieve the auto-gereated key and set it, so it can be used by the caller.
			resultKey = insertStmt.getGeneratedKeys();
			Integer userID = -1;
			if(resultKey.next()) {
				userID = resultKey.getInt(1);
			} else {
				throw new SQLException("Unable to retrieve auto-generated key.");
			}
			user.setUserID(userID);
			
			return user;
			
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
	 * 2.public User getUserByUserID(Integer userID).
	 * Given that userID serves as the primary key, the method returns a singular User instance.
	 */
	public User getUserByUserID(Integer userID) throws SQLException {
		String selectUser = "SELECT userID,Username,firstName,lastName,email FROM User WHERE userID=?;";
		Connection connection = null;
		PreparedStatement selectStmt = null;
		ResultSet results = null;
		try {
			connection = connectionManager.getConnection();
			selectStmt = connection.prepareStatement(selectUser);
			selectStmt.setInt(1, userID);
			results = selectStmt.executeQuery();
			if(results.next()) {
				Integer resultuserID = results.getInt("userID");
				String username = results.getString("username");
				String firstName = results.getString("firstName");
				String lastName = results.getString("lastName");
				String email = results.getString("email");
				User user = new User(resultuserID,username,firstName,lastName,email);
				return user;
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
	/**
	 * 3. public User delete(User user).
	 * @param user The user to delete.
     * @return null, to indicate that the user object should no longer be used.
     * @throws SQLException if a database access error occurs.
	 */
	public User delete(User user) throws SQLException {
		String deleteUser = "DELETE FROM User WHERE userID=?;";
		Connection connection = null;
		PreparedStatement deleteStmt = null;
		try {
			connection = connectionManager.getConnection();
			deleteStmt = connection.prepareStatement(deleteUser);
			deleteStmt.setInt(1, user.getUserID());
			deleteStmt.executeUpdate();
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(connection != null) {
				connection.close();
			}
			if(deleteStmt != null) {
				deleteStmt.close();
			}
		}
	}
}
