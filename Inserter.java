package Application.tools;

import Application.dal.*;
import Application.model.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;


/**
 *For each data-access class above: exercise the create, read, update, and delete operations 
 *(if the method exists) using the data access object.
 */
public class Inserter {

	public static void main(String[] args) throws SQLException {
		
		UserDao userDao = UserDao.getInstance();
		ApplicantDao applicantDao = ApplicantDao.getInstance();
		ReviewerDao reviewerDao = ReviewerDao.getInstance();
		LetterWriterDao letterWriterDao = LetterWriterDao.getInstance();
		DegreeDao degreeDao = DegreeDao.getInstance();
		RecLetterDao recLetterDao = RecLetterDao.getInstance();
		RatingDao ratingDao = RatingDao.getInstance();
		
		// INSERT objects from models.
		// 1. Insert data into "User"
		User user1 = new User(1,"a", "apple", "chen","hi@gmial.com");
		user1 = userDao.create(user1);
		
		User user2 = new User(2,"b", "banana", "Wong","hello@gmial.com");
		user2 = userDao.create(user2);
		
		User user3 = new User(3,"c", "channel", "Java","java@gmial.com");
		user3 = userDao.create(user3);
		// 2. Insert data into "Applicant"
		Applicant applicant1 = new Applicant("ba", "bananaa", "Wonga","helloa@gmial.com",Applicant.Program.masters,"good essay");
		applicant1 = applicantDao.create(applicant1);
		
		Applicant applicant2 = new Applicant("bb", "bananab", "Wongb","hellob@gmial.com",Applicant.Program.phd,"good essay good");
		applicant2 = applicantDao.create(applicant2);
		
		Applicant applicant3 = new Applicant("bc", "bananac", "Wongc","helloc@gmial.com",Applicant.Program.masters,"perfect essay");
		applicant3 = applicantDao.create(applicant3);
		
		//3. Insert data into " Reviewer"
		Reviewer reviewer1 = new Reviewer("aa", "applea", "chena","hia@gmial.com", Reviewer.Programs.phd);
		reviewer1 = reviewerDao.create(reviewer1);
		
		Reviewer reviewer2 = new Reviewer("ab", "appleb", "chenb","hib@gmial.com", Reviewer.Programs.masters);
		reviewer2 = reviewerDao.create(reviewer2);
		
		Reviewer reviewer3 = new Reviewer("ac", "applec", "chenc","hiac@gmial.com", Reviewer.Programs.phd);
		reviewer3 = reviewerDao.create(reviewer3);
		// 4. Insert data into "LetterWriter"
		LetterWriter letterWriter1 = new LetterWriter ("ca", "channela", "Javaa","javaa@gmial.com","Neu");
		letterWriter1 = letterWriterDao.create(letterWriter1);
		
		LetterWriter letterWriter2 = new LetterWriter ("cab", "channelb", "Javaab","javaa@gmial.com","UCLA");
		letterWriter2 = letterWriterDao.create(letterWriter2);
		
		LetterWriter letterWriter3 = new LetterWriter ("caC", "channelaC", "JavaaC","javaa@gmial.com","Yale");
		letterWriter3 = letterWriterDao.create(letterWriter3);
		// 5. Insert Data into "Degree"
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		try {
		    Date dateGranted1 = dateFormat.parse("12/12/2020");
		    Degree degree1 = new Degree(1, applicant1, Degree.DegreeType.bachelors, "Science", "biology", dateGranted1);
		    degree1 = degreeDao.create(degree1);
		} catch (ParseException e) {
		    System.out.println("Error parsing the date: " + e.getMessage());
		}
		
		try {
        Date dateGrante2 = dateFormat.parse("10/12/2010");
		Degree degree2 = new Degree(2,applicant2,Degree.DegreeType.bachelors,"math","math",dateGrante2);
		degree2 = degreeDao.create(degree2);
		} catch (ParseException e) {
		    System.out.println("Error parsing the date: " + e.getMessage());
		}
		try {
		Date dateGrante3 = dateFormat.parse("1/1/2010");
		Degree degree3 = new Degree(3,applicant3,Degree.DegreeType.bachelors,"math","math",dateGrante3);
		degree3 = degreeDao.create(degree3);
	    } catch (ParseException e) {
	    System.out.println("Error parsing the date: " + e.getMessage());
	    }
		
		//6. Insert Data into "RecLetter"
		Date date = new Date();
		RecLetter recLetter1 = new RecLetter(letterWriter1,applicant1,date,"good student");
		recLetter1 = recLetterDao.create(recLetter1);
		
		RecLetter recLetter2 = new RecLetter(letterWriter2,applicant1,date,"excellent student");
		recLetter2 = recLetterDao.create(recLetter2);
		
		RecLetter recLetter3 = new RecLetter(letterWriter3,applicant2,date,"good student");
		recLetter3 = recLetterDao.create(recLetter3);
		
		//7. Insert Data into "Rating"
		Rating rating1 = new Rating(reviewer1,applicant1,2);
		rating1 = ratingDao.create(rating1);
		
		Rating rating2 = new Rating(reviewer1,applicant2,5);
		rating2 = ratingDao.create(rating2);
		
		Rating rating3 = new Rating(reviewer3,applicant1,4);
		rating3 = ratingDao.create(rating3);
		
		// READ
		//1. Get User By UserID
		User u1 = userDao.getUserByUserID(1);
		
		//2.1 Get Applicant By UserID
		Applicant a1 = applicantDao.getApplicantByUserID(applicant1.getUserID());
		
		//2.2 Get Applicant List By Program
		List<Applicant> aList1 = applicantDao.getApplicantsByProgram(Applicant.Program.masters);
		System.out.format("Reading applicant: ID:%d u:%s f:%s l:%s e:%s \n",
			a1.getUserID(), a1.getusername(), a1.getFirstName(), a1.getLastName(),a1.getemail());
		for(Applicant a : aList1) {
			System.out.format("Looping applicants: ID:%d u:%s f:%s l:%s e:%s p:%s e:%s \n",
			        a.getUserID(),a.getusername(), a.getFirstName(), a.getLastName(), a.getemail(), a.getProgram().name(), a.getEssay());
		}
		//3. Get Reviewer By UserID
		Reviewer r1 = reviewerDao.getReviewerByUserID(21);
		
		//4. Get LetterWriter By UserID
		LetterWriter l1 = letterWriterDao.getLetterWriterByUserID(31);
		
		//5. Get Degree list By Applicant
		List<Degree> dList1 = degreeDao.getDegreesByApplicant(applicant1);
		System.out.format("Reading applicant: ID:%d u:%s f:%s l:%s e:%s \n",
			applicant1.getUserID(), applicant1.getusername(), applicant1.getFirstName(), applicant1.getLastName(),applicant1.getemail());
		for(Degree d : dList1) {
			System.out.format("Looping degrees: ID:%d t:%s i:%s s:%s d:%s \n",
			        d.getDegreeID(),d.getDegreeType(), d.getGrantingInstitution(), d.getSubject(), d.getDateGranted().toString());
		}
		
		//6. Get RecLetter By Applicant And Author
		RecLetter rl1 = recLetterDao.getRecLetterByApplicantAndAuthor(applicant1, letterWriter1);
		
		//7. Get RecLetters List By Applicant
		List<RecLetter> rlList1 = recLetterDao.getRecLettersByApplicant(applicant1);
		System.out.format("Reading recommendation letters for Applicant ID:%d \n", applicant1.getUserID());
		if (rlList1.isEmpty()) {
		    for (RecLetter recLetter : rlList1) {
		        System.out.format("RecLetter: AuthorID:%d, DateReceived:%s, Body:%s \n",
		            recLetter.getAuthorID().getUserID(), 
		            recLetter.getDateReceived().toString(), recLetter.getBody());
		    }
		} else {
		    System.out.println("No recommendation letters found for the specified applicant.");
		}
		
		//8. Get Rating List By Applicant
		List<Rating> ratingList1 = ratingDao.getRatingByApplicant(applicant1);
		for(Rating rat : ratingList1) {
			System.out.format("Looping ratings: ApplicantID:%d, ReviewerID:%d, Rating:%d \n",
				applicant1.getUserID(), rat.getReviewerID().getUserID(), rat.getRating());
		}
		// Delete
		//1. Delete(User user)
		try {
		    User result = userDao.delete(user1);
		    if (result == null) {
		        System.out.println("User successfully deleted: " + user1.getUserID());
		    }
		} catch (SQLException e) {
		    System.out.println("Error occurred during deletion: " + e.getMessage());
		}
		
		//2. Delete(Applicant applicant)
		try {
		    User result = userDao.delete(applicant3);
		    if (result == null) {
		        System.out.println("User successfully deleted: " + applicant3.getUserID());
		    }
		} catch (SQLException e) {
		    System.out.println("Error occurred during deletion: " + e.getMessage());
		}
		
		// Update
		//1. Update Rating
		for (Rating rat : ratingList1) {
		    if (rat.getReviewerID().getUserID() == reviewer1.getUserID()) {
		        // Update the rating to 5
		        try {
		            Rating updatedRating = ratingDao.updateRating(rat, 5);
		            System.out.format("Rating updated: ReviewerID:%d ApplicantID:%d New Rating:%d \n",
		                updatedRating.getReviewerID().getUserID(), updatedRating.getApplicantID().getUserID(), updatedRating.getRating());
		            break; 
		        } catch (SQLException e) {
		            System.out.println("Error occurred during rating update: " + e.getMessage());
		            break; 
		        }
		    }
	     }
     }
}