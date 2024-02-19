package Application.model;

import java.util.Date;


public class Degree {
	protected Integer degreeID;
	protected Applicant applicantID;
	protected DegreeType degreeType;
	protected String grantingInstitution;
	protected String subject;
	protected Date dateGranted;
	
	public enum DegreeType{
		bachelors, masters, phd
	}

	public Degree(Integer degreeID,Applicant applicantID, DegreeType degreeType, String grantingInstitution,String subject, Date dateGranted) {
		this.degreeID = degreeID;
		this.applicantID = applicantID;
		this.degreeType = degreeType;
		this.grantingInstitution = grantingInstitution;
		this.subject = subject;
		this.dateGranted = dateGranted;
	}
	
	// This constructor can be used for reading records from MySQL, where we only have the degreeID.
	public Degree(Integer degreeID) {
		this.degreeID = degreeID;
	}
	
	// This constructor can be used for creating new records, where the degreeID may not be
	// assigned yet since it is auto-generated by MySQL.
	public Degree(Applicant applicantID, DegreeType degreeType, String grantingInstitution,String subject, Date dateGranted) {
		this.applicantID = applicantID;
		this.degreeType = degreeType;
		this.grantingInstitution = grantingInstitution;
		this.subject = subject;
		this.dateGranted = dateGranted;
	}
	
	/** Getters and setters. */

	public Integer getDegreeID() {
		return degreeID;
	}

	public void setDegreeID(Integer degreeID) {
		this.degreeID = degreeID;
	}

	public Applicant getApplicantID() {
		return applicantID;
	}

	public void setApplicantID(Applicant applicantID) {
		this.applicantID = applicantID;
	}

	public DegreeType getDegreeType() {
		return degreeType;
	}

	public void setDegreeType(DegreeType degreeType) {
		this.degreeType = degreeType;
	}

	public String getGrantingInstitution() {
		return grantingInstitution;
	}

	public void setGrantingInstitution(String grantingInstitution) {
		this.grantingInstitution = grantingInstitution;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Date getDateGranted() {
		return dateGranted;
	}

	public void setDateGranted(Date dateGranted) {
		this.dateGranted = dateGranted;
	}
}