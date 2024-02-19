package Application.dal;

import Application.model.*;
import Application.model.Applicant;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * Data access object (DAO) class to interact with the underlying Applicant table in
 * MySQL instance. This is used to store {@link Applicant} into MySQL instance and 
 * retrieve {@link Applicant} from MySQL instance.
 */
public class ApplicantDao extends UserDao {
	// Single pattern: instantiation is limited to one object.
	private static ApplicantDao instance = null;
	
	protected ApplicantDao() {
		super();
	}
	
	public static ApplicantDao getInstance() {
		if(instance == null) {
			instance = new ApplicantDao();
		}
		return instance;
	}

   /**
    * 1. public Applicant create(Applicant applicant)
    * @param applicant The applicant to be created.
    * @return The created applicant.
    * @throws SQLException if a database access error occurs.
    */
	public Applicant create(Applicant applicant) throws SQLException {
		// Insert into the superclass table first.
		Integer user_id = applicant.getUserID();
		if (user_id == null) {
			User user = super.create(new User(applicant.getusername(), applicant.getFirstName(), 
					applicant.getLastName(), applicant.getemail()));
			user_id = user.getUserID();
			applicant.setUserID(user.getUserID());
		}
		
		String insertApplicant = "INSERT INTO Applicant(userID,program,essay) VALUES(?,?,?);";
		Connection connection = null;
		PreparedStatement insertStmt = null;
		try {
			connection = connectionManager.getConnection();
			insertStmt = connection.prepareStatement(insertApplicant);
			insertStmt.setInt(1, user_id);
			insertStmt.setString(2, applicant.getProgram().name());
			insertStmt.setString(3, applicant.getEssay());
			insertStmt.executeUpdate();
			return applicant;
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
	 * 2. public Applicant getApplicantByUserID(Integer userID)
	 */
	public Applicant getApplicantByUserID(Integer userID) throws SQLException {
		// To build an Applicant object, we need the User record, too.
		String selectApplicant =
			"SELECT Applicant.userID AS UserID, username, firstName, lastName, email, program, essay " +
			"FROM Applicant INNER JOIN User " +
			"  ON Applicant.userID = User.userID " +
			"WHERE Applicant.userID=?;";
		Connection connection = null;
		PreparedStatement selectStmt = null;
		ResultSet results = null;
		try {
			connection = connectionManager.getConnection();
			selectStmt = connection.prepareStatement(selectApplicant);
			selectStmt.setInt(1, userID);
			results = selectStmt.executeQuery();
			if(results.next()) {
				Integer resultuserID = results.getInt("userID");
				String username = results.getString("username");
				String firstName = results.getString("firstName");
				String lastName = results.getString("lastName");
				String email = results.getString("email");
				Applicant.Program program = Applicant.Program.valueOf(results.getString("program"));
				String essay = results.getString("essay");
				Applicant applicant = new Applicant(resultuserID,username, firstName, lastName,email, program, essay);
				return applicant;
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
     * 3. public List<Applicant> getApplicantsByProgram(Applicant.Program)
     * @param firstName
     * @return applicant
     * @throws SQLException
     */
	public List<Applicant> getApplicantsByProgram(Applicant.Program program)
			throws SQLException {
		List<Applicant> applicant = new ArrayList<Applicant>();
		String selectApplicant =
			"SELECT Applicant.userID AS UserID, username, firstName, lastName, email, program, essay " +
			"FROM Applicant INNER JOIN User " +
			"  ON Applicant.userID = User.userID " +
			"WHERE Applicant.program=?;";
		Connection connection = null;
		PreparedStatement selectStmt = null;
		ResultSet results = null;
		try {
			connection = connectionManager.getConnection();
			selectStmt = connection.prepareStatement(selectApplicant);
			selectStmt.setString(1, program.name());
			results = selectStmt.executeQuery();
			while(results.next()) {
				int userID = results.getInt("userID");
				String username = results.getString("username");
				String firstName = results.getString("firstName");
				String lastName = results.getString("lastName");
				String email = results.getString("email");
				Applicant.Program resultprogram = Applicant.Program.valueOf(results.getString("program"));
				String essay = results.getString("essay");
				
				Applicant newApplicant = new Applicant(userID,username, firstName, lastName,email,resultprogram, essay);
				applicant.add(newApplicant);
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
		return applicant;
	}
	
	/**
	 * 4. public Applicant delete(Applicant applicant)
	 */
	
	public Applicant delete(Applicant applicant) throws SQLException {
		String deleteApplicant = "DELETE FROM Applicant WHERE userID=?;";
		Connection connection = null;
		PreparedStatement deleteStmt = null;
		try {
			connection = connectionManager.getConnection();
			deleteStmt = connection.prepareStatement(deleteApplicant);
			deleteStmt.setInt(1, applicant.getUserID());
			Integer affectedRows = deleteStmt.executeUpdate();
			
			if (affectedRows == 0) {
				throw new SQLException("No records available to delete for UserID=" + applicant.getUserID());
			}	
			//I want to delete both User and Applicant records for a given Applicant.
			super.delete(applicant);
			
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
