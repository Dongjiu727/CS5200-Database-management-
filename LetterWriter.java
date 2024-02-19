package Application.model;

/**
 * LetterWriter is a simple, plain old java objects (POJO).
 * Well, almost (it extends {@link User}).
 */
public class LetterWriter extends User {
	protected String university;
	
	public LetterWriter(Integer userID, String username, String firstName, String lastName, String email,String university) {
		super(userID, username, firstName, lastName, email);
		this.university = university;
	}
	
	public LetterWriter(String username, String firstName, String lastName, String email,String university) {
		super(username, firstName, lastName, email);
		this.university = university;
	}
	
	public LetterWriter(Integer userID) {
		super(userID);
	}


	/** Getters and setters. */
	public String getUniversity() {
		return university;
	}

	public void setUniversity(String university) {
		this.university = university;
	}	
}
