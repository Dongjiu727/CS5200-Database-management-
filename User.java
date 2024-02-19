package Application.model;

/**
 * User is a simple, plain old java objects (POJO).
 * 
 * User/UserDao is the superclass for LetterWriter/LetterWriterDao, Reviewer/ReviewerDao and
 * Applicant/ApplicantDao.
 */
public class User {
	protected Integer userID;
	protected String username;
	protected String firstName;
	protected String lastName;
	protected String email;
	
	public User(String username, String firstName, String lastName, String email) {
		this.userID = null;
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}
	
	public User(Integer userID, String username, String firstName, String lastName, String email) {
		this.userID = userID;
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}
	
	public User(Integer userID) {
		this.userID = userID;
		this.username = null;
		this.firstName = null;
		this.lastName = null;
		this.email = null;
	}

	/** Automatically generating getters and setters. */
	public Integer getUserID() {
		return userID;
	}

	public void setUserID(Integer userID) {
		this.userID = userID;
	}

	public String getusername() {
		return username;
	}

	public void setusername(String userName) {
		this.username = userName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getemail() {
		return email;
	}

	public void setemail(String email) {
		this.email = email;
	}	
}
