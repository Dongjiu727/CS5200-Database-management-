package Application.dal;

import Application.model.*;
import Application.model.Degree;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class DegreeDao {
	protected ConnectionManager connectionManager;

	private static DegreeDao instance = null;
	protected DegreeDao() {
		connectionManager = new ConnectionManager();
	}
	public static DegreeDao getInstance() {
		if(instance == null) {
			instance = new DegreeDao();
		}
		return instance;
	}

	/**
	 * 1. public Degree create(Degree degree).
	 */
	public Degree create(Degree degree) throws SQLException {
		String insertDegree =
			"INSERT INTO Degree(applicantID, degreeType, grantingInstitution, subject, dateGranted) " +
			"VALUES(?,?,?,?,?);";
		Connection connection = null;
		PreparedStatement insertStmt = null;
		ResultSet resultKey = null;
		try {
			connection = connectionManager.getConnection();
			
			// Degree has an auto-generated key. So we want to retrieve that key.
			insertStmt = connection.prepareStatement(insertDegree,
				Statement.RETURN_GENERATED_KEYS);
			insertStmt.setInt(1, degree.getApplicantID().getUserID());
			insertStmt.setString(2, degree.getDegreeType().name());
			insertStmt.setString(3, degree.getGrantingInstitution());
			insertStmt.setString(4, degree.getSubject());
			java.sql.Date sqlDate = new java.sql.Date(degree.getDateGranted().getTime());
			insertStmt.setDate(5, sqlDate);
			insertStmt.executeUpdate();
			
			// Retrieve the auto-generated key and set it, so it can be used by the caller.
			resultKey = insertStmt.getGeneratedKeys();
			Integer degreeID = -1;
			if(resultKey.next()) {
				degreeID = resultKey.getInt(1);
			} else {
				throw new SQLException("Unable to retrieve auto-generated key.");
			}
			degree.setDegreeID(degreeID);
			return degree;
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
			if(resultKey != null) {
				resultKey.close();
			}
		}
	}

	/**
	 *2. public List<Degree> getDegreesByApplicant(Applicant applicant).
	 * Get the all the degrees for a applicant.
	 */
	public List<Degree> getDegreesByApplicant(Applicant applicant) throws SQLException {
		List<Degree> degrees = new ArrayList<Degree>();
		String selectDegree =
			"SELECT degreeID, applicantID, degreeType, grantingInstitution, subject, dateGranted " +
			"FROM Degree " +
			"WHERE applicantID=?;";
		Connection connection = null;
		PreparedStatement selectStmt = null;
		ResultSet results = null;
		try {
			connection = connectionManager.getConnection();
			selectStmt = connection.prepareStatement(selectDegree);
			selectStmt.setInt(1, applicant.getUserID());
			results = selectStmt.executeQuery();
			while(results.next()) {
				Integer degreeID = results.getInt("degreeID");
				Degree.DegreeType degreeType = Degree.DegreeType.valueOf(results.getString("degreeType"));
				String grantingInstitution = results.getString("grantingInstitution");
				String subject = results.getString("subject");
				java.sql.Date sqldateGranted = results.getDate("dateGranted");
			    Date dateGranted = new Date(sqldateGranted.getTime());  // Convert java.sql.Date to java.util.Date
				Degree degree = new Degree (degreeID, applicant, degreeType, grantingInstitution, subject, dateGranted);
				degrees.add(degree);
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
		return degrees;
	}
}