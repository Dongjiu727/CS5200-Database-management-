package Application.model;

/**
 * Applicant is a simple, plain old java objects (POJO).
 * Well, almost (it extends {@link User}).
 */
public class Applicant extends User {
	protected Program program;
	protected String essay;
	
	public enum Program {
		masters, phd
	}
	
	public Applicant(String username, String firstName, String lastName, String email,Program program, String essay) {
		super(username, firstName, lastName, email);
		this.program = program;
		this.essay = essay;
	}
	
	public Applicant(Integer userID, String username, String firstName, String lastName, String email,Program program, String essay) {
		super(userID, username, firstName, lastName, email);
		this.program = program;
		this.essay = essay;
	}
	
	public Applicant(Integer userID) {
		super(userID);
	}
	
	/** Getters and setters. */
	public Program getProgram() {
		return program;
	}

	public void setProgram(Program program) {
		this.program = program;
	}

	public String getEssay() {
		return essay;
	}

	public void setEssay(String essay) {
		this.essay = essay;
	}	
}
