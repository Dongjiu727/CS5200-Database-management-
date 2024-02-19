package Application.dal;

import Application.model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class RecLetterDao {
	protected ConnectionManager connectionManager;

	private static RecLetterDao instance = null;
	protected RecLetterDao() {
		connectionManager = new ConnectionManager();
	}
	public static RecLetterDao getInstance() {
		if(instance == null) {
			instance = new RecLetterDao();
		}
		return instance;
	}

	/**
	 * 1. public RecLetter create(RecLetter letter)
	 */
	public RecLetter create(RecLetter letter) throws SQLException {
		String insertRecLetter =
			"INSERT INTO RecLetter(authorID, applicantID, dateReceived, body) " +
			"VALUES(?,?,?,?);";
		Connection connection = null;
		PreparedStatement insertStmt = null;
		try {
			connection = connectionManager.getConnection();
			insertStmt = connection.prepareStatement(insertRecLetter);
			insertStmt.setInt(1, letter.getAuthorID().getUserID());
			insertStmt.setInt(2, letter.getApplicantID().getUserID());
			java.sql.Date sqlDate = new java.sql.Date(letter.getDateReceived().getTime());
			insertStmt.setDate(3, sqlDate);
			insertStmt.setString(4, letter.getBody());
			insertStmt.executeUpdate();
			
			return letter;
		}  catch (SQLException e) {
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
	 * 2. public RecLetter getRecLetterByApplicantAndAuthor(Applicant applicant, LetterWriter writer)
	 */
	public RecLetter getRecLetterByApplicantAndAuthor(Applicant applicant, LetterWriter writer) throws SQLException {
		String selectRecLetter =
			"SELECT authorID, applicantID, dateReceived, body " +
			"FROM RecLetter " +
			"WHERE authorID=? AND applicantID=?;";
		Connection connection = null;
		PreparedStatement selectStmt = null;
		ResultSet results = null;
		
		try {
			connection = connectionManager.getConnection();
			selectStmt = connection.prepareStatement(selectRecLetter);
			selectStmt.setInt(1, writer.getUserID());
			selectStmt.setInt(2, applicant.getUserID());
			
			results = selectStmt.executeQuery();
			
			LetterWriterDao letterWriterDao = LetterWriterDao.getInstance();
	        ApplicantDao applicantDao = ApplicantDao.getInstance();
			
			if(results.next()) {
				Integer resultauthorID = results.getInt("authorID");
				Integer resultapplicantID = results.getInt("applicantID");
				java.sql.Date sqlDateReceived = results.getDate("dateReceived");
                Date dateReceived = new Date(sqlDateReceived.getTime());
                String body = results.getString("body");
				
                LetterWriter fetchedWriter = letterWriterDao.getLetterWriterByUserID(resultauthorID);
                Applicant fetchedApplicant = applicantDao.getApplicantByUserID(resultapplicantID);
               
                RecLetter recLetter = new RecLetter(fetchedWriter, fetchedApplicant, dateReceived, body);
				return recLetter;
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
	 * 3.public List<RecLetter> getRecLettersByApplicant(Applicant applicant)
	 */
	public List<RecLetter> getRecLettersByApplicant(Applicant applicant) throws SQLException {
		List<RecLetter> recLetters = new ArrayList<RecLetter>();
		String selectRecLetter =
			"SELECT authorID, applicantID, dateReceived, body " +
			"FROM RecLetter " +
			"WHERE applicantID=?;";
		Connection connection = null;
		PreparedStatement selectStmt = null;
		ResultSet results = null;
		try {
			connection = connectionManager.getConnection();
			selectStmt = connection.prepareStatement(selectRecLetter);
			selectStmt.setInt(1, applicant.getUserID());
			results = selectStmt.executeQuery();
			
			LetterWriterDao letterWriterDao = LetterWriterDao.getInstance();

			while(results.next()) {
				Integer authorID = results.getInt("authorID");
				java.sql.Date sqlDateReceived = results.getDate("dateReceived");
                Date dateReceived = new Date(sqlDateReceived.getTime());
                String body = results.getString("body");
                
                LetterWriter writer = letterWriterDao.getLetterWriterByUserID(authorID);
				
				RecLetter recLetter = new RecLetter(writer, applicant, dateReceived, body);
				recLetters.add(recLetter);
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
		return recLetters;
	}
}
