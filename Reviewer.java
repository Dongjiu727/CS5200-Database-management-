package Application.model;


/**
 * Reviewer is a simple, plain old java objects (POJO).
 * Well, almost (it extends {@link User}).
 */
public class Reviewer extends User {
	protected Programs program;
	
	public enum Programs {
		masters, phd
	}
	
	public Reviewer(Integer userID, String username, String firstName, String lastName, String email,Programs program) {
		super(userID, username, firstName, lastName, email);
		this.program = program;
	}
	
	public Reviewer(String username, String firstName, String lastName, String email,Programs program) {
		super(username, firstName, lastName, email);
		this.program = program;
	}
	
	public Reviewer(Integer userID) {
		super(userID);
	}

	/** Getters and setters. */
	public Programs getPrograms() {
		return program;
	}

	public void setPrograms(Programs program) {
		this.program = program;
	}	
}
