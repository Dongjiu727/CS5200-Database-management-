package Application.model;

import java.util.Date;


public class RecLetter {
	protected LetterWriter authorID;
	protected Applicant applicantID;
	protected Date dateReceived;
	protected String body;
	
	public RecLetter(LetterWriter authorID, Applicant applicantID, Date dateReceived, String body) {
		this.authorID = authorID;
		this.applicantID = applicantID;
		this.dateReceived = dateReceived;
		this.body = body;
	}
	
	public RecLetter(LetterWriter authorID, Applicant applicantID) {
		this.authorID = authorID;
		this.applicantID = applicantID;
	}
	
	public RecLetter(Date dateReceived, String body) {
		this.dateReceived = dateReceived;
		this.body = body;
	}

	/** Getters and setters. */
	public LetterWriter getAuthorID() {
		return authorID;
	}

	public void setAuthorID(LetterWriter authorID) {
		this.authorID = authorID;
	}

	public Applicant getApplicantID() {
		return applicantID;
	}

	public void setApplicantID(Applicant applicantID) {
		this.applicantID = applicantID;
	}

	public Date getDateReceived() {
		return dateReceived;
	}

	public void setDateReceived(Date dateReceived) {
		this.dateReceived = dateReceived;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
}
