package Application.model;

public class Rating {
	protected Reviewer reviewerID;
	protected Applicant applicantID;
	protected Integer rating;
	
	public Rating(Reviewer reviewerID, Applicant applicantID, Integer rating) {
		this.reviewerID = reviewerID;
		this.applicantID = applicantID;
		this.rating = rating;
	}
	
	public Rating (Applicant applicantID) {
		this.applicantID = applicantID;
	}
	
	public Rating(Reviewer reviewerID, Applicant applicantID) {
		this.reviewerID = reviewerID;
		this.applicantID = applicantID;
	}
	
	public Rating(Integer rating) {
		this.rating = rating;
	}

	/** Getters and setters. */
	public Reviewer getReviewerID() {
		return reviewerID;
	}

	public void setReviewerID(Reviewer reviewerID) {
		this.reviewerID = reviewerID;
	}

	public Applicant getApplicantID() {
		return applicantID;
	}

	public void setApplicantID(Applicant applicantID) {
		this.applicantID = applicantID;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}
}
