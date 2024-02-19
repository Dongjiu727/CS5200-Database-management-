package Application.dal;

import Application.model.*;
import Application.model.Rating;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class RatingDao {
	protected ConnectionManager connectionManager;

	private static RatingDao instance = null;
	protected RatingDao() {
		connectionManager = new ConnectionManager();
	}
	public static RatingDao getInstance() {
		if(instance == null) {
			instance = new RatingDao();
		}
		return instance;
	}

	/**
	 * 1. public Rating create(Rating rating)
	 */
	public Rating create(Rating rating) throws SQLException {
		String insertRating =
			"INSERT INTO Rating(reviewerID, applicantID, rating) " +
			"VALUES(?,?,?);";
		Connection connection = null;
		PreparedStatement insertStmt = null;
		
		try {
			connection = connectionManager.getConnection();
			insertStmt = connection.prepareStatement(insertRating);
			insertStmt.setInt(1, rating.getReviewerID().getUserID());
			insertStmt.setInt(2, rating.getApplicantID().getUserID());
			insertStmt.setInt(3, rating.getRating());
			insertStmt.executeUpdate();
			
			return rating;
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
	 * 2. public List<Rating> getRatingByApplicant(Applicant applicant)
	 */
	public List<Rating> getRatingByApplicant(Applicant applicant) throws SQLException {
		List<Rating> ratings = new ArrayList<Rating>();
		String selectRating =
			"SELECT reviewerID,applicantID,rating " +
			"FROM Rating " + 
			"WHERE applicantID=?;";
		Connection connection = null;
		PreparedStatement selectStmt = null;
		ResultSet results = null;
		
		try {
			connection = connectionManager.getConnection();
			selectStmt = connection.prepareStatement(selectRating);
			selectStmt.setInt(1, applicant.getUserID());
			results = selectStmt.executeQuery();
			
			ReviewerDao reviewerDao = ReviewerDao.getInstance();
			
			while(results.next()) {
				Integer reviewerID = results.getInt("reviewerID");
				Integer ratingValue = results.getInt("rating");

				Reviewer reviewer = reviewerDao.getReviewerByUserID(reviewerID);
				
				Rating rating = new Rating(reviewer, applicant, ratingValue);
				ratings.add(rating);
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
		return ratings;
	}
	
	
	/**
	 * 3. public Rating updateRating(Rating rating, int newRating)
	 */
	public Rating updateRating(Rating rating, int newRating) throws SQLException {
		String updateRating = "UPDATE Rating SET rating=? WHERE reviewerID=? AND applicantID=?;";
		Connection connection = null;
		PreparedStatement updateStmt = null;
		
		try {
			connection = connectionManager.getConnection();
			updateStmt = connection.prepareStatement(updateRating);
			updateStmt.setInt(1, newRating);
			updateStmt.setInt(2, rating.getReviewerID().getUserID());
			updateStmt.setInt(3, rating.getApplicantID().getUserID());
			
			updateStmt.executeUpdate();
			
			int affectedRows = updateStmt.executeUpdate();

	        if (affectedRows == 0) {
	            throw new SQLException("Updating rating failed, no rows affected.");
	        }

			// Update the rating parameter before returning to the caller.
			
			rating.setRating(newRating);
			
			return rating;
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(connection != null) {
				connection.close();
			}
			if(updateStmt != null) {
				updateStmt.close();
			}
		}
	}
}
